package amnl.pylizard.util;

import amnl.pylizard.exception.LizardExecutionException;
import amnl.pylizard.mock.FakeSourceDirectorySet;
import amnl.pylizard.mock.FakeSourceSet;
import junit.framework.TestCase;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.tasks.SourceSet;
import org.mockito.Mockito;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for SourceSetAnalyzer
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.util.SourceSetAnalyzer
 */
public class SourceSetAnalyzerTest extends TestCase {

    SourceSetAnalyzer analyzer;
    SourceSet realSet;
    FakeSourceSet fakeSet;
    FakeSourceDirectorySet fakeDirSet;
    Set<File> directories;
    int numOfDirs;

    public void setUp() throws Exception {
        super.setUp();

        analyzer = new SourceSetAnalyzer();

        numOfDirs = 10;
        directories = new HashSet<File>(numOfDirs);
        for (int i = 0; i < numOfDirs; i++) {
            directories.add(new File("dir" + i)); // NOPMD
        }
        fakeDirSet = Mockito.spy(new FakeSourceDirectorySet(directories));
        fakeSet = Mockito.spy(new FakeSourceSet(fakeDirSet));
        analyzer.setObject(fakeSet);

        realSet = Mockito.spy(new DefaultSourceSet("Real", Mockito.mock(FileResolver.class)));
    }

    public void testEmptyCollectionAfterInitialization() {
        assertEquals(0, analyzer.getSourceDirectories().size());
    }

    public void testNullObjectShouldFailSilently() {
        analyzer.setObject(null);
        analyzer.analyze();
        assertEquals(0, analyzer.getSourceDirectories().size());
    }

    public void testThrowsExceptionOnUnuseableObject() {
        analyzer.setObject("unuseable");
        boolean exceptionThrown = false;
        try {
            analyzer.analyze();
        } catch (LizardExecutionException e) {
            exceptionThrown = true;
        }

        assertTrue("Expecting LizardExecutionException", exceptionThrown);
    }

    public void testAnalyzeCallsGetAllSourceOnRealSourceSet() {
        analyzer.setObject(realSet);
        analyzer.analyze();
        Mockito.verify(realSet, Mockito.atLeastOnce()).getAllSource();
    }

    public void testAnalyzeCallsGetAllSource() {
        analyzer.analyze();
        Mockito.verify(fakeSet, Mockito.atLeastOnce()).getAllSource();
    }

    public void testAnalyzeCallsGetAllJava() {
        analyzer.analyze();
        Mockito.verify(fakeSet, Mockito.atLeastOnce()).getAllJava();
    }

    public void testAnalyzeCallsGetJava() {
        analyzer.analyze();
        Mockito.verify(fakeSet, Mockito.atLeastOnce()).getJava();
    }

    public void testAnalyzeCallsGetSrcDirs() {
        analyzer.analyze();
        Mockito.verify(fakeDirSet, Mockito.atLeast(3)).getSrcDirs();
    }

    public void testGetSourceDirectoriesSize() {
        analyzer.analyze();
        assertEquals(numOfDirs, analyzer.getSourceDirectories().size());
    }

}