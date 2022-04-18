package johny.dotsville.benefit.validator.register;

import johny.dotsville.benefit.domain.register.CityRegisterResponse;
import johny.dotsville.benefit.domain.Person;
import johny.dotsville.benefit.exception.CityRegisterException;
import johny.dotsville.benefit.domain.Adult;
import johny.dotsville.benefit.domain.Child;
import johny.dotsville.benefit.exception.TransportException;

public class FakeCityRegisterChecker implements CityRegisterChecker {
    private static final String GOOD_1 = "1000";
    private static final String GOOD_2 = "2000";
    private static final String BAD_1 = "1001";
    private static final String BAD_2 = "2001";
    private static final String ERROR_1 = "1002";
    private static final String ERROR_2 = "2002";
    private static final String ERROR_TRANSPORT_1 = "1003";
    private static final String ERROR_TRANSPORT_2 = "2003";

    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        CityRegisterResponse response = new CityRegisterResponse();
        if (person instanceof Adult) {
            Adult adult = (Adult)person;
            String passportSeria = adult.getPassportSeria();

            if (passportSeria.equals(GOOD_1) || passportSeria.equals(GOOD_2)) {
                response.setExisting(true);
                response.setTemporal(false);
            }
            if (passportSeria.equals(BAD_1) || passportSeria.equals(BAD_2)) {
                response.setExisting(false);
            }
            if (passportSeria.equals(ERROR_2) || passportSeria.equals(ERROR_2)) {
                throw new CityRegisterException("1", "GRN ERROR");
            }
            if (passportSeria.equals(ERROR_TRANSPORT_1) || passportSeria.equals(ERROR_TRANSPORT_2)) {
                throw new TransportException("TRANSPORT ERROR");
            }
        }
        if (person instanceof Child) {
            response.setExisting(true);
            response.setTemporal(true);
        }

        System.out.println(response);

        return response;
    }
}
