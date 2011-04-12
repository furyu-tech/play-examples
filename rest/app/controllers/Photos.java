package controllers;

import models.Photo;
import play.data.validation.*;
import play.data.validation.Error;
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
        Photo photo = findPhotoOrNotFound(id);

        response.contentType = photo.data.type();
        renderBinary(photo.data.get());
    }

    public static void upload(@Valid Photo photo) {
        validateOrError();

        photo.save();
    }

    public static void update(@Required String title, @Required long id) {
        validateOrError();

        Photo photo = findPhotoOrNotFound(id);
        photo.title = title;

        photo.save();
    }

    public static void delete(long id) {
        Photo photo = findPhotoOrNotFound(id);

        photo.delete();
    }

    private static Photo findPhotoOrNotFound(long id) {
        Photo photo = Photo.findById(id);

        if (photo == null) {
            notFound("Photo for id " + id + " is not found.");
        }
        return photo;
    }

    private static void validateOrError() {
        if (validation.hasErrors()) {
            StringBuffer buffer = new StringBuffer();
            for (Error error : validation.errors()) {
                buffer.append(error.message() + ": " + error.getKey() + "\n");
            }
            error(buffer.toString());
        }
    }
}
