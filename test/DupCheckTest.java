import org.junit.Test;

import java.io.IOException;

public class DupCheckTest {
    @Test
    public void evaluatesExpression() throws IOException {

        DupCheck dupCheck = new DupCheck();
        dupCheck.checkForDuplicates("/Users/scotts/data/workspace/duplicate_checker/test/test_images", true);
        if (!true) throw new AssertionError();
    }
}