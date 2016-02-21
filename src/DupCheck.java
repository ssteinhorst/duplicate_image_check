import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DupCheck  {


    public static void main(String[] args){

//        String path = "/Users/scotts/data/workspace/test/";
//        String path = "/Users/scotts/Mine/DCIM/";
//        String path = "/Users/scotts/Mine/wallpaper/";
        String path = args[0];
        try {
            checkForDuplicates(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void checkForDuplicates(String path) throws IOException {
        File picsDir = new File(path);

        final BlockingQueue<ImageCompareThread> queue = new ArrayBlockingQueue<ImageCompareThread>(10000000);

        int totalCompares = 0;
//        long startTime, endTime;
        if(picsDir.exists()){
            String[] listOfFilesToCheck = picsDir.list();
            System.out.println("directory holds " + listOfFilesToCheck.length + " files");
            for(int i = 0; i < listOfFilesToCheck.length; ++i) {

                if (stringResolvesAsImage(listOfFilesToCheck[i])) {

                    for (int u = i + 1; u < listOfFilesToCheck.length; ++u) {
                        if (stringResolvesAsImage(listOfFilesToCheck[u])) {
//                            System.out.println("Checking: " + listOfFilesToCheck[i] + " " + listOfFilesToCheck[u]);
                            queue.add( new ImageCompareThread(path, listOfFilesToCheck[i], listOfFilesToCheck[u]) );
                            ++totalCompares;
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid Directory");
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);

        for(int i = 0; i < 5; i++){
            Runnable r = new Runnable(){
                public void run() {
                    ImageCompareThread workFile;
                    while((workFile = queue.poll()) != null){
                        //work on the file.
//                        System.out.println("starting check of files "+workFile.image1name+" "+workFile.image2name);
                        workFile.check();
                    }
                }
            };
            pool.execute(r);
        }
        pool.shutdown();

        System.out.println("total compares: "+totalCompares);
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
}

