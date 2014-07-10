package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable
{
    private ServerSocket serverSocket;
    private Server server;

    public Listener(Server server)
    {
        this.server = server;

        try {
            serverSocket = new ServerSocket(server.getPort());
            server.setRunning(true);

        } catch (IOException e){
            server.setRunning(false);
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(server.isRunning())
        {
            try {
                Socket connection = serverSocket.accept();

                ConnectionHandler connectionHandler = new ConnectionHandler(server,connection);
                server.addObserver(connectionHandler);

                System.out.println("Connection received from " + connection.getInetAddress().getHostName());

            } catch (IOException e) {
                server.setRunning(false);
            }
        }
    }

    public void stopListening()
    {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
