package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @author kuoka
 */
@Entity
public class Task extends Model {
    public String title;
    @ManyToOne
    public User user;
}
