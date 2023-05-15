package finnbyte.service;

import finnbyte.model.Invoice;
import finnbyte.model.tariff.Package;

public interface Calculator {

    Invoice calculate(Package pckg, int sms_amount, int minutes_amount);

}
