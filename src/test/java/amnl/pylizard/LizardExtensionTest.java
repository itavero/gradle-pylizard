package amnl.pylizard;

import junit.framework.TestCase;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Tests for LizardExtension
 *
 * @author Arno Moonen <info@arnom.nl>
 * @see amnl.pylizard.LizardExtension
 */
public class LizardExtensionTest extends TestCase {

    LizardExtension lizardExtension;

    public void setUp() throws Exception {
        super.setUp();

        lizardExtension = new LizardExtension();
    }

    public void testGetReportsDir() {
        assertNotNull(lizardExtension.getReportsDir());
    }

    public void testSetReportsDir() {
        final File value = new File(UUID.randomUUID().toString());
        lizardExtension.setReportsDir(value);
        assertEquals(value, lizardExtension.getReportsDir());
    }

    public void testGetSourceSets() {
        assertNotNull(lizardExtension.getSourceSets());
    }

    public void testSetSourceSets() {
        final Set<Object> value = new HashSet<Object>();
        lizardExtension.setSourceSets(value);
        assertEquals(value, lizardExtension.getSourceSets());
    }

    public void testSetNumberOfThreads() {
        final int value = 25;
        lizardExtension.setNumberOfThreads(value);
        assertEquals(value, lizardExtension.getNumberOfThreads());
    }

    public void testDefaultNumberOfThreads() {
        final int count = Runtime.getRuntime().availableProcessors();
        assertEquals(count, lizardExtension.getNumberOfThreads());
    }

    public void testGetIncludes() {
        assertNotNull(lizardExtension.getIncludes());
    }

    public void testSetIncludes() {
        final Set<String> value = new HashSet<String>();
        lizardExtension.setIncludes(value);
        assertEquals(value, lizardExtension.getIncludes());
    }

    public void testGetExcludes() {
        assertNotNull(lizardExtension.getExcludes());
    }

    public void testSetExcludes() {
        final Set<String> value = new HashSet<String>();
        lizardExtension.setExcludes(value);
        assertEquals(value, lizardExtension.getExcludes());
    }
    
}