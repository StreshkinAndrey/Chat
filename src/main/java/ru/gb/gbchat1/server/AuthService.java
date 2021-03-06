package ru.gb.gbchat1.server;

import java.io.Closeable;
import java.io.IOException;

public interface AuthService extends Closeable {
    String getNickname(String login, String password);

    boolean changeNickname(String currentNickname, String newNickname);

    String getNickByLoginAndPassword(String login, String password);

    void run();

    @Override
    void close() throws IOException;
}