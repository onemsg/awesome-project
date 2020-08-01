package com.onemsg.infosharing.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class FileManagementTest {

    FileManagement fm = null;

    @Before
    public void init() {
        try {
            fm = FileManagement.create();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRoot() throws IOException {

        String root = "webroot/content";

        URI rootURL = null;
        try {
            rootURL = ClassLoader.getSystemResource(root).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("rootURL: " + rootURL.getPath());
        System.out.println("rootURL: " + rootURL);
        
        boolean isExists =  Paths.get(rootURL).toFile().exists();
        System.out.println(root + " is exists? " + isExists);

        var list = Files.list(Path.of(rootURL) ).collect(Collectors.toList());
        list.forEach( path ->{
            System.out.println(path.getFileName());
        } );

        Path file = Paths.get(Path.of(rootURL).toString() , "Python数据科学速查表 - Spark SQL 基础.pdf");
        System.out.println(file);
    }


    @Test
    public void testList(){

        List<Path> list = fm.list();
        System.out.println(fm.getRoot() + " has files:");
        list.forEach( path -> {
            System.out.println(path.getFileName() + ": " + path.toString());
        });
    }

    @Test
    public void testUnzip(){
        // String zip = "Desktop.zip";
        // fm.unzip(zip);
    }
}