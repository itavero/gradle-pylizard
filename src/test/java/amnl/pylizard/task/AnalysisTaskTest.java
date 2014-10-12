package amnl.pylizard.task;

import amnl.pylizard.LizardExtension;
import amnl.pylizard.exception.LizardExecutionException;
import amnl.pylizard.mock.MockSourceSet;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for AnalysisTask
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.task.AnalysisTask
 */
public class AnalysisTaskTest extends AbstractLizardTaskTestCase<AnalysisTask> {

    protected LizardExtension extension;
    protected File reportsDir;
    protected Set<String> includes;
    protected Set<String> excludes;
    protected Set<Object> sourceSets;

    public AnalysisTaskTest() {
        super(AnalysisTask.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Properties of LizardExtension
        reportsDir = Mockito.spy(new File("whatever"));
        includes = Mockito.spy(new HashSet<String>(1));
        includes.add("include");
        excludes = Mockito.spy(new HashSet<String>(1));
        excludes.add("include");
        sourceSets = Mockito.spy(new HashSet<Object>());
        sourceSets.add(new MockSourceSet("mock"));

        extension = project.getExtensions().getByType(LizardExtension.class);
        extension.setReportsDir(reportsDir);
        extension.setIncludes(includes);
        extension.setExcludes(excludes);
        extension.setSourceSets(sourceSets);

        Mockito.reset(reportsDir, includes, excludes, sourceSets);
    }

    public void testRunLizardCallsLizard() throws IOException, InterruptedException {
        task.runLizard();
        Mockito.verify(lizardRunner, Mockito.atLeastOnce()).runLizardWithArgs((String) Mockito.anyVararg());
    }

    public void testShouldThrowExceptionOnInvalidXml() throws IOException, InterruptedException {
        boolean exceptionThrown = false;
        Mockito.doReturn("this is not xml").when(lizardRunner).runLizardWithArgs(Mockito.anyListOf(String.class));

        try {
            task.runLizard();
        } catch (LizardExecutionException e) {
            exceptionThrown = true;
        }

        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
        assertTrue("LizardExecutionException expected", exceptionThrown);
    }

    public void testShouldCatchIOExceptionFromLizardRunner() throws IOException, InterruptedException {
        Mockito.doThrow(new IOException("test")).when(lizardRunner).runLizardWithArgs(Mockito.anyListOf(String.class));

        try {
            task.runLizard();
        } catch (Exception e) {
            fail("One exception escaped!");
        }

        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
    }

    public void testShouldCatchInterruptedExceptionFromLizardRunner() throws IOException, InterruptedException {
        Mockito.doThrow(new InterruptedException("test")).when(lizardRunner).runLizardWithArgs(Mockito.anyListOf(String.class));

        try {
            task.runLizard();
        } catch (Exception e) {
            fail("One exception escaped!");
        }

        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
    }

    public void testShouldAddWorkingThreadsFlagWhenNeeded() throws IOException, InterruptedException {
        extension.setNumberOfThreads(4);
        task.runLizard();

        // TODO Add assertion that checks the actual arguments
        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
    }

    public void testShouldNotAddWorkingThreadsFlagWhenUnnecessary() throws IOException, InterruptedException {
        extension.setNumberOfThreads(1);
        task.runLizard();

        // TODO Add assertion that checks the actual arguments
        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
    }

    public void testShouldCallExtensionGetIncludes() {
        task.runLizard();
        Mockito.verify(includes, Mockito.atLeastOnce()).iterator();
    }

    public void testShouldCallExtensionGetExcludes() {
        task.runLizard();
        Mockito.verify(excludes, Mockito.atLeastOnce()).iterator();
    }

    public void testShouldCallExtensionGetSourceSets() {
        task.runLizard();
        Mockito.verify(sourceSets, Mockito.atLeastOnce()).iterator();
    }

    public void testShouldCallExtensionGetReportsDir() {
        task.runLizard();
        Mockito.verify(reportsDir, Mockito.atLeastOnce()).getPath();
    }

    public void testShouldUseSrcDirectoryAsDefault() throws IOException, InterruptedException {
        includes.clear();
        sourceSets.clear();
        excludes.clear();

        task.runLizard();

        // TODO Add assertion that checks the actual arguments
        Mockito.verify(lizardRunner, Mockito.times(1)).runLizardWithArgs(Mockito.anyListOf(String.class));
    }

}