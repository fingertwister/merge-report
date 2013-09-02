package au.net.thehardings.ims.mergereport.dispatch;

import au.net.thehardings.ims.mergereport.model.Commit;

import javax.mail.MessagingException;
import java.util.List;

/**
 * The class <code>Dispatcher</code>
 */
public interface Dispatcher {
    void dispatch(List<Commit> outstanding) throws MessagingException;
}
