package johny.dotsville.benefit.domain;

/*
Результат проверки ОДНОГО человека из заявки
 */
public class CityRegisterCheckerResponse {
    private boolean existing;  // Существует ли человек в системе
    private Boolean temporal;  // Временная или постоянная регистрация

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public Boolean getTemporal() {
        return temporal;
    }

    public void setTemporal(Boolean temporal) {
        this.temporal = temporal;
    }
}
