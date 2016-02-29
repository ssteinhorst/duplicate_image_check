import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ImageCompareTest {

    @Test
    public void testCheckNoMatch() throws Exception {
        File file1 = new File("./test/test_images/random.png");
        File file2 = new File("./test/test_images/inverted.png");
        ImageCompare compare = new ImageCompare(file1, file2);
        assertEquals(false, compare.check());
    }
    @Test
    public void testCheckValidMatch() throws Exception {
        File file1 = new File("./test/test_images/random.png");
        File file2 = new File("./test/test_images/random_copy.png");
        ImageCompare compare = new ImageCompare(file1, file2);
        assertEquals(true, compare.check());
    }
}