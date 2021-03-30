/**
 * Script to translate nucleotide sequence to amino acid sequence.
 * @Author: Rosa Zhang
 */

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;

public class Translate {

    static Pattern rgx = Pattern.compile("\\\"([A-Z]{3})\":\\s\"([A-Z]|\\*)\\\"");
    static Pattern stop = Pattern.compile("(TAA|TAG|TGA)");

    /** HashMap for codon translation where key = 3 letter sequence and value = translated letter. **/
    static Map<String,String> codontable = new HashMap<>();

    /** Builds HashMap for codon translation. */
    public static void buildmap() throws FileNotFoundException {
        try {
            Scanner input = new Scanner(new File("./codontable.txt"));
        }
        catch (Exception e) {
            throw new FileNotFoundException("Missing codontable.txt file.");
        }
        finally {
            Scanner sc = new Scanner(new File("codontable.txt"));
            sc.useDelimiter(",");
            while (sc.hasNext()) {
                String scan = sc.next();
                //System.out.println(scan);
                Matcher seq = rgx.matcher(scan);
                if (seq.find()) {
                    codontable.put(seq.group(1), seq.group(2));
                    Matcher stp = stop.matcher(scan);
                    if (stp.matches()) {
                        codontable.put(seq.group(1), "");
                    }
                }
            }
            sc.close();
            //System.out.println("ATG: " + codontable.get("ATG"));
            //System.out.println("Translator ready.");
        }
    }

    /**
     * Splits input arguments into segments between start and end codons.
     * @param input nucleotide sequence
     * @return nested array containing split segments for translation.
     */
    public static void translate(String input) {
        List<String> out = new ArrayList<>();
        int len = input.length();
        // if (len%3 != 0) { System.out.println("WARNING: Input may have invalid sequences."); }
        boolean started = false;
        for (int i=0; i<len; i+=3) {
            String inseq = input.substring(i, Math.min(len, i+3));
            if (inseq.equals("ATG")) { //checks for starting seq, skips if not
                started = true;
            }
            if (started){
                Matcher stp = stop.matcher(inseq);
                if (started && stp.matches()) {
                    started = false;
                } else {
                    out.add(codontable.get(inseq));
                }
            }
        }
        out.stream().forEach(s -> System.out.print(s));
    }

    /**
     * Executable method to translate sequence.
     * Builds translation table if first time executing translation command.
     * @param 'start' or sequence
     */
    public static void main(String... input) throws FileNotFoundException{
        buildmap();
        //System.out.println(input[0].toUpperCase());
        translate(input[0].toUpperCase());
    }

}
