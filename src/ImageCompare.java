import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCompare {
    public String image1name;
    public String image2name;
    private String path;

    private File file1;
    private File file2;

//    public ImageCompare(String pth, String img1name, String img2name) {
//        this.path = pth;
//        this.image1name = img1name;
//        this.image2name = img2name;
//    }

    public ImageCompare(String pth, File img1, File img2) {
        this.path = pth;
        this.file1 = img1;
        this.file2 = img2;
    }

    public boolean check() {

        boolean dupfound;
//        long startTime, endTime;

        BufferedImage image1 = null;
        BufferedImage image2 = null;
        try {

//            startTime = System.nanoTime();

//            File file1 = new File(this.path + this.image1name);
//            File file2 = new File(this.path + this.image2name);

//            endTime = System.nanoTime();
//            System.out.println("creating file objects took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

            // Only process files of equal size, good for exact maches
            if(file1.length() != file2.length()) {
                return false;
            }

//            startTime = System.nanoTime();

            image1 = ImageIO.read(this.file1);
            image2 = ImageIO.read(this.file2);

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
            System.out.println("MSE: " + this.getMSE(image1, image2));

            if(dupfound) {
                System.out.println("Dup " + path + this.image1name + " == " + path + this.image2name);
                return true;
            } else {
//                System.out.println("NOT Dup " + path + this.image1name + " == " + path + this.image2name);
                return false;
            }
        } else {
//            System.out.println(this.image1name + " - "+this.image2name+" are not same size");
            return false;
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

    private double getMSE(BufferedImage image1, BufferedImage image2) {

        double total = 0;
        for(int y = 0; y < image1.getHeight(); ++y) {
            for(int x = 0; x < image1.getWidth(); ++x){
                total += (((image1.getRGB(x, y) >> 16) & 0xFF) - ((image2.getRGB(x, y) >> 16) & 0xFF)) * (((image1.getRGB(x, y) >> 16) & 0xFF) - ((image2.getRGB(x, y) >> 16) & 0xFF));
            }

        }

        return (total / (image1.getWidth() * image1.getHeight()));
//        (rgb >> 16) & 0xFF

        //Calculates the MSE between two images
//        private double MSE(Bitmap original, Bitmap enhanced)
//        {
//            Size imgSize = original.Size;
//            double total = 0;
//
//            for (int y = 0; y < imgSize.Height; y++)
//            {
//                for (int x = 0; x < imgSize.Width; x++)
//                {
//                    total += System.Math.Pow(original.GetPixel(x, y).R - enhanced.GetPixel(x, y).R, 2);
//                              original.GetPixel(x, y).R gets the red pixel
//                }
//
//            }
//
//            return (total / (imgSize.Width * imgSize.Height));
//        }







    }
}
