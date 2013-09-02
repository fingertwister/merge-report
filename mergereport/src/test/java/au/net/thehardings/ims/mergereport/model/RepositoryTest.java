package au.net.thehardings.ims.mergereport.model;

import au.net.thehardings.ims.mergereport.AllTests;

import java.io.File;

/**
 * The class <code>RepositoryTest</code>
 */
public class RepositoryTest extends AllTests {
    Repository repository;

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        repository = new Repository();
    }

    /**
     * Test case for the <code>Repository()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRepository() throws Exception {
        repository = new Repository(VALUE00, VALUE01, VALUE02, VALUE03);
        assertEquals("The branchName variable was not retrieved correctly.", VALUE00, repository.getBranchName());
        assertEquals("The base variable was not retrieved correctly.", VALUE01, repository.getBase().getName());
        assertEquals("The repository name was not retrieved correctly.", VALUE02, repository.getName());
        assertEquals("The repository url was not retrieved correctly.", VALUE03, repository.getUrl());
    }

    /**
     * Test case for the <code>getBranchName()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetBranchName() throws Exception {
        repository.branchName = TEST_STRING;
        assertNotNull("The branchName variable was not retrieved.", repository.getBranchName());
        assertEquals("The branchName variable was not retrieved correctly.", TEST_STRING, repository.getBranchName());
    }

    /**
     * Test case for the <code>getBranchName()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetBranchUrl() throws Exception {
        repository.branchName = "branchName";
        repository.url = "url";
        assertNotNull("The branchName variable was not retrieved.", repository.getBranchUrl());
        assertEquals("The branchName variable was not retrieved correctly.", "url/branches/branchName", repository.getBranchUrl());
        repository.branchName = "trunk";
        assertEquals("The trunk url was not retrieved correctly.", "url/trunk", repository.getBranchUrl());
    }

    /**
     * Test case for the <code>setBranchName()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetBranchName() throws Exception {
        repository.setBranchName(TEST_STRING);
        assertNotNull("The branchName variable was not set.", repository.branchName);
        assertEquals("The branchName variable was not set correctly.", TEST_STRING, repository.branchName);
    }

    /**
     * Test case for the <code>getBase()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetBase() throws Exception {
        repository.base = new File(TEST_STRING);
        assertNotNull("The base variable was not retrieved.", repository.getBase());
        assertEquals("The base variable was not retrieved correctly.", TEST_STRING, repository.getBase().getName());
    }

    /**
     * Test case for the <code>setBasePath()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetBasePath() throws Exception {
        repository.setBasePath(TEST_STRING);
        assertNotNull("The base variable was not set.", repository.base);
        assertEquals("The base variable was not set correctly.", TEST_STRING, repository.base.getName());
    }

    /**
     * Test case for the <code>getName()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetName() throws Exception {
        repository.name = TEST_STRING;
        assertNotNull("The name variable was not retrieved.", repository.getName());
        assertEquals("The name variable was not retrieved correctly.", TEST_STRING, repository.getName());
    }

    /**
     * Test case for the <code>setName()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetName() throws Exception {
        repository.setName(TEST_STRING);
        assertNotNull("The name variable was not set.", repository.name);
        assertEquals("The name variable was not set correctly.", TEST_STRING, repository.name);
    }

    /**
     * Test case for the <code>getUrl()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testGetUrl() throws Exception {
        repository.url = TEST_STRING;
        assertNotNull("The url variable was not retrieved.", repository.getUrl());
        assertEquals("The url variable was not retrieved correctly.", TEST_STRING, repository.getUrl());
    }

    /**
     * Test case for the <code>setUrl()</code> method
     * of class <code>Repository</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testSetUrl() throws Exception {
        repository.setUrl(TEST_STRING);
        assertNotNull("The url variable was not set.", repository.url);
        assertEquals("The url variable was not set correctly.", TEST_STRING, repository.url);
    }
}