package controllers;

import play.data.validation.Required;
import play.mvc.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Device device = getDevice();

        render(device);
    }

    public static void saveScreenSize(@Required Integer width, @Required Integer height) {
        if (validation.hasErrors()) {
            validation.keep();
            index();
        }

        Device device = getDevice();

        // この端末の画面サイズがデータベースになければ、新規保存
        if (device != null) {
            device = new Device();
        }

        device.width = width;
        device.height = height;
        device.save();

        // Application.indexにリダイレクト
        index();
    }

    public static void image() {
        
    }

    /**
     * @return User-Agent of the client. not null.
     */
    private static String userAgent() {
        Http.Header header = request.headers.get("user-agent");

        return header != null ? header.value() : "";
    }

    public static Device getDevice() {
        return Device.findByUserAgent(userAgent());
    }
}