package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.CityRegisterCheckerResponse;
import johny.dotsville.benefit.domain.Person;

public class FakeCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterCheckerResponse checkPerson(Person person) {
        return null;
    }
}
