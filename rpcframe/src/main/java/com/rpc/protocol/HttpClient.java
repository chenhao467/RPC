package com.rpc.protocol;

import com.rpc.common.Invocation;
import com.rpc.interceptor.RpcInterceptor;
import com.rpc.register.ConsumerInterceptorRegister;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午9:34
*/
public class HttpClient {

    public String send(String hostname, Integer port, Invocation invocation) {

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
            String result = IOUtils.toString(inputStream);


            return result;
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

}
