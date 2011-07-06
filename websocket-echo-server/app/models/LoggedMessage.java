package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class LoggedMessage extends Model {
    public String message;

    public LoggedMessage(String message) {
        this.message = message;
    }
}
