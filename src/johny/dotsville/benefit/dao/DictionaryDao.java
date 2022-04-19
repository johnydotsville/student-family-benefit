package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.CountryArea;
import johny.dotsville.benefit.domain.PassportOffice;
import johny.dotsville.benefit.domain.RegisterOffice;
import johny.dotsville.benefit.domain.Street;
import johny.dotsville.benefit.exception.DaoException;

import java.util.List;

public interface DictionaryDao {
    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffices(String pattern) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException;
}
