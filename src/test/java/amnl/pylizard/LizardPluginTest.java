package amnl.pylizard;

import amnl.pylizard.task.AnalysisTask;
import amnl.pylizard.task.VersionTask;
import junit.framework.TestCase;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.util.Set;

/**
 * Tests for LizardPlugin
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.LizardPlugin
 */
public class LizardPluginTest extends TestCase {

    LizardPlugin lizardPlugin;
    Project project;

    public void setUp() throws Exception {
        super.setUp();

        final String projectName = "TestProject";
        final File projectDir = Files.createTempDirectory(projectName).toFile();
        final Project gradleProject = ProjectBuilder.builder().withProjectDir(projectDir).withName(projectName).build();
        project = Mockito.spy(gradleProject);

        lizardPlugin = new LizardPlugin();
    }

    public void testApplyAddsExtension() {
        lizardPlugin.apply(project);
        assertNotNull(project.getExtensions().getByType(LizardExtension.class));
    }

    public void testApplyAddsVersionTask() {
        lizardPlugin.apply(project);
        final Set<Task> tasks = project.getTasksByName(VersionTask.TASK_COMMAND, false);
        assertEquals(1, tasks.size());
    }

    public void testApplyAddsAnalysisTask() {
        lizardPlugin.apply(project);
        final Set<Task> tasks = project.getTasksByName(AnalysisTask.TASK_COMMAND, false);
        assertEquals(1, tasks.size());
    }

    public void testApplyAddsDependencyToCheckTask() {
        project.task("check");
        lizardPlugin.apply(project);
        final Set<Task> tasks = project.getTasksByName("check", false);
        boolean dependencyFound = false;
        for (final Task t : tasks) {
            final Set<Task> dependencies = (Set<Task>) t.getTaskDependencies().getDependencies(t);
            for (final Task dependency : dependencies) {
                if (dependency instanceof AnalysisTask) {
                    dependencyFound = true;
                }
            }
        }
        assertTrue("Dependency found", dependencyFound);
    }

    public void testApplyCallsGetTaskByNameCheck() {
        lizardPlugin.apply(project);
        Mockito.verify(project, Mockito.atLeastOnce()).getTasksByName(Mockito.eq("check"), Mockito.anyBoolean());
    }

}