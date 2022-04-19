package johny.dotsville.benefit.dao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.domain.CountryArea;
import johny.dotsville.benefit.domain.PassportOffice;
import johny.dotsville.benefit.domain.RegisterOffice;
import johny.dotsville.benefit.domain.Street;
import johny.dotsville.benefit.exception.DaoException;

public class DictionaryDaoImpl implements DictionaryDao {
    private static final String GET_STREET = "select * from jc_street " +
        "where upper(street_name) like upper(?)";
    private static final String GET_PASSPORT_OFFICE = "select * from jc_passport_office " +
        "where p_office_area_id = ?";
    private static final String GET_REGISTER_OFFICE = "select * from jc_register_office " +
            "where r_office_area_id = ?";
    private static final String GET_AREA = "select * from jc_country_struct " +
            "where area_id like ? and area_id <> ?";

    private Connection getConnection() throws SQLException {
        String connectionString = Config.getProperty(Config.DB_URL);
        String username = Config.getProperty(Config.DB_LOGIN);
        String pass = Config.getProperty(Config.DB_PASSWORD);

        return DriverManager.getConnection(connectionString, username, pass);
    }

    @Override
    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> streets = new LinkedList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STREET)) {

            stmt.setString(1, "%" + pattern + "%");

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Street street = new Street(result.getLong("street_code"),
                    result.getString("street_name"));
                streets.add(street);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return streets;
    }

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> offices = new LinkedList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PASSPORT_OFFICE)) {

            stmt.setString(1, areaId);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                PassportOffice office = new PassportOffice(
                    result.getLong("p_office_id"),
                    result.getString("p_office_area_id"),
                    result.getString("p_office_name"));
                offices.add(office);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return offices;
    }

    @Override
    public List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> offices = new LinkedList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_REGISTER_OFFICE)) {

            stmt.setString(1, areaId);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                RegisterOffice office = new RegisterOffice(
                        result.getLong("r_office_id"),
                        result.getString("r_office_area_id"),
                        result.getString("r_office_name"));
                offices.add(office);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return offices;
    }

    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException {
        List<CountryArea> areas = new LinkedList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_AREA)) {

            String param1 = buildParam(areaId);
            String param2 = areaId;

            stmt.setString(1, param1);
            stmt.setString(2, param2);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                CountryArea area = new CountryArea(
                        result.getString("area_id"),
                        result.getString("area_name"));
                areas.add(area);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return areas;
    }

    // С первого взгляда какая-то мутная тема территориального разделения
    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        }
        if (areaId.endsWith("0000000000")) {
            return areaId.substring(0, 2) + "___0000000";
        }
        if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        }
        if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        }
        throw new SQLException("Invalid parameter 'areaId':" + areaId);
    }
}
