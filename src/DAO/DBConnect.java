package DAO;

/**
 *
 * @author PC
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3307/ql_cuahangthethao";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Đảm bảo có thư viện mysql-connector-java
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy Driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối DB thất bại");
            e.printStackTrace();
        }
        return null;
    }
}
