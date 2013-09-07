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
        Scanner scanner = simpleProcessWrapper.runProcess("echo this is a test", new File("."));
        assertEquals("Process was not executed successfully.", "this is a test", scanner.nextLine());
    }

    /**
     * Test case for the <code>runProcess()</code> method
     * of class <code>SimpleProcessWrapper</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRunProcessException() throws Exception {
        File dir = new File("bad-file-name");
        try {
            simpleProcessWrapper.runProcess("invalid-command", dir);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            String command = simpleProcessWrapper.convertCommand("invalid-command");
            assertEquals("Not the expected exception message.", String.format("An error occurred while running process '%s' in directory '%s'", command, dir.getAbsolutePath()), e.getMessage());
        }
    }
}