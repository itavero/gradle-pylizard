package amnl.pylizard.task;

import amnl.pylizard.exception.LizardExecutionException;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

/**
 * Task that runs Lizard to determine it's version and prints out that version number.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class VersionTask extends AbstractLizardTask {

    public static final String TASK_COMMAND = "lizardVersion";

    /**
     * Run this task (lizard -- version)
     */
    @TaskAction
    public void showVersion() throws LizardExecutionException {
        try {
            final String result = getLizardRunner().runLizardWithArgs("--version");
            getLogger().quiet("Version:\t" + result);
        } catch (IOException e) {
            getLogger().log(LogLevel.ERROR, "Lizard execution failed", e);
        } catch (InterruptedException e) {
            getLogger().log(LogLevel.ERROR, "Lizard execution interrupted", e);
        }
    }

}