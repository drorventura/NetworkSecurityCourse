package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable
{
    private ServerSocket serverSocket;
    private Shell shell;

    public Listener(Shell shell)
    {
        this.shell = shell;

        try {
            serverSocket = new ServerSocket(2014);
            Server.running = true;

        } catch (IOException e){
            Server.running = false;
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(Server.running)
        {
            try {
                Socket connection = serverSocket.accept();

                ConnectionHandler connectionHandler = new ConnectionHandler(connection);
                shell.addObserver(connectionHandler);

                System.out.println("Connection received from " + connection.getInetAddress().getHostName());

            } catch (IOException e) {
                e.printStackTrace();
                Server.running = false;
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
