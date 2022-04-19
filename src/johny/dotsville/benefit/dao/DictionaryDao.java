package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.Street;
import johny.dotsville.benefit.exception.DaoException;

import java.util.List;

public interface DictionaryDao {
    List<Street> findStreets(String pattern) throws DaoException;
}
