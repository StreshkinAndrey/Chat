package ru.gb.gbchat1.server;

import java.io.IOException;

public class DatabaseAuthService implements AuthService {
    @Override
    public String getNickname(String login, String password) {
        return Database.getUserNickname(login, password);
    }

    @Override
    public boolean changeNickname(String currentNickname, String newNickname) {
        return Database.changeUserNickname(currentNickname, newNickname);
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public void close() throws IOException {

    }
}
