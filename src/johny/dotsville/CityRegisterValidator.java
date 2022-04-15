package johny.dotsville;

public class CityRegisterValidator {
    String hostName;

    AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        System.out.println("Запрос регистрации по адресу " + hostName);
        AnswerCityRegister answer = new AnswerCityRegister();
        answer.success = false;

        return answer;
    }
}
