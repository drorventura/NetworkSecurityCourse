package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class Server extends Observable
{
    private boolean running;
    private int port;

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = Integer.parseInt(port);
    }

    public void startServer()
    {
        while(running)
        {
            try{
                String message;
                do {
                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    message = bufferedReader.readLine();

                    setChanged();
                    notifyObservers(message);

                    if (message.equals("quit"))
                        System.out.println("closing connection");

                } while(!message.equals("quit"));
            }
            catch(IOException ioException) {
                ioException.printStackTrace();
            } finally {
                running = false;

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.setPort(args[0]);

        final Listener listener = new Listener(server);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        server.startServer();
        listener.stopListening();
    }
}
