import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class DupCheck extends Thread {
    private String img1;
    private String img2;

    public DupCheck(String a, String b) {
        this.img1 = a;
        this.img2 = b;
    }
    public void run() {

    }
    public static void main(String[] args){

        //            getpixels(args[0]);
        // inside a loop that compares each item in the list to
        // each other, compare each picture and if a dup is found,
        // add to a map of key,value

        // so

        // for each 2 pictures:
        // determine if images are the same size
        // no = no dup
        // yes means we check pixels
        // check pixel call takes 2 BufferedImage objects
        // and returns true/false
        // for each true, update
        // a map of each file as the key and a string of results as the value
        // then come up with a clever way to process the map to output

//        String path = "/Users/scotts/data/workspace/test/";
//        String path = "/Users/scotts/Mine/DCIM/";
        String path = "/Users/scotts/Mine/wallpaper/";

        try {
            System.out.println(checkForDuplicates(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean checkForDuplicates(String path) throws IOException {
        File picsDir = new File(path);
        boolean dupfound = false;

        int totalCompares = 0;
        long startTime, endTime;
        if(picsDir.exists()){
            String[] listOfFilesToCheck = picsDir.list();
            System.out.println("directory holds " + listOfFilesToCheck.length + " files");
            for(int i = 0; i < listOfFilesToCheck.length; ++i) {
                for(int u = i + 1; u < listOfFilesToCheck.length; ++u) {
                    boolean stringsResolve = stringResolvesAsImage(listOfFilesToCheck[u]) && stringResolvesAsImage(listOfFilesToCheck[i]);

                    if(stringsResolve) {


                        // create some kind of image class here
                        // image class should implement runnable
                        // call start on it, should spin off and check for the next compare
                        // should image class take a compare? yea, each thread compares two images and
                        // prints out the results

                        // how to check for the return? if we find animage we need to mark it
                        // do that in the runnable? I'll need some storage for the results then
                        // or I guess I can just print it off for now. Thats what I'm doing anyway




//                        startTime = System.nanoTime();
//
//                        File file1 = new File(path + listOfFilesToCheck[i]);
//                        File file2 = new File(path + listOfFilesToCheck[u]);
//                        BufferedImage image11 = ImageIO.read(file1);
//                        BufferedImage image22 = ImageIO.read(file2);
//
//                        endTime = System.nanoTime();
//                        System.out.println("Opening files OLD STYLE took :"+ ((endTime - startTime)/1000000)+" ms");  //divide by 1000000 to get milliseconds.


                        startTime = System.nanoTime();

                        BufferedImage image1 = ImageIO.read(Files.newInputStream(Paths.get(path + listOfFilesToCheck[i])));
                        BufferedImage image2 = ImageIO.read(Files.newInputStream(Paths.get(path + listOfFilesToCheck[u])));

                        endTime = System.nanoTime();
//                        System.out.println("Opening files NEW STYLE took :"+ ((endTime - startTime)/1000000)+" ms");  //divide by 1000000 to get milliseconds.

//                        System.out.println("");
                        // check the size, only continue if the size is identical
                        if(isSameSize(image1, image2)) {
                            startTime = System.nanoTime();

                            dupfound = isExactDuplicate(image1, image2);
                            endTime = System.nanoTime();
//                            System.out.println("isExactDuplicate took :"+ (endTime - startTime) + " ns");  //divide by 1000000 to get milliseconds.

                            ++totalCompares;
                            if(dupfound) {
                                System.out.println("Dup " + path + listOfFilesToCheck[i] + " == " + path + listOfFilesToCheck[u]);
                            } else {
//                                System.out.println("NOT Dup " + path + listOfFilesToCheck[i] + " == " + path + listOfFilesToCheck[u]);
                            }
                        }

                    }
                }
            }
        } else {
            System.out.println("Invalid Directory");
        }
        System.out.println("total compares: "+totalCompares);
        return dupfound;
    }

    // named to be readable with statement "if (stringResolvesAsImage)"
    public static boolean stringResolvesAsImage(String file) {

        boolean retVal = false;
        String validTypes = "jpeg, jpg, tiff, png, bmp";
        int fileExtensionLocation = file.lastIndexOf('.');
        if(fileExtensionLocation > 0) {
            String extension = file.substring(fileExtensionLocation + 1);
            if(validTypes.toLowerCase().contains(extension.toLowerCase())) {
                retVal = true;
            } else {
//                System.out.println("extension: "+extension+" is not valid image type.");
            }
        }
        return retVal;
    }

    public static boolean isSameSize(BufferedImage image1, BufferedImage image2) {

//        System.out.println(image1.getHeight() +""+ image2.getHeight() +""+ image1.getWidth() +""+ image2.getWidth());
        // return true if sizes are identical
        if(image1.getHeight() == image2.getHeight() && image1.getWidth() == image2.getWidth()) {
            return true;
        }
        return false;
    }

    public static boolean isExactDuplicate(BufferedImage image1, BufferedImage image2) {

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

    public static double getMSE(String pic1, String pic2) throws IOException {
        double retVal = 0;
        File file1= new File(pic1);
        BufferedImage image1 = ImageIO.read(file1);
        File file2= new File(pic1);
        BufferedImage image2 = ImageIO.read(file2);
        for(int x = 0; x < image1.getWidth(); ++x) {
            for(int y = 0; y < image1.getHeight(); ++y) {
//                total
            }
        }
        return retVal;
    }

    // debug function, outputs all the pixel values for the image at the given path
    public static void getpixels(String arg) throws IOException {
        File file= new File(arg);
        BufferedImage image = ImageIO.read(file);
        // outputs all the pixel vales of the given image.
        // this overloads the buffer ATM so you probably dont want to run it for large images
        for(int x = 0; x < image.getWidth(); ++x) {
            for(int y = 0; y < image.getHeight(); ++y) {
//                System.out.println(image.getRGB(x, y));

                int pixel = image.getRGB(x,y);
                Color color = new Color(pixel);
                System.out.println("pixel "+color.toString());

//                int red = (pixel & 0x00ff0000) >> 16;
//                int green = (pixel & 0x0000ff00) >> 8;
//                int blue  =  pixel & 0x000000ff;
//                System.out.println("Red Color value = "+ red);
//                System.out.println("Green Color value = "+ green);
//                System.out.println("Blue Color value = "+ blue);
            }
        }
    }
}

//    //Calculates the MSE between two images
//    private double MSE(Bitmap original, Bitmap enhanced)
//    {
//        Size imgSize = original.Size;
//        double total = 0;
//
//        for (int y = 0; y < imgSize.Height; y++)
//        {
//            for (int x = 0; x < imgSize.Width; x++)
//            {
//                total += System.Math.Pow(original.GetPixel(x, y).R - enhanced.GetPixel(x, y).R, 2);
//
//            }
//
//        }
//
//        return (total / (imgSize.Width * imgSize.Height));
//    }