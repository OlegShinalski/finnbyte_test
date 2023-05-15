package finnbyte.model.tariff;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDatabase {

    private Map<Package, PackageTariff> packageTariffs = new HashMap<>();

    public Tariff getTariff(Package pckg, TariffUnit unit) {
        return getPackageTariff(pckg).getTariff(unit);
    }

    public PackageTariff getPackageTariff(Package pckg) {
        packageTariffs.putIfAbsent(pckg, new PackageTariff());
        return packageTariffs.get(pckg);
    }

    public void setPackageTariff(Package pckg, PackageTariff packageTariff) {
        packageTariffs.put(pckg, packageTariff);
    }

}
