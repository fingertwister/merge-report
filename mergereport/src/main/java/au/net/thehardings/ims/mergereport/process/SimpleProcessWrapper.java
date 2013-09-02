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
    Process p;

    public Scanner runProcess(String command, File dir) {
        LOG.debug("Running command '" + command + "' in directory '" + dir.getAbsolutePath() + "'.");
        try {
            p = Runtime.getRuntime().exec(command, null, dir);
            return new Scanner(p.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while running process '" + command + "' in directory '" + dir.getAbsolutePath() + "'", e);
        }
    }
}
