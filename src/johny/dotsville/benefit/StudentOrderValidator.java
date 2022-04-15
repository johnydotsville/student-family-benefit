package johny.dotsville.benefit;

import johny.dotsville.benefit.domain.*;
import johny.dotsville.benefit.validator.ChildrenValidator;
import johny.dotsville.benefit.validator.CityRegisterValidator;
import johny.dotsville.benefit.validator.StudentValidator;
import johny.dotsville.benefit.validator.WeddingValidator;
import johny.dotsville.benefit.mail.MailSender;

public class StudentOrderValidator {
    private WeddingValidator weddingValidator;
    private ChildrenValidator childrenValidator;
    private StudentValidator studentValidator;
    private CityRegisterValidator cityRegisterValidator;
    private MailSender mailSender;

    public StudentOrderValidator() {
        weddingValidator = new WeddingValidator();
        childrenValidator = new ChildrenValidator();
        studentValidator = new StudentValidator();
        cityRegisterValidator = new CityRegisterValidator();
        cityRegisterValidator.hostName = "Московский сервер";
    }

    public static void main(String[] args) {
        System.out.println("Программа выплат пособий родителям-студентам");
        new StudentOrderValidator().checkAll();
    }

    AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        AnswerCityRegister answer = cityRegisterValidator.checkCityRegister(studentOrder);

        return answer;
    }

    AnswerWedding checkWedding(StudentOrder studentOrder) {
        AnswerWedding answer = weddingValidator.checkWedding(studentOrder);

        return answer;
    }

    AnswerChildren checkChildren(StudentOrder studentOrder) {
        AnswerChildren answer = childrenValidator.checkChildren(studentOrder);

        return answer;
    }

    AnswerStudent checkStudent(StudentOrder studentOrder) {
        AnswerStudent answer = studentValidator.checkStudent(studentOrder);

        return answer;
    }

    static StudentOrder readStudentOrder() {
        return new StudentOrder();
    }

    void sendMail(StudentOrder studentOrder) {
        mailSender.sendMail(studentOrder);
    }

    public void checkAll() {
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
