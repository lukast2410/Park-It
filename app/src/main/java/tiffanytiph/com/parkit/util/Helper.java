package tiffanytiph.com.parkit.util;

import tiffanytiph.com.parkit.model.User;

public class Helper {
    private static Helper data = null;
    private User currentUser;

    private Helper() { }

    public static Helper getInstance() {
        return data = (data == null) ? new Helper() : data;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
