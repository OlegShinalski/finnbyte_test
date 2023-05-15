package finnbyte.service;

import java.util.Optional;

import finnbyte.model.MonetaryAmount;

public class MonetaryAmountUtils {

    public static boolean isZeroOrEmpty(MonetaryAmount value) {
        return Optional.ofNullable(value).map(MonetaryAmount::getValue).map(e -> e.signum() == 0)
                .orElse(true);
    }

}
