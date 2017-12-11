package com.kaishengit.test;

import com.kaishengit.service.exception.ServiceException;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by User on 2017/11/17.
 */
public class FastDFSTestCase {

    /**
     * 文件上传
     */
    @Test
    public void upload(){

        //配置tracker
        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.245.210:22122,192.168.245.211:22122");

        try {
            ClientGlobal.initByProperties(properties);

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer,null);

            InputStream inputStream = new FileInputStream("d:/img/ofyou.mp3");
            byte[] bytes = IOUtils.toByteArray(inputStream);

            NameValuePair[] nameValuePairs = new NameValuePair[3];
            nameValuePairs[0] = new NameValuePair("name","jack");
            nameValuePairs[1] = new NameValuePair("price","1000");
            nameValuePairs[2] = new NameValuePair("job","author");

            String[] strings = storageClient.upload_file(bytes,"mp3",nameValuePairs);

            for(String str : strings){
                System.out.println(str);
            }
            inputStream.close();

        } catch (IOException | MyException e ) {
            e.printStackTrace();
            throw new ServiceException("文件上传异常",e);
        }
    }

    /**
     * 文件下载
     * @throws IOException
     * @throws MyException
     */
    @Test
    public void download() throws IOException, MyException {

        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.245.128:22122");

        ClientGlobal.initByProperties(properties);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);

        byte[] bytes = storageClient.download_file("group1","M00/00/00/wKj1gFoOqESAcC7_AIwwXNojkW8006.mp3");


        NameValuePair[] nameValuePairs = storageClient.get_metadata("group1","M00/00/00/wKj1gFoOqESAcC7_AIwwXNojkW8006.mp3");

        for(NameValuePair nameValuePair : nameValuePairs){
            System.out.println(nameValuePair.getName()+ ">>>>>" + nameValuePair.getValue());
        }

        FileOutputStream outputStream = new FileOutputStream("d:/img/new.mp3");
        outputStream.write(bytes,0,bytes.length);
        outputStream.flush();
        outputStream.close();

    }

}
