package johny.dotsville.benefit.domain;

import java.time.LocalDate;

public abstract class Person {
    private String surname;
    private String givenName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private Address address;

    public Person(String surname, String givenName, String patronymic, LocalDate dateOfBirth) {
        this.surname = surname;
        this.givenName = givenName;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
