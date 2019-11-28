package piwords;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AlphabetGenerator {
    /**
     * Given a numeric base, return a char[] that maps every digit that is
     * representable in that base to a lower-case char.
     * 
     * This method will try to weight each character of the alphabet
     * proportional to their occurrence in words in a training set.
     * 
     * This method should do the following to generate an alphabet:
     *   1. Count the occurrence of each character a-z in trainingData.
     *   2. Compute the probability of each character a-z by taking
     *      (occurrence / total_num_characters).
     *   3. The output generated in step (2) is a PDF of the characters in the
     *      training set. Convert this PDF into a CDF for each character.
     *   4. Multiply the CDF value of each character by the base we are
     *      converting into.
     *   5. For each index 0 <= i < base,
     *      output[i] = (the first character whose CDF * base is > i)
     * 
     * A concrete example:
     * 	 0. Input = {"aaaaa..." (302 "a"s), "bbbbb..." (500 "b"s),
     *               "ccccc..." (198 "c"s)}, base = 93
     *   1. Count(a) = 302, Count(b) = 500, Count(c) = 193
     *   2. Pr(a) = 302 / 1000 = .302, Pr(b) = 500 / 1000 = .5,
     *      Pr(c) = 198 / 1000 = .198
     *   3. CDF(a) = .302, CDF(b) = .802, CDF(c) = 1
     *   4. CDF(a) * base = 28.086, CDF(b) * base = 74.586, CDF(c) * base = 93
     *   5. Output = {"a", "a", ... (28 As, indexes 0-27),
     *                "b", "b", ... (47 Bs, indexes 28-74),
     *                "c", "c", ... (18 Cs, indexes 75-92)}
     * 
     * The letters should occur in lexicographically ascending order in the
     * returned array.
     *   - {"a", "b", "c", "c", "d"} is a valid output.
     *   - {"b", "c", "c", "d", "a"} is not.
     *   
     * If base >= 0, the returned array should have length equal to the size of
     * the base.
     * 
     * If base < 0, return null.
     * 
     * If a String of trainingData has any characters outside the range a-z,
     * ignore those characters and continue.
     * 
     * @param base A numeric base to get an alphabet for.
     * @param trainingData The training data from which to generate frequency
     *                     counts. This array is not mutated.
     * @return A char[] that maps every digit of the base to a char that the
     *         digit should be translated into.
     */
    public static char[] generateFrequencyAlphabet(int base,
                                                   String[] trainingData) {
        if (base < 0) {
            return null;
        }
        
        // compute nums of letters, store in map
        Map<Character, Integer> chars = new HashMap<>();
        int numOfCharacters = 0;
        for (String data: trainingData) {
            for (char letter: data.toCharArray()) {
                if (letter >= 'a' && letter <= 'z') {
                    Integer freq = chars.get(letter);
                    chars.put(letter, (freq == null ? 1 : freq + 1));
                    numOfCharacters++;
                }
            }
        }
        
        if (numOfCharacters == 0) {
            return null;
        }
        
        Map<Character, Float> PDF = new HashMap<>();
        for (Character c: chars.keySet()) {
            PDF.put(c, (float)chars.get(c) / numOfCharacters);
        }
        
        Map<Character, Float> CDF = new HashMap<>();
        float sum = 0.0F;
        Character[] charsArray = new Character[PDF.size()];
        Arrays.sort(PDF.keySet().toArray(charsArray));
        for (Character c: charsArray) {
            sum += PDF.get(c);
            CDF.put(c, sum);
        }
        
        Map<Character, Integer> lastIndexOfLetterInAlphabet = new HashMap<>();
        for (Character c : PDF.keySet()) {
            lastIndexOfLetterInAlphabet.put(c, Math.round(CDF.get(c) * base));
        }
        
        char[] output = new char[base];
        for (int i = 0; i < base; i++) {
            for (Character c : charsArray) {
                if (i < lastIndexOfLetterInAlphabet.get(c)) {
                    output[i] = c;
                    break;
                }
            }
        }
        
        
        return output;
    }
}
