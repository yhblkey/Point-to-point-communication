import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class C_Read<e> extends Thread{

    private Socket read;

    public C_Read(Socket s){
        read = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(read.getInputStream()));
            String temp;
            while (true){
                if ((temp=reader.readLine())!=null){
                    System.out.println(temp);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}