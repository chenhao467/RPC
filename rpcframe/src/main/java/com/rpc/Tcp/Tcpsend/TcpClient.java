package com.rpc.Tcp.Tcpsend;

import com.rpc.common.entity.Invocation;
import java.io.*;
import java.net.Socket;

public class TcpClient {
    private String hostname;
    private int port;

    public TcpClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public Object send(Invocation invocation) {
        try (Socket socket = new Socket(hostname, port);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            // 序列化Invocation
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(invocation);
            oos.flush();
            byte[] data = baos.toByteArray();

            // 写长度，避免粘包
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();

            // 读取响应长度
            DataInputStream dis = new DataInputStream(in);
            int length = dis.readInt();
            byte[] respData = new byte[length];
            dis.readFully(respData);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(respData));
            return ois.readObject();

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("TCP RPC 调用失败", e);
        }
    }
}
