package client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.net.*;

public class Client
{
    private Socket requestSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    private boolean triggered;
    private String[] address;
    private final String URL = "http://drorventura.github.io/templates/mp/addr.html";

    public Client() {
        triggered = false;
    }

    private void  initConnection()
    {
        try {
            requestSocket = new Socket("localhost", 2014);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            System.out.println("Connected to localhost in port 2004");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection()
    {
        try{
            in.close();
            out.close();
            requestSocket.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private boolean checkForTrigger() {
        try
        {
            URL url = new URL(URL);
            Document doc = Jsoup.parse(url, 1000 * 3);
            String text = doc.body().text();

            address = text.split(";");
            System.out.println("ip: " + address[0] +", port: "+ address[1]); // outputs 1
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void handleMessage(String message)
    {
        System.out.println("server: " + message);
    }

    private void sendMessage(String msg)
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

    public void run()
    {
        while(!triggered){

            triggered = checkForTrigger();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        initConnection();
        do{
            try{
                message = (String)in.readObject();
                handleMessage(message);

            } catch(ClassNotFoundException classNot){
                System.err.println("data received in unknown format");
            } catch (IOException e) {
                System.out.println("no object to read");
            }

        }while(!message.equals("quit"));

        closeConnection();
    }

    public static void main(String args[])
    {
        Client client = new Client();
        client.run();
    }

}
