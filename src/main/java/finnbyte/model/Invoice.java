package finnbyte.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import finnbyte.model.tariff.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Invoice {
    private Package pckg;
    private List<InvoiceRow> rows;

    public MonetaryAmount calculateTotal() {
        MonetaryAmount summa = rows.stream()
                .map(InvoiceRow::getSumma)
                .reduce(MonetaryAmount.EMPTY_EUR, MonetaryAmount::add);
        return summa;
    }
}
