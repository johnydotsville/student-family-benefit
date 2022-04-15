package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.CityRegisterCheckerResponse;
import johny.dotsville.benefit.domain.Person;

public interface CityRegisterChecker {
    CityRegisterCheckerResponse checkPerson(Person person);
}
