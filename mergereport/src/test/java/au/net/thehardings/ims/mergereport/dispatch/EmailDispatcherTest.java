package au.net.thehardings.ims.mergereport.dispatch;

import au.net.thehardings.ims.mergereport.AllTests;
import au.net.thehardings.ims.mergereport.MergeReport;
import au.net.thehardings.ims.mergereport.model.Commit;
import au.net.thehardings.ims.mergereport.model.Repository;
import org.jmock.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The class <code>EmailDispatcherTest</code>
 */
public class EmailDispatcherTest extends AllTests {
    public static final String TEST_MAIL_HOST = "mail.mycompany.com";
    public static final String TEST_MAIL_DOMAIN = "mycompany.com";
    TestableEmailDispatcher emailDispatcher;

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        MergeReport.setMailDomain(TEST_MAIL_DOMAIN);
        MergeReport.setMailHost(TEST_MAIL_HOST);
        emailDispatcher = new TestableEmailDispatcher();
    }

    /**
     * Test case for the <code>EmailDispatcher()</code> method
     * of class <code>EmailDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testEmailDispatcher() throws Exception {
        assertTrue("Should be a JavaMailSenderImpl by default", emailDispatcher.sender instanceof JavaMailSenderImpl);
        assertEquals("Should use mailhost by default", MergeReport.getMailHost(), ((JavaMailSenderImpl) emailDispatcher.sender).getHost());
    }

    /**
     * Test case for the <code>dispatch()</code> method
     * of class <code>EmailDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testDispatch1() throws Exception {
        List<String> comments = new ArrayList<String>();
        comments.add("Test comment");
        Repository repository = new Repository();
        repository.setName("repository");
        Commit commit = new Commit.Builder(repository, "branch", 400).username("user").date("20071010").comment(comments).build();

        Map<String, Map<String, List<Commit>>> userCommits = new HashMap<String, Map<String, List<Commit>>>();
        userCommits.put(commit.getUsername(), new HashMap<String, List<Commit>>());
        userCommits.get(commit.getUsername()).put(commit.getRepository().getName(), new ArrayList<Commit>());
        userCommits.get(commit.getUsername()).get(commit.getRepository().getName()).add(commit);

        Map<String, Map<String, String>> userCommands = new HashMap<String, Map<String, String>>();
        userCommands.put(commit.getUsername(), new HashMap<String, String>());
        userCommands.get(commit.getUsername()).put(commit.getRepository().getName(), "svnmerge command");

        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        Mock senderMock = mock(JavaMailSender.class);
        JavaMailSender sender = (JavaMailSender) senderMock.proxy();
        senderMock.expects(once()).method("createMimeMessage").withNoArguments().will(returnValue(message));
        senderMock.expects(once()).method("send").with(eq(message));

        MergeReport.setRequestorAddress("unit-test" + MergeReport.getMailDomain());
        emailDispatcher.processString = TEST_STRING;
        emailDispatcher.userCommits = userCommits;
        emailDispatcher.userCommands = userCommands;
        emailDispatcher.sender = sender;

        MergeReport.setNotifyAll(false);
        emailDispatcher.dispatch(null);
        assertEquals("Template name has changed", "svnmergeReminder.ftl", emailDispatcher.templateName);
        assertEquals("Incorrect email subject", "unmerged changes", message.getSubject());
        assertEquals("Incorrect from address", "no-reply" + MergeReport.getMailDomain(), message.getFrom()[0].toString());
        assertEquals("Incorrect to address", "unit-test" + MergeReport.getMailDomain(), message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("Incorrect email subject", TEST_STRING, (((MimeMultipart) ((MimeBodyPart) ((MimeMultipart) message.getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent());
        assertEquals("Incorrect to content type", "text/html", (((MimeMultipart) ((MimeBodyPart) ((MimeMultipart) message.getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContentType());
    }

    /**
     * Test case for the <code>dispatch()</code> method
     * of class <code>EmailDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testDispatch2() throws Exception {
        List<String> comments = new ArrayList<String>();
        comments.add("Test comment");
        Repository repository = new Repository();
        repository.setName("repository");
        Commit commit = new Commit.Builder(repository, "branch", 400).username("user").date("20071010").comment(comments).build();

        Map<String, Map<String, List<Commit>>> userCommits = new HashMap<String, Map<String, List<Commit>>>();
        userCommits.put(commit.getUsername(), new HashMap<String, List<Commit>>());
        userCommits.get(commit.getUsername()).put(commit.getRepository().getName(), new ArrayList<Commit>());
        userCommits.get(commit.getUsername()).get(commit.getRepository().getName()).add(commit);

        Map<String, Map<String, String>> userCommands = new HashMap<String, Map<String, String>>();
        userCommands.put(commit.getUsername(), new HashMap<String, String>());
        userCommands.get(commit.getUsername()).put(commit.getRepository().getName(), "svnmerge command");

        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        Mock senderMock = mock(JavaMailSender.class);
        JavaMailSender sender = (JavaMailSender) senderMock.proxy();
        senderMock.expects(once()).method("createMimeMessage").withNoArguments().will(returnValue(message));
        senderMock.expects(once()).method("send").with(eq(message));

        MergeReport.setRequestorAddress("unit-test" + MergeReport.getMailDomain());
        emailDispatcher.processString = TEST_STRING;
        emailDispatcher.userCommits = userCommits;
        emailDispatcher.userCommands = userCommands;
        emailDispatcher.sender = sender;

        MergeReport.setNotifyAll(true);
        emailDispatcher.dispatch(null);
        assertEquals("Template name has changed", "svnmergeReminder.ftl", emailDispatcher.templateName);
        assertEquals("Incorrect email subject", "unmerged changes", message.getSubject());
        assertEquals("Incorrect from address", "no-reply" + MergeReport.getMailDomain(), message.getFrom()[0].toString());
        assertEquals("Incorrect to address", "user" + MergeReport.getMailDomain(), message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("Incorrect bcc address", "unit-test" + MergeReport.getMailDomain(), message.getRecipients(MimeMessage.RecipientType.BCC)[0].toString());
        assertEquals("Incorrect email subject", TEST_STRING, (((MimeMultipart) ((MimeBodyPart) ((MimeMultipart) message.getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent());
        assertEquals("Incorrect to content type", "text/html", (((MimeMultipart) ((MimeBodyPart) ((MimeMultipart) message.getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContent()).getBodyPart(0)).getDataHandler().getContentType());
    }

    /**
     * Test case for the <code>dispatch()</code> method
     * of class <code>EmailDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testDispatchException() throws Exception {
        Map<String, Map<String, List<Commit>>> userCommits = new HashMap<String, Map<String, List<Commit>>>();
        Map<String, Map<String, String>> userCommands = new HashMap<String, Map<String, String>>();

        Mock messageMock = mock(MockableMimeMessage.class);
        messageMock.expects(once()).method("setContent").withAnyArguments().will(throwException(new MessagingException("unit test")));
        Mock senderMock = mock(JavaMailSender.class);
        senderMock.expects(once()).method("createMimeMessage").withNoArguments().will(returnValue(messageMock.proxy()));

        MergeReport.setRequestorAddress("unit-test" + MergeReport.getMailDomain());
        emailDispatcher.processString = TEST_STRING;
        emailDispatcher.userCommits = userCommits;
        emailDispatcher.userCommands = userCommands;
        emailDispatcher.sender = (JavaMailSender) senderMock.proxy();

        try {
            emailDispatcher.dispatch(null);
            fail("should've thrown exception");
        } catch (RuntimeException e) {
            assertEquals("Exception message not as expected.", "An error occurred while trying to send the email.", e.getMessage());
        }
    }

    /**
     * Test case for the <code>setSender()</code> method
     * of class <code>EmailDispatcher</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetSender() throws Exception {
        JavaMailSender sender = new JavaMailSenderImpl();
        emailDispatcher.setSender(sender);
        assertNotNull("The sender variable was not set.", emailDispatcher.sender);
        assertEquals("The sender variable was not set correctly.", sender, emailDispatcher.sender);
    }

    public class TestableEmailDispatcher extends EmailDispatcher {
        public String templateName;
        public String processString;
        public Map<String, Map<String, List<Commit>>> userCommits;
        public Map<String, Map<String, String>> userCommands;

        protected String process(Map<String, Object> context, String templateName) {
            this.templateName = templateName;
            return processString;
        }

        Map<String, Map<String, List<Commit>>> getUserCommits(List<Commit> outstanding) {
            return userCommits;
        }

        Map<String, Map<String, String>> getUserCommands(List<Commit> outstanding) {
            return userCommands;
        }
    }

    public static class MockableMimeMessage extends MimeMessage {

        public MockableMimeMessage() {
            super((Session.getInstance(new Properties())));
        }
    }
}