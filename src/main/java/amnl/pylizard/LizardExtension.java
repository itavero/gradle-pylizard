package amnl.pylizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Configuration options
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class LizardExtension {

    /**
     * The directory where the report (lizard.xml) will be generated.
     */
    protected File reportsDir;

    /**
     * Collection of objects that provide a SourceSet.
     * Preferably this should be instances of the org.gradle.api.tasks.SourceSet interface.
     * Some plugins (like the Android plug-in) do not implement this interface, so the plugin uses reflection if it can not cast the Object to SourceSet.
     *
     * @see org.gradle.api.tasks.SourceSet
     */
    protected Collection<Object> sourceSets;

    /**
     * Number of threads that Lizard should use.
     * Defaults to the number of processors available to the JVM.
     *
     * @see Runtime#availableProcessors()
     */
    protected int numberOfThreads;

    /**
     * Paths to include when analyzing the code complexity.
     * These should be relative to the root of the project.
     */
    protected Collection<String> includes;

    /**
     * Paths to exclude when analyzing the code complexity.
     * These should be relative to the root of the project.
     */
    protected Collection<String> excludes;

    public LizardExtension() {
        reportsDir = new File("reports");
        sourceSets = new ArrayList<Object>();
        includes = new ArrayList<String>();
        excludes = new ArrayList<String>();
        numberOfThreads = Runtime.getRuntime().availableProcessors();
    }

    /**
     * @see amnl.pylizard.LizardExtension#reportsDir
     */
    public File getReportsDir() {
        return reportsDir;
    }

    /**
     * @see amnl.pylizard.LizardExtension#reportsDir
     */
    public void setReportsDir(final File reportsDir) {
        this.reportsDir = reportsDir;
    }

    /**
     * @see amnl.pylizard.LizardExtension#sourceSets
     */
    public Collection<Object> getSourceSets() {
        return sourceSets;
    }

    /**
     * @see amnl.pylizard.LizardExtension#sourceSets
     */
    public void setSourceSets(final Collection<Object> sourceSets) {
        this.sourceSets = sourceSets;
    }

    /**
     * @see amnl.pylizard.LizardExtension#numberOfThreads
     */
    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    /**
     * @see amnl.pylizard.LizardExtension#numberOfThreads
     */
    public void setNumberOfThreads(final int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    /**
     * @see amnl.pylizard.LizardExtension#includes
     */
    public Collection<String> getIncludes() {
        return includes;
    }

    /**
     * @see amnl.pylizard.LizardExtension#includes
     */
    public void setIncludes(final Collection<String> includes) {
        this.includes = includes;
    }

    /**
     * @see amnl.pylizard.LizardExtension#excludes
     */
    public Collection<String> getExcludes() {
        return excludes;
    }

    /**
     * @see amnl.pylizard.LizardExtension#excludes
     */
    public void setExcludes(final Collection<String> excludes) {
        this.excludes = excludes;
    }

}