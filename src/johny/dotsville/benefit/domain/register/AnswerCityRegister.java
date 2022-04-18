package johny.dotsville.benefit.domain.register;

import java.util.LinkedList;
import java.util.List;

/*
Результат проверки ВСЕХ людей из заявки. Предполагается, что здесь
будет какая-то аккумуляция всех проверок в одну.
 */
public class AnswerCityRegister {
    private List<AnswerCityRegisterItem> items;

    public void addItem(AnswerCityRegisterItem item) {
        if (items == null) {
            items = new LinkedList<AnswerCityRegisterItem>();
        }
        items.add(item);
    }

    public List<AnswerCityRegisterItem> getItems() {
        return items;
    }
}
