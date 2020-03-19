package com.neuedu.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class FTPUtil {
    //step1:连接到ftp服务器

    private static FTPClient ftpClient;
    private static boolean login;
    static  {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(PropertiesUtil.getProperty("ftpIp"));
            login = ftpClient.login(PropertiesUtil.getProperty("ftpUser"), PropertiesUtil.getProperty("ftpPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //step2：文件上传
    public static boolean uploadFile(List<File> fileList)  {
        return uploadFile("/QRcode",fileList);
    }
    public static boolean uploadFile(String filePath, List<File> fileList)  {
        FileInputStream fileIn = null;
        if (login){//先登录到ftp
            //切换工作目录
            try {
             /*切换的目录不可携带根目录根目录是/succez，当要访问/succez/succezbi目录，
             就要将filePath写成/succezbi，而不是/succez/succezbi。就 是不能将根目录写进去。
            当要访问/succez/succezbi/succezci时，filePath=/succezbi/succezci。*/
                boolean directory = ftpClient.changeWorkingDirectory(filePath);
                if (directory){
                        LOGGER.info("进入文件"+filePath+"夹成功.");
                    }else {
                        LOGGER.info("进入文件"+filePath+"夹失败.开始创建文件夹");
                        boolean makeDirectory = ftpClient.makeDirectory(filePath);
                        if(makeDirectory) {
                            LOGGER.info("创建文件夹"+filePath+"成功");
                            boolean changeWorkingDirectory2 = ftpClient.changeWorkingDirectory(filePath);
                            System.out.println(changeWorkingDirectory2);
                            if(changeWorkingDirectory2) {
                                LOGGER.info("进入文件"+filePath+"夹成功.");
                            }
                        }else {
                            LOGGER.info("创建文件夹"+filePath+"失败");
                        }
                }
                ftpClient.setBufferSize(1024*1024*10);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动传输模式
                for (File file : fileList) {
                    fileIn = new FileInputStream(file);
                    boolean storeFile = ftpClient.storeFile(file.getName(), fileIn);
                    if (!storeFile){
                        LOGGER.info(file.getName()+"文件上传失败");
                    }
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fileIn != null){
                       fileIn.close();
                    }
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

//    public static void main(String[] args) {
//        List<File> files = new ArrayList<>();
//        File file = new File("C:/Users/GuXinYu/Desktop/qr-1584427926907.png");
//        files.add(file);
//        FTPUtil.uploadFile(files);
//    }
}
