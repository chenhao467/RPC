package com.rpc.http.httpsend;

import com.rpc.common.entity.Invocation;
import com.rpc.common.exception.BusinessException;
import com.rpc.common.exception.ResultCodeEnum;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午9:34
*/
public class HttpClient {

    public Object send(String hostname, Integer port, Invocation invocation) {
        ObjectInputStream ois = null;
        try {
            URL url = new URL("http", hostname, port, "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(invocation);
            objectOutputStream.flush();
            objectOutputStream.close();

            InputStream inputStream = conn.getInputStream();
            ois = new ObjectInputStream(inputStream);
                return ois.readObject();  // 返回 Object

        } catch (IOException e) {
            throw new BusinessException(ResultCodeEnum.IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new BusinessException(ResultCodeEnum.CLASS_NOT_FOUND);
        }finally {
            if (ois != null) {
                try {
                    ois.close(); // 手动关闭资源
                } catch (IOException e) {
                    e.printStackTrace(); // 记录关闭异常
                }
            }
        }
    }

}
