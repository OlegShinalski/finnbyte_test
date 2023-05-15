package finnbyte.ui;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import finnbyte.InputParams;

public class InputLineParser {

    private static final String PATTERN = "(^\\s*[smlSML])\\s+(\\d+)\\s+(\\d+)\\s*$";

    public static Optional<InputParams> parseLine(String text) {
        Optional<InputParams> result = Optional.empty();
        if (text != null) {
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(text.trim());

            if (matcher.find()) {
                InputParams ip = new InputParams();
                ip.setPckg(matcher.group(1));
                ip.setSmsNumber(Integer.valueOf(matcher.group(2)));
                ip.setMinutesNumber(Integer.valueOf(matcher.group(3)));
                result = Optional.of(ip);
            } else {
                System.err.println("Invalid data :" + text);
            }
        }

        return result;
    }

}
