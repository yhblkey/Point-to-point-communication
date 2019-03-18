import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 *点对点即时通信客户端
 *
 *
 * 协议规则：
 * userid;password           实现登录
 * new;userid:password       实现用户注册
 * show;online               查看在线用户
 * TO;userid                 与用户进行通信
 * exit                      结束通信
 * exit;Account              退出登录
 *
 *
 */




public class Main {

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1", 8888);
        Thread writeThread = new C_Write(client);
        Thread readThread = new C_Read(client);
        readThread.start();
        writeThread.start();
    }
}
