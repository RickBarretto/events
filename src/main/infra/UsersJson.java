package main.infra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.roles.repositories.Users;

public class UsersJson implements Users {
    private final String filepath;
    private UsersInMemory allUsersInMemory;

    public UsersJson(String filepath) {
        this(filepath, new UsersInMemory(load(filepath)));
    }

    public UsersJson(String filepath, UsersInMemory repository) {
        this.filepath = filepath;
        this.allUsersInMemory = repository;
        persist();
    }

    private static List<User> load(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            return new Gson().fromJson(reader, new TypeToken<List<User>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void persist() {
        try (FileWriter writer = new FileWriter(filepath)) {
            new Gson().toJson(allUsersInMemory.list(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(User user) {
        allUsersInMemory.register(user);
        persist();
    }

    @Override
    public void update(User target, User newUser) {
        allUsersInMemory.update(target, newUser);
        persist();
    }

    @Override
    public Optional<User> ownerOf(String email, String password) {
        return allUsersInMemory.ownerOf(email, password);
    }

    @Override
    public boolean has(UserId id) { return allUsersInMemory.has(id); }

    @Override
    public boolean has(String email) { return allUsersInMemory.has(email); }

    @Override
    public List<User> list() { return allUsersInMemory.list(); }

}
