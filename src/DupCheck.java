import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DupCheck  {


    public static void main(String[] args){
        String path = null;
        boolean doRecursive = false;

        // parse args for -r flag for recursive search
        // also populate the path
        for(int i = 0; i < args.length; ++i){
            if(args[i].equals("-r")) {
                doRecursive = true;
            } else if(args[i].equals("-p")) {
                path = args[i+1];
            }
        }
        try {
            checkForDuplicates(path, doRecursive);
        } catch (IOException e) {
            //print usage info
            System.out.println("outputs the path of duplicate images");
            System.out.println("-r for recursive");
            System.out.println("-p to pass a path to scan");
            System.out.println("Example: duplicate_checker -r -p /path/to/images");
//            e.printStackTrace();
        }
    }

    public static void checkForDuplicates(String path, boolean doRecursive) throws IOException {

        // TODO: this code removed, threading not needed yet but still fun
        // TODO: clean up this queue, arbitary huge queue should work better
//        final BlockingQueue<ImageCompare> queue = new ArrayBlockingQueue<ImageCompare>(10000000);
        ArrayList<File> listOfFilesToCheck = getFiles(path, doRecursive);

        // TODO: seperate the putting of things into the queue and
        // the running of the things from the queue
        // they need to run async

        int totalCompares = 0;

        if(listOfFilesToCheck.size() > 0) {
            System.out.println("directory holds " + listOfFilesToCheck.size() + " files");
            for(int i = 0; i < listOfFilesToCheck.size(); ++i) {
                for (int u = i + 1; u < listOfFilesToCheck.size(); ++u) {
                    (new ImageCompare(listOfFilesToCheck.get(i), listOfFilesToCheck.get(u))).check();
                    ++totalCompares;

                }
            }
        } else {
            return;
        }


// TODO: this code removed, threading not needed yet but still fun
//        long startTime, endTime;
//        if(listOfFilesToCheck.size() > 0) {
//            System.out.println("directory holds " + listOfFilesToCheck.size() + " files");
//            for(int i = 0; i < listOfFilesToCheck.size(); ++i) {
//                for (int u = i + 1; u < listOfFilesToCheck.size(); ++u) {
//                    queue.add( new ImageCompare(listOfFilesToCheck.get(i), listOfFilesToCheck.get(u)) );
//                    ++totalCompares;
//                }
//            }
//        } else {
//            return;
//        }
//
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//
//        for(int i = 0; i < 1; i++){
//            Runnable r = new Runnable(){
//                public void run() {
//                    ImageCompare workFile;
//                    while((workFile = queue.poll()) != null){
//                        workFile.check();
//                    }
//                }
//            };
//            pool.execute(r);
//        }
//        pool.shutdown();

        System.out.println("total compares: "+totalCompares);
    }

    public static ArrayList<File> getFiles(String path, boolean doRecursive) {
        File filesPath = new File(path);
        if(filesPath.exists()) {
            // we are looking for images recursively in this code path
            if (doRecursive) {
                return getFilesIncludingSubdir(path, new ArrayList<File>());
            } else {
                // just return the top level images in this codepath
                ArrayList<File> singleLevelFileList = new ArrayList<File>();
                for(File file : filesPath.listFiles()) {
                    if(resolvesAsImage(file)) {
                        singleLevelFileList.add(file);
                    }
                }
                return singleLevelFileList;
            }
        } else {
            // invalid file path, return empty list and output error message
            System.out.println("getFiles error: Invalid path");
            return new ArrayList<File>();
        }
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

    public static boolean resolvesAsImage(File file) {
        return resolvesAsImage(file.getName());
    }

    // named to be readable with statement "if (resolvesAsImage)"
    public static boolean resolvesAsImage(String file) {

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

