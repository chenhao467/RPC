package com.rpc.Tcp.Tcpsend;

import com.rpc.Tcp.TcpConnectPool.TcpConnectionPool;
import com.rpc.common.entity.Invocation;

import java.io.*;
import java.net.Socket;

public class TcpClient {

    private final String hostname;
    private final int port;
    private final TcpConnectionPool pool;

    public TcpClient(String hostname, int port, int poolSize) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.pool = new TcpConnectionPool(hostname, port, poolSize);
    }

    public Object send(Invocation invocation) {
        Socket socket = null;
        try {
            socket = pool.borrow();
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // 写请求
            byte[] data = serializeInvocation(invocation);
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();

            // 读响应
            DataInputStream dis = new DataInputStream(in);
            int len = dis.readInt();
            byte[] resp = new byte[len];
            dis.readFully(resp);

            return deserialize(resp);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("TCP RPC 调用失败", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                pool.giveBack(socket);
            }
        }
    }

    private byte[] serializeInvocation(Invocation invocation) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(invocation);
            oos.flush();
            return baos.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }
}
