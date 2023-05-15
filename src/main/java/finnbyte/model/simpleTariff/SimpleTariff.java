package finnbyte.model.simpleTariff;

import finnbyte.model.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleTariff {
    private int smsLimit;
    private int minsLimit;
    private MonetaryAmount fixedPrice;
}
