package amnl.pylizard.util;

import amnl.pylizard.exception.LizardExecutionException;
import org.gradle.api.GradleException;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is class is responsible for running Lizard and collecting its output.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 */
public class LizardRunner {

    protected static final Logger LOGGER = Logging.getLogger(LizardRunner.class);

    protected LizardRunner() {
        // Protected constructor so it can only be instantiated internally
    }

    /**
     * @return Shared instance of LizardRunner
     */
    public static LizardRunner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Helper method that returns the path to 'file', relative to 'basePath'.
     */
    public static String getRelativePath(final File basePath, final File file) {
        return basePath.toURI().relativize(file.toURI()).getPath();
    }

    /**
     * Helper method that returns the path to 'file', relative to the current working directory.
     */
    public static String getRelativePathFromCurrentWorkingDirectory(final File file) {
        File here = new File(".");
        try {
            here = here.getCanonicalFile();
        } catch (IOException e) {
            here = here.getAbsoluteFile();
        }
        return getRelativePath(here, file);
    }

    /**
     * Run Lizard with the given arguments.
     * Calls the vararg implementation internally.
     *
     * @return Output of the command
     *
     * @see LizardRunner#runLizardWithArgs(String...)
     */
    public String runLizardWithArgs(final List<String> arguments) throws IOException, InterruptedException, GradleException {
        return runLizardWithArgs(arguments.toArray(new String[]{}));
    }

    /**
     * Run Lizard with the given arguments.
     *
     * @return Output of the command
     */
    public String runLizardWithArgs(final String... args) throws IOException, InterruptedException, GradleException {
        Process process = null;
        try {
            process = startProcess(args);
        } catch (IOException e) {
            final String message = "It looks like Lizard is not installed on this machine!";
            failWithMessage(message);
        }
        return getOutputForProcess(process);
    }

    private void failWithMessage(final String message, final String... otherMessages) throws RuntimeException {
        LOGGER.log(LogLevel.ERROR, message);
        for (final String m : otherMessages) {
            LOGGER.log(LogLevel.ERROR, m);
        }
        throw new LizardExecutionException(message);
    }

    private Process startProcess(final String... args) throws IOException {
        final List<String> command = new ArrayList<String>(args.length + 1);
        command.add("lizard");
        command.addAll(Arrays.asList(args));

        outputCommand(command);

        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        return processBuilder.start();
    }

    private void outputCommand(final List<String> command) {
        final StringBuilder stringBuilder = new StringBuilder("Command: ");
        for (final String a : command) {
            stringBuilder.append(a);
            stringBuilder.append(' ');
        }
        LOGGER.debug(stringBuilder.toString().trim());
    }

    private String getOutputForProcess(final Process process) throws IOException, InterruptedException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
        final StringBuilder stringBuilder = new StringBuilder("");

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }

        process.waitFor();
        reader.close();

        return stringBuilder.toString();
    }

    protected static class SingletonHolder {
        protected static final LizardRunner INSTANCE = new LizardRunner();
    }
}