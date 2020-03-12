package test;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

public class HTTPServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 10);
            while (true) {
                Socket socket = serverSocket.accept();
//                InputStream is = socket.getInputStream();
//                is.read(new byte[2048]);
                BufferedReader httpReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String requestLine=httpReader.readLine();
                String[] requestArray = requestLine.split(" ");
//                System.out.println("Request: "+requestLine);
                System.out.println(Arrays.toString(requestArray));
//                String readStr = httpReader.readLine();
//                String[] strs = readStr.split(" ");
//                System.out.println(httpReader.toString());
                OutputStream os = socket.getOutputStream();

                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
                os.write("Content-Length:38\r\n".getBytes());
                os.write("Server:gybs\r\n".getBytes());
                os.write(("Date:"+new Date()+"\r\n").getBytes());
                os.write("\r\n".getBytes());
                os.write("<body>".getBytes());
                os.write("<h1>hello!</h1>".getBytes());
                os.write("<h3>HTTP服务器!</h3>".getBytes("utf-8"));
                os.write("</body>".getBytes());
                os.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
