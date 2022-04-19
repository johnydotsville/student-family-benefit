package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.Adult;
import johny.dotsville.benefit.domain.Address;
import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.domain.CountryArea;
import johny.dotsville.benefit.domain.StudentOrder;
import johny.dotsville.benefit.domain.StudentOrderStatus;
import johny.dotsville.benefit.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;

public class StudentDaoImpl implements StudentOrderDao {
    public static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, " +
            "h_sur_name, h_given_name, h_patronymic, h_date_of_birth, " +
            "h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, " +
            "h_post_index, h_street_code, h_building, h_extension, h_apartment, " +
            "w_sur_name, w_given_name, wh_patronymic, w_date_of_birth, " +
            "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, " +
            "w_post_index, w_street_code, w_building, w_extension, w_apartment, " +
            "certificate_id, register_office_id, marriage_date) " +
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
        long savedOrderId = -1L;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     INSERT_ORDER,
                     new String[] {"student_order_id"})) {
            // Это массив колонок, которые нужно вернуть для вставленных записей после вставки,
            // см. использование ниже, нужно для получения id сохраненной заявки

            // Заголовок
            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            // Муж и жена
            setParamsForAdult(stmt, 3, order.getHusband());
            setParamsForAdult(stmt, 16, order.getWife());
            // Брак
            stmt.setString(29, order.getMarriageCertificateId());
            stmt.setLong(30, order.getMarriageOffice().getOfficeId());
            stmt.setDate(31, java.sql.Date.valueOf(order.getMarriageDate()));

            stmt.executeUpdate();
            
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                savedOrderId = result.getLong("student_order_id");
            }
            result.close();

            return savedOrderId;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    private void setParamsForAdult(PreparedStatement stmt, int startParamInd, Adult adult)
            throws SQLException {
        stmt.setString(startParamInd, adult.getSurname());
        stmt.setString(startParamInd + 1, adult.getGivenName());
        stmt.setString(startParamInd + 2, adult.getPatronymic());
        stmt.setDate(startParamInd + 3, java.sql.Date.valueOf(adult.getDateOfBirth()));
        stmt.setString(startParamInd + 4, adult.getPassportSeria());
        stmt.setString(startParamInd + 5, adult.getPassportNumber());
        stmt.setDate(startParamInd + 6, java.sql.Date.valueOf(adult.getIssueDate()));
        stmt.setLong(startParamInd + 7, adult.getIssueDepartment().getOfficeId());
        Address h_address = adult.getAddress();
        stmt.setString(startParamInd + 8, h_address.getPostCode());
        stmt.setLong(startParamInd + 9, h_address.getStreet().getStreetCode());
        stmt.setString(startParamInd + 10, h_address.getBuilding());
        stmt.setString(startParamInd + 11, h_address.getExtension());
        stmt.setString(startParamInd + 12, h_address.getApartment());
    }
}
