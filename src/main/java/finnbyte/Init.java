package finnbyte;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.math.BigDecimal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import finnbyte.model.MonetaryAmount;
import finnbyte.model.simpleTariff.SimpleTariff;
import finnbyte.model.simpleTariff.SimpleTariffDatabase;
import finnbyte.model.tariff.Package;
import finnbyte.model.tariff.PackageTariff;
import finnbyte.model.tariff.Tariff;
import finnbyte.model.tariff.TariffDatabase;
import finnbyte.model.tariff.TariffScaleRow;
import finnbyte.model.tariff.TariffUnit;

public class Init {

    private static final MonetaryAmount ZERO_COST = MonetaryAmount.builder().value(BigDecimal.ZERO).curreny("EUR").build();
    private static final MonetaryAmount ONE_MINUTE_COST = MonetaryAmount.builder().value(new BigDecimal("0.20")).curreny("EUR").build();
    private static final MonetaryAmount ONE_SMS_COST = MonetaryAmount.builder().value(new BigDecimal("0.30")).curreny("EUR").build();

    public static SimpleTariffDatabase initSimpleTariffDatabase() {
        SimpleTariffDatabase database = new SimpleTariffDatabase();
        database.setMinutePrice(ONE_MINUTE_COST);
        database.setSmsPrice(ONE_SMS_COST);
        database.setTariffs(
                Maps.newHashMap(
                        ImmutableMap.<Package, SimpleTariff> builder().
                                put(Package.SMALL,
                                        SimpleTariff.builder().minsLimit(10).smsLimit(50).fixedPrice(
                                                MonetaryAmount.builder().value(new BigDecimal(5)).build()
                                        ).build()).
                                put(Package.MEDIUM,
                                        SimpleTariff.builder().minsLimit(50).smsLimit(100).fixedPrice(
                                                MonetaryAmount.builder().value(new BigDecimal(10)).build()
                                        ).build()).
                                put(Package.LARGE,
                                        SimpleTariff.builder().minsLimit(500).smsLimit(500).fixedPrice(
                                                MonetaryAmount.builder().value(new BigDecimal(20)).build()
                                        ).build()
                                ).build()
                )
        );
        return database;
    }

    public static TariffDatabase initTariffDatabase() {
        TariffDatabase tariffDatabase = new TariffDatabase();
        initPackageTariff(tariffDatabase, Package.SMALL, 10, 50, 5);
        initPackageTariff(tariffDatabase, Package.MEDIUM, 50, 100, 10);
        initPackageTariff(tariffDatabase, Package.LARGE, 500, 500, 20);
        return tariffDatabase;
    }

    private static void initPackageTariff(TariffDatabase tariffDatabase, Package pckg, int minutesLimit, int smsLimit, double fixedPrice) {
        Tariff t_mins = Tariff.builder()
                .scale(newArrayList(
                        TariffScaleRow.builder().from(0).to(minutesLimit).price(ZERO_COST).build(),
                        TariffScaleRow.builder().from(minutesLimit).to(Integer.MAX_VALUE).price(ONE_MINUTE_COST).build()
                ))
                .build();
        Tariff t_sms = Tariff.builder()
                .scale(newArrayList(
                        TariffScaleRow.builder().from(0).to(smsLimit).price(ZERO_COST).build(),
                        TariffScaleRow.builder().from(smsLimit).to(Integer.MAX_VALUE).price(ONE_SMS_COST).build()
                ))
                .build();

        PackageTariff packageTariff = PackageTariff.builder()
                .pckg(pckg)
                .fixedPrice(new MonetaryAmount(new BigDecimal(fixedPrice), MonetaryAmount.EUR).round2())
                .tariffs(newHashMap(
                        ImmutableMap.<TariffUnit, Tariff> builder().
                                put(TariffUnit.MINUTES, t_mins).
                                put(TariffUnit.SMS, t_sms).
                                build()
                )).build();

        tariffDatabase.setPackageTariff(pckg, packageTariff);
    }
}
