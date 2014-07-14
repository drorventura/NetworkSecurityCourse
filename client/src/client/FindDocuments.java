package client;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class FindDocuments
{
    public void getDocuments(File file, Collection<File> filesFound) throws IOException
    {
        File[] list = file.listFiles();
        if(list != null)
        {
            for (File userDirectory : list)
            {
                getDocumentsFromUser(userDirectory, filesFound);
            }
        }
    }

    public void getDocumentsFromUser(File file, Collection<File> filesFound) throws IOException
    {
        File[] list = file.listFiles();
        if (list != null)
        {
            for (File subFile : list)
            {
                if (subFile.isDirectory())
                {
                    String directoryName = subFile.getName();
                    boolean relevantDir = directoryName.equalsIgnoreCase("desktop") ||
                            directoryName.equalsIgnoreCase("documents")||
                            directoryName.equalsIgnoreCase("downloads");
                    if(relevantDir)
                        findRelevantDocuments(subFile, filesFound);
                }
                else
                {
                    String fileExtension = getFileExtension(subFile.getName());

                    if( fileExtension.equalsIgnoreCase("docx")||
                            fileExtension.equalsIgnoreCase("doc") ||
                            fileExtension.equalsIgnoreCase("pdf") ||
                            fileExtension.equalsIgnoreCase("xls") ||
                            fileExtension.equalsIgnoreCase("xlsx") ||
                            fileExtension.equalsIgnoreCase("txt"))
                    {
                        filesFound.add(subFile);
                    }
                }
            }
        }
    }

    private void findRelevantDocuments(File file, Collection<File> filesFound)
    {
        File[] list = file.listFiles();
        if (list != null)
        {
            for (File subFile : list)
            {
                if (subFile.isDirectory())
                {
                    findRelevantDocuments(subFile, filesFound);
                }
                else
                {
                    String fileExtension = getFileExtension(subFile.getName());

                    if( fileExtension.equalsIgnoreCase("docx")||
                            fileExtension.equalsIgnoreCase("doc") ||
                            fileExtension.equalsIgnoreCase("pdf") ||
                            fileExtension.equalsIgnoreCase("xls") ||
                            fileExtension.equalsIgnoreCase("txt"))
                    {
                        filesFound.add(subFile);
                    }
                }
            }
        }
    }

    private String getFileExtension(String filename)
    {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0)
        {
            extension = filename.substring(i+1);
        }
        return extension;
    }
}
