package johny.dotsville;

public class StudentOrderValidator {

    static AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        CityRegisterValidator validator1 = new CityRegisterValidator();
        validator1.hostName = "Московский сервер";
        CityRegisterValidator validator2 = new CityRegisterValidator();
        validator2.hostName = "Питерский сервер";

        AnswerCityRegister answer1 = validator1.checkCityRegister(studentOrder);
        AnswerCityRegister answer2 = validator2.checkCityRegister(studentOrder);

        return answer1;
    }

    static AnswerWedding checkWedding(StudentOrder studentOrder) {
        return new AnswerWedding();
    }

    static AnswerChildren checkChildren(StudentOrder studentOrder) {
        return new AnswerChildren();
    }

    static AnswerStudent checkStudent(StudentOrder studentOrder) {
        return new AnswerStudent();
    }

    static StudentOrder readStudentOrder() {
        return new StudentOrder();
    }

    static void sendMail(StudentOrder studentOrder) {

    }

    static void checkAll() {
        while (true) {
            StudentOrder studentOrder = readStudentOrder();

            if (studentOrder == null) {
                break;
            }

            AnswerCityRegister cityAnswer = checkCityRegister(studentOrder);
            if (!cityAnswer.success) {
                continue;
            }

            AnswerWedding weddingAnswer = checkWedding(studentOrder);
            AnswerChildren childrenAnswer = checkChildren(studentOrder);
            AnswerStudent studentAnswer = checkStudent(studentOrder);

            sendMail(studentOrder);
        }
    }
}
