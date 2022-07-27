package DataBase;

import java.sql.DriverManager;
import java.sql.SQLException;

// singleton
public class DBConnection {

    private static java.sql.Connection connection;

    public static DBConnection only_connection = null;

    public static DBConnection getInstance() throws SQLException {
        if(only_connection == null)
            only_connection = new DBConnection();
        return only_connection;
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialmedia",
                "root", "S.Pouria.H13812003");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
}
