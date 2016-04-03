import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.*;

/**
 * The data getter class that collects data and picks the top topics
 */
public class DataExtractor {
    Document doc;
    DataExtractor(Document doc){
        this.doc = doc;
    }

    /**
     * The runner method that gets the key strings out of the HTML by parsing the doc,then extracts the significant
     * words out of them using the parser, NameExtractor and NounExtractor.
     * @param topRes number of top topics to display
     * @return a List of the Top topics
     */
    public List<String> extract(int topRes){
        String title = doc.title();
        //create and update SiteData object with it's specifically chosen fields
        SiteData result = new SiteData();
        if (title != null) {
            result.setTitle(doc.title());
        }
        Elements h1Tags = doc.body().getElementsByTag("h1");
        Elements h2Tags = doc.body().getElementsByTag("h2");;
        if (!h1Tags.isEmpty()) {
            result.setH1(h1Tags.text().toString());
        }
        if (!h2Tags.isEmpty()) {
            result.setH2(h2Tags.first().text().toString());
        }
        Elements metalinksDescription = doc.select("meta[name=description]");
        if (!metalinksDescription.isEmpty()){
            result.setMetaTagDescription(metalinksDescription.first().attr("content").toString());
        }
        Elements metalinksTwitterDescription = doc.select("meta[name=twitter:description]");
        if (!metalinksTwitterDescription.isEmpty()) {
            result.setMetaTagTwitterDescription(metalinksTwitterDescription.first().attr("content").toString());
        }
        List<Set<String>> nounsAndNamesSetArr = new ArrayList<Set<String>>();
        NounExtractor neTitle = new NounExtractor();
        NounExtractor neH1 = new NounExtractor();
        NounExtractor neH2 = new NounExtractor();
        NounExtractor neMetaDes = new NounExtractor();
        NounExtractor neMetaTwDes = new NounExtractor();
        NameExtractor naeTitle = new NameExtractor();
        NameExtractor naeH1 = new NameExtractor();
        NameExtractor naeH2 = new NameExtractor();
        NameExtractor naeMetaDes = new NameExtractor();
        NameExtractor naeMetaTwDes = new NameExtractor();
        //Add all Nouns and Names collected to a single data structure in order to make it easier to manipulate
        nounsAndNamesSetArr.add(neTitle.nounExtractor(result.getTitle()));
        nounsAndNamesSetArr.add(neH1.nounExtractor(result.getH1()));
        nounsAndNamesSetArr.add(neH2.nounExtractor(result.getH2()));
        nounsAndNamesSetArr.add(neMetaDes.nounExtractor(result.getMetaTagDescription()));
        nounsAndNamesSetArr.add(neMetaTwDes.nounExtractor(result.getMetaTagTwitterDescription()));
        nounsAndNamesSetArr.add(naeTitle.nameExtractor(result.getTitle()));
        nounsAndNamesSetArr.add(naeH1.nameExtractor(result.getH1()));
        nounsAndNamesSetArr.add(naeH2.nameExtractor(result.getH2()));
        nounsAndNamesSetArr.add(naeMetaDes.nameExtractor(result.getMetaTagDescription()));
        nounsAndNamesSetArr.add(naeMetaTwDes.nameExtractor(result.getMetaTagTwitterDescription()));
        //Make a Set containing all possible Nouns and Names in the collected data
        Set<String> combinedNounsAndNames = new HashSet<String>();
        for (Set set : nounsAndNamesSetArr){
            combinedNounsAndNames.addAll(set);
        }
        //count appearances of each noun or name and sort by size
        SortedSet<Map.Entry<String,Integer>> apperancesTable =
                entriesSortedByValues(countAppereancesInSet(combinedNounsAndNames, nounsAndNamesSetArr));
        //get only the top X Topics from the full list
        Iterator it = apperancesTable.iterator();
        int i = 0;
        List<String> topStrings = new ArrayList<String>();
        while (it.hasNext() && i<topRes){
            Map.Entry<String, Integer> str = (Map.Entry<String, Integer>)it.next();
            topStrings.add(str.getKey());
            i++;
        }
        //Set<String> verbs = NounExtractor.nounExtractor(sentence);

        return topStrings;
    }

    /**
     * Given a list of possible nouns and names, counts each ones appearances
     * @param combinedNounsAndNames all possible nouns and names
     * @param nounsSetArr lists of nouns and names collected from each field
     * @return number of appearances of each string
     */
    private Map<String, Integer> countAppereancesInSet(Set<String> combinedNounsAndNames, List<Set<String>> nounsSetArr){
        Map<String, Integer> nounsCountMap = new TreeMap<String, Integer>();
        for (String str : combinedNounsAndNames){
            for (Set set : nounsSetArr){
                if (set.contains(str)){
                    if(nounsCountMap.get(str) != null) {
                        nounsCountMap.put(str, nounsCountMap.get(str) + 1);
                    }else{
                        nounsCountMap.put(str, new Integer(1));
                    }
                }
            }


        }
        return nounsCountMap;
    }

    /**
     * Sort the String(names or nouns) - Integer(appearance count) data structure
     * @param map the map that contains the topics and their count
     * @return an descending sorted Set of the topics and their count
     */
    SortedSet<Map.Entry<String, Integer>> entriesSortedByValues(Map<String, Integer> map) {
        SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<Map.Entry<String, Integer>>(
                new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        //descending order (easier with iterator)
                        return res != 0 ? -res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
