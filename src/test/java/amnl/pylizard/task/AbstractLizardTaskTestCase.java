package amnl.pylizard.task;

import amnl.pylizard.LizardPlugin;
import amnl.pylizard.util.LizardRunner;
import junit.framework.TestCase;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Basic test case for testing a Task that runs Lizard.
 * Automatically adds a Mockito.spy to the LizardRunner.
 * Also adds some tests for setting and getting the internal
 * LizardRunner instance of an AbstractLizardTask.
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.task.AbstractLizardTask
 */
public abstract class AbstractLizardTaskTestCase<T extends AbstractLizardTask> extends TestCase {

    /**
     * Type of AbstractLizardTask that is being tested
     */
    protected final Class<T> taskType;

    /**
     * Instance of an AbstractLizardTask
     */
    protected T task;

    /**
     * Instance of LizardPlugin
     */
    protected LizardPlugin lizardPlugin;

    /**
     * Generated project to which lizardPlugin is added.
     */
    protected Project project;

    /**
     * Instance of LizardRunner that has been prepared with Mockito.spy
     *
     * @see org.mockito.Mockito#spy(Object)
     */
    protected LizardRunner lizardRunner;

    /**
     * @param taskType
     *         Type of AbstractLizardTask that is being tested.
     */
    public AbstractLizardTaskTestCase(final Class<T> taskType) {
        this.taskType = taskType;
    }

    /**
     * Calls setUpProjectAndTask() and sets the LizardRunner instance
     * of the Task to an instance that has been prepared with Mockito.spy()
     *
     * @see AbstractLizardTaskTestCase#setUpProjectAndTask()
     * @see amnl.pylizard.task.AbstractLizardTaskTestCase#lizardRunner
     */
    public void setUp() throws Exception {
        super.setUp();
        setUpProjectAndTask();
        lizardRunner = Mockito.spy(LizardRunner.getInstance());
        task.setLizardRunner(lizardRunner);
    }

    public void testCanNotSetLizardRunnerToNull() {
        task.setLizardRunner(null);
        assertNotNull(task.getLizardRunner());
    }

    public void testAutoInitializesLizardRunnerl() throws IOException {
        // Do setup again so the LizardRunner has not yet been set
        setUpProjectAndTask();
        assertNotNull(task.getLizardRunner());
    }

    /**
     * Builds a Gradle Project, adds the this plugin
     * and finds the task that is tested by this TestCase.
     */
    protected void setUpProjectAndTask() throws IOException {
        final String projectName = "TestProject";
        final File projectDir = Files.createTempDirectory(projectName).toFile();
        project = ProjectBuilder.builder().withProjectDir(projectDir).withName(projectName).build();
        project.getTasks().create("check");
        lizardPlugin = new LizardPlugin();
        lizardPlugin.apply(project);

        for (final Task t : project.getTasks()) {
            if (taskType.isInstance(t)) {
                task = (T) t;
                break;
            }
        }

        if (task == null) {
            fail("Task not added to Project");
        }
    }

}