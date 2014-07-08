package server;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectionHandler implements Observer
{
    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectionHandler(Socket connection)
    {
        this.connection = connection;
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage("Connection successful");
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

    private void closeConnetion()
    {
        System.out.println("closing connection");

        try{
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
        System.out.println("in update");
        String message = (String) arg;

        if (message.equals("quit"))
            closeConnetion();
        else
            sendMessage(message);
    }
}

