package protocol;

import java.io.File;

public interface Protocol
{
    public boolean processMessage(File file);
    public boolean processMessage(String string);
    public String prepareMessage(String message);
}
