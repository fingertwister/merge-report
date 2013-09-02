package au.net.thehardings.ims.mergereport.process;

import java.util.Scanner;
import java.io.File;

/**
 * The class <code>ProcessWrapper</code>
 */
public interface ProcessWrapper {
    Scanner runProcess(String command, File dir);
}
