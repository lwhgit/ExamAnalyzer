import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        Analyzer analyzer = new Analyzer(fileName);
        analyzer.parseFile();
        analyzer.analyze();
    }
}
