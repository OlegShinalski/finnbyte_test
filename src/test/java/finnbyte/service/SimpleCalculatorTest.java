package finnbyte.service;

import finnbyte.Init;
import finnbyte.service.Impl.SimpleCalculatorImpl;

public class SimpleCalculatorTest extends AbstractCalculatorTest {

    @Override
    Calculator getCalculator() {
        return new SimpleCalculatorImpl(Init.initSimpleTariffDatabase());
    }
}
