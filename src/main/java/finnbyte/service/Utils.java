package finnbyte.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import finnbyte.model.InvoiceRowUnit;
import finnbyte.model.tariff.TariffUnit;

public class Utils {

    public static BigDecimal roundTo2(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static Map<TariffUnit, InvoiceRowUnit> UNIT_TO_INVOICE_UNIT = new ImmutableMap.Builder<TariffUnit, InvoiceRowUnit>()
            .put(TariffUnit.MINUTES, InvoiceRowUnit.MINUTES)
            .put(TariffUnit.SMS, InvoiceRowUnit.SMS)
            .build();

    public static InvoiceRowUnit unitToInvoiceUnit(TariffUnit unit) {
        return UNIT_TO_INVOICE_UNIT.getOrDefault(unit, InvoiceRowUnit.FIXED_PRICE);
    }

}
