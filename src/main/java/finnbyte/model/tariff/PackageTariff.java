package finnbyte.model.tariff;

import java.util.HashMap;
import java.util.Map;

import finnbyte.model.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageTariff {
    private Package pckg;
    private MonetaryAmount fixedPrice;
    private Map<TariffUnit, Tariff> tariffs = new HashMap<>();

    public Tariff getTariff(TariffUnit unit) {
        tariffs.putIfAbsent(unit, new Tariff());
        return tariffs.get(unit);
    }
}
