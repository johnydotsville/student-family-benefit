package johny.dotsville.benefit;

import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.domain.Adult;

public class SaveStudentOrder {
    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        return answer;
    }

    static StudentOrder buildStudentOrder(long id) {
        StudentOrder order = new StudentOrder();

        Adult husband = new Adult("Пахомов", "Сергей", "Игоревич", null);
        Adult wife = new Adult("Баскова", "Светлана", "Юрьевна", null);

        order.setStudentOrderId(id);
        order.setHusband(husband);
        order.setWife(wife);

        return order;
    }
}
