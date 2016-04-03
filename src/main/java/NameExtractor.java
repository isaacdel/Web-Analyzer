import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * extract names phrases from a sentence using OpenNLP
 */
public class NameExtractor {
    private static final String TOKENLIBRARY = "/en-token.bin";
    private static final String PERSONLIBRARY = "/en-ner-person.bin";
    /**
     * extract the names from the given sentence using NLP
     * @param sentence to be parsed
     * @return the names
     */
    public Set<String> nameExtractor(String sentence) {

        InputStream modelInToken = null;
        InputStream modelIn = null;
        Set<String> nameSet = new HashSet<String>();

        try {

            //convert sentence into tokens
            modelInToken = getClass().getResourceAsStream(TOKENLIBRARY);
            TokenizerModel modelToken = new TokenizerModel(modelInToken);
            Tokenizer tokenizer = new TokenizerME(modelToken);
            String tokens[] = tokenizer.tokenize(sentence);

            //find names
            modelIn = getClass().getResourceAsStream(PERSONLIBRARY);
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);

            Span nameSpans[] = nameFinder.find(tokens);

            //find probabilities for names
            double[] spanProbs = nameFinder.probs(nameSpans);

            //export names
            for( int i = 0; i<nameSpans.length; i++) {
                nameSet.add(tokens[nameSpans[i].getStart()] + " " + tokens[nameSpans[i].getStart()+1]);
            }
        }
        catch (Exception ex) {}
        finally {
            try { if (modelInToken != null) modelInToken.close(); } catch (IOException e){};
            try { if (modelIn != null) modelIn.close(); } catch (IOException e){};
            return nameSet;
        }
    }
}
