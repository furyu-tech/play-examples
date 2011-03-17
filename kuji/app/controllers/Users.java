package controllers;

import models.Task;
import models.User;
import play.data.validation.Required;
import play.mvc.Controller;

import java.util.List;

/**
 * @author kuoka
 */
public class Users extends Controller {
    public static void index() {
        List<Users> users = User.all().fetch();
        render(users);
    }

    public static void show(@Required Long id) {
        if (validation.hasErrors()) {
            validation.keep();
            index();
        }

        User user = User.findById(id);

        if (user == null) {
            flash("message", "指定されたIDのユーザはいません!");
            index();
        }

        List<Task> tasks = Task.find("user = ?", user).fetch();

        render(user, tasks);
    }
}
