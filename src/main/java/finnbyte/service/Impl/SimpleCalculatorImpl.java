package finnbyte.service.Impl;

import static com.google.common.base.MoreObjects.firstNonNull;
import static finnbyte.model.MonetaryAmount.EMPTY_EUR;

import java.util.List;

import com.google.common.collect.Lists;

import finnbyte.model.Invoice;
import finnbyte.model.InvoiceRow;
import finnbyte.model.InvoiceRowUnit;
import finnbyte.model.MonetaryAmount;
import finnbyte.model.simpleTariff.SimpleTariff;
import finnbyte.model.simpleTariff.SimpleTariffDatabase;
import finnbyte.model.tariff.Package;
import finnbyte.service.Calculator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SimpleCalculatorImpl implements Calculator {

    @Getter
    private SimpleTariffDatabase tariffDatabase;

    @Override
    public Invoice calculate(Package pckg, int smsAmount, int minutesAmount) {
        if (pckg == null) {
            throw new IllegalArgumentException();
        }
        List<InvoiceRow> invoiceRows = Lists.newArrayList();
        SimpleTariff tariff = tariffDatabase.getTariffs().get(pckg);
        if (tariff != null) {
            if (smsAmount > tariff.getSmsLimit()) {
                int amount = smsAmount - tariff.getSmsLimit();
                MonetaryAmount summa = firstNonNull(tariffDatabase.getSmsPrice(), EMPTY_EUR).multiply(amount);
                invoiceRows.add(new InvoiceRow(InvoiceRowUnit.SMS, amount, tariffDatabase.getSmsPrice(), summa));
            }
            if (minutesAmount > tariff.getMinsLimit()) {
                int amount = minutesAmount - tariff.getMinsLimit();
                MonetaryAmount summa = firstNonNull(tariffDatabase.getMinutePrice(), EMPTY_EUR).multiply(amount);
                invoiceRows.add(new InvoiceRow(InvoiceRowUnit.MINUTES, amount, tariffDatabase.getMinutePrice(), summa));
            }
            invoiceRows.add(new InvoiceRow(InvoiceRowUnit.FIXED_PRICE, firstNonNull(tariff.getFixedPrice(), EMPTY_EUR).round2()));
        } else {
            throw new IllegalArgumentException("Can't find tafirf for Package=" + pckg);
        }
        return new Invoice(pckg, invoiceRows);
    }
}
