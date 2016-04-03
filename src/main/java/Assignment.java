import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.List;

/**
 * The Handler class - The main class that shifts between the other classes to get the output
 */
public class Assignment {
    private static final int NuOfTopTopics = 5;
    /**
     * Get the HTML page as a Document
     * @param url the required URL to be analyzed
     * @return the HTML as a Document
     */
    private static Document getHtmlContent(String url){
        Document html = null;
        try {
            html = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    /**
     * Displays the top topics of the page in the console
     * @param topics the site's top topics
     */
    private static void printTopics(List<String> topics){
        System.out.println("Topics:");
        for (String topic : topics){
            System.out.println(topic);
        }
    }

    /**
     * The runner method that handels other classes
     * @param args the URL to be analyzed as an arg
     */
    public static void main(String[] args){
        if (args[0] != null) {
            //assuming the bash will hold the URL in args[0]
            DataExtractor dataExt = new DataExtractor(getHtmlContent(args[0]));
            List<String> topics = dataExt.extract(NuOfTopTopics);
            printTopics(topics);
        }
    }
}
