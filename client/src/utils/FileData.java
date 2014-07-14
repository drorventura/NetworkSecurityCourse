package utils;

import java.io.Serializable;

public class FileData implements Serializable
{
    private String destinationDirectory;
    private String sourceDirectory;
    private String filename;
    private long fileSize;
    private byte[] fileData;
    private Status status;

    public String getDestinationDirectory()
    {
        return destinationDirectory;
    }

    public void setDestinationDirectory(String destinationDirectory)
    {
        this.destinationDirectory = destinationDirectory;
    }

    public String getSourceDirectory()
    {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory)
    {
        this.sourceDirectory = sourceDirectory;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(long fileSize)
    {
        this.fileSize = fileSize;
    }

    public byte[] getFileData()
    {
        return fileData;
    }

    public void setFileData(byte[] fileData)
    {
        this.fileData = fileData;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public enum Status
    {
        SUCCESS,ERROR
    }
}
