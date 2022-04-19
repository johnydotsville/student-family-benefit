package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.domain.CountryArea;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.exception.DaoException;

import java.sql.*;

public class StudentDaoImpl implements StudentOrderDao {
    // TODO вынести соединение куда-нибудь в общее место
    private Connection getConnection() throws SQLException {
        String connectionString = Config.getProperty(Config.DB_URL);
        String username = Config.getProperty(Config.DB_LOGIN);
        String pass = Config.getProperty(Config.DB_PASSWORD);

        return DriverManager.getConnection(connectionString, username, pass);
    }

    @Override
    public Long saveStudentOrder(StudentOrder order) throws DaoException {
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
    }
}
