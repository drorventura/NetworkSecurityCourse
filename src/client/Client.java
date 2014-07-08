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
    private String[] address;
    private final String URL = "http://drorventura.github.io/templates/mp/addr.html";

    private void  initConnection()
    {
        try {
            int port = Integer.parseInt(address[1]);
            requestSocket = new Socket(address[0], port);

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            String hostName = requestSocket.getInetAddress().getHostName();
            System.out.println("Connected to " + hostName + " in port " + port);
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

    private boolean checkForTrigger()
    {
        try
        {
            URL url = new URL(URL);
            Document doc = Jsoup.parse(url, 1000 * 3);
            String text = doc.body().text();

            address = text.split(";");
            System.out.println("ip: " + address[0] + ", port: " + address[1]); // outputs 1
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
        switch (message)
        {
            case "0":
                closeConnection();
                break;

            default:
                System.out.println("server: " + message);
                break;
        }
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
        while(!checkForTrigger())
        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        initConnection();

        while(!requestSocket.isClosed())
        {
            try
            {
                String message = (String) in.readObject();
                handleMessage(message);
            }
            catch(ClassNotFoundException classNot){
                System.err.println("data received in unknown format");
            }
            catch (IOException e) {
                closeConnection();
                break;
            }
        }
    }

    public static void main(String args[])
    {
        while (true) {
            Client client = new Client();
            client.run();
        }
    }

}
