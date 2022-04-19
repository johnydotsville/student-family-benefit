package johny.dotsville.benefit;

import java.time.LocalDate;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import johny.dotsville.benefit.dao.DictionaryDao;
import johny.dotsville.benefit.dao.DictionaryDaoImpl;
import johny.dotsville.benefit.domain.*;

public class SaveStudentOrder {
    public static void main(String[] args) throws Exception {
        DictionaryDao dao = new DictionaryDaoImpl();
        List<Street> streets = dao.findStreets("ица");
        List<PassportOffice> passportOffices = dao.findPassportOffices("010020000000");
        List<RegisterOffice> registerOffices = dao.findRegisterOffices("010010000000");
        List<CountryArea> countryAreas = dao.findAreas("");
        List<CountryArea> countryAreasLevel2 = dao.findAreas("010000000000");
    }

    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        return answer;
    }

    static StudentOrder buildStudentOrder(long id) {
        StudentOrder order = new StudentOrder();
        order.setStudentOrderId(id);
        order.setMarriageCertificateId("" + (123456000 + id));
        order.setMarriageDate(LocalDate.of(2016, 7, 4));
//        order.setMarriageOffice("Отдел ЗАГС");
        order.setMarriageOffice(new RegisterOffice(1L, "", ""));

        Street street = new Street(1L, "First Street");
        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
//        husband.setIssueDepartment("Отдел милиции №" + id);
        husband.setIssueDepartment(new PassportOffice(1L, "", ""));
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);

        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
//        wife.setIssueDepartment("Отдел милиции №" + id);
        wife.setIssueDepartment(new PassportOffice(2L, "", ""));
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);

        // Ребенок 1
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 7, 19));
//        child1.setIssueDepartment("Отдел ЗАГС №" + id);
        child1.setIssueDepartment(new RegisterOffice(2L, "", ""));
        child1.setAddress(address);

        // Ребенок 2
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
//        child2.setIssueDepartment("Отдел ЗАГС №" + id);
        child2.setIssueDepartment(new RegisterOffice(3L, "", ""));
        child2.setAddress(address);

        order.setHusband(husband);
        order.setWife(wife);
        order.addChild(child1);
        order.addChild(child2);

        return order;
    }
}
