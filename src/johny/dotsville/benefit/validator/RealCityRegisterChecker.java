package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.Person;
import johny.dotsville.benefit.domain.CityRegisterCheckerResponse;
import johny.dotsville.benefit.exception.CityRegisterException;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterCheckerResponse checkPerson(Person person)
            throws CityRegisterException {
        return null;
    }
}
