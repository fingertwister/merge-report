package au.net.thehardings.ims.mergereport.process;

import au.net.thehardings.ims.mergereport.AllTests;

import java.io.File;
import java.util.Scanner;

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
        String cmd = "echo this is a test";
        if (System.getProperty("os.name").startsWith("Windows")) {
            cmd = "cmd /c " + cmd;
        }
        Scanner scanner = simpleProcessWrapper.runProcess(cmd, new File("."));
        assertEquals("Process was not executed successfully.", "this is a test", scanner.nextLine());
    }

    /**
     * Test case for the <code>runProcess()</code> method
     * of class <code>SimpleProcessWrapper</code>
     *
     * @throws Exception if an error occurs during test
     */
    public void testRunProcessException() throws Exception {
        File dir = new File("invalid-directory");
        try {
            simpleProcessWrapper.runProcess("invalid-command", dir);
        } catch (Exception e) {
            assertEquals("Not the expected exception message.", String.format("An error occurred while running process 'invalid-command' in directory '%s'", dir.getAbsolutePath()), e.getMessage());
        }
    }
}