package johny.dotsville.benefit;

import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.domain.Adult;

public class SaveStudentOrder {
    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        return answer;
    }

    static StudentOrder buildStudentOrder() {
        StudentOrder order = new StudentOrder();

        Adult husband = new Adult();
        order.setHusband(husband);
        Adult wife = new Adult();
        order.setWife(wife);

        return order;
    }
}
