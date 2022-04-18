package johny.dotsville.benefit;

import johny.dotsville.benefit.domain.*;
import johny.dotsville.benefit.validator.ChildrenValidator;
import johny.dotsville.benefit.validator.register.CityRegisterValidator;
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
        mailSender = new MailSender();
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

    void sendMail(StudentOrder studentOrder) {
        mailSender.sendMail(studentOrder);
    }

    public void checkAll() {
        StudentOrder[] studentOrders = readStudentOrders();

        for (StudentOrder order : studentOrders) {
            System.out.println("Запуск проверки заявки");
            checkOneOrder(order);
            System.out.println();
        }
    }

    static StudentOrder[] readStudentOrders() {
        StudentOrder[] orders = new StudentOrder[3];

        for (int i = 0; i < orders.length; i++) {
            orders[i] = SaveStudentOrder.buildStudentOrder(i);
        }

        return orders;
    }

    public void checkOneOrder(StudentOrder studentOrder) {
        AnswerCityRegister cityAnswer = checkCityRegister(studentOrder);
        AnswerWedding weddingAnswer = checkWedding(studentOrder);
        AnswerChildren childrenAnswer = checkChildren(studentOrder);
        AnswerStudent studentAnswer = checkStudent(studentOrder);
        sendMail(studentOrder);
    }
}
