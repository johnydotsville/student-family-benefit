package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.AnswerCityRegister;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.exception.CityRegisterException;

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

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        System.out.println("Запрос регистрации по адресу " + hostName + "...");

        try {
            personChecker.checkPerson(studentOrder.getHusband());
            personChecker.checkPerson(studentOrder.getWife());
            personChecker.checkPerson(studentOrder.getChild());
        } catch (CityRegisterException ex) {
            ex.printStackTrace();
        }

        AnswerCityRegister answer = new AnswerCityRegister();
        //answer.success = false;

        return answer;
    }
}
