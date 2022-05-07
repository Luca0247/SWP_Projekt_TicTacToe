package DB;

import Models.User;

import java.sql.SQLException;

public class VIPReposyDB implements VIPReposy{

    @Override
    public void open() throws SQLException {

    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public User getUserByName(String username) throws SQLException {
        return null;
    }

    @Override
    public boolean register(User user) throws SQLException {
        return false;
    }

    @Override
    public boolean checkIfUserExists(String username) throws SQLException {
        return false;
    }

    @Override
    public boolean login(String username, String password) throws SQLException {
        return false;
    }

    @Override
    public String getIP(String name) throws SQLException {
        return null;
    }

    @Override
    public boolean insertFriends(User user) throws SQLException {
        return false;
    }

    @Override
    public int getIdByUsername(String name) throws SQLException {
        return 0;
    }

    @Override
    public void updateScore(String name, int score) throws SQLException {

    }

    @Override
    public int getScore(String name) throws SQLException {
        return 0;
    }
}
