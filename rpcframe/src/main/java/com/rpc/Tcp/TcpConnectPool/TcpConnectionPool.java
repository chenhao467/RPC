package com.rpc.Tcp.TcpConnectPool;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TcpConnectionPool {
    private final BlockingQueue<Socket> pool = new ArrayBlockingQueue<>(50);
    private final String host;
    private final int port;

    public TcpConnectionPool(String host, int port, int initial) throws IOException {
        this.host = host; this.port = port;
        for (int i = 0; i < initial; i++) {
            pool.add(createSocket());
        }
    }

    private Socket createSocket() throws IOException {
        Socket s = new Socket(host, port);
        s.setTcpNoDelay(true);      // 关闭 Nagle，低延迟小包即时发送
        s.setKeepAlive(true);       // 让底层维护 keepalive
        return s;
    }

    public Socket borrow() throws IOException, InterruptedException {
        Socket s = pool.poll();
        if (s == null || s.isClosed() || !s.isConnected()) {
            return createSocket();
        }
        return s;
    }

    public void giveBack(Socket s) {
        if (s != null && s.isConnected() && !s.isClosed()) {
            pool.offer(s); // 非严格回收策略
        } else {
            try { s.close(); } catch (IOException ignored) {}
        }
    }
}
