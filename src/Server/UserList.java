package Server;

import java.net.Socket;
import java.util.HashMap;

public class UserList {
    static HashMap<String,String> account = new HashMap<>(30);                 //记录用户的账号密码
    static HashMap<String, Socket> users = new HashMap<String, Socket>(30);   //存储连接到服务器的Socket对象

    public static void creatAccount(String user_nuo,String passwd){
        account.put(user_nuo,passwd);
    }
    public static void removeAccout(String user_nuo){
        account.remove(user_nuo);
    }
    public static boolean login(String user_id,String passwd){
        if (account.containsKey(user_id)&&account.get(user_id).equals(passwd)){
            return true;
        }else {
            return false;
        }
    }
    public static void putUsers(String user_id,Socket socket){
        users.put(user_id,socket);
    }
    public static void removeUsers(String user_id){
        users.remove(user_id);
    }

}
