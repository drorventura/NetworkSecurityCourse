package client;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetUrl {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://drorventura.github.io/templates/mp/addr.html");
        Document doc = Jsoup.parse(url,1000*3);

        String text = doc.body().text();
        String[] parsedText = text.split(";");
        System.out.println("ip: " + parsedText[0] +", port: "+ parsedText[1]); // outputs 1
    }

}
