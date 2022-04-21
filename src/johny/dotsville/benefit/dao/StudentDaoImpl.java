package johny.dotsville.benefit.dao;

import johny.dotsville.benefit.domain.*;
import johny.dotsville.benefit.config.Config;
import johny.dotsville.benefit.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class StudentDaoImpl implements StudentOrderDao {
    // TODO адский треш например, переделать
    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, " +

            "h_sur_name, h_given_name, h_patronymic, h_date_of_birth, " +
            "h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, " +
            "h_post_index, h_street_code, h_building, h_extension, h_apartment," +
            "h_university_id, h_student_number, " +

            "w_sur_name, w_given_name, w_patronymic, w_date_of_birth, " +
            "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, " +
            "w_post_index, w_street_code, w_building, w_extension, w_apartment, " +
            "w_university_id, w_student_number, " +

            "certificate_id, register_office_id, marriage_date) " +
            "VALUES (" +
            "?, ?," +

            "?, ?, ?, ?," +
            "?, ?, ?, ?," +
            "?, ?, ?, ?, ?," +
            "?, ?," +

            "?, ?, ?, ?," +
            "?, ?, ?, ?," +
            "?, ?," +
            "?, ?, ?, ?, ?," +

            "?, ?, ?);";
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
    private static final String SELECT_ORDERS =
            "select so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id as h_p_office_area_id, " +
            "po_h.p_office_name as h_p_office_name, " +
            "po_w.p_office_area_id as w_p_office_area_id, " +
            "po_w.p_office_name as w_p_office_name " +
            "from jc_student_order so " +
            "inner join jc_register_office ro on ro.r_office_id = so.register_office_id " +
            "inner join jc_passport_office po_h on po_h.p_office_id = so.h_passport_office_id " +
            "inner join jc_passport_office po_w on po_w.p_office_id = so.w_passport_office_id " +
            "where student_order_status = ? order by student_order_date";

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
                // TODO Это 3.14здец конечно такое заполнение поддерживать, нужно переделать
                // Заголовок
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                // Муж и жена
                setParamsForAdult(stmt, 3, order.getHusband());
                setParamsForAdult(stmt, 18, order.getWife());
                // Брак
                stmt.setString(33, order.getMarriageCertificateId());
                stmt.setLong(34, order.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(order.getMarriageDate()));

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
        stmt.setLong(startParamInd + 13, adult.getUniversity().getUniversityId());
        stmt.setString(startParamInd + 14, adult.getStudentId());
    }

    public List<StudentOrder> getStudentOrders()
            throws DaoException {
        List<StudentOrder> orders = new LinkedList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDERS)) {

            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                orders.add(extractStudentOrder(result));
            }
            result.close();
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return orders;
    }

    private StudentOrder extractStudentOrder(ResultSet raw)
            throws SQLException {
        StudentOrder order = new StudentOrder();

        fillHeader(raw, order);
        fillMarriage(raw, order);
        fillHusband(raw, order);
        fillWife(raw, order);

        return order;
    }
    private void fillHeader(ResultSet raw, StudentOrder order)
            throws SQLException {
        order.setStudentOrderId(raw.getLong("student_order_id"));
        order.setStudentOrderDate(raw.getTimestamp("student_order_date").toLocalDateTime());
        order.setStudentOrderStatus(StudentOrderStatus.fromValue(raw.getInt("student_order_status")));
    }
    private void fillMarriage(ResultSet raw, StudentOrder order)
            throws SQLException {
        order.setMarriageCertificateId(raw.getString("certificate_id"));
        order.setMarriageDate(raw.getDate("marriage_date").toLocalDate());
;
        RegisterOffice registerOffice = new RegisterOffice(
                raw.getLong("register_office_id"),
                raw.getString("r_office_area_id"),
                raw.getString("r_office_name")
            );
        order.setMarriageOffice(registerOffice);
    }
    private void fillHusband(ResultSet raw, StudentOrder order)
            throws SQLException {
        // TODO Опять 3.14здец, поправить
        String surname = raw.getString("h_sur_name");
        String firstname = raw.getString("h_given_name");
        String patronymic = raw.getString("h_patronymic");
        LocalDate birth = raw.getDate("h_date_of_birth").toLocalDate();

        Adult husband = new Adult(surname, firstname, patronymic, birth);

        husband.setPassportSeria(raw.getString("h_passport_seria"));
        husband.setPassportNumber(raw.getString("h_passport_number"));
        husband.setIssueDate(raw.getDate("h_passport_date").toLocalDate());

        PassportOffice passportOffice = new PassportOffice(
                raw.getLong("h_passport_office_id"),
                raw.getString("h_p_office_area_id"),
                raw.getString("h_p_office_name")
            );
        husband.setIssueDepartment(passportOffice);

        Street street = new Street(
                raw.getLong("h_street_code"),
                ""
            );
        Address address = new Address(
                raw.getString("h_post_index"),
                street,
                raw.getString("h_building"),
                raw.getString("h_extension"),
                raw.getString("h_apartment")
            );
        husband.setAddress(address);

        University university = new University(
                raw.getLong("h_university_id"),
                ""
            );
        husband.setUniversity(university);

        husband.setStudentId(raw.getString("h_student_number"));

        order.setHusband(husband);
    }
    private void fillWife(ResultSet raw, StudentOrder order)
            throws SQLException {
        // TODO Опять 3.14здец, поправить
        String surname = raw.getString("w_sur_name");
        String firstname = raw.getString("w_given_name");
        String patronymic = raw.getString("w_patronymic");
        LocalDate birth = raw.getDate("w_date_of_birth").toLocalDate();

        Adult wife = new Adult(surname, firstname, patronymic, birth);

        wife.setPassportSeria(raw.getString("w_passport_seria"));
        wife.setPassportNumber(raw.getString("w_passport_number"));
        wife.setIssueDate(raw.getDate("w_passport_date").toLocalDate());

        PassportOffice passportOffice = new PassportOffice(
                raw.getLong("w_passport_office_id"),
                raw.getString("w_p_office_area_id"),
                raw.getString("w_p_office_name")
            );
        wife.setIssueDepartment(passportOffice);

        Street street = new Street(
                raw.getLong("w_street_code"),
                ""
            );
        Address address = new Address(
                raw.getString("w_post_index"),
                street,
                raw.getString("w_building"),
                raw.getString("w_extension"),
                raw.getString("w_apartment")
            );
        wife.setAddress(address);

        University university = new University(
                raw.getLong("w_university_id"),
                raw.getString("w_student_number")
            );
        wife.setUniversity(university);

        order.setWife(wife);
    }
}