package controllers;

import models.Task;
import models.User;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void assign(@Required(message = "ニックネームを入力してください!") String name) {
        if (validation.hasErrors()) {
            validation.keep();
            index();
        }

        User user = User.find("byName", name).first();

        if (user == null) {
            user = new User();
            user.name = name;
            user.save();
        }

        long count = Task.count("user is null");
        int index = (int) Math.floor(Math.random() * count);

        Task task = Task.find("user is null").from(index).first();

        if (task == null) {
            flash("message", "タスクがありません！追加してください。");
            Tasks.newTask();
        }

        task.user = user;

        Logger.debug("task=" + task.toString());
        Logger.debug("user=" + user.toString());

        task.save();

        render(count, user, task);
    }

}