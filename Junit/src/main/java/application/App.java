package application;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    System.out.println("Please select your preferred language. 1: English. 2: Finnish. 3: Swedish. 4: Japanese.");
    int lang = 1;
    Scanner scanner = new Scanner(System.in);

    try {
      lang = scanner.nextInt();
    } catch (Exception e) {
      System.out.println("Skill issue when typing a language. defaulting to English.");
    }
    // hey look at that, the demonstrated way got deprecated by the time I did the
    // task. (java 19+)
    // Now it's apparently Locale.COUTRY if it's common, and Locale.of() for
    // everything else
    // Also I didn't need to do any encoding of file stuff when doing this in linux
    // through the cli
    // Steps: touch (to make the .properties files) ->
    // maybe <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    // in pom might is to blame for this working
    Locale locale = Locale.US;
    switch (lang) {
      case 1: // EN
        break;
      case 2: // FI
        locale = Locale.of("fi", "FI");
        break;
      case 3: // SW
        locale = Locale.of("sv", "SE");
        break;
      case 4: // JP
        locale = Locale.JAPAN;
        break;
    }
    // named Messages, since it's less work to do one word, when we have no other
    // property files in play.
    ResourceBundle rb = ResourceBundle.getBundle("Messages", locale);
    System.out.println(rb.getObject("choice"));
    int choice = 0;
    while (choice != 1 || choice != 2) {
      choice = scanner.nextInt();
      if (choice == 1 || choice == 2) {
        break;
      }
      System.out.println(rb.getObject("bad_choice"));
    }
    System.out.print(rb.getObject("input"));
    float in = scanner.nextFloat();
    System.out.print(rb.getObject("output"));

    TemperatureConverter converter = new TemperatureConverter();

    if (choice == 1) {
      System.out.println(converter.fahrToCel(in));
    } else {
      System.out.println(converter.celToFahr(in));
    }
    scanner.close();
  }
}
