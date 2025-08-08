package com.rpc.Tcp.Tcpserver;

import com.rpc.common.entity.Invocation;
import com.rpc.register.LocalRegister;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private int port;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public TcpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("TCP RPC Server started on port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            threadPool.submit(() -> handle(client));
        }
    }

    private void handle(Socket client) {
        try (InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {

            DataInputStream dis = new DataInputStream(in);
            int length = dis.readInt();
            byte[] data = new byte[length];
            dis.readFully(data);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Invocation invocation = (Invocation) ois.readObject();

            // 从本地服务注册表拿服务实例

            Object service = LocalRegister.get(invocation.getInterfaceName(), invocation.getVersion());
            Method method = service.getClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Object result = method.invoke(service, invocation.getParameters());

            // 序列化返回结果
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(result);
            oos.flush();

            DataOutputStream dos = new DataOutputStream(out);
            byte[] respData = baos.toByteArray();
            dos.writeInt(respData.length);
            dos.write(respData);
            dos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }
}