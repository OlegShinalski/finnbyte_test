package finnbyte.model.simpleTariff;

import java.util.Map;

import finnbyte.model.MonetaryAmount;
import finnbyte.model.tariff.Package;
import lombok.Data;

@Data
public class SimpleTariffDatabase {

    private Map<Package, SimpleTariff> tariffs;
    private MonetaryAmount smsPrice;
    private MonetaryAmount minutePrice;

}
