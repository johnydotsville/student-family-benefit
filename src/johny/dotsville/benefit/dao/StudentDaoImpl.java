package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.domain.CountryArea;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.exception.DaoException;

import java.sql.*;

public class StudentDaoImpl implements StudentOrderDao {
    public static final String INSERT_ORDER = "INSERT INTO jc_student_order(student_order_status, student_order_date, h_sur_name, h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, h_post_index, h_street_code, h_building, h_extension, h_apartment, w_sur_name, w_given_name, wh_patronymic, w_date_of_birth, w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, w_post_index, w_street_code, w_building, w_extension, w_apartment, certificate_id, register_office_id, marriage_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
             PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER)) {

            return 1L;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }
}
