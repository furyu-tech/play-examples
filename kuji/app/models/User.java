package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author kuoka
 */
@Entity
public class User extends Model {
    @Required
    public String name;
}
