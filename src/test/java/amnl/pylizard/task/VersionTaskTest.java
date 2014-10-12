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

    public void testShouldCatchIOExceptionFromLizardRunner() throws IOException, InterruptedException {
        Mockito.doThrow(new IOException("test")).when(lizardRunner).runLizardWithArgs((String) Mockito.anyVararg());

        try {
            task.showVersion();
        } catch (Exception e) {
            fail("One exception escaped!");
        }

        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs((String) Mockito.anyVararg());
    }

    public void testShouldCatchInterruptedExceptionFromLizardRunner() throws IOException, InterruptedException {
        Mockito.doThrow(new InterruptedException("test")).when(lizardRunner).runLizardWithArgs((String) Mockito.anyVararg());

        try {
            task.showVersion();
        } catch (Exception e) {
            fail("One exception escaped!");
        }

        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs((String) Mockito.anyVararg());
    }
}