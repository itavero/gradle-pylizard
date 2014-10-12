package amnl.pylizard.mock;

import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Part of PyLizardPlugin
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 * @since 2014/10/12
 */
public class MockSourceDirectorySet extends DefaultSourceDirectorySet {
    public MockSourceDirectorySet(final String name, final String displayName, final FileResolver fileResolver) {
        super(name, displayName, fileResolver);
    }

    @Override
    public Set<File> getSrcDirs() {
        final Set<File> files = new HashSet<File>(2);
        files.add(new File(UUID.randomUUID().toString()));
        files.add(new File(UUID.randomUUID().toString()));
        return files;
    }
}
