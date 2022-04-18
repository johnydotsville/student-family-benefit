package johny.dotsville.benefit.validator.register;

import johny.dotsville.benefit.domain.AnswerCityRegister;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.exception.CityRegisterException;
import johny.dotsville.benefit.domain.Child;

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

        try {
            personChecker.checkPerson(order.getHusband());
            personChecker.checkPerson(order.getWife());
            for (Child child :  order.getChildren()) {
                personChecker.checkPerson(child);
            }
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
        }

        AnswerCityRegister answer = new AnswerCityRegister();
        //answer.success = false;

        return answer;
    }
}
