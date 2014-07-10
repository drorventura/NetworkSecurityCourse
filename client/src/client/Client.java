package client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.net.*;
import java.util.UUID;

public class Client
{
    private UUID uniqueId;
    private Socket requestSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String[] address;
    private final String URL = "http://drorventura.github.io/templates/mp/addr.html";

    public Client(UUID uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    private boolean  initConnection()
    {
        try
        {
            int port = Integer.parseInt(address[2]);
            requestSocket = new Socket(address[1], port);

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

//            String hostName = requestSocket.getInetAddress().getHostName();

            sendMessage(uniqueId + ";1");
            return true;
        }
        catch (IOException e) {
            return false;
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
//            System.out.println("ip: " + address[0] + ", port: " + address[1]); // outputs 1

            if (address[0].equals("1"))
            {
//                System.out.println(address[0] + ";" + address[1] + ";" + address[2]);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private void handleMessage(String message)
    {
        switch (message)
        {
            case "10000":
                sendMessage(uniqueId + ";0");
                sendMessage(uniqueId + ";" + "done");
                closeConnection();
                break;

            default:
                sendMessage(uniqueId + ";recived message - " + message);
                sendMessage(uniqueId + ";" + "done");
                break;
        }
    }

    private void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
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

        if(!initConnection())
            return;

        while(!requestSocket.isClosed())
        {
            try
            {
                String message = (String) in.readObject();
                handleMessage(message);
            }
            catch(ClassNotFoundException classNot){
                sendMessage(uniqueId + ";data received in unknown format");
            }
            catch (IOException e) {
                closeConnection();
                break;
            }
        }
    }
}
