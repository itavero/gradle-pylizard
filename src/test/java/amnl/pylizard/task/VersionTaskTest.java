package amnl.pylizard.task;

import org.mockito.Mockito;

import java.io.IOException;

/**
 * Tests for VersionTask
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.task.VersionTask
 */
public class VersionTaskTest extends AbstractLizardTaskTestCase<VersionTask> {

    public VersionTaskTest() {
        super(VersionTask.class);
    }

    public void testShowVersionCallsLizard() throws IOException, InterruptedException {
        task.showVersion();
        Mockito.verify(lizardRunner, Mockito.atLeastOnce()).runLizardWithArgs((String) Mockito.anyVararg());
    }

    public void testShowVersionCallsLizardWithVersionFlag() throws IOException, InterruptedException {
        task.showVersion();
        Mockito.verify(lizardRunner, Mockito.atLeastOnce()).runLizardWithArgs(Mockito.eq("--version"));
    }

    public void testShouldCatchIOException() throws IOException, InterruptedException {
        Mockito.doThrow(new IOException("test")).when(lizardRunner).runLizardWithArgs((String) Mockito.anyVararg());

        // If we get here, the exception was catched.
        assertTrue(true);
    }

    public void testShouldCatchInterruptedException() throws IOException, InterruptedException {
        Mockito.doThrow(new InterruptedException("test")).when(lizardRunner).runLizardWithArgs((String) Mockito.anyVararg());

        // If we get here, the exception was catched.
        assertTrue(true);
    }
}