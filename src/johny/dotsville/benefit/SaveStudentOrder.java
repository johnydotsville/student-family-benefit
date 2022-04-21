package johny.dotsville.benefit;

import java.time.LocalDate;

import johny.dotsville.benefit.dao.StudentDaoImpl;
import johny.dotsville.benefit.dao.StudentOrderDao;
import johny.dotsville.benefit.domain.*;

// TODO класс скорее всего лучше переименовать будет
public class SaveStudentOrder {
    public static void main(String[] args) throws Exception {
        StudentOrder order = buildStudentOrder(10);
        StudentOrderDao dao = new StudentDaoImpl();
        long id = dao.saveStudentOrder(order);
        System.out.println(id);
    }

    // TODO Это походу надо удалить потом, раз весь БД-движ происходит в StudentOrderDao
    // UPD Вряд ли, скорее всего просто код сохранения из main переедет сюда в нужный момент
    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        return answer;
    }

    static StudentOrder buildStudentOrder(long id) {
        StudentOrder order = new StudentOrder();
        order.setStudentOrderId(id);
        order.setMarriageCertificateId("" + (123456000 + id));
        order.setMarriageDate(LocalDate.of(2016, 7, 4));
        order.setMarriageOffice(new RegisterOffice(1L, "", ""));

        Street street = new Street(1L, "First Street");
        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        husband.setIssueDepartment(new PassportOffice(1L, "", ""));
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");

        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        wife.setIssueDepartment(new PassportOffice(2L, "", ""));
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");

        // Ребенок 1
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 7, 19));
        child1.setIssueDepartment(new RegisterOffice(2L, "", ""));
        child1.setAddress(address);

        // Ребенок 2
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
        child2.setIssueDepartment(new RegisterOffice(3L, "", ""));
        child2.setAddress(address);

        order.setHusband(husband);
        order.setWife(wife);
        order.addChild(child1);
        order.addChild(child2);

        return order;
    }
}
