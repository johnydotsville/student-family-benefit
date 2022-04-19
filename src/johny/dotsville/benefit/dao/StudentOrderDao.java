package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.exception.DaoException;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder order) throws DaoException;
}
