import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/stein";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;
    private static DatabaseConnector instance;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get instance of DatabaseConnector (Singleton pattern)
    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    // Method to authenticate user (synchronized)
    public synchronized boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row is returned, authentication is successful
        }
    }

    // Method to get user type (synchronized)
    public synchronized String getUserType(String username, String password) throws SQLException {
        String userType = null;
        String query = "SELECT type FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userType = resultSet.getString("type");
            }
        }
        return userType;
    }

    // Method to add a new client (synchronized)
    public synchronized boolean addClient(String nom, String prenom, String username, String password, String type) throws SQLException {
        String query = "INSERT INTO users (nom, prenom, username, password, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, type);

            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected (1 row should be affected if insertion was successful)
            return rowsAffected > 0;
        }
    }
    public synchronized List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type")
                );
                userList.add(user);
            }
        }
        return userList;
    }
    // Method to close the connection
    public synchronized void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}