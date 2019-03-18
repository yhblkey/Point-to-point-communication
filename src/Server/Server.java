package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private ServerSocket serverSocket;
    private  int user_id = 1;
//    private  HashMap<Integer,Socket> users = new HashMap<Integer, Socket>(30);

    public Server()throws IOException {

        serverSocket = new ServerSocket(8888,10);

        System.out.println("服务启动!!!");
    }

    public void run(){


        //服务器主程序
        while (true) {
            try {

                Socket temp = serverSocket.accept();
                //客户端连接成功服务器

//                users.put(user_id++,temp);
//                Thread clientThread = new ServerToSend(temp,users,user_id-1);

                Thread clientThread = new ServerToSend(temp);

                clientThread.start();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void putSocket(){

    }

}
