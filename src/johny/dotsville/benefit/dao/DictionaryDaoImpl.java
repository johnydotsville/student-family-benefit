package johny.dotsville.benefit.dao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import johny.dotsville.benefit.domain.Street;
import johny.dotsville.benefit.exception.DaoException;

public class DictionaryDaoImpl implements DictionaryDao {
    private Connection getConnection() throws SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/jc_student";
        String username = "postgres";
        String pass = "j123";

        return DriverManager.getConnection(connectionString, username, pass);
    }

    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> streets = new LinkedList<>();

        try {
            Connection conn = getConnection();

            String sql = "select * from jc_street " +
                    "where upper(street_name) " +
                    "like upper('%" + pattern + "%')";
            Statement stmt = conn.createStatement();

            ResultSet result = stmt.executeQuery(sql);
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
