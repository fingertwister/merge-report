package au.net.thehardings.ims.mergereport;

import org.jmock.cglib.MockObjectTestCase;

/**
 * Just a test utility base class
 */
public abstract class AllTests extends MockObjectTestCase implements TestConstants {

    /**
     * Ensure that the TestApplicationContext is configured
     * with a Mock BeanFactory;
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
