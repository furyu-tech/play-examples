package models;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.mvc.Router;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.HashMap;
import java.util.Map;

// See also: "Keep track of record creation/updates", http://www.playframework.org/community/snippets/5

@Entity
public class Photo extends Model {
    @Required
    public String title;

    @Required
    public Blob data;

    public String dataUrl;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("id", id);

        Router.ActionDefinition actionDefinition = Router.reverse("Photos.data", args);
        actionDefinition.absolute();

        dataUrl = actionDefinition.url;
    }
}
