package au.net.thehardings.ims.mergereport.model;

import au.net.thehardings.ims.mergereport.AllTests;

import java.util.Collections;
import java.util.List;

/**
 * The class <code>CommitTest</code>
 */
public class CommitTest extends AllTests {
    Commit commit;
    Repository repository = new Repository();
    String branchName = "branchName";
    String commitId = "1234";
    String username = "username";
    String date = "date";
    List<String> comment = Collections.emptyList();

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        Commit.Builder builder = new Commit.Builder(repository, branchName, Integer.valueOf(commitId));
        commit = new Commit.Builder(repository, branchName, Integer.valueOf(commitId)).username(username).date(date).comment(comment).build();
    }

    /**
     * Test case for the <code>getRepository()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testMandatoryParams() throws Exception {
        assertNotNull("The repository variable was not retrieved.", commit.getRepository());
        assertEquals("The repository variable was not retrieved correctly.", repository, commit.getRepository());
    }

    /**
     * Test case for the <code>getBranchName()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetBranchName() throws Exception {
        assertNotNull("The branchName variable was not retrieved.", commit.getBranchName());
        assertEquals("The branchName variable was not retrieved correctly.", branchName, commit.getBranchName());
    }

    /**
     * Test case for the <code>getCommitId()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetCommitId() throws Exception {
        assertNotNull("The commitId variable was not retrieved.", commit.getCommitId());
        assertEquals("The commitId variable was not retrieved correctly.", commitId, commit.getCommitId());
    }

    /**
     * Test case for the <code>getUsername()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUsername() throws Exception {
        assertNotNull("The username variable was not retrieved.", commit.getUsername());
        assertEquals("The username variable was not retrieved correctly.", username, commit.getUsername());
    }

    /**
     * Test case for the <code>getDate()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetDate() throws Exception {
        assertNotNull("The date variable was not retrieved.", commit.getDate());
        assertEquals("The date variable was not retrieved correctly.", date, commit.getDate());
    }

    /**
     * Test case for the <code>getComment()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetComment() throws Exception {
        assertNotNull("The comment variable was not retrieved.", commit.getComment());
        assertEquals("The comment variable was not retrieved correctly.", comment, commit.getComment());
    }

    /**
     * Test case for the <code>Commit()</code> method
     * of class <code>Commit</code>
     *
     * @throws Exception if an error occurs during test
     */
//    public void testCommit() throws Exception {
//        List<String> testList = new ArrayList<String>();
//        Repository repository = new Repository();
//        commit = new Commit(repository, VALUE00, ONE, VALUE01, VALUE02, testList);
//        assertEquals("The repository was not set", repository, commit.repository);
//        assertEquals("The branchName was not set", VALUE00, commit.branchName);
//        assertEquals("The commit id was not set", ONE.toString(), commit.commitId);
//        assertEquals("The username was not set", VALUE01, commit.username);
//        assertEquals("The date was not set", VALUE02, commit.date);
//        assertEquals("The comment was not set", testList, commit.comment);
//    }
}