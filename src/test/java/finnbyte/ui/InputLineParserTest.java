package finnbyte.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

import finnbyte.InputParams;
import finnbyte.model.MonetaryAmount;
import finnbyte.service.MonetaryAmountUtils;

public class InputLineParserTest {

    @Test
    public void shouldHandleIncorrectValues() {
        assertThat(InputLineParser.parseLine("")).isEqualTo(Optional.empty());
        assertThat(InputLineParser.parseLine(null)).isEqualTo(Optional.empty());
        assertThat(InputLineParser.parseLine("              ")).isEqualTo(Optional.empty());
        assertThat(InputLineParser.parseLine("sml 1 1")).isEqualTo(Optional.empty());
        assertThat(InputLineParser.parseLine("s s 1")).isEqualTo(Optional.empty());
        assertThat(InputLineParser.parseLine("l 10 10 11 12")).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldParseCorrectValues() {
        assertThat(InputLineParser.parseLine("s 1 1")).isEqualTo(Optional.of(new InputParams("s", 1, 1)));
        assertThat(InputLineParser.parseLine("S 1 1")).isEqualTo(Optional.of(new InputParams("S", 1, 1)));
        assertThat(InputLineParser.parseLine("  s   1    1  ")).isEqualTo(Optional.of(new InputParams("s", 1, 1)));
        assertThat(InputLineParser.parseLine("l 10 10")).isEqualTo(Optional.of(new InputParams("l", 10, 10)));
    }
}