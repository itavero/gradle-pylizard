package amnl.pylizard.mock;

import java.io.File;
import java.util.Set;

/**
 * Used to test the reflection within SourceSetAnalyzer
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.util.SourceSetAnalyzer
 */
public class FakeSourceDirectorySet {

    private final Set<File> directories;

    public FakeSourceDirectorySet(final Set<File> directories) {
        this.directories = directories;
    }

    public Set<File> getSrcDirs() {
        return directories;
    }

}