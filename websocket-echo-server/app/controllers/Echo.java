package controllers;

import models.LoggedMessage;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.WebSocketController;

import static play.mvc.Http.WebSocketEvent.TextFrame;

public class Echo extends Controller {
    public static void demo() {
        render();
    }

    public static class WebSocketEcho extends WebSocketController {
        public static void listen() {
            while(inbound.isOpen()) {
                Http.WebSocketEvent event = await(inbound.nextEvent());

                for (String message : TextFrame.match(event)) {
                    outbound.send(message);

                    new LoggedMessage(message).save();
                }
            }
        }
    }

}
