package au.net.thehardings.ims.mergereport.dispatch;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import au.net.thehardings.ims.mergereport.model.Commit;
import au.net.thehardings.ims.mergereport.model.Repository;

/**
 * The class <code>FreemarkerDispatcher</code>
 */
public abstract class FreemarkerDispatcher implements Dispatcher {

    Configuration cfg = new Configuration();

    protected FreemarkerDispatcher() {
        cfg.setClassForTemplateLoading(getClass(), "/templates/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    protected String process(Map<String, Object> context, String templateName) {
        try {
            Template template = cfg.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(context, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get freemarker template named '" + templateName + "'.", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Exception while processing template named '" + templateName + "'.", e);
        }
    }

    Map<String, Map<String, List<Commit>>> getUserCommits(List<Commit> outstanding) {
        Map<String, Map<String, List<Commit>>> users = new HashMap<String, Map<String, List<Commit>>>();
        for (Commit commit : outstanding) {
            Map<String, List<Commit>> userCommits = users.get(commit.getUsername());
            if (userCommits == null) {
                userCommits = new HashMap<String, List<Commit>>();
                users.put(commit.getUsername(), userCommits);
            }

            List<Commit> commits = userCommits.get(commit.getRepository().getName());
            if (commits == null) {
                commits = new ArrayList<Commit>();
                userCommits.put(commit.getRepository().getName(), commits);
            }
            commits.add(commit);
        }
        return users;
    }

    Map<String, Map<String, String>> getUserCommands(List<Commit> outstanding) {
        Map<String, Map<String, String>> commands = new HashMap<String, Map<String, String>>();
        Map<String, Repository> repositories = new HashMap<String, Repository>();

        for (Commit commit : outstanding) {
            if (!repositories.keySet().contains(commit.getRepository().getName())) {
                repositories.put(commit.getRepository().getName(), commit.getRepository());
            }

            Map<String, String> userCommands = commands.get(commit.getUsername());
            if (userCommands == null) {
                userCommands = new HashMap<String, String>();
                commands.put(commit.getUsername(), userCommands);
            }

            String command = userCommands.get(commit.getRepository().getName());
            if (command == null) {
                String mergeType = commit.getUsername().startsWith("zz") ? "--record-only " : "";
                command = "svn merge " + mergeType + "-c " + commit.getCommitId();
            } else {
                command = command.concat(",").concat(commit.getCommitId());
            }
            userCommands.put(commit.getRepository().getName(), command);
        }

        //need to do this at the end because the repository url is now the last thing on the command
        for (Map<String, String> userCommands : commands.values()) {
            for (String repositoryName : userCommands.keySet()) {
                Repository repository = repositories.get(repositoryName);
                String command = userCommands.get(repositoryName);
                command = command.concat(" ").concat(repository.getBranchUrl());
                userCommands.put(repositoryName, command);
            }
        }

        return commands;
    }
}
