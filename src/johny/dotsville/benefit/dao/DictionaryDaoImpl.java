package johny.dotsville.benefit.dao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.domain.Street;
import johny.dotsville.benefit.exception.DaoException;

public class DictionaryDaoImpl implements DictionaryDao {
    private static final String GET_STREET = "select * from jc_street " +
        "where upper(street_name) like upper(?)";

    private Connection getConnection() throws SQLException {
        String connectionString = Config.getProperty(Config.DB_URL);
        String username = Config.getProperty(Config.DB_LOGIN);
        String pass = Config.getProperty(Config.DB_PASSWORD);

        return DriverManager.getConnection(connectionString, username, pass);
    }

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

            conn.close();
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return streets;
    }

    public static void main(String[] args) throws  Exception {
        DictionaryDaoImpl dao = new DictionaryDaoImpl();
        List<Street> streets = dao.findStreets("irs");
        for (Street street : streets) {
            System.out.println(street.getStreetName());
        }
    }
}
