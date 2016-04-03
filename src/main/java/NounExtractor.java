import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 *extract noun phrases from a sentence using OpenNLP
 */
public class NounExtractor {
    //chunking model data structures
    Set<String> nounPhrases = new HashSet<String>();
    Set<String> verbPhrases = new HashSet<String>();
    private static final String CHUNKINGLIBRARY = "/en-parser-chunking.bin";

    /**
     * extract the nouns from the given sentence using NLP
     * @param sentence to be parsed
     * @return the nouns
     */
    public Set<String> nounExtractor(String sentence) {

        InputStream modelInParse = null;
        try {
            //load chunking model
            modelInParse = getClass().getResourceAsStream(CHUNKINGLIBRARY);
            ParserModel model = new ParserModel(modelInParse);

            //create parse tree
            Parser parser = ParserFactory.create(model);
            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            //call subroutine to extract noun phrases
            for (Parse p : topParses)
                getNounPhrases(p);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelInParse != null) {
                try {
                    modelInParse.close();
                }
                catch (IOException e) {
                }
            }
            return nounPhrases;
        }
    }

    /**
     *loop through tree recursively, extracting noun and not verbs phrases
     * @param p the parameter to parse
     */
    private void getNounPhrases(Parse p) {
        if(p.getType().equals("NP") && !(p.getType().equals("VB") && p.getType().equals("VBP") &&
                p.getType().equals("VBG")|| p.getType().equals("VBD") || p.getType().equals("VBN"))){
            nounPhrases.add(p.getCoveredText());
        }
        for (Parse child : p.getChildren())
            getNounPhrases(child);
    }
}