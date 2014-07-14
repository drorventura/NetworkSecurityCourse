package utils;

import java.io.File;
import java.io.FilenameFilter;

public class KeyboardFilesFilter implements FilenameFilter
{
    @Override
    public boolean accept(File dir, String name)
    {
        return name.startsWith("keys_");
    }
}
