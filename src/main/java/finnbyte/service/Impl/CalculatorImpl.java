package finnbyte.service.Impl;

import static com.google.common.base.MoreObjects.firstNonNull;
import static finnbyte.model.MonetaryAmount.EMPTY_EUR;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import finnbyte.model.Invoice;
import finnbyte.model.InvoiceRow;
import finnbyte.model.InvoiceRowUnit;
import finnbyte.model.MonetaryAmount;
import finnbyte.model.tariff.Package;
import finnbyte.model.tariff.Tariff;
import finnbyte.model.tariff.TariffDatabase;
import finnbyte.model.tariff.TariffScaleRow;
import finnbyte.model.tariff.TariffUnit;
import finnbyte.service.Calculator;
import finnbyte.service.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CalculatorImpl implements Calculator {

    @Getter
    private TariffDatabase tariffDatabase;

    @Override
    public Invoice calculate(Package pckg, int sms_amount, int minutes_amount) {
        if (pckg == null) {
            throw new IllegalArgumentException();
        }
        List<InvoiceRow> invoiceRows = Lists.newArrayList();
        MonetaryAmount fixedPrice = firstNonNull(tariffDatabase.getPackageTariff(pckg).getFixedPrice(), EMPTY_EUR).round2();
        invoiceRows.add(new InvoiceRow(InvoiceRowUnit.FIXED_PRICE, fixedPrice));
        if (sms_amount > 0) {
            invoiceRows.addAll(calculate(pckg, TariffUnit.SMS, sms_amount));
        }
        if (minutes_amount > 0) {
            invoiceRows.addAll(calculate(pckg, TariffUnit.MINUTES, minutes_amount));
        }

        invoiceRows = invoiceRows.stream()
                .filter(e -> !e.isZeroSumma())
                .collect(toList());
        return new Invoice(pckg, invoiceRows);
    }

    private List<InvoiceRow> calculate(Package pckg, TariffUnit unit, int amount) {
        List<InvoiceRow> rows = Lists.newArrayList();

        Tariff tariff = tariffDatabase.getTariff(pckg, unit);
        List<TariffScaleRow> scale = tariff.getScale().stream().sorted(Comparator.comparing(TariffScaleRow::getFrom)).collect(toList());
        for (TariffScaleRow tariffRow : scale) {
            if (amount > tariffRow.getFrom()) {
                int caculatedAmount = Math.min(amount, tariffRow.getTo()) - tariffRow.getFrom();
                MonetaryAmount summa = firstNonNull(tariffRow.getPrice(), EMPTY_EUR).multiply(caculatedAmount);
                rows.add(new InvoiceRow(Utils.unitToInvoiceUnit(unit), caculatedAmount, tariffRow.getPrice(), summa));
            }
        }
        return rows;
    }

}
