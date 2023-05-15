package finnbyte.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;

import org.junit.Test;

import finnbyte.model.MonetaryAmount;

public class MonetaryAmountTest {

    @Test
    public void shouldRoundEmptyValue() {
        assertThat(new MonetaryAmount(BigDecimal.ZERO, MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(null, MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(null, MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
    }

    @Test
    public void shouldRoundNonEmptyValue() {
        assertThat(new MonetaryAmount(new BigDecimal("10.12345"), MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("10.12"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("99.999"), MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("100.00"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("0.005"), MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.01"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("0.004"), MonetaryAmount.EUR).round2())
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
    }

    @Test
    public void shouldHandleEmptyValueInMultiply() {
        assertThat(new MonetaryAmount(BigDecimal.ZERO, MonetaryAmount.EUR).multiply(10))
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(null, MonetaryAmount.EUR).multiply(10))
                .isEqualTo(new MonetaryAmount(new BigDecimal("0.00"), MonetaryAmount.EUR));
    }

    @Test
    public void shouldMultiplyNonEmptyValueInMultiply() {
        assertThat(new MonetaryAmount(new BigDecimal("1.123"), MonetaryAmount.EUR).multiply(10))
                .isEqualTo(new MonetaryAmount(new BigDecimal("11.23"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("3.333333"), MonetaryAmount.EUR).multiply(3))
                .isEqualTo(new MonetaryAmount(new BigDecimal("10.00"), MonetaryAmount.EUR));
    }

    @Test
    public void shouldAddNotEmptyValue() {
        assertThat(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal("2.34"), MonetaryAmount.EUR)))
                .isEqualTo(new MonetaryAmount(new BigDecimal("3.46"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("12.345"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal("67.892"), MonetaryAmount.EUR)))
                .isEqualTo(new MonetaryAmount(new BigDecimal("80.24"), MonetaryAmount.EUR));

    }

    @Test
    public void shouldAddEmptyValue() {
        assertThat(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR).add(null))
                .isEqualTo(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR).add(new MonetaryAmount(null, MonetaryAmount.EUR)))
                .isEqualTo(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR));
        assertThat(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal(0), MonetaryAmount.EUR)))
                .isEqualTo(new MonetaryAmount(new BigDecimal("1.12"), MonetaryAmount.EUR));
    }

    @Test
    public void shouldThrowAnExceptionAddingDiffewrentCurrency() {
        Throwable thrown = catchThrowable(() ->
                new MonetaryAmount(new BigDecimal("1.00"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal("1.00"), "USD")));
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);

        thrown = catchThrowable(() ->
                new MonetaryAmount(new BigDecimal("1.00"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal("1.00"), "")));
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);

        thrown = catchThrowable(() ->
                new MonetaryAmount(new BigDecimal("1.00"), MonetaryAmount.EUR).add(new MonetaryAmount(new BigDecimal("1.00"), null)));
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
    }

}