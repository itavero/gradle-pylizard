package amnl.pylizard.mock;

/**
 * Used to test the reflection within SourceSetAnalyzer
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.util.SourceSetAnalyzer
 */
public class FakeSourceSet {

    private final FakeSourceDirectorySet directorySet;

    public FakeSourceSet(final FakeSourceDirectorySet directorySet) {
        this.directorySet = directorySet;
    }

    public FakeSourceDirectorySet getAllJava() {
        return directorySet;
    }

    public FakeSourceDirectorySet getAllSource() {
        return getAllJava();
    }

    public FakeSourceDirectorySet getJava() {
        return getAllJava();
    }

}