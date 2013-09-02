package au.net.thehardings.ims.mergereport;

import au.net.thehardings.ims.mergereport.dispatch.*;
import au.net.thehardings.ims.mergereport.model.*;
import au.net.thehardings.ims.mergereport.process.*;
import org.jmock.*;

import java.io.*;
import java.util.*;

/**
 * The class <code>MergeReportTest</code>
 */
public class MergeReportTest extends AllTests {
    TestableMergeReport mergeReport;

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("mail.host", "mail.mycompany.com");
        System.setProperty("mail.domain", "mycompany.com");
        mergeReport = new TestableMergeReport();
    }

    protected void tearDown() throws Exception {
        MergeReport.requestorAddress = null;
        MergeReport.contextFile = null;
    }

    /**
     * Test case for the <code>main()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testMain() throws Exception {
        DoNothingMergeReport.reset();
        try {
            MergeReport.main(new String[]{"-f","unit-test-context.xml"});
        } catch (Throwable t) {
            fail("The main method should never throw any throwable.");
        }
        assertTrue("the run method was not invoked.", DoNothingMergeReport.runInvocationCount == 1);
    }

    /**
     * Test case for the <code>main()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testMainException() throws Exception {
        DoNothingMergeReport.reset();
        DoNothingMergeReport.throwException = true;
        try {
            MergeReport.main(new String[]{"-f","unit-test-context.xml"});
        } catch (Throwable t) {
            fail("The main method should never throw any throwable.");
        }
        assertTrue("the run method was not invoked.", DoNothingMergeReport.runInvocationCount == 1);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs1() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"help"});
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs2() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"?"});
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs3() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"-?"});
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs4() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"-help"});
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs5() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"-e"});
        assertNull("requestorAddress should be null.", MergeReport.requestorAddress);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs6() throws Exception {
        MergeReport.parseArgs(new String[]{"-e", TEST_STRING});
        assertEquals("requestorAddress should be set.", TEST_STRING, MergeReport.requestorAddress);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs7() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.parseArgs(new String[]{"-f"});
        assertNull("contextFile should be null.", MergeReport.contextFile);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs8() throws Exception {
        MergeReport.parseArgs(new String[]{"-f", TEST_STRING});
        assertEquals("contextFile should be set.", TEST_STRING, MergeReport.contextFile);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs9() throws Exception {
        MergeReport.parseArgs(new String[0]);
        assertFalse("notifyAll should default to false.", MergeReport.notifyAll);
    }

    /**
     * Test case for the <code>parseArgs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testParseArgs10() throws Exception {
        MergeReport.parseArgs(new String[]{"-a"});
        assertTrue("notifyAll should be true.", MergeReport.notifyAll);
    }

    /**
     * Test case for the <code>showUsage()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testShowUsage() throws Exception {
        MergeReport.out = getShowUsageMock();
        MergeReport.showUsage();
    }

    /**
     * Test case for the <code>run()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRun() throws Exception {
        List<Repository> pairs = new ArrayList<Repository>();
        pairs.add(new Repository("branch-name", "trunk1", "repositoryName1", "repositoryUrl1"));
        pairs.add(new Repository("branch-name", "trunk2", "repositoryName2", "repositoryUrl2"));
        List<Commit> outstanding = new ArrayList<Commit>();

        Mock dispatcherMock = mock(Dispatcher.class);
        dispatcherMock.expects(once()).method("dispatch").with(eq(outstanding));

        mergeReport.outstanding = outstanding;
        mergeReport.dispatcher = (Dispatcher) dispatcherMock.proxy();
        mergeReport.repositories = pairs;
        mergeReport.overrideGetUnmergedCommits = true;
        mergeReport.run();

        assertEquals("getnmergedCommits should have been called twice", 2, mergeReport.getUnmergedCommitsInvocationCount);
    }

    /**
     * Test case for the <code>getUnmergedCommits()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUnmergedCommits() throws Exception {
        Repository pair = new Repository("branch-name", "trunk", "repositoryName", "repositoryUrl");
        StringBuffer commitLog = new StringBuffer();
        commitLog.append("r519\nr550\nr554\nr567\n");
        commitLog.append("\n");
        Scanner scanner = new Scanner(new ByteArrayInputStream(commitLog.toString().getBytes()));

        Mock processorMock = mock(ProcessWrapper.class);
        processorMock.expects(once()).method("runProcess").with(eq("svn mergeinfo --show-revs eligible " + pair.getUrl() + "/branches/" + pair.getBranchName()), eq(pair.getBase())).will(returnValue(scanner));
        mergeReport.processor = (ProcessWrapper) processorMock.proxy();

        mergeReport.overrideGetCommit = true;
        mergeReport.getUnmergedCommits(pair);
        assertTrue("Commit id's not parsed successfully.", mergeReport.outstanding.size() == 4);
        assertEquals("Commit id's not parsed successfully.", "519", mergeReport.outstanding.get(0).getCommitId());
        assertEquals("Commit id's not parsed successfully.", "550", mergeReport.outstanding.get(1).getCommitId());
        assertEquals("Commit id's not parsed successfully.", "554", mergeReport.outstanding.get(2).getCommitId());
        assertEquals("Commit id's not parsed successfully.", "567", mergeReport.outstanding.get(3).getCommitId());
    }

    /**
     * Test case for the <code>getCommitIds()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetCommitIds() throws Exception {
        List<Integer> test = mergeReport.getCommitIds(null);
        assertNotNull("Should never be null - 1", test);
        assertTrue("Commit id's were not parsed successfully - 1", test.size() == 0);

        test = mergeReport.getCommitIds("");
        assertNotNull("Should never be null - 2", test);
        assertTrue("Commit id's were not parsed successfully - 2", test.size() == 0);

        test = mergeReport.getCommitIds("100");
        assertNotNull("Should never be null - 3", test);
        assertTrue("Commit id's were not parsed successfully - 3", test.size() == 1);
        assertTrue("Commit id 100 not found", test.contains(100));

        test = mergeReport.getCommitIds("200,201,202");
        assertNotNull("Should never be null - 4", test);
        assertTrue("Commit id's were not parsed successfully - 4", test.size() == 3);
        assertTrue("Commit id 200 not found", test.contains(200));
        assertTrue("Commit id 201 not found", test.contains(201));
        assertTrue("Commit id 202 not found", test.contains(202));

        test = mergeReport.getCommitIds("300:302");
        assertNotNull("Should never be null - 5", test);
        assertTrue("Commit id's were not parsed successfully - 5", test.size() == 3);
        assertTrue("Commit id 300 not found", test.contains(300));
        assertTrue("Commit id 301 not found", test.contains(301));
        assertTrue("Commit id 302 not found", test.contains(302));

        test = mergeReport.getCommitIds("400,401:402,404,410:412,431");
        assertNotNull("Should never be null - 6", test);
        assertTrue("Commit id's were not parsed successfully - 6", test.size() == 8);
        assertTrue("Commit id 400 not found", test.contains(400));
        assertTrue("Commit id 401 not found", test.contains(401));
        assertTrue("Commit id 402 not found", test.contains(402));
        assertTrue("Commit id 404 not found", test.contains(404));
        assertTrue("Commit id 410 not found", test.contains(410));
        assertTrue("Commit id 411 not found", test.contains(411));
        assertTrue("Commit id 412 not found", test.contains(412));
        assertTrue("Commit id 431 not found", test.contains(431));
    }

    /**
     * Test case for the <code>getCommit()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetCommit() throws Exception {
        int commitId = 100;
        Repository pair = new Repository("branch-name", "trunk", "repositoryName", "repositoryUrl");
        StringBuffer commitLog = new StringBuffer();
        commitLog.append("------------------------------------------------------------------------").append("\n");
        commitLog.append("r100 | user | 2007-09-21 12:12:52 +0900 (Fri, 21 Sep 2007) | 3 lines").append("\n");
        commitLog.append("\n");
        commitLog.append("comment line 1").append("\n");
        commitLog.append("comment line 2").append("\n");
        commitLog.append("comment line 3").append("\n");
        commitLog.append("------------------------------------------------------------------------").append("\n");
        commitLog.append("\n");
        Scanner scanner = new Scanner(new ByteArrayInputStream(commitLog.toString().getBytes()));

        Mock processorMock = mock(ProcessWrapper.class);
        processorMock.expects(once()).method("runProcess").with(eq("svn log " + pair.getUrl() + " -r " + commitId), eq(pair.getBase())).will(returnValue(scanner));
        mergeReport.processor = (ProcessWrapper) processorMock.proxy();

        Commit commit = mergeReport.getCommit(100, pair);
        assertEquals("branch name was not correct", "branch-name", commit.getBranchName());
        assertTrue("comment was not successfully read", commit.getComment().size() == 3);
        assertEquals("comment 1 was not correct", "comment line 1", commit.getComment().get(0));
        assertEquals("comment 2 was not correct", "comment line 2", commit.getComment().get(1));
        assertEquals("comment 3 was not correct", "comment line 3", commit.getComment().get(2));
        assertEquals("commit id was not correct", Integer.toString(commitId), commit.getCommitId());
        assertEquals("date was not correct", "2007-09-21 12:12:52 +0900 (Fri, 21 Sep 2007)", commit.getDate());
        assertEquals("repository name was not correct", pair, commit.getRepository());
        assertEquals("username was not correct", "user", commit.getUsername());
    }

    /**
     * Test case for the <code>setProcessor()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetProcessor() throws Exception {
        ProcessWrapper processor = new SimpleProcessWrapper();
        mergeReport.setProcessor(processor);
        assertNotNull("The processor variable was not set.", mergeReport.processor);
        assertEquals("The processor variable was not set correctly.", processor, mergeReport.processor);
    }

    /**
     * Test case for the <code>setDispatcher()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetDispatcher() throws Exception {
        Dispatcher dispatcher = new EmailDispatcher();
        mergeReport.setDispatcher(dispatcher);
        assertNotNull("The dispatcher variable was not set.", mergeReport.dispatcher);
        assertEquals("The dispatcher variable was not set correctly.", dispatcher, mergeReport.dispatcher);
    }

    /**
     * Test case for the <code>setDirectoryPairs()</code> method
     * of class <code>MergeReport</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetRepositories() throws Exception {
        List<Repository> list = new ArrayList<Repository>();
        mergeReport.setRepositories(list);
        assertNotNull("The repositories variable was not set.", mergeReport.repositories);
        assertEquals("The repositories variable was not set correctly.", list, mergeReport.repositories);
    }

    PrintStream getShowUsageMock() {
        Mock mockOut = mock(MockablePrintStream.class);
        mockOut.expects(once()).method("println").with(eq("Usage: MergeReport [-f <context file>] [-e <email addresses>] [-a] [-auto]"));
        mockOut.expects(once()).method("println").with(eq(" The system properties mail.host and mail.domain must be set."));
        mockOut.expects(once()).method("println").with(eq(" Options:"));
        mockOut.expects(once()).method("println").with(eq("  -f <context file>: the spring context file for the report (default merge-report.xml)"));
        mockOut.expects(once()).method("println").with(eq("  -e <email addresses>: comma separated email to always send report to (default system user)"));
        mockOut.expects(once()).method("println").with(eq("  -a: send report to users that have outstanding merges"));
        return (PrintStream) mockOut.proxy();
    }

    public static class MockablePrintStream extends PrintStream {
        public MockablePrintStream() throws FileNotFoundException {
            super("/temp/test.txt");
        }
    }

    public class TestableMergeReport extends MergeReport {

        boolean overrideGetUnmergedCommits = false;
        int getUnmergedCommitsInvocationCount = 0;

        void getUnmergedCommits(Repository pair) throws IOException, InterruptedException {
            if (overrideGetUnmergedCommits) {
                getUnmergedCommitsInvocationCount++;
            } else {
                super.getUnmergedCommits(pair);
            }
        }

        boolean overrideGetCommit = false;

        Commit getCommit(Integer commitId, Repository pair) throws IOException, InterruptedException {
            if (overrideGetCommit) {
                return new Commit.Builder(pair, pair.getBranchName(), commitId).username("user").date("date").comment(new ArrayList<String>()).build();
            } else {
                return super.getCommit(commitId, pair);
            }
        }
    }
}