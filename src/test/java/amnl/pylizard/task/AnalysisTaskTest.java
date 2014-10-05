package amnl.pylizard.task;

import org.mockito.Mockito;

import java.io.IOException;

/**
 * Tests for AnalysisTask
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.task.AnalysisTask
 */
public class AnalysisTaskTest extends AbstractLizardTaskTestCase<AnalysisTask> {

    public AnalysisTaskTest() {
        super(AnalysisTask.class);
    }

    public void testRunLizardCallsLizard() throws IOException, InterruptedException {
        task.runLizard();
        Mockito.verify(lizardRunner, Mockito.atLeastOnce()).runLizardWithArgs((String) Mockito.anyVararg());
    }

}