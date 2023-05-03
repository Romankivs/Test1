import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

class Pair<L,R> {
    public L a;
    public R b;
    public Pair(L a, R b){
        this.a = a;
        this.b = b;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        Pair pair = (Pair) obj;
        return a.equals(pair.a) && b.equals(pair.b);
    }
}

class WordDistanceAnalyzer {
    private static int distanceBetweenWords(String a, String b) {
        int maxLength = Math.max(a.length(), b.length());
        int minLength = Math.min(a.length(), b.length());
        int distance = maxLength - minLength;
        for (int i = 0; i < minLength; i++) {
            if (Character.toLowerCase(a.charAt(i)) != Character.toLowerCase(b.charAt(i))) {
                distance++;
            }
        }
        return distance;
    }

    public List<Pair<String, String>> getPairsWithMaxDistance(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Empty input");
        }

        Scanner scanner = new Scanner(input);

        Pattern delimPattern = Pattern.compile("[\\p{javaWhitespace}\\.!\"#$%&()*+^,-./:;<=>\\?@_\\{|\\}~\\[\\]\\\\]+");
        scanner.useDelimiter(delimPattern);
        List<String> words = new ArrayList<String>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() > 30) {
                word = word.substring(0, 30);
            }
            String finalWord = word;
            if (words.stream().filter(x -> x.equalsIgnoreCase(finalWord)).count() == 0) {
                words.add(word);
            }
        }

        int maxDistance = Integer.MIN_VALUE;
        List<Pair<String, String>> maxDistWordPairs = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                String firstWord = words.get(i);
                String secondWord = words.get(j);
                int distanceBetweenWords = distanceBetweenWords(firstWord, secondWord);
                if (distanceBetweenWords == maxDistance) {
                    Pair<String, String> pairOfWords = new Pair<String, String>(firstWord, secondWord);
                    maxDistWordPairs.add(pairOfWords);
                }
                else if (distanceBetweenWords > maxDistance) {
                    maxDistWordPairs.clear();
                    Pair<String, String> pairOfWords = new Pair<String, String>(firstWord, secondWord);
                    maxDistWordPairs.add(pairOfWords);
                    maxDistance = distanceBetweenWords;
                }
            }
        }
        return maxDistWordPairs;
    }
}

public class Main {
    public static void main(String[] args) {
        var analyzer = new WordDistanceAnalyzer();
        var maxDistWordPairs = analyzer.getPairsWithMaxDistance("");
        for (int i = 0; i < maxDistWordPairs.size(); i++) {
            System.out.println(maxDistWordPairs.get(i).a + " - " + maxDistWordPairs.get(i).b);
        }
    }
}