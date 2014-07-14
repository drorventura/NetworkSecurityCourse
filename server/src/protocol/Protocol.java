package protocol;

import utils.FileData;

public interface Protocol
{
    public boolean processMessage(FileData fileData);
    public boolean processMessage(String string);
    public String prepareMessage(String message);
}
