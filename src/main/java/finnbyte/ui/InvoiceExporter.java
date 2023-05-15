package finnbyte.ui;

import java.io.PrintWriter;
import java.util.Optional;

import finnbyte.model.Invoice;
import finnbyte.model.InvoiceRow;
import finnbyte.model.MonetaryAmount;

public class InvoiceExporter {

    private PrintWriter writer;

    public InvoiceExporter(PrintWriter writer) {
        this.writer = writer;
    }

    public void export(Invoice invoice) {
        writer.println("Invoice. Package=" + invoice.getPckg());
        writer.println("Unit          Amount          Price               Summa");

        for (InvoiceRow row : invoice.getRows()) {
            writer.println(
                    String.format("%-15s %5s %-20s %-20s",
                            row.getUnit().toString(),
                            Optional.ofNullable(row.getAmount()).map(Object::toString).orElse(""),
                            toString(row.getPrice()),
                            toString(row.getSumma()))
            );
        }
        writer.println("---------------------------------------------------------");
        writer.println(
                String.format("Total :                                    %s",
                        toString(invoice.calculateTotal()))
        );
        writer.flush();
    }

    private String toString(MonetaryAmount value) {
        String result = "";
        if (value != null) {
            result = String.format("%10s %s",
                    value.getValue().toString(),
                    value.getCurreny());
        }
        return result;
    }

}
