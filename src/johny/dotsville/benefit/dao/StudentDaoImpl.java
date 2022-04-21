package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.*;
import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;

public class StudentDaoImpl implements StudentOrderDao {
    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, " +
            "h_sur_name, h_given_name, h_patronymic, h_date_of_birth, " +
            "h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, " +
            "h_post_index, h_street_code, h_building, h_extension, h_apartment, " +
            "w_sur_name, w_given_name, wh_patronymic, w_date_of_birth, " +
            "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, " +
            "w_post_index, w_street_code, w_building, w_extension, w_apartment, " +
            "certificate_id, register_office_id, marriage_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String INSERT_CHILD = "INSERT INTO jc_student_child(" +
            "student_order_id, " +  // 1
            "c_sur_name, " +  // 2
            "c_given_name, " +  // 3
            "c_patronymic, " +  //4
            "c_date_of_birth, " +  // 5
            "c_certificate_number, " +  // 6
            "c_certificate_date, " +  // 7
            "c_register_office_id, " +  // 8
            "c_post_index, " +  // 9
            "c_street_code, " +  // 10
            "c_building, " +  // 11
            "c_extension, " +  // 12
            "c_apartment)" +  // 13
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


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

            conn.setAutoCommit(false);
            try {
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

                saveChildren(conn, order, savedOrderId);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }

            conn.commit();

            return savedOrderId;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    // TODO Возможно сделать отдельный метод с заданием размера буфера и выполнением пачки
    private void saveChildren(Connection conn, StudentOrder order, long orderId)
            throws SQLException{
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_CHILD)) {
            int batchMaxSize = 10_000;
            int batchCurrentSize = 0;
            // Пакетная обработка. Не сразу будем выполнять вставку, а накопим "пачку" вставок
            // со всеми детьми разом
            for (Child child : order.getChildren()) {
                setParamsForChild(stmt, child, orderId);
                //stmt.executeUpdate();
                // Если вдруг в пачке мб ну очень много записей, организуем "буфер"
                stmt.addBatch();
                batchCurrentSize++;
                if (batchCurrentSize >= batchCurrentSize) {
                    stmt.executeBatch();
                    batchCurrentSize = 0;
                }
            }
            if (batchCurrentSize > 0) {
                stmt.executeBatch();
            }
        }
    }

    private void setParamsForChild(PreparedStatement stmt, Child child, long orderId)
            throws SQLException{
        stmt.setLong(1, orderId);
        stmt.setString(2, child.getSurname());
        stmt.setString(3, child.getGivenName());
        stmt.setString(4, child.getPatronymic());
        stmt.setDate(5, java.sql.Date.valueOf(child.getDateOfBirth()));

        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());

        Address address = child.getAddress();
        stmt.setString( 9, address.getPostCode());
        stmt.setLong(10, address.getStreet().getStreetCode());
        stmt.setString( 11, address.getBuilding());
        stmt.setString(12, address.getExtension());
        stmt.setString(13, address.getApartment());
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
        Address address = adult.getAddress();
        stmt.setString(startParamInd + 8, address.getPostCode());
        stmt.setLong(startParamInd + 9, address.getStreet().getStreetCode());
        stmt.setString(startParamInd + 10, address.getBuilding());
        stmt.setString(startParamInd + 11, address.getExtension());
        stmt.setString(startParamInd + 12, address.getApartment());
    }
}