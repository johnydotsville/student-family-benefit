package johny.dotsville.benefit;

import johny.dotsville.benefit.domain.*;
import johny.dotsville.benefit.validator.ChildrenValidator;
import johny.dotsville.benefit.validator.CityRegisterValidator;
import johny.dotsville.benefit.validator.StudentValidator;
import johny.dotsville.benefit.validator.WeddingValidator;
import johny.dotsville.benefit.mail.MailSender;

public class StudentOrderValidator {

    public static void main(String[] args) {
        System.out.println("Программа выплат пособий родителям-студентам");
        StudentOrderValidator.checkAll();
    }

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
        WeddingValidator validator = new WeddingValidator();
        AnswerWedding answer = validator.checkWedding(studentOrder);

        return answer;
    }

    static AnswerChildren checkChildren(StudentOrder studentOrder) {
        ChildrenValidator validator = new ChildrenValidator();
        AnswerChildren answer = validator.checkChildren(studentOrder);

        return answer;
    }

    static AnswerStudent checkStudent(StudentOrder studentOrder) {
        StudentValidator validator = new StudentValidator();
        AnswerStudent answer = validator.checkStudent(studentOrder);

        return answer;
    }

    static StudentOrder readStudentOrder() {
        return new StudentOrder();
    }

    static void sendMail(StudentOrder studentOrder) {
        MailSender mailer = new MailSender();
        mailer.sendMail(studentOrder);
    }

    public static void checkAll() {
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
