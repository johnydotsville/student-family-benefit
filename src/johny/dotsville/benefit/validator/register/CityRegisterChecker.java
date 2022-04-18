package johny.dotsville.benefit.validator.register;

import johny.dotsville.benefit.domain.register.CityRegisterResponse;
import johny.dotsville.benefit.domain.Person;
import johny.dotsville.benefit.exception.CityRegisterException;

public interface CityRegisterChecker {
    CityRegisterResponse checkPerson(Person person) throws CityRegisterException;
}
