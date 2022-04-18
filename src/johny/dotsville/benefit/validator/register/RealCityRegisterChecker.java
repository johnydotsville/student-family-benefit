package johny.dotsville.benefit.validator.register;

import jdk.jshell.spi.ExecutionControl;
import johny.dotsville.benefit.domain.Person;
import johny.dotsville.benefit.domain.register.CityRegisterResponse;
import johny.dotsville.benefit.exception.CityRegisterException;
import johny.dotsville.benefit.exception.TransportException;
import johny.dotsville.benefit.validator.register.CityRegisterChecker;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        return null;
    }
}
