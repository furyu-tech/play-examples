package controllers;

import models.Task;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.mvc.Controller;

import java.util.List;

/**
 * @author kuoka
 */
public class Tasks extends Controller {
    public static void index() {
        List<Task> tasks = Task.all().fetch();

        render(tasks);
    }

    public static void newTask() {
        render();
    }

    public static void create(
            @Required(message = "タイトルを入力してください")
            String title) {

        if (validation.hasErrors()) {
            validation.keep();
            newTask();
        }
        
        Task task = new Task();

        task.title = title;
        task.save();

        index();
    }

    public static void put(Long taskId) {
        Task task = Task.findById(taskId);

        if (task == null) {
            notFound();
        }

        task.user = null;
        task.save();

        if (request.isAjax()) {
            renderJSON("{success:true}");
        } else {
            render(task);
        }
    }

    public static void delete(Long taskId) {
        Task task = Task.findById(taskId);

        if (task == null) {
            notFound();
        }

        task.delete();
        index();
    }
}
