package finnbyte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;

import finnbyte.model.tariff.Package;
import finnbyte.model.tariff.TariffDatabase;
import finnbyte.service.Calculator;
import finnbyte.service.Impl.CalculatorImpl;
import finnbyte.ui.InputLineParser;
import finnbyte.ui.InvoiceExporter;

public class InvoiceTestApp {

    public static void main(String[] args) throws IOException {
        TariffDatabase database = Init.initTariffDatabase();
        Calculator calculator = new CalculatorImpl(database);
        PrintWriter writer = new PrintWriter(System.out);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        InvoiceExporter invoicePrinter = new InvoiceExporter(writer);

        String line;
        while ((line = reader.readLine()) != null) {
            Optional<InputParams> parsed = InputLineParser.parseLine(line);
            parsed.ifPresent(e -> {
                invoicePrinter.export(calculator.calculate(Package.forCode(e.getPckg()), e.getSmsNumber(), e.getMinutesNumber()));
            });
        }

        writer.close();
        reader.close();
    }

}
