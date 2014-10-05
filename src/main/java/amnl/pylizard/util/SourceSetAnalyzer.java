package amnl.pylizard.util;

import amnl.pylizard.exception.LizardExecutionException;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.SourceSet;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Extracts source directories from a SourceSet or an Object with the same method signatures.
 * This is needed because some plugins do not implement the SourceSet interface.
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 * @see org.gradle.api.tasks.SourceSet
 */
public class SourceSetAnalyzer {

    private static final Logger LOGGER = Logging.getLogger(SourceSetAnalyzer.class);
    private final Set<File> sourceDirectories;
    private Object object;

    public SourceSetAnalyzer() {
        this.object = null;
        this.sourceDirectories = new HashSet<File>();
    }

    /**
     * Set the Object to extract the directories from.
     * Call this method before calling analyze()
     *
     * @see SourceSetAnalyzer#analyze()
     */
    public void setObject(final Object object) {
        sourceDirectories.clear();
        this.object = object;
    }

    /**
     * Actually get the directories from the Object.
     * First it checks to see if the Object implements SourceSet,
     * otherwise it tries to get the directories using reflection.
     * Before calling this method, be sure to set the internal object
     * reference using setObject()
     *
     * @see SourceSetAnalyzer#setObject(Object)
     * @see org.gradle.api.tasks.SourceSet#getAllSource()
     * @see org.gradle.api.tasks.SourceSet#getAllJava()
     * @see org.gradle.api.tasks.SourceSet#getJava()
     */
    public void analyze() {
        if (object == null) {
            return;
        }

        if (object instanceof SourceSet) {
            sourceDirectories.addAll(((SourceSet) object).getAllSource().getSrcDirs());
            return;
        }

        final String[] methodsToTry = {"getAllSource", "getAllJava", "getJava"};
        boolean atLeastOneSuccess = false;
        for (final String methodName : methodsToTry) {
            if (tryToGetDirectoriesWithMethod(methodName)) {
                atLeastOneSuccess = true;
            }
        }

        if (!atLeastOneSuccess) {
            failWithMessage("Unusable SourceSet of type " + object.getClass().getCanonicalName());
        }
    }

    /**
     * Call this method after running analyze.
     *
     * @return All source directories that were found.
     *
     * @see SourceSetAnalyzer#analyze()
     */
    public Set<File> getSourceDirectories() {
        return sourceDirectories;
    }

    private void failWithMessage(final String message, final String... otherMessages) throws RuntimeException {
        LOGGER.log(LogLevel.ERROR, message);
        for (final String m : otherMessages) {
            LOGGER.log(LogLevel.ERROR, m);
        }
        throw new LizardExecutionException(message);
    }

    private boolean tryToGetDirectoriesWithMethod(final String methodName) {
        final Collection<File> directories = tryToGetSourceSetDirectoriesUsingMethod(methodName);
        if (directories != null) {
            sourceDirectories.addAll(directories);
            return true;
        }
        return false;
    }

    private Collection<File> tryToGetSourceSetDirectoriesUsingMethod(final String methodName) {
        final Object sourceSet = tryToInvokeMethod(object, methodName);
        return tryToGetSourceDirectoriesFromSourceSet(sourceSet);
    }

    private Collection<File> tryToGetSourceDirectoriesFromSourceSet(final Object sourceSet) {
        if (sourceSet == null) {
            return null;
        }

        if (sourceSet instanceof SourceDirectorySet) {
            final SourceDirectorySet set = (SourceDirectorySet) sourceSet;
            return set.getSrcDirs();
        }

        final String methodName = "getSrcDirs";
        final Object srcDirs = tryToInvokeMethod(sourceSet, methodName);
        if (srcDirs instanceof Collection<?>) {
            try {
                return (Collection<File>) srcDirs;
            } catch (ClassCastException e) {
                LOGGER.debug("Can not cast result of '" + methodName + "' to Collection<File>");
            }
        }

        return null;
    }

    private Object tryToInvokeMethod(final Object obj, final String methodName) {
        try {
            final Method method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        } catch (NoSuchMethodException e) {
            LOGGER.debug("Method '" + methodName + "' not found in " + obj.getClass().getCanonicalName());
        } catch (InvocationTargetException e) {
            LOGGER.debug("Failed to invoke '" + methodName + "' on instance of " + obj.getClass().getCanonicalName());
        } catch (IllegalAccessException e) {
            LOGGER.debug("'" + methodName + "' on instance of " + obj.getClass().getCanonicalName() + " is inaccessible");
        }
        return null;
    }
}
