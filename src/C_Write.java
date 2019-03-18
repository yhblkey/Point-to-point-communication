import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class C_Write extends Thread {

    private Socket write;

    public C_Write(Socket s){

        write = s;
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        try {
            PrintWriter out = new PrintWriter(write.getOutputStream(),true);
            while (true){
                String temp;
                temp = scan.nextLine();
                out.println(">"+temp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
