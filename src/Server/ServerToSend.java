package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class ServerToSend extends Thread {
    private Socket src_ip;
    private String id;
//    private HashMap<Integer,Socket> users = new HashMap<>(30);

//    public ServerToSend(Socket s ,HashMap<Integer,Socket> u,int id){
    public ServerToSend(Socket s ){
        src_ip = s;
//        users = u;
//        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("开始工作!!");
        System.out.println(src_ip.getPort());
        String dst;
            BufferedReader in  = null;
            PrintWriter out =null;
            try {
                in = new BufferedReader( new InputStreamReader(src_ip.getInputStream()));
                out =new PrintWriter(src_ip.getOutputStream(),true);



                /**
                 * 登录功能实现
                 */

                while (true) {
                    String tmp;
                    String passwd;
                    out.println("Input your username and passwd  (username;passwd) OR Creat a Account (new;username;passwd)");
                    if (src_ip.getInputStream().read() != -1){
                        if ((tmp = in.readLine()) != null) {
                        if (tmp.indexOf(';') > -1) {

                            if (tmp.substring(0, tmp.indexOf(';')).equals("new")) {

                                if (UserList.account.containsKey(tmp.substring(tmp.indexOf(';') + 1, tmp.lastIndexOf(';')))){

                                    out.println("账户已经存在!!!");
                                    continue;

                                }else {
                                    id = tmp.substring(tmp.indexOf(';') + 1, tmp.lastIndexOf(';'));
                                    passwd = tmp.substring(tmp.lastIndexOf(';') + 1);
                                    UserList.creatAccount(id, passwd);
                                    UserList.putUsers(id, src_ip);
                                    out.println(id + " , 您已成功登录!!");
                                    break;
                                }
                            } else {
                                id = tmp.substring(0, tmp.indexOf(';'));
                                passwd = tmp.substring(tmp.indexOf(';') + 1);
                                if (UserList.users.containsKey(id)&&UserList.login(id,passwd)){
                                    out.println("该账户已经在线，请重新登录");
                                    UserList.users.get(id).close();               //关闭了与服务器的连接
                                    UserList.removeUsers(id);                     //删除连接列表
                                    continue;
                                }
                                if (UserList.login(id, passwd)) {
                                    UserList.putUsers(id, src_ip);
                                    out.println(id + " , 您已成功登录!!");
                                    break;
                                } else {
                                    out.println("Account does not exist");
                                    continue;
                                }
                            }
                        } else {
                            out.println("请求错误，对话连接失败");
                            continue;
                        }
                    }
                    }else {
                        System.out.println("客户端连接中断！！！！");
                        src_ip.close();
                        return;
                    }
                }

                if (UserList.users.size() > 0) {
                    for (String key : UserList.users.keySet()) {
                        System.out.println(key + " is online !!");
//                        System.out.println(key + " : " + UserList.account.get(key));
                        out.println(key + " is online !!");

                    }
                }


                /**
                 * 客户端点对点通信实现模块,服务器起到转发功能.
                 */

                while (true) {

                    // TO;111  向111发送信息
                    String method ;
                    if (src_ip.getInputStream().read() != -1) {
                        if ((method = in.readLine()) != null) {
                            System.out.println(method);
                            if (method.indexOf(';') > -1) {

                                if (method.substring(0, method.indexOf(';')).equals("TO")) {
                                    dst = method.substring(method.indexOf(';') + 1);

                                    if (UserList.users.containsKey(dst)) {
                                        Socket dst_ip = UserList.users.get(dst);
                                        PrintWriter dst_out = new PrintWriter(dst_ip.getOutputStream(), true);
                                        String temp;
                                        while (true) {
                                            if (src_ip.getInputStream().read() != -1) {
                                                if ((temp = in.readLine()) != null) {
                                                    if (temp.equals("exit")) {
                                                        out.println(id + "会话结束！");
                                                        dst_out.println(id + "会话结束！");
                                                        break;
                                                    }
                                                    dst_out.println(id + ": " + temp);

                                                }
                                            }else {
                                                System.out.println("客户端连接中断！！！！！！");
                                                UserList.users.get(id).close();                                 //关闭了与服务器的连接
                                                UserList.removeUsers(id);                                       //删除连接列表
                                                break;
                                            }
                                        }
                                    } else {
                                        out.println("用户已经下线！！！");
                                        continue;
                                    }
                                } else if (method.substring(0, method.indexOf(';')).equals("exit") && method.substring(method.indexOf(';') + 1).equals("Account")) {

                                    out.println("Account " + id + " out ");
                                    UserList.users.get(id).close();                                 //关闭了与服务器的连接
                                    UserList.removeUsers(id);                                       //删除连接列表
                                    break;

                                } else if (method.substring(0, method.indexOf(';')).equals("show") && method.substring(method.indexOf(';') + 1).equals("online")) {

                                    if (UserList.users.size() > 0) {
                                        for (String key : UserList.users.keySet()) {
                                            System.out.println(key + " is online !!");
                                            out.println(key + " is online !!");
                                        }
                                    }
                                    continue;
                                } else {
                                    out.println("请求错误，对话连接失败");
                                    continue;
                                }
                            } else {
                                out.println("请求错误，对话连接失败");
                                continue;
                            }
                        }
                    }else {
                        System.out.println("客户端连接中断！！！！！！");
                        UserList.users.get(id).close();                                 //关闭了与服务器的连接
                        UserList.removeUsers(id);                                       //删除连接列表
                    }

                }
            }catch (SocketException e){
                e.printStackTrace();
                try {
                    src_ip.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (UserList.users.containsKey(id)){
                    UserList.removeUsers(id);                     //删除连接列表
                }
            } catch (IOException e){
                e.printStackTrace();
            }

    }
}
