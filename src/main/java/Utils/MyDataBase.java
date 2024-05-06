package Utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private final String URL = "jdbc:mysql://127.0.0.1:3306/bloodify0";
    private final String USER = "root";
    private final String PASS = "";

    private Connection connection;

    private static MyDataBase instance;

    private MyDataBase(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if(instance == null)
            instance = new MyDataBase();
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }
        return connection;
    }

    // Optionally, you can add a method to close the connection manually if needed
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

}
