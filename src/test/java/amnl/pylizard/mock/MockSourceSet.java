package amnl.pylizard.mock;

import org.gradle.api.PathValidation;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.resources.ReadableResource;
import org.gradle.internal.Factory;
import org.gradle.internal.typeconversion.NotationParser;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

/**
 * Part of PyLizardPlugin
 *
 * @author Arno Moonen {@literal <info@arnom.nl>}
 * @since 2014/10/12
 */
public class MockSourceSet extends DefaultSourceSet {

    protected final MockFileResolver mockFileResolver;

    public MockSourceSet(final String name) {
        super(name, new MockFileResolver());
        mockFileResolver = new MockFileResolver();
    }

    @Override
    public SourceDirectorySet getJava() {
        return new MockSourceDirectorySet("java", "java", mockFileResolver);
    }

    @Override
    public SourceDirectorySet getAllJava() {
        return new MockSourceDirectorySet("java", "java", mockFileResolver);
    }

    @Override
    public SourceDirectorySet getAllSource() {
        return new MockSourceDirectorySet("all", "all", mockFileResolver);
    }

    public static class MockFileResolver implements FileResolver {

        @Override
        public File resolve(final Object path) {
            if (path instanceof String) {
                return new File((String) path);
            }
            if (path instanceof File) {
                return (File) path;
            }
            if (path instanceof Path) {
                return ((Path) path).toFile();
            }
            return new File(path.toString());
        }

        @Override
        public ReadableResource resolveResource(final Object path) {
            return null;
        }

        @Override
        public File resolve(final Object path, final PathValidation validation) {
            return resolve(path);
        }

        @Override
        public Factory<File> resolveLater(final Object path) {
            return null;
        }

        @Override
        public FileCollection resolveFiles(final Object... paths) {
            return null;
        }

        @Override
        public FileTree resolveFilesAsTree(final Object... paths) {
            return null;
        }

        @Override
        public FileTree compositeFileTree(final List<FileTree> fileTrees) {
            return null;
        }

        @Override
        public URI resolveUri(final Object path) {
            return null;
        }

        @Override
        public String resolveAsRelativePath(final Object path) {
            return null;
        }

        @Override
        public NotationParser<Object, File> asNotationParser() {
            return null;
        }
    }
}
