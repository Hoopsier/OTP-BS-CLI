package application;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest {

  private InputStream originalIn;
  private PrintStream originalOut;
  private ByteArrayOutputStream output;

  @BeforeEach
  void setUp() {
    originalIn = System.in;
    originalOut = System.out;
    output = new ByteArrayOutputStream();
  }

  @AfterEach
  void tearDown() {
    System.setIn(originalIn);
    System.setOut(originalOut);
  }

  private String runApp(String input) {
    System.setIn(new ByteArrayInputStream(
        input.getBytes(StandardCharsets.UTF_8)));
    System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

    App.main(new String[0]);

    return output.toString(StandardCharsets.UTF_8);
  }

  @Test
  void displaysLanguagePrompt() {
    String result = runApp("1\n1\n32\n");

    assertTrue(result.contains(
        "Please select your preferred language."));
    assertTrue(result.contains("1: English"));
    assertTrue(result.contains("2: Finnish"));
    assertTrue(result.contains("3: Swedish"));
    assertTrue(result.contains("4: Japanese"));
  }

  @Test
  void defaultsToEnglishForUnsupportedLanguageNumber() {
    String result = runApp("9\n1\n32\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.US);

    assertTrue(result.contains(rb.getString("choice")));
    assertTrue(result.contains(rb.getString("input")));
    assertTrue(result.contains(rb.getString("output")));
  }

  @Test
  void selectsFinnishLanguage() {
    String result = runApp("2\n1\n32\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.of("fi", "FI"));

    assertTrue(result.contains(rb.getString("choice")));
    assertTrue(result.contains(rb.getString("input")));
  }

  @Test
  void selectsSwedishLanguage() {
    String result = runApp("3\n1\n32\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.of("sv", "SE"));

    assertTrue(result.contains(rb.getString("choice")));
    assertTrue(result.contains(rb.getString("input")));
  }

  @Test
  void selectsJapaneseLanguage() {
    String result = runApp("4\n1\n32\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.JAPAN);

    assertTrue(result.contains(rb.getString("choice")));
    assertTrue(result.contains(rb.getString("input")));
  }

  @Test
  void convertsFahrenheitToCelsius() {
    String result = runApp("1\n1\n32\n");

    assertTrue(result.contains("0.0"));
  }

  @Test
  void convertsCelsiusToFahrenheit() {
    String result = runApp("1\n2\n100\n");

    assertTrue(result.contains("212.0"));
  }

  @Test
  void rejectsInvalidChoiceAndPromptsAgain() {
    String result = runApp("1\n7\n1\n32\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.US);

    assertTrue(result.contains(rb.getString("bad_choice")));
    assertTrue(result.contains("0.0"));
  }

  @Test
  void acceptsChoiceTwoAfterInvalidChoice() {
    String result = runApp("1\n0\n2\n100\n");
    ResourceBundle rb = ResourceBundle.getBundle(
        "Messages", Locale.US);

    assertTrue(result.contains(rb.getString("bad_choice")));
    assertTrue(result.contains("212.0"));
  }

  @Test
  void convertsFreezingPointOfCelsiusCorrectly() {
    String result = runApp("1\n2\n0\n");

    assertTrue(result.contains("32.0"));
  }

  @Test
  void convertsBoilingPointOfFahrenheitCorrectly() {
    String result = runApp("1\n1\n212\n");

    assertTrue(result.contains("100.0"));
  }
}
