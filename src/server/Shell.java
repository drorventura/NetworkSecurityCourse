package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class Shell extends Observable implements  Runnable
{
    @Override
    public void run()
    {
        while(Server.running)
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

                }while(!message.equals("quit"));
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            } finally {
                Server.running = false;
            }
        }
    }
}
