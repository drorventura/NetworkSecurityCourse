package client;

/**
 * Created with IntelliJ IDEA.
 * User: Dror
 * Date: 20/06/14
 * Time: 15:57
 */
import java.io.*;
import java.net.*;

public class Client {

    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    boolean triggered;

    Client(){
        triggered = false;
    }

    void  initConnection() {
        try {
            requestSocket = new Socket("localhost", 2004);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            System.out.println("Connected to localhost in port 2004");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() {

        while(!triggered){

            triggered = checkForTrigger();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        initConnection();

        try{
            do{
                try{
                    message = (String)in.readObject();

                    handleMessage(message);
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
        finally {
            closeConnection();
        }
    }

    private boolean checkForTrigger() {
        // TODO
        return false;
    }

    private void handleMessage(String message) {
        System.out.println("server: " + message);
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
