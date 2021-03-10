import com.hp.hpl.jena.util.FileManager;
import org.apache.jena.rdf.model.*;

import java.io.InputStream;

public class requetes {

    private Model model;

    public requetes(Model model) {
        this.model = model;
    }

    /**
     * @param s subject
     * @param p propriety
     * @param o object --> S'il pointe vers une ressource alors o devra suivre ce format : "URI|..."
     */
    public void addStatement(String s, String p, String o) {
        Resource res;
        Property property;
        String[] dataO = o.split("\\|");
        if ((res = model.getResource(s)) == null) {
            res = model.createResource(s);
            if ((property = model.getProperty(p)) == null) {
                property = model.createProperty(p);
            }
            if (dataO.length != 2) res.addProperty(property, o);
            else res.addProperty(property, model.getResource(dataO[1]));
        } else {
            if ((property = model.getProperty(p)) == null) {
                property = model.createProperty(p);

            }
            if (dataO.length != 2) res.addProperty(property, o);
            else res.addProperty(property, model.getResource(dataO[1]));
        }
    }

    public static void main(String[] args) {
        String DataSet = "C:\\Users\\adolp\\OneDrive\\Bureau\\Fac\\Master\\M1\\S2\\Web-Sémantique\\Projet\\projetJena\\src\\main\\resources\\Conferences";
        InputStream in = FileManager.get().open(DataSet);
        Model model = ModelFactory.createDefaultModel();
        model.read(in, null, "RDF/XML");
    }
}
