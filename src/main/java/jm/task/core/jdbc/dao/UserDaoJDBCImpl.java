package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.open();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                first_name varchar(255) NOT NULL,
                last_name varchar(255) NOT NULL,
                age TINYINT(11) NOT NULL
                )""";

        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropUsersTable() {

        String sql = """
                DROP TABLE IF EXISTS users""";

        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveUser(String name, String lastName, byte valueAge) {

        String sql = """
                INSERT INTO users(first_name, last_name, age) VALUE (?, ?, ?);""";

        try {
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, lastName);
            preparedStatement.setObject(3, valueAge);

            preparedStatement.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users WHERE users.id = ?;
                """;
        try {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {

        String sql = """
                SELECT * FROM users
                """;
        List<User> users = new ArrayList<>();

        try {
            var preparedStatement = connection.prepareStatement(sql);

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var firstName = resultSet.getString("first_name");
                var lastName = resultSet.getString("last_name");
                var age = resultSet.getByte("age");

                users.add(new User(firstName, lastName, age));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(users);
        return users;
    }

    public void cleanUsersTable() {

        String sql = """
                DELETE FROM users""";

        try {
            connection.setAutoCommit(false);
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
