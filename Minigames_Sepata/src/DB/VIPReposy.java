package DB;

import Models.*;
import java.sql.SQLException;
import java.util.List;

public interface VIPReposy {

    void open() throws SQLException;
    void close() throws SQLException;

    User getUserByName(String username) throws SQLException;

    boolean register(User user) throws SQLException;

    boolean checkIfUserExists(String username) throws SQLException;

    boolean login(String username, String password) throws SQLException;

    String getIP(String name) throws SQLException;

    boolean insertFriends(User user) throws SQLException;

    int getIdByUsername(String name) throws SQLException;

    void updateScore(String name, int score) throws SQLException;

    int getScore(String name) throws SQLException;
}