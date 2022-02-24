package dm;

import java.io.File;

/**
 * TestDir
 */
public class TestDir {

    public static void main(String[] args) {
        File datasets = new File("dataset/public");
        System.out.println(datasets.getAbsolutePath());
        if(datasets.exists()){
            System.out.println(datasets.getPath());
        }
    }
}