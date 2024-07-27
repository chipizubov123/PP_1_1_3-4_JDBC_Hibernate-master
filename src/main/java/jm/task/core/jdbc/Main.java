package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Misha","Ivanov", (byte)15);
        userService.saveUser("VVasiliy","Mirniy", (byte)18);
        userService.saveUser("Masha","Petrova", (byte)22);
        userService.saveUser("Alexey","Borodin", (byte)12);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
