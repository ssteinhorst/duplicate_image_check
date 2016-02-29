import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DupCheckTest {
    @Test
    public void testNonRecursiveSearch() throws IOException {

        DupCheck dupCheck = new DupCheck();
        ArrayList<File> list = dupCheck.getFiles("/Users/scotts/data/workspace/duplicate_checker/test/test_images", false);
        assertEquals("Non recursive search should have ", 6, list.size());
    }

    @Test
    public void testRecursiveSearch() throws IOException {
        DupCheck dupCheck = new DupCheck();

        ArrayList<File> list =  dupCheck.getFiles("/Users/scotts/data/workspace/duplicate_checker/test/test_images", true);
        assertEquals("Recursive file count should be: ", 8, list.size());
    }
}