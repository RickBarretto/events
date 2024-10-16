package main.infra.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.domain.models.users.User;
import main.infra.virtual.UsersInMemory;
import main.roles.repositories.Users;

public class UsersJson implements Users {
    private final JsonFile file;
    private UsersInMemory users;

    public UsersJson(JsonFile filepath) {
        this(filepath, new UsersInMemory(load(filepath)));
    }

    public UsersJson(JsonFile filepath, UsersInMemory repository) {
        this.file = filepath;
        this.users = repository;
        persist();
    }

    private static List<User> load(JsonFile filepath) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filepath))) {
            return new Gson().fromJson(reader, new TypeToken<List<User>>() {});
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void persist() {
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(users.list(), writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(User user) {
        users.register(user);
        persist();
    }

    @Override
    public void update(User target, User newUser) {
        users.update(target, newUser);
        persist();
    }

    @Override
    public Optional<User> ownerOf(String email, String password) {
        return users.ownerOf(email, password);
    }

    @Override
    public boolean has(String email) { return users.has(email); }

    @Override
    public List<User> list() { return users.list(); }

}
