package com.onemsg.infosharing.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class FileManagementImpl implements FileManagement {

    public static final String DEFAULT_ROOT = "webroot/content";
    private String ROOT = null;
    private Path ABSOULTE_ROOT = null;

    public FileManagementImpl() throws URISyntaxException {
        ROOT = DEFAULT_ROOT;
        ABSOULTE_ROOT = Path.of(ClassLoader.getSystemResource(ROOT).toURI());
    }

    public FileManagementImpl(String root, boolean isAbsoultePath) throws URISyntaxException {
        ROOT = root;
        if(isAbsoultePath){
            ABSOULTE_ROOT = Path.of(root);
        }else{
            ABSOULTE_ROOT = Path.of(ClassLoader.getSystemResource(ROOT).toURI());
        }
    }

    @Override
    public void unzip(String file) {
        try( var zf = new ZipFile(Path.of(file).toFile()) ){
            var zipEntries = zf.entries();
            zipEntries.asIterator().forEachRemaining( entriy -> {
                try{
                    if(entriy.isDirectory()){
                        Path dirToCreat = ABSOULTE_ROOT.resolve(entriy.getName());
                        Files.createDirectory(dirToCreat);
                    } else{
                        Path fileToCreate = ABSOULTE_ROOT.resolve(entriy.getName());
                        Files.copy(zf.getInputStream(entriy), fileToCreate);
                    }
                } catch( IOException e2){
                    e2.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(String file) {
        try {
            return Files.deleteIfExists( ABSOULTE_ROOT.resolve(file) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save(String file) {
        Path filePath = Path.of(file);
        Path fileToSave = ABSOULTE_ROOT.resolve(filePath.getFileName());
        try {
            Files.copy(filePath, fileToSave, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String file, String newName) {

        Path filePath = Path.of(file);
        Path fileToSave = ABSOULTE_ROOT.resolve(newName);
        try {
            Files.copy(filePath, fileToSave, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Path> list() {

        List<Path> list = null;
        try {
            list = Files.list( ABSOULTE_ROOT).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getRoot() {
        return ABSOULTE_ROOT.toString();
    }


}