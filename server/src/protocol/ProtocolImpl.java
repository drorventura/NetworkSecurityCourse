package protocol;

import utils.FileData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtocolImpl implements Protocol
{
    @Override
    public boolean processMessage(FileData fileData)
    {
        String destinationDirectory = fileData.getDestinationDirectory();
        File file = new File (destinationDirectory);

        if (!file.exists())
        {
            new File(destinationDirectory).mkdirs();
        }

        String outputPath = destinationDirectory + fileData.getFilename();
        File outputFile = new File(outputPath);

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(fileData.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Output file : " + outputFile + " is successfully saved ");
        return false;
    }

    @Override
    public boolean processMessage(String string)
    {
        String[] parsedMessage = string.split(";");
        String clientId = parsedMessage[0];
        String messageContent = parsedMessage[1];
        switch (messageContent)
        {
            case "0":
                System.out.println(clientId + " is closing connection");
                break;
            case "1":
                System.out.println(clientId + " connected successfully");
                break;
            case "done":
                return true;
            default:
                System.out.println(clientId + ": " + messageContent);
                break;
        }
        return false;
    }

    @Override
    public String prepareMessage(String string)
    {
        String[] commands = string.split(";");
        StringBuilder message = new StringBuilder("00000");

        for (String command : commands){
            try
            {
                switch (Integer.parseInt(command))
                {
                    case 0:
                        message.setCharAt(0,'1');
                        break;
                    case 1:
                        message.setCharAt(1,'1');
                        break;
                    case 2:
                        message.setCharAt(2, '1');
                        break;
                    case 3:
                        message.setCharAt(3, '1');
                        break;
                    case 4:
                        message.setCharAt(4, '1');
                        break;
                    default:
                        break;
                }
            }
            catch (NumberFormatException e)
            {
                return message.toString();
            }
        }

        return message.toString();
    }
}
