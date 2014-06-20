package server;

/**
 * Created with IntelliJ IDEA.
 * User: Dror
 * Date: 20/06/14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.net.*;
public class Server {

    ServerSocket serverSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    BufferedReader bufferedReader;

    public Server(){}

    void run()
    {
        try{
            serverSocket = new ServerSocket(2004);

            System.out.println("Waiting for connection");
            connection = serverSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());

            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();

            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");

            do {
                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                bufferedReader = new BufferedReader(inputStreamReader);
                message = bufferedReader.readLine();

                if (message.equals("quit"))
                    sendMessage("closing connection");
                else
                    sendMessage(message);

            }while(!message.equals("quit"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            try{
                in.close();
                out.close();
                serverSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();

            System.out.println("**Message** " + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        Server server = new Server();
        while(true) {
            server.run();
        }
    }
}
