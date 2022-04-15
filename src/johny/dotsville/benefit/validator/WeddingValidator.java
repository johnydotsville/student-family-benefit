package johny.dotsville.benefit.validator;

import johny.dotsville.benefit.domain.AnswerWedding;
import johny.dotsville.benefit.domain.StudentOrder;

public class WeddingValidator {
    public AnswerWedding checkWedding(StudentOrder studentOrder) {
        System.out.println("Проверка, находится ли заявитель в браке...");
        return new AnswerWedding();
    }
}
