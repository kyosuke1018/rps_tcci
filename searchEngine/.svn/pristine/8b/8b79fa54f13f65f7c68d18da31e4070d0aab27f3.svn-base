
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter
 */
public class Item {
    @Field
    String id;

    @Field("cat")
    String[] categories;

    @Field
    List<String> features;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCategories() {
        return categories;
    }

    // @Field("cat") //The @Field annotation can be applied on setter methods
    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
    
  }
