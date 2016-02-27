import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DupCheck  {


    public static void main(String[] args){

        String path = "/Users/scotts/data/workspace/test_images/";
        boolean doRecursive = false;
//        String path = "/Users/scotts/Mine/DCIM/";
//        String path = "/Users/scotts/Mine/wallpaper/";

        // parse args for -r flag for recursive search
        // also populate the path
        for(int i = 0; i < args.length; ++i){
//        for (String arg : args) {
            if(args[i].equals("-r")) {
                doRecursive = true;
            } else if(args[i].equals("-p")) {
                path = args[i+1];
            }
        }
        try {
            checkForDuplicates(path, doRecursive);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkForDuplicates(String path, boolean doRecursive) throws IOException {
        File picsDir = new File(path);

        final BlockingQueue<ImageCompare> queue = new ArrayBlockingQueue<ImageCompare>(10000000);

        int totalCompares = 0;
//        long startTime, endTime;
        if(picsDir.exists()) {
            ArrayList<File> listOfFilesToCheck = getFilesIncludingSubdir(path);
//            List<String> listOfFilesToCheck = Arrays.asList(picsDir.list());
            System.out.println("directory holds " + listOfFilesToCheck.size() + " files");
            for(int i = 0; i < listOfFilesToCheck.size(); ++i) {

                if (resolvesAsImage(listOfFilesToCheck.get(i))) {

                    for (int u = i + 1; u < listOfFilesToCheck.size(); ++u) {
                        if (resolvesAsImage(listOfFilesToCheck.get(u))) {
//                            System.out.println("Checking: " + listOfFilesToCheck[i] + " " + listOfFilesToCheck[u]);
                            queue.add( new ImageCompare(path, listOfFilesToCheck.get(i), listOfFilesToCheck.get(u)) );
                            ++totalCompares;
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid Directory");
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);

        for(int i = 0; i < 1; i++){
            Runnable r = new Runnable(){
                public void run() {
                    ImageCompare workFile;
                    while((workFile = queue.poll()) != null){
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

    private static ArrayList<File> getFilesIncludingSubdir(String path) {
        return getFilesIncludingSubdir(path, new ArrayList<File>() );
    }

    private static ArrayList<File> getFilesIncludingSubdir(String path, ArrayList<File> results) {

        File picsDir = new File(path);
        File[] files = picsDir.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                getFilesIncludingSubdir(file.getPath(), results);
            } else if (resolvesAsImage(file)) {
                results.add(file);
            }
        }


        return results;
    }

    private static boolean resolvesAsImage(File file) {
        return resolvesAsImage(file.getName());
    }

    // named to be readable with statement "if (resolvesAsImage)"
    private static boolean resolvesAsImage(String file) {

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

