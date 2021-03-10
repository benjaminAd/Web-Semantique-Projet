import com.hp.hpl.jena.util.FileManager;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.io.FileWriter;
import java.io.IOException;
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

    public Model getModel() {
        return model;
    }

    public void writeModel() throws IOException {
        FileWriter RDFXMLOUT = new FileWriter("src/main/resources/conferences.xml");
        try {
            this.model.write(RDFXMLOUT);
        } finally {
            try {
                RDFXMLOUT.close();
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) throws IOException {
        String DataSet = "C:\\Users\\adolp\\OneDrive\\Bureau\\Fac\\Master\\M1\\S2\\Web-Sémantique\\Projet\\projetJena\\src\\main\\resources\\Conferences";
        InputStream in = FileManager.get().open(DataSet);
        Model model = ModelFactory.createDefaultModel();
        model.read(in, null, "RDF/XML");
        requetes req = new requetes(model);
        String req_1 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>"
                + "SELECT ?nomConf " +
                "WHERE {?s rdf:type conf:Conférence ." +
                "?s conf:nomConf ?nomConf .}";
        Query q = QueryFactory.create(req_1);
        QueryExecution qexec = QueryExecutionFactory.create(q, req.getModel());
        try {

            ResultSet rs = qexec.execSelect();

            ResultSetFormatter.out(System.out, rs, q);

        } finally {

            qexec.close();
        }
    }
}
