package finnbyte.service;

import finnbyte.Init;
import finnbyte.service.Impl.CalculatorImpl;

public class CalculatorTest extends AbstractCalculatorTest {

    @Override
    Calculator getCalculator() {
        return new CalculatorImpl(Init.initTariffDatabase());
    }

}
