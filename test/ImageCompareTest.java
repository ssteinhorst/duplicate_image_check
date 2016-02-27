import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ImageCompareTest {

    @Test
    public void testCheck() throws Exception {
//        File file1 = new File("/Users/scotts/data/workspace/duplicate_checker/test/test_images/random.png");
//        File file2 = new File("/Users/scotts/data/workspace/duplicate_checker/test/test_images/inverted.png");
        File file1 = new File("./test/test_images/random.png");
        File file2 = new File("./test/test_images/inverted.png");
        ImageCompare compare = new ImageCompare("./", file1, file2);
        assertEquals(false, compare.check());
    }
}