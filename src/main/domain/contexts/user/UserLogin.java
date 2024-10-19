package main.domain.contexts.user;

import main.domain.exceptions.PermissionDenied;
import main.infra.Session;
import main.roles.Context;
import main.roles.repositories.Users;

public class UserLogin implements Context {
    private Session session;
    private Users users;

    public UserLogin(Users users) { this.users = users; }

    public UserLogin withSession(Session session) {
        this.session = session;
        return this;
    }

    public void logAs(String email, String password) throws PermissionDenied {
        session.logInAs(users.ownerOf(email, password).orElseThrow(
                () -> new PermissionDenied("Wrong login credentials")));
    }

    public void logOut() { session.logOut(); }

}
