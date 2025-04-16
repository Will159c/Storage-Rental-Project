import javax.swing.JFormattedTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A) Class name: DateLabelFormatter
 * B) Date of the Code: March 30 ,2025
 * C) Programmer's name: Alexis Anguiano
 * D) Brief description: Utility class used by JDatePicker to convert between Date and String. It handles formatting user selected dates and parsing back into usable values.
 * E) Brief explanation of important function: stringToValue parses a string to a date object. valueToString formats a date to a string.
 * F) Important data structures: Uses simpledateformat internally with fixed format: m-d-y
 * G) Algorithm used: simpledateformat
 */

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    /**
     * Converts a string to a {@link java.util.Date}.
     * @param text the date string
     * @return the parsed date
     * @throws ParseException if the date is invalid
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormat.parse(text);
    }

    /**
     * Converts a {@link java.util.Date} to a string
     * @param value value the date object
     * @return formatted date string
     * @throws ParseException
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormat.format(cal.getTime());
        }
        return "";
    }
}
