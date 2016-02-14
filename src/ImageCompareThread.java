import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageCompareThread implements Runnable {
    private BufferedImage image1;
    private String image2name;
    private String image1name;
    private String path;
    private ArrayList<String> listOfFilesToCheck;
    private int totalCompares;

    public ImageCompareThread(String pth, BufferedImage img1, String img1name, String img2, ArrayList<String> fileList, int totalCompares) {
        this.path = pth;
        this.image1 = img1;
        this.image2name = img2;
        this.image1name = img1name;
        this.totalCompares = totalCompares;
        //unused yet
        this.listOfFilesToCheck = fileList;
    }
    @Override
    public void run() {

        boolean dupfound;

        // wrap this in a loop and loop through all the compares in this one


//        BufferedImage image1 = null;
        BufferedImage image2 = null;
//            image1 = ImageIO.read(Files.newInputStream(Paths.get(this.path + this.image1)));
        try {
//            image2 = ImageIO.read(Files.newInputStream(Paths.get(this.path + this.image2name)));
            File file2 = new File(this.path + this.image2name);
            image2 = ImageIO.read(file2);


        } catch (IOException e) {
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
                System.out.println("Dup " + path + this.image1name + " == " + path + this.image2name);
            } else {
                System.out.println("NOT Dup " + path + this.image1name + " == " + path + this.image2name);
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
