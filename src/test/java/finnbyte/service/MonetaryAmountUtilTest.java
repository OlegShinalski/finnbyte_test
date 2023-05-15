package finnbyte.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import finnbyte.model.MonetaryAmount;

public class MonetaryAmountUtilTest {

    @Test
    public void isZeroOrEmpty() {
        assertThat(MonetaryAmountUtils.isZeroOrEmpty(null)).isTrue();
        assertThat(MonetaryAmountUtils.isZeroOrEmpty(new MonetaryAmount(null, MonetaryAmount.EUR))).isTrue();
        assertThat(MonetaryAmountUtils.isZeroOrEmpty(new MonetaryAmount(BigDecimal.ZERO, MonetaryAmount.EUR))).isTrue();
        assertThat(MonetaryAmountUtils.isZeroOrEmpty(new MonetaryAmount(BigDecimal.ONE, MonetaryAmount.EUR))).isFalse();
    }

}