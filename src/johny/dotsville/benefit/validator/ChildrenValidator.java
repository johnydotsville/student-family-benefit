package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.AnswerChildren;
import johny.dotsville.benefit.domain.StudentOrder;

public class ChildrenValidator {
    public AnswerChildren checkChildren(StudentOrder studentOrder) {
        System.out.println("Проверка детей...");
        return new AnswerChildren();
    }
}
