package stemmer;

public class TestApp {

    public static void main(String[] args) throws Throwable {

        String input = "biserică";
        RomanianStemmer stemmer = new RomanianStemmer();

        if (input.length() > 0) {
            stemmer.setCurrent(input.toString());
            stemmer.stem();

            System.out.println(stemmer.getCurrent());

        }
    }
}
