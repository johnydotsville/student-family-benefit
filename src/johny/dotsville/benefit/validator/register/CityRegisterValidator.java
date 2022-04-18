package johny.dotsville.benefit.validator.register;

import johny.dotsville.benefit.domain.register.AnswerCityRegister;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.domain.register.AnswerCityRegisterItem;
import johny.dotsville.benefit.domain.register.CityRegisterResponse;
import johny.dotsville.benefit.exception.CityRegisterException;
import johny.dotsville.benefit.domain.Child;
import johny.dotsville.benefit.domain.Person;
import johny.dotsville.benefit.exception.TransportException;

/*
Проверяет регистрацию в городе
 */
public class CityRegisterValidator {
    public String hostName;
    public int port;
    private CityRegisterChecker personChecker;
    public static final String INTERNAL_CODE = "NO_GRN";

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder order) {
        System.out.println("Запрос регистрации по адресу " + hostName + "...");
        AnswerCityRegister answer = new AnswerCityRegister();

        answer.addItem(checkPerson(order.getHusband()));
        answer.addItem(checkPerson(order.getWife()));
        for (Child child :  order.getChildren()) {
            answer.addItem(checkPerson(child));
        }

        return answer;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;

        try {
            CityRegisterResponse response = personChecker.checkPerson(person);
            status = response.isExisting()
                    ? AnswerCityRegisterItem.CityStatus.YES
                    : AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException ex) {
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
            ex.printStackTrace(System.out);
        } catch (TransportException ex) {
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(INTERNAL_CODE, ex.getMessage());
            ex.printStackTrace(System.out);
        }

        AnswerCityRegisterItem answer = new AnswerCityRegisterItem(person, status, error);

        return answer;
    }
}
