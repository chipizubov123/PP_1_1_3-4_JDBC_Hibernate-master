package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public static final Connection connection = Util.open();

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                first_name varchar(255) NOT NULL,
                last_name varchar(255) NOT NULL,
                age TINYINT(11) NOT NULL
                )""";

        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {

        String sql = """
                DROP TABLE IF EXISTS users""";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte valueAge) {

        String sql = """
                INSERT INTO users(first_name, last_name, age) VALUE (?, ?, ?);""";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, lastName);
            preparedStatement.setObject(3, valueAge);

            preparedStatement.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
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
        }
    }

    @Override
    public void removeUserById(long id) {

        String sql = """
                DELETE FROM users WHERE users.id = ?;
                """;
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
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
        }
    }

    @Override
    public List<User> getAllUsers() {

        String sql = """
                SELECT * FROM users
                """;
        List<User> users = new ArrayList<>();

        try (var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                var firstName = resultSet.getString("first_name");
                var lastName = resultSet.getString("last_name");
                var age = resultSet.getByte("age");

                users.add(new User(firstName, lastName, age));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(users);
        return users;
    }

    @Override
    public void cleanUsersTable() {

        String sql = """
                DELETE FROM users""";

        try (var preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
