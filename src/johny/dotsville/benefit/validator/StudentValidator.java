package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.AnswerStudent;
import johny.dotsville.benefit.domain.StudentOrder;

public class StudentValidator {
    public AnswerStudent checkStudent(StudentOrder studentOrder) {
        System.out.println("Проверка, является ли заявитель студентом...");
        return new AnswerStudent();
    }
}
