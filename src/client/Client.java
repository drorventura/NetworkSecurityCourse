package client;

/**
 * Created with IntelliJ IDEA.
 * User: Dror
 * Date: 20/06/14
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.net.*;
public class Client {

    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Client(){}

    void initConnection() {
        try {
            requestSocket = new Socket("localhost", 2004);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            System.out.println("Connected to localhost in port 2004");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    void run() {
        try{
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("server: " + message);
                }
                catch(ClassNotFoundException classNot){
                    System.err.println("data received in unknown format");
                }
            }while(!message.equals("quit"));
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();

            System.out.println("client: " + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    void closeConnection() {
        try{
            in.close();
            out.close();
            requestSocket.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        Client client = new Client();
        client.run();
    }

}
