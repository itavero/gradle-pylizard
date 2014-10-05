package amnl.pylizard;

import amnl.pylizard.task.AnalysisTask;
import amnl.pylizard.task.VersionTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Set;

/**
 * Gradle plugin that integrates the Lizard Code Complexity tool into your build automation.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class LizardPlugin implements Plugin<Project> {

    /**
     * Adds all the Lizard tasks to the given Project.
     * Also adds a dependency on AnalysisTask to the 'check' task (if present).
     */
    @Override
    public void apply(final Project project) {
        project.getExtensions().create("lizard", LizardExtension.class);

        project.getTasks().create(VersionTask.TASK_COMMAND, VersionTask.class);
        project.getTasks().create(AnalysisTask.TASK_COMMAND, AnalysisTask.class);

        final Set<Task> checkTasks = project.getTasksByName("check", false);
        for (final Task t : checkTasks) {
            t.getDependsOn().add(AnalysisTask.TASK_COMMAND);
        }
    }
}