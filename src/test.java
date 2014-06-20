import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Dror
 * Date: 20/06/14
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public class test
{
    public static void main(String[] args) {
        System.out.println("hahaha");

        PrintWriter writer = null;
        try{
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        writer.println("The first line");
        writer.println("The second line");
        writer.close();
    }
}
