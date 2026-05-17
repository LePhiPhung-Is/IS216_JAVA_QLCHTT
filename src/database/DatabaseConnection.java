package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. Khai báo thông tin kết nối (Cần sửa lại cho khớp với máy của bạn)
    // 'xe' là tên mặc định của bản Oracle Express. Nếu cài bản Enterprise, nó thường là 'orcl'



    // ĐIỀN THÔNG TIN TÀI KHOẢN ORACLE CỦA BẠN VÀO ĐÂY
    private static final String USERNAME = "FASHION_ADMIN"; // Ví dụ: system, hr, hoặc user bạn tự tạo
    private static final String PASSWORD = "123456";
    // 2. Hàm tạo kết nối
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Gọi "phiên dịch viên" Driver mà bạn vừa thêm lúc nãy
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Tiến hành kết nối
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối Oracle Database thành công tuyệt đối!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy thư viện ojdbc. Hãy kiểm tra lại bước Add Referenced Libraries.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi: Sai Username/Password, sai URL, hoặc Database Oracle trên máy chưa được bật.");
            e.printStackTrace();
        }
        return connection;
    }

    // 3. Hàm main để chạy test thử độc lập file này
    public static void main(String[] args) {
        getConnection();
    }
}