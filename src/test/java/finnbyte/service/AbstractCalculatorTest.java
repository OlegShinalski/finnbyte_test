package finnbyte.service;

import static finnbyte.model.InvoiceRowUnit.FIXED_PRICE;
import static finnbyte.model.InvoiceRowUnit.MINUTES;
import static finnbyte.model.InvoiceRowUnit.SMS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import finnbyte.Init;
import finnbyte.model.Invoice;
import finnbyte.model.InvoiceRow;
import finnbyte.model.MonetaryAmount;
import finnbyte.model.tariff.Package;
import finnbyte.model.tariff.TariffDatabase;
import finnbyte.service.Impl.CalculatorImpl;

public abstract class AbstractCalculatorTest {

    public static final String EUR = MonetaryAmount.EUR;
    private static final MonetaryAmount EURO_5 = new MonetaryAmount(new BigDecimal("5.00"), EUR);
    private static final MonetaryAmount EURO_10 = new MonetaryAmount(new BigDecimal("10.00"), EUR);
    private static final MonetaryAmount EURO_20 = new MonetaryAmount(new BigDecimal("20.00"), EUR);

    private Calculator calculator;

    abstract Calculator getCalculator();
    
    @Before
    public void startUp() {
        calculator = getCalculator();
    }

    @Test
    public void shouldThrowAnExceptionForNullPackage() {
        Throwable thrown = catchThrowable(() -> getCalculator().calculate(null, 1, 1));
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldBeFixedSummaForNegativeValues() {
        Invoice invoice;
        invoice = getCalculator().calculate(Package.SMALL, -1, -1);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_5);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_5));
    }

    @Test
    public void shouldBeFixedSummaForZeroValues() {
        Invoice invoice;
        invoice = getCalculator().calculate(Package.SMALL, 0, 0);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_5);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_5));

        invoice = getCalculator().calculate(Package.MEDIUM, 0, 0);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_10);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_10));

        invoice = getCalculator().calculate(Package.LARGE, 0, 0);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_20);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_20));
    }

    @Test
    public void shouldBeFixedSummaForCornerValues() {
        Invoice invoice;

        invoice = getCalculator().calculate(Package.SMALL, 50, 10);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_5);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_5));

        invoice = getCalculator().calculate(Package.MEDIUM, 100, 50);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_10);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_10));

        invoice = getCalculator().calculate(Package.LARGE, 500, 500);
        assertThat(invoice).isNotNull();
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_20));
    }

    @Test
    public void shouldBeFixedSummaForValueBelowFixedAmountForSmallPackage() {
        Invoice invoice;

        invoice = getCalculator().calculate(Package.SMALL, 20, 5);
        assertThat(invoice).isNotNull();
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(EURO_5);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_5));
    }

    @Test
    public void shouldBeFixedAndSummaByTariffForValueAboveFixedAmountForSmallPackage() {
        Invoice invoice;

        invoice = getCalculator().calculate(Package.SMALL, 51, 10);
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(new MonetaryAmount(new BigDecimal("5.30"), EUR));
        assertThat(invoice.getRows()).containsOnly(
                new InvoiceRow(FIXED_PRICE, EURO_5),
                new InvoiceRow(SMS, 1, new MonetaryAmount(new BigDecimal("0.30"), EUR), new MonetaryAmount(new BigDecimal("0.30"), EUR))
        );

        invoice = getCalculator().calculate(Package.SMALL, 5, 11);
        assertThat(invoice).isNotNull().extracting(Invoice::calculateTotal).isEqualTo(new MonetaryAmount(new BigDecimal("5.20"), EUR));
        assertThat(invoice.getRows()).containsOnly(
                new InvoiceRow(FIXED_PRICE, EURO_5),
                new InvoiceRow(MINUTES, 1, new MonetaryAmount(new BigDecimal("0.20"), EUR), new MonetaryAmount(new BigDecimal("0.20"), EUR))
        );

        invoice = getCalculator().calculate(Package.SMALL, 999, 999);
        assertThat(invoice).isNotNull().extracting(Invoice::calculateTotal).isEqualTo(new MonetaryAmount(new BigDecimal("487.50"), EUR));
        assertThat(invoice.getRows()).containsOnly(
                new InvoiceRow(FIXED_PRICE, EURO_5),
                new InvoiceRow(SMS, 949, new MonetaryAmount(new BigDecimal("0.30"), EUR), new MonetaryAmount(new BigDecimal("284.70"), EUR)),
                new InvoiceRow(MINUTES, 989, new MonetaryAmount(new BigDecimal("0.20"), EUR), new MonetaryAmount(new BigDecimal("197.80"), EUR))
        );
    }

    @Test
    public void shouldBeFixedSummaForValueBelowFixedAmountForLargePackage() {
        Invoice invoice;

        invoice = getCalculator().calculate(Package.LARGE, 200, 200);
        assertThat(invoice).isNotNull().extracting(Invoice::calculateTotal).isEqualTo(EURO_20);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_20));

        invoice = getCalculator().calculate(Package.LARGE, 500, 500);
        assertThat(invoice).isNotNull().extracting(Invoice::calculateTotal).isEqualTo(EURO_20);
        assertThat(invoice.getRows()).containsOnly(new InvoiceRow(FIXED_PRICE, EURO_20));
    }

    @Test
    public void shouldBeFixedAndSummaByTariffForValueAboveFixedAmountForLargePackage() {
        Invoice invoice;

        invoice = getCalculator().calculate(Package.LARGE, 501, 501);
        assertThat(invoice).extracting(Invoice::calculateTotal).isEqualTo(new MonetaryAmount(new BigDecimal("20.50"), EUR));
        assertThat(invoice.getRows()).containsOnly(
                new InvoiceRow(FIXED_PRICE, EURO_20),
                new InvoiceRow(SMS, 1, new MonetaryAmount(new BigDecimal("0.30"), EUR), new MonetaryAmount(new BigDecimal("0.30"), EUR)),
                new InvoiceRow(MINUTES, 1, new MonetaryAmount(new BigDecimal("0.20"), EUR), new MonetaryAmount(new BigDecimal("0.20"), EUR))
        );

        invoice = getCalculator().calculate(Package.LARGE, 999, 999);
        assertThat(invoice).isNotNull().extracting(Invoice::calculateTotal).isEqualTo(new MonetaryAmount(new BigDecimal("269.50"), EUR));
        assertThat(invoice.getRows()).containsOnly(
                new InvoiceRow(FIXED_PRICE, EURO_20),
                new InvoiceRow(SMS, 499, new MonetaryAmount(new BigDecimal("0.30"), EUR), new MonetaryAmount(new BigDecimal("149.70"), EUR)),
                new InvoiceRow(MINUTES, 499, new MonetaryAmount(new BigDecimal("0.20"), EUR), new MonetaryAmount(new BigDecimal("99.80"), EUR))
        );
    }
}
