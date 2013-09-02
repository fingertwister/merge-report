package au.net.thehardings.ims.mergereport;

import javax.mail.*;
import java.io.*;

/**
 * The class <code>DoNothingMergeReport</code> is here for testing purposes only
 */
public class DoNothingMergeReport extends MergeReport {

    static int runInvocationCount;
    static boolean throwException = false;

    public static void reset() {
        runInvocationCount = 0;
        throwException = false;
    }

    public void run() throws IOException, InterruptedException, MessagingException {
        runInvocationCount++;
        if (throwException) {
            throw new RuntimeException("Blow the program up!");
        }
    }
}
