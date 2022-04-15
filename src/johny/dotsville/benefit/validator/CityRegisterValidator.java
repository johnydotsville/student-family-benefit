package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.AnswerCityRegister;
import johny.dotsville.benefit.domain.StudentOrder;

public class CityRegisterValidator {
    public String hostName;
    public int port;

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        System.out.println("Запрос регистрации по адресу " + hostName + "...");
        AnswerCityRegister answer = new AnswerCityRegister();
        answer.success = false;

        return answer;
    }
}
