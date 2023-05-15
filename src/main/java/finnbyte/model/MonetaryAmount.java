package finnbyte.model;

import static com.google.common.base.MoreObjects.firstNonNull;
import static finnbyte.service.Utils.roundTo2;

import java.math.BigDecimal;
import java.util.Objects;

import finnbyte.service.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MonetaryAmount {
    public static final String EUR = "EUR";
    public static final MonetaryAmount EMPTY_EUR = MonetaryAmount.builder().value(BigDecimal.ZERO).curreny(EUR).build();

    private BigDecimal value = BigDecimal.ZERO;

    @Builder.Default
    private String curreny = "EUR";

    public MonetaryAmount add(MonetaryAmount other) {
        if (other == null || other.getValue() == null) {
            return this;
        }
        if (!Objects.equals(curreny, other.curreny)) {
            throw new IllegalArgumentException("Different currencies");
        }
        return new MonetaryAmount(Utils.roundTo2(value == null ? other.value : value.add(other.value)), curreny);
    }

    public MonetaryAmount multiply(int value) {
        return new MonetaryAmount(
                roundTo2(firstNonNull(this.getValue(), BigDecimal.ZERO).multiply(new BigDecimal(value))),
                this.getCurreny());
    }

    public MonetaryAmount round2() {
        return new MonetaryAmount(Utils.roundTo2(firstNonNull(this.getValue(), BigDecimal.ZERO)), this.getCurreny());
    }
}
