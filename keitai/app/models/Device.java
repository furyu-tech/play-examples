package models;

import play.db.jpa.Model;

import java.util.List;

public class Device extends Model {
    public String userAgent;
    public Integer width;
    public Integer height;

    public static Device findByUserAgent(String userAgent) {
        List<Device> devices = Device.find("byUserAgent", userAgent).fetch();

        if (devices.isEmpty()) {
            return null;
        } else {
            return devices.get(0);
        }
    }
}
