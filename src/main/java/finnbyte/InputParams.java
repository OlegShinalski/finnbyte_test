package finnbyte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputParams {

    private String pckg;
    private int smsNumber;
    private int minutesNumber;

}
