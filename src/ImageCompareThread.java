import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageCompareThread implements Runnable {
    private String image1;
    private String image2;
    private String path;

    public ImageCompareThread(String pth, String img1, String img2) {
        this.path = pth;
        this.image1 = img1;
        this.image2 = img2;
    }
    @Override
    public void run() {

        boolean dupfound;

        BufferedImage image1 = null;
        BufferedImage image2 = null;
        try {
            image1 = ImageIO.read(Files.newInputStream(Paths.get(this.path + this.image1)));
            image2 = ImageIO.read(Files.newInputStream(Paths.get(this.path + this.image2)));
        } catch (IOException e) {
            // this should never happen unless the file system is changing while
            // this is running
            e.printStackTrace();
        }

//        endTime = System.nanoTime();
//                        System.out.println("Opening files NEW STYLE took :"+ ((endTime - startTime)/1000000)+" ms");  //divide by 1000000 to get milliseconds.

//                        System.out.println("");
        // check the size, only continue if the size is identical
        if(isSameSize(image1, image2)) {
//            startTime = System.nanoTime();

            dupfound = isExactDuplicate(image1, image2);
//            endTime = System.nanoTime();
//                            System.out.println("isExactDuplicate took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

//            ++totalCompares;
            if(dupfound) {
                System.out.println("Dup " + path + this.image1 + " == " + path + this.image2);
            } else {
                System.out.println("NOT Dup " + path + this.image1 + " == " + path + this.image2);
            }
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

        for(int x = 0; x < image1.getWidth(); ++x) {
            for(int y = 0; y < image1.getHeight(); ++y) {
//                System.out.println(image.getRGB(x, y));

                int pixel1 = image1.getRGB(x, y);
                int pixel2 = image2.getRGB(x, y);
                if(pixel1 != pixel2) {
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

        return true;
    }
}
