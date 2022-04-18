package johny.dotsville.benefit.validator.register;

import johny.dotsville.benefit.domain.register.AnswerCityRegister;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.domain.register.AnswerCityRegisterItem;
import johny.dotsville.benefit.exception.CityRegisterException;
import johny.dotsville.benefit.domain.Child;
import johny.dotsville.benefit.domain.Person;

/*
Проверяет регистрацию в городе
 */
public class CityRegisterValidator {
    public String hostName;
    public int port;

    private CityRegisterChecker personChecker;

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
        try {
            personChecker.checkPerson(person);
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
        }

        return null;
    }
}
