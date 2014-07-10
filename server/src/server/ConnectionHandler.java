package server;

import protocol.Protocol;
import protocol.ProtocolImpl;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectionHandler implements Observer
{
    private Server server;
    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Protocol protocol;

    public ConnectionHandler(Server server, Socket connection)
    {
        this.connection = connection;
        this.server = server;
        try {
            protocol = new ProtocolImpl();
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();

            Object object = in.readObject();
            if(object instanceof String){
                String message = (String) object;
                protocol.processMessage(message);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void closeConnection()
    {
        System.out.println("closing connection");

        try {
            in.close();
            out.close();
            connection.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        String message = protocol.prepareMessage((String) arg);

        if(message.equals("00000"))
        {
            System.out.println("Invalid command");
        }
        else
        {
            sendMessage(message);
            receiveMessages();

            if (message.equals("10000"))
            {
                server.deleteObserver(this);
                closeConnection();
            }
        }
    }

    private void receiveMessages()
    {
        System.out.println("Waiting for response");

        boolean done = false;
        while (!done)
        {
            try
            {
                Object object = in.readObject();
                if(object instanceof String)
                {
                    String message = (String) object;
                    done = protocol.processMessage(message);
                }
                if(object instanceof File)
                {
                    File message = (File) object;
                    protocol.processMessage(message);
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}