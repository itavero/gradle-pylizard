package amnl.pylizard.exception;

/**
 * Exception used by every Task within this Plugin.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class LizardExecutionException extends RuntimeException {
    public LizardExecutionException(final String message) {
        super(message);
    }
}