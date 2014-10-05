package amnl.pylizard.task;

import amnl.pylizard.LizardExtension;
import amnl.pylizard.exception.LizardExecutionException;
import amnl.pylizard.util.LizardRunner;
import amnl.pylizard.util.SourceSetAnalyzer;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Task that runs Lizard to analyze your code.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class AnalysisTask extends AbstractLizardTask {

    public static final String TASK_COMMAND = "lizard";

    /**
     * Runs Lizard with flags based on your Gradle configuration.
     */
    @TaskAction
    public void runLizard() throws LizardExecutionException {
        try {
            final String result = getLizardRunner().runLizardWithArgs(determineArgumentsFromExtension());

            if (!isValidXml(result)) {
                failWithMessage("Failed to run Lizard. Invalid XML returned.", result);
            }

            final File destination = getReportDestination();
            final String destinationPath = destination.getPath();

            if (!destination.getParentFile().exists() && !destination.getParentFile().mkdirs()) {
                failWithMessage("Failed to create parent directory.", destinationPath);
            }

            if (!storeStringToFile(result, destination)) {
                failWithMessage("Failed to store the report.", destinationPath);
            }

        } catch (IOException e) {
            getLogger().log(LogLevel.ERROR, "Lizard execution failed", e);
        } catch (InterruptedException e) {
            getLogger().log(LogLevel.ERROR, "Lizard execution interrupted", e);
        }
    }

    private void failWithMessage(final String message, final String... otherMessages) throws RuntimeException {
        getLogger().log(LogLevel.ERROR, message);
        for (final String m : otherMessages) {
            getLogger().log(LogLevel.ERROR, m);
        }
        throw new LizardExecutionException(message);
    }

    private List<String> determineArgumentsFromExtension() {
        final List<String> arguments = new ArrayList<String>(10);
        final File projectPath = new File(LizardRunner.getRelativePathFromCurrentWorkingDirectory(getProject().getProjectDir()));
        final LizardExtension extension = getProject().getExtensions().getByType(LizardExtension.class);

        // XML output
        arguments.add("--xml");

        // Number of threads
        final int numberOfThreads = extension.getNumberOfThreads();
        if (numberOfThreads > 1) {
            arguments.add("--working_threads=" + numberOfThreads);
        }

        // Exclude patterns
        for (final String pattern : extension.getExcludes()) {
            arguments.add("--exclude=\"" + getRelativePath(projectPath, pattern) + '"');
        }

        int includeCounter = 0;

        // Includes
        for (final String include : extension.getIncludes()) {
            arguments.add(getRelativePath(projectPath, include));
            includeCounter++;
        }

        // SourceSet
        final SourceSetAnalyzer sourceSetAnalyzer = new SourceSetAnalyzer();
        for (final Object sourceSet : extension.getSourceSets()) {
            sourceSetAnalyzer.setObject(sourceSet);
            sourceSetAnalyzer.analyze();
            for (final File directory : sourceSetAnalyzer.getSourceDirectories()) {
                arguments.add(getRelativePath(projectPath, directory));
                includeCounter++;
            }
        }

        // Add project folder if no includes are set
        if (includeCounter == 0) {
            arguments.add(getRelativePath(projectPath, "/"));
        }

        return arguments;
    }

    private String getRelativePath(final File projectPath, final File file) {
        if (file.isAbsolute()) {
            return prependPathWithRelativePath(LizardRunner.getRelativePathFromCurrentWorkingDirectory(file));
        }
        return getRelativePath(projectPath, file.getPath());
    }

    private String getRelativePath(final File projectPath, final String file) {
        final File path = new File(projectPath, file);
        return prependPathWithRelativePath(path);
    }

    private String prependPathWithRelativePath(final File path) {
        return prependPathWithRelativePath(path.getPath());
    }

    private String prependPathWithRelativePath(final String path) {
        if (path.charAt(0) == File.separatorChar) {
            return '.' + path;
        }
        return "./" + path;
    }

    private boolean isValidXml(final String xml) {
        // TODO Improve this check
        return xml != null && xml.charAt(0) == '<' && xml.contains("<cppncss>");
    }

    private File getReportDestination() {
        final LizardExtension extension = getProject().getExtensions().getByType(LizardExtension.class);
        File dir = extension.getReportsDir();

        if (!dir.isAbsolute()) {
            dir = new File(getProject().getBuildDir(), dir.getPath());
        }

        return new File(dir, "lizard.xml");
    }

    private boolean storeStringToFile(final String input, final File destination) {
        try {
            final PrintWriter writer = new PrintWriter(destination, "UTF-8");
            writer.print(input);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}