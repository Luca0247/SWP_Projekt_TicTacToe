package DB;

import Models.*;
import java.sql.SQLException;
import java.util.List;

public interface Reposy {

    void open() throws SQLException;
    void close() throws SQLException;

    User getUserByName(String username) throws SQLException;

    boolean register(User user) throws SQLException;

    boolean checkIfUserExists(String username) throws SQLException;

    boolean login(String username, String password) throws SQLException;
}