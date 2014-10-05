package amnl.pylizard.util;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * Tests for LizardRunner
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.util.LizardRunner
 */
public class LizardRunnerTest extends TestCase {

    public void testGetVersion() throws IOException, InterruptedException {
        final String result = LizardRunner.getInstance().runLizardWithArgs("--version");
        assertTrue("Version number check", Character.isDigit(result.charAt(0)));
    }

}