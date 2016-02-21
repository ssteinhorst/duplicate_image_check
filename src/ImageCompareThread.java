import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCompareThread{
    public String image1name;
    public String image2name;
    private String path;

    public ImageCompareThread(String pth, String img1name, String img2name) {
        this.path = pth;
        this.image1name = img1name;
        this.image2name = img2name;
    }

    public void check() {

        boolean dupfound;
//        long startTime, endTime;

        BufferedImage image1 = null;
        BufferedImage image2 = null;
        try {

//            startTime = System.nanoTime();

            File file1 = new File(this.path + this.image1name);
            File file2 = new File(this.path + this.image2name);

//            endTime = System.nanoTime();
//            System.out.println("creating file objects took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

            if(file1.length() != file2.length()) {
                return;
            }

//            startTime = System.nanoTime();

            image1 = ImageIO.read(file1);
            image2 = ImageIO.read(file2);

//            endTime = System.nanoTime();
//            System.out.println("creating Image objects took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

        } catch (IOException e) {
            e.printStackTrace();
        }

        // check the resolution, only continue if it is identical
        if(isSameSize(image1, image2)) {
//            startTime = System.nanoTime();

            dupfound = isExactDuplicate(image1, image2);
//            endTime = System.nanoTime();
//            System.out.println("isExactDuplicate took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

            if(dupfound) {
                System.out.println("Dup " + path + this.image1name + " == " + path + this.image2name);
            } else {
//                System.out.println("NOT Dup " + path + this.image1name + " == " + path + this.image2name);
            }
        } else {
//            System.out.println(this.image1name + " - "+this.image2name+" are not same size");
        }
    }

    private boolean isSameSize(BufferedImage image1, BufferedImage image2) {

//        System.out.println(image1.getHeight() +""+ image2.getHeight() +""+ image1.getWidth() +""+ image2.getWidth());
        // return true if sizes are identical
        if(image1.getHeight() == image2.getHeight() && image1.getWidth() == image2.getWidth()) {
            return true;
        }
        return false;
    }

    private boolean isExactDuplicate(BufferedImage image1, BufferedImage image2) {
        int x, y;
        for(x = 0; x < image1.getWidth(); ++x) {
            for(y = 0; y < image1.getHeight(); ++y) {
//                System.out.println(image.getRGB(x, y));

                int pixel1 = image1.getRGB(x, y);
                int pixel2 = image2.getRGB(x, y);
                if(pixel1 != pixel2) {
//                    System.out.println("isExactDuplicate x:"+x+" y:"+y);
                    return false;
                }

//                Color color = new Color(pixel);
//                System.out.println("pixel "+color.toString());

//                int red = (pixel & 0x00ff0000) >> 16;
//                int green = (pixel & 0x0000ff00) >> 8;
//                int blue  =  pixel & 0x000000ff;
//                System.out.println("Red Color value = "+ red);
//                System.out.println("Green Color value = "+ green);
//                System.out.println("Blue Color value = "+ blue);
            }
        }
//        System.out.println("isExactDuplicate x:"+x+" y:"+y);
        return true;
    }
}
