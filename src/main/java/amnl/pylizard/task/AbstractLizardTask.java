package amnl.pylizard.task;

import amnl.pylizard.util.LizardRunner;
import org.gradle.api.DefaultTask;

/**
 * Abstract class that contains a LizardRunner property.
 * Tasks that call Lizard should extend from this class.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 * @see amnl.pylizard.util.LizardRunner
 */
public abstract class AbstractLizardTask extends DefaultTask {

    private volatile LizardRunner lizardRunner;

    /**
     * Returns an instance of LizardRunner.
     * If no internal reference was set, it returns
     * a shared instance of LizardRunner.
     */
    protected LizardRunner getLizardRunner() {
        if (lizardRunner == null) {
            lizardRunner = LizardRunner.getInstance();
        }
        return lizardRunner;
    }

    /**
     * If you wish to use a different LizardRunner than
     * the default shared instance, you can set it
     * explicitly using this method.
     *
     * @param runner
     *         Internal property will not be changed if this is null
     */
    public void setLizardRunner(final LizardRunner runner) {
        if (runner != null) {
            this.lizardRunner = runner;
        }
    }
}