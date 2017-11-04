package nl.micheldebree.vice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {

    private Socket socket;

    private DataOutputStream out = null;
    private DataInputStream in = null;


    public Connection(String hostname, int port) throws IOException {
        this.socket = new Socket();
        this.socket.bind(null);
        InetSocketAddress isa = new InetSocketAddress(hostname, port);
        this.socket.connect(isa, 1000);
        this.socket.setKeepAlive(true);

        if (socket.isConnected()) {
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }

    }

    public void send(String data) throws IOException {
        this.out.writeBytes(data);
    }

    public String receive(int length) throws IOException {
        out.flush();
        byte[] response = new byte[length];
        in.readFully(response);
        return new String(response);
    }

    public void close() throws IOException {

            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }

            if (socket != null) {
                socket.close();
            }

    }

}
