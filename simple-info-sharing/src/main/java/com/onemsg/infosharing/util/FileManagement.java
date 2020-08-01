package com.onemsg.infosharing.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

// import java.util.zip.ZipInputStream;
/**
 * FileManagement,管理文件，解压文件、删除文件、保存文件、读取所有文件
 * 有一个根目录
 */
public interface FileManagement {

    /** 解压zip文件 到根目录
     * @param file 压缩文件的绝对路径
     */
    void unzip(String file);

    /** 删除文件 
     * @param file 被删除文件的相对路径（根目录）
    */
    boolean delete(String file);

    /**
     * 保存文件到根目录
     * 
     * @param file 被保存文件的绝对路径
     */
    void save(String file);

    /**
     * 保存文件到根目录
     * 
     * @param file 被保存文件的绝对路径
     * @param newName 文件新名字
     */
    void save(String file, String newName);

    /** 返回根目录内所有文件 */
    List<Path> list();

    /** 返回根目录 */
    String getRoot();

    static FileManagement create() throws URISyntaxException {
        return new FileManagementImpl();
    }

    static FileManagement create(String root, boolean isAbsoultePath ) throws URISyntaxException {
        return new FileManagementImpl(root, isAbsoultePath);
    }
}