package server;

public class Server
{
    public static boolean running = false;

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public static void main(String args[])
    {
        final Shell shell = new Shell();
        final Listener listener = new Listener(shell);

        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        Thread shellThread = new Thread(shell);
        shellThread.start();

        if(!running)
        {
            listener.stopListening();
        }
    }
}
