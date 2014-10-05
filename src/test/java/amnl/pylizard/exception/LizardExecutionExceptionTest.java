package amnl.pylizard.exception;

import junit.framework.TestCase;

public class LizardExecutionExceptionTest extends TestCase {

    public void testMessageInConstructorSetsProperty() {
        final String message = "This is a test message.";
        final LizardExecutionException exception = new LizardExecutionException(message);
        assertEquals(message, exception.getMessage());
    }
}