package finnbyte.model.tariff;

import finnbyte.model.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TariffScaleRow {

    private int from = Integer.MIN_VALUE;
    private int to = Integer.MAX_VALUE;
    private MonetaryAmount price;

}
