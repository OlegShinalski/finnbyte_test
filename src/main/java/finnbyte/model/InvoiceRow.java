package finnbyte.model;

import finnbyte.service.MonetaryAmountUtils;
import lombok.Data;

@Data
public class InvoiceRow {
    private InvoiceRowUnit unit;
    private Integer amount;
    private MonetaryAmount price;
    private MonetaryAmount summa;

    public InvoiceRow(InvoiceRowUnit unit, Integer amount, MonetaryAmount price, MonetaryAmount summa) {
        this.unit = unit;
        this.amount = amount;
        this.price = price;
        this.summa = summa;
    }

    public InvoiceRow(InvoiceRowUnit unit, MonetaryAmount summa) {
        this(unit, null, null, summa);
    }

    public boolean isZeroSumma() {
        return MonetaryAmountUtils.isZeroOrEmpty(getSumma());
    }

}
