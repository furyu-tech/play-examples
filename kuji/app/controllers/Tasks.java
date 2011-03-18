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
            index();
        }
        
        Task task = new Task();

        task.title = title;
        task.save();

        index();
    }

    public static void batchCreate(
            @Required(message = "タイトルを入力してください") String text
            ) {
        if (validation.hasErrors()) {
            validation.keep();
            index();
        }

        int duplicates = 0;

        String[] titles = text.split("\n");
        for (String title : titles) {

            validation.required(title);
            if (validation.hasErrors()) {
                index();
            }

            if (Task.find("title = ?", title).first() == null) {
                Task task = new Task();
                task.title = title;
                task.save();
            } else {
                duplicates ++;
            }
        }

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
