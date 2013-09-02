package au.net.thehardings.ims.mergereport.dispatch;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jmock.Mock;

import au.net.thehardings.ims.mergereport.AllTests;
import au.net.thehardings.ims.mergereport.model.Commit;
import au.net.thehardings.ims.mergereport.model.Repository;

/**
 * The class <code>FreemarkerDispatcherTest</code>
 */
public class FreemarkerDispatcherTest extends AllTests {
    FreemarkerDispatcher freemarkerDispatcher;

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        freemarkerDispatcher = new TestableFreemarkerDispatcher();
    }

    /**
     * Test case for the <code>FreemarkerDispatcher()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testFreemarkerDispatcher() throws Exception {
        assertNotNull("The Freemarker configuration must have a default.", freemarkerDispatcher.cfg);
    }

    /**
     * Test case for the <code>process()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testProcess() throws Exception {
        String templateName = "template.ftl";
        Map<String, Object> context = new HashMap<String, Object>();

        Mock cfgMock = mock(Configuration.class);
        cfgMock.expects(once()).method("getStrictSyntaxMode").will(returnValue(true));
        cfgMock.expects(once()).method("getWhitespaceStripping").will(returnValue(true));
        cfgMock.expects(once()).method("getTagSyntax").will(returnValue(0));
        MockableTemplate.cfg = (Configuration) cfgMock.proxy();
        Mock templateMock = mock(MockableTemplate.class);

        templateMock.expects(once()).method("process").with(eq(context), isA(StringWriter.class));
        cfgMock.expects(once()).method("getTemplate").with(eq(templateName)).will(returnValue(templateMock.proxy()));

        freemarkerDispatcher.cfg = (Configuration) cfgMock.proxy();
        freemarkerDispatcher.process(context, templateName);
    }

    /**
     * Test case for the <code>process()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testProcessException1() throws Exception {
        String templateName = "template.ftl";
        Map<String, Object> context = new HashMap<String, Object>();

        Mock cfgMock = mock(Configuration.class);
        cfgMock.expects(once()).method("getTemplate").withAnyArguments().will(throwException(new IOException("unit test")));
        freemarkerDispatcher.cfg = (Configuration) cfgMock.proxy();

        try {
            freemarkerDispatcher.process(context, templateName);
            fail("should have thrown exception");
        } catch (RuntimeException e) {
            assertEquals("The exception text was not as expected", "Couldn't get freemarker template named '" + templateName + "'.", e.getMessage());
        }
    }

    /**
     * Test case for the <code>process()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testProcessException2() throws Exception {
        String templateName = "template.ftl";
        Map<String, Object> context = new HashMap<String, Object>();

        Mock cfgMock = mock(Configuration.class);
        cfgMock.expects(once()).method("getStrictSyntaxMode").will(returnValue(true));
        cfgMock.expects(once()).method("getWhitespaceStripping").will(returnValue(true));
        cfgMock.expects(once()).method("getTagSyntax").will(returnValue(0));
        MockableTemplate.cfg = (Configuration) cfgMock.proxy();
        Mock templateMock = mock(MockableTemplate.class);

        templateMock.expects(once()).method("process").withAnyArguments().will(throwException(new TemplateException("unit test", null)));
        cfgMock.expects(once()).method("getTemplate").with(eq(templateName)).will(returnValue(templateMock.proxy()));

        freemarkerDispatcher.cfg = (Configuration) cfgMock.proxy();
        try {
            freemarkerDispatcher.process(context, templateName);
            fail("should have thrown exception");
        } catch (RuntimeException e) {
            assertEquals("The exception text was not as expected", "Exception while processing template named '" + templateName + "'.", e.getMessage());
        }
    }

    /**
     * Test case for the <code>getUserCommits()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUserCommits() throws Exception {
        List<String> comments = new ArrayList<String>();
        comments.add("Test comment");
        Repository repository = new Repository();
        repository.setName("repository");
        Commit commit = new Commit.Builder(repository, "branch", 400).username("user").date("20071010").comment(comments).build();
        List<Commit> commits = new ArrayList<Commit>();
        commits.add(commit);
        Map<String, Map<String, List<Commit>>> userCommits = freemarkerDispatcher.getUserCommits(commits);
        assertTrue("There should only be one user", userCommits.size() == 1);
        assertNotNull("The user was not found", userCommits.get(commit.getUsername()));
        assertTrue("There should only be one repository", userCommits.get(commit.getUsername()).size() == 1);
        assertNotNull("The repository was not found", userCommits.get(commit.getUsername()).get(commit.getRepository().getName()));
        assertTrue("There should only be one commit", userCommits.get(commit.getUsername()).get(commit.getRepository().getName()).size() == 1);
        assertEquals("The commit was different", commit.getCommitId(), userCommits.get(commit.getUsername()).get(commit.getRepository().getName()).get(0).getCommitId());
    }

    /**
     * Test case for the <code>getUserCommands()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUserCommands1() throws Exception {
        List<String> comments = new ArrayList<String>();
        comments.add("Test comment");
        Repository repository = new Repository();
        repository.setName("repository");
        repository.setUrl("url");
        repository.setBranchName("branch");
        Commit commit1 = new Commit.Builder(repository, "branch", 400).username("user").date("20071010").comment(comments).build();
        Commit commit2 = new Commit.Builder(repository, "branch", 401).username("user").date("20071010").comment(comments).build();
        List<Commit> commits = new ArrayList<Commit>();
        commits.add(commit1);
        commits.add(commit2);
        Map<String, Map<String, String>> userCommands = freemarkerDispatcher.getUserCommands(commits);
        assertTrue("There should only be one user", userCommands.size() == 1);
        assertNotNull("The user was not found", userCommands.get(commit1.getUsername()));
        assertTrue("There should only be one repository", userCommands.get(commit1.getUsername()).size() == 1);
        assertEquals("The command was different", "svn merge -c " + commit1.getCommitId() + "," + commit2.getCommitId() + " " + commit1.getRepository().getBranchUrl(), userCommands.get(commit1.getUsername()).get(commit1.getRepository().getName()));
    }

    /**
     * Test case for the <code>getUserCommands()</code> method
     * of class <code>FreemarkerDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUserCommands2() throws Exception {
        List<String> comments = new ArrayList<String>();
        comments.add("Test comment");
        Repository repository = new Repository();
        repository.setName("repository");
        repository.setUrl("url");
        repository.setBranchName("branch");
        Commit commit1 = new Commit.Builder(repository, "branch", 400).username("zzsysuser").date("20071010").comment(comments).build();
        Commit commit2 = new Commit.Builder(repository, "branch", 401).username("zzsysuser").date("20071010").comment(comments).build();
        List<Commit> commits = new ArrayList<Commit>();
        commits.add(commit1);
        commits.add(commit2);
        Map<String, Map<String, String>> userCommands = freemarkerDispatcher.getUserCommands(commits);
        assertTrue("There should only be one system user", userCommands.size() == 1);
        assertNotNull("The system user was not found", userCommands.get(commit1.getUsername()));
        assertTrue("There should only be one repository", userCommands.get(commit1.getUsername()).size() == 1);
        assertEquals("The command was different", "svn merge --record-only -c " + commit1.getCommitId() + "," + commit2.getCommitId() + " " + commit1.getRepository().getBranchUrl(), userCommands.get(commit1.getUsername()).get(commit1.getRepository().getName()));
    }

    public class TestableFreemarkerDispatcher extends FreemarkerDispatcher {
        public void dispatch(List<Commit> outstanding) throws MessagingException {
            //do nothing here;
        }
    }

    public static class MockableTemplate extends Template {
        static Configuration cfg;

        public MockableTemplate() throws IOException {
            super("template.ftl", new StringReader(""), cfg);
        }
    }
}