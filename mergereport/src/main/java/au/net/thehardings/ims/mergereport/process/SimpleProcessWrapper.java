package au.net.thehardings.ims.mergereport.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The class <code>SimpleProcessWrapper</code>
 */
public class SimpleProcessWrapper implements ProcessWrapper {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProcessWrapper.class);
    static String OS = null;
    Process p;

    public Scanner runProcess(String command, File dir) {
        String cmd = convertCommand(command);
        LOG.debug("Running command '" + cmd + "' in directory '" + dir.getAbsolutePath() + "'.");
        try {
            p = Runtime.getRuntime().exec(cmd, null, dir);
            return new Scanner(p.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while running process '" + cmd + "' in directory '" + dir.getAbsolutePath() + "'", e);
        }
    }

    String convertCommand(String command) {
        return isWindows() ? "cmd /c " + command : command;
    }

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }
}
