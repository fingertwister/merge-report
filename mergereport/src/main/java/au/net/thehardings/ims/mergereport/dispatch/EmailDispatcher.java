package au.net.thehardings.ims.mergereport.dispatch;

import au.net.thehardings.ims.mergereport.MergeReport;
import au.net.thehardings.ims.mergereport.model.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <code>EmailDispatcher</code>
 */
public class EmailDispatcher extends FreemarkerDispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(EmailDispatcher.class);
    JavaMailSender sender = new JavaMailSenderImpl();

    public EmailDispatcher() {
        if (sender instanceof JavaMailSenderImpl) {
            ((JavaMailSenderImpl) sender).setHost(MergeReport.getMailHost());
        }
    }

    /**
     * Email implementation of the dispatch method.
     */
    public void dispatch(List<Commit> outstanding) {
        Map<String, Map<String, List<Commit>>> users = getUserCommits(outstanding);
        Map<String, Map<String, String>> commands = getUserCommands(outstanding);

        //print it all out to the log
        List<String> userEmails = new ArrayList<String>();
        for (String user : users.keySet()) {
            LOG.debug(user);
            for (String repository : users.get(user).keySet()) {
                LOG.debug(repository + "(" + commands.get(user).get(repository) + ")");
                for (Commit commit : users.get(user).get(repository)) {
                    LOG.debug(commit.getCommitId() + ": " + commit.getComment().get(0));
                }
            }
            userEmails.add(user + MergeReport.getMailDomain());
        }

        //generate the email text
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("users", users);
        context.put("commands", commands);
        String text = process(context, "svnmergeReminder.ftl");

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no-reply" + MergeReport.getMailDomain());
            if (MergeReport.isNotifyAll()) {
                helper.setTo(userEmails.toArray(new String[userEmails.size()]));
                helper.setBcc(MergeReport.getRequestorAddress().split(","));
            } else {
                helper.setTo(MergeReport.getRequestorAddress().split(","));
            }
            helper.setSubject("unmerged changes");
            helper.setText(text, true);
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("An error occurred while trying to send the email.", e);
        }
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }
}
