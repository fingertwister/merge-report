package au.net.thehardings.ims.mergereport;

import au.net.thehardings.ims.mergereport.dispatch.Dispatcher;
import au.net.thehardings.ims.mergereport.dispatch.EmailDispatcher;
import au.net.thehardings.ims.mergereport.model.Commit;
import au.net.thehardings.ims.mergereport.model.Repository;
import au.net.thehardings.ims.mergereport.process.ProcessWrapper;
import au.net.thehardings.ims.mergereport.process.SimpleProcessWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The class <code>Test</code>
 */
public class MergeReport {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MergeReport.class);

    static String contextFile;
    static String requestorAddress;
    static String mailHost;
    static String mailDomain;
    static boolean notifyAll = false;
    static PrintStream out = System.out;

    ProcessWrapper processor = new SimpleProcessWrapper();
    Dispatcher dispatcher = new EmailDispatcher();
    List<Commit> outstanding = new ArrayList<Commit>();
    List<Repository> repositories;

    public static void main(String[] args) {
        try {
            LOG.info("*****************************************");
            LOG.info("**            Merge Report             **");
            LOG.info("*****************************************");

            parseArgs(args);

            ApplicationContext context = new ClassPathXmlApplicationContext(contextFile);
            MergeReport mergeReport = (MergeReport) context.getBean("mergeReport");
            mergeReport.run();
        } catch (Throwable e) {
            LOG.error("An exception occurred during processing.", e);
        }
    }

    public static String getRequestorAddress() {
        return requestorAddress;
    }

    public static void setRequestorAddress(String requestorAddress) {
        MergeReport.requestorAddress = requestorAddress;
    }

    public static String getMailHost() {
        return mailHost;
    }

    public static void setMailHost(String mailHost) {
        MergeReport.mailHost = mailHost;
    }

    public static String getMailDomain() {
        return mailDomain;
    }

    public static void setMailDomain(String mailDomain) {
        MergeReport.mailDomain = "@" + mailDomain;
    }

    public static boolean isNotifyAll() {
        return notifyAll;
    }

    public static void setNotifyAll(boolean notifyAll) {
        MergeReport.notifyAll = notifyAll;
    }

    static void parseArgs(String[] args) {
        setMailHost(System.getProperty("mail.host"));
        if (mailHost == null || "".equals(mailHost)) {
            showUsage();
        }

        setMailDomain(System.getProperty("mail.domain"));
        if ("@null".equals(mailDomain) || "@".equals(mailDomain)) {
            showUsage();
        }

        List<String> params = Arrays.asList(args);

        if (params.indexOf("help") > -1) {
            showUsage();
        }
        if (params.indexOf("?") > -1) {
            showUsage();
        }
        if (params.indexOf("-?") > -1) {
            showUsage();
        }
        if (params.indexOf("-help") > -1) {
            showUsage();
        }

        if (params.indexOf("-e") > -1) {
            if (params.size() > params.indexOf("-e") + 1) {
                requestorAddress = params.get(params.indexOf("-e") + 1);
            }
        } else {
            requestorAddress = System.getProperty("user.name") + mailDomain;
        }

        if (requestorAddress == null || "".equals(requestorAddress)) {
            showUsage();
        }

        if (params.indexOf("-f") > -1) {
            if (params.size() > params.indexOf("-f") + 1) {
                contextFile = params.get(params.indexOf("-f") + 1);
            }
        } else {
            contextFile = "merge-report.xml";
        }

        setNotifyAll(params.indexOf("-a") > -1);

        if (contextFile == null || "".equals(contextFile)) {
            showUsage();
        }
    }

    static void showUsage() {
        out.println("Usage: MergeReport [-f <context file>] [-e <email addresses>] [-a] [-auto]");
        out.println(" The system properties mail.host and mail.domain must be set.");
        out.println(" Options:");
        out.println("  -f <context file>: the spring context file for the report (default merge-report.xml)");
        out.println("  -e <email addresses>: comma separated email to always send report to (default system user)");
        out.println("  -a: send report to users that have outstanding merges");
    }

    public void run() throws IOException, InterruptedException, MessagingException {
        for (Repository repository : repositories) {
            getUnmergedCommits(repository);
        }

        LOG.info("Sending the report email");
        dispatcher.dispatch(outstanding);
    }

    void getUnmergedCommits(Repository repository) throws IOException, InterruptedException {
        LOG.info("Getting unmerged commits from the branch");
        String command = "svn mergeinfo --show-revs eligible " + repository.getBranchUrl();
        LOG.debug(command);
        Scanner scanner = processor.runProcess(command, repository.getBase());
        List<Integer> commitIds = new ArrayList<Integer>();
        while (scanner.hasNextLine()) {
            String commitDetail = scanner.nextLine().trim().replace("r", "");
            if (commitDetail.equals("")) {
                continue;
            }
            commitIds.add(Integer.valueOf(commitDetail));
        }
        LOG.info("Found " + commitIds.size() + " commits that have not been merged.");

        LOG.info("Retrieving the detail for each commit...");
        for (Integer commitId : commitIds) {
            outstanding.add(getCommit(commitId, repository));
        }
    }

    List<Integer> getCommitIds(String commitDetail) throws IOException, InterruptedException {
        List<Integer> commitIds = new ArrayList<Integer>();

        if (commitDetail == null || "".equals(commitDetail)) {
            return commitIds;
        }

        String[] commits = commitDetail.split(",");

        for (String commit : commits) {
            if (commit.indexOf(":") > -1) {
                String[] range = commit.split(":");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (; start <= end; start++) {
                    commitIds.add(start);
                }
            } else {
                commitIds.add(Integer.valueOf(commit));
            }
        }

        return commitIds;
    }

    Commit getCommit(Integer commitId, Repository repository) throws IOException, InterruptedException {
        Scanner scanner = processor.runProcess("svn log " + repository.getUrl() + " -r " + commitId, repository.getBase());
        scanner.nextLine(); //header
        String[] details = scanner.nextLine().split(" \\| ");
        scanner.nextLine(); //blank
        List<String> comment = new ArrayList<String>();
        while (comment.size() == 0 || !"------------------------------------------------------------------------".equals(comment.get(comment.size() - 1))) {
            comment.add(scanner.nextLine());
        }
        comment.remove(comment.size() - 1); //the last line is the footer
        return new Commit.Builder(repository, repository.getBranchName(), commitId).username(details[1].trim()).date(details[2].trim()).comment(comment).build();
    }

    public void setProcessor(ProcessWrapper processor) {
        this.processor = processor;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
