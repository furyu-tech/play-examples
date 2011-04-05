package controllers;

import models.Photo;
import play.data.validation.Min;
import play.data.validation.Valid;
import play.mvc.Controller;

import java.util.List;

// See also: "Upload and store image", http://www.playframework.org/community/snippets/4

public class Photos extends Controller {
    public static void index(@Min(0) Integer start, @Min(1) Integer results) {
        if (validation.hasErrors() || start == null || results == null) {
            results = 5;
            start = 0;
        }

        List<Photo> photos = Photo.all().from(start).fetch(results);

        if (request.format.equals("json")) {
            renderJSON(photos);
        } else {
            // Render html or xml.
            render(photos);
        }
    }

    public static void data(long id) {
        Photo photo = Photo.findById(id);

        if (photo == null) {
            error("Photo for id " + id + " is not found.");
        }

        renderBinary(photo.data.get());
    }

    public static void upload(@Valid Photo photo) {
        if (validation.hasErrors()) {
            error("No valid photo given. ");
        }

        photo.save();
    }
}
