package au.net.thehardings.ims.mergereport.process;

import java.io.File;
import java.util.Scanner;

import au.net.thehardings.ims.mergereport.AllTests;

/**
 * The class <code>SimpleProcessWrapperTest</code>
 */
public class SimpleProcessWrapperTest extends AllTests {
    SimpleProcessWrapper simpleProcessWrapper;

    /**
     * Test lifecycle method runs before each test case.
     *
     * @throws Exception if an error occurs during test
     */
    protected void setUp() throws Exception {
        super.setUp();
        simpleProcessWrapper = new SimpleProcessWrapper();
    }

    /**
     * Test case for the <code>runProcess()</code> method
     * of class <code>SimpleProcessWrapper</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRunProcess() throws Exception {
        Scanner scanner = simpleProcessWrapper.runProcess("cmd /c echo this is a test", new File("."));
        assertEquals("Process was not executed successfully.", "this is a test", scanner.nextLine());
    }

    /**
     * Test case for the <code>runProcess()</code> method
     * of class <code>SimpleProcessWrapper</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRunProcessException() throws Exception {
        File dir = new File(".");
        try {
            simpleProcessWrapper.runProcess("echo", dir);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            assertEquals("Not the expected exception message.", "An error occurred while running process 'echo' in directory '" + dir.getAbsolutePath() + "'", e.getMessage());
        }
    }
}