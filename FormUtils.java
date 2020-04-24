import static j2html.TagCreator.*;
import j2html.tags.Tag;
import java.util.Map;


public class FormUtils {

    public static String docType
            = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

    /**
     * Generate a text box for email address input.
     *
     * @param sample Example of what input should look like
     * @return a j2html Tag for the input box.
     */
    public static Tag emailInput(String sample) {
        return input()
                .withType("email")
                .withName("email")
                .withPlaceholder(sample)
                .isRequired();
    }

    /**
     * Generate a text box for telephone number input.
     *
     * @param sample Example of what input should look like
     * @return a j2html Tag for the input box.
     */
    public static Tag phoneInput(String sample) {
        return input()
                .withType("tel")
                .withName("phone")
                .withPlaceholder(sample)
                .isRequired();
    }

    /**
     * Generate a text box for general text input.
     *
     * @param name The field name
     * @param sample Example of what input should look like
     * @return a j2html Tag for the input box.
     */
    public static Tag textboxInput(String name, String sample) {
        return input()
                .withType("text")
                .withName(name)
                .withPlaceholder(sample)
                .isRequired();
    }

    /**
     * Generate a labeled submit button.
     *
     * @param text The text for the button
     * @return a j2html Tag for the button
     */
    public static Tag submitButton(String text) {
        return button(text).withType("submit");
    }

    /**
     * Generate a set of radio buttons.
     *
     * @param title The text to put before the set of buttons.  @todo this is inconsistent with other methods in this class.
     * @param name The button set name
     * @param vt A map of keys and values for the result (key) and the label
     *           to go with the button (value).
     * @return a j2html Tag for the button set
     */
    public static Tag radioSet(String title, String name, Map<String,String> vt) {
        return span(
                span(title),
                each(vt.keySet(), k -> {
                    return span(
                            input().withType("radio").withName(name).withValue(k),
                            span(vt.get(k))
                    );
                })
        );
    }
}
