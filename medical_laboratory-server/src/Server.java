import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("server started.");

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Client connected"+socket);
            ClientHandler clientHandler = new ClientHandler(socket);
            new Thread(clientHandler).start();

        }}catch (IOException e){
            e.printStackTrace();

        }
    }


}
