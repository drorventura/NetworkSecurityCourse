package server;

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

    public ConnectionHandler(Server server, Socket connection)
    {
        this.connection = connection;
        this.server = server;
        try {
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
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
        System.out.println("in update");
        String message = (String) arg;

        switch (message)
        {
            case "0":
                sendMessage(message);
                server.deleteObserver(this);
                break;

            case "quit":
                closeConnetion();
                break;

            default:
                sendMessage(message);
                break;
        }

    }
}

