import com.hp.hpl.jena.util.FileManager;
import org.apache.jena.ontology.OntModel;
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

    public static void main(String[] args) {
        String DataSet = "C:\\Users\\adolp\\OneDrive\\Bureau\\Fac\\Master\\M1\\S2\\Web-Sémantique\\Projet\\projetJena\\src\\main\\resources\\Conferences";
        InputStream in = FileManager.get().open(DataSet);
        OntModel model = ModelFactory.createOntologyModel();
        model.read(in, null, "RDF/XML");
        requetes req = new requetes(model);
        String req_1 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?s ?date " +
                "WHERE {?s conf:date ?date ." +
                "BIND((NOW()) as ?nowTime ) " +
                "BIND(((DAY(?nowTime))+7) as ?nextWeek) " +
                "FILTER ( ?date > ?nowTime && DAY(?date) <= ?nextWeek && MONTH(?date) = MONTH(?nowTime) && YEAR(?date) = YEAR(?nowTime))}";

        String req_animateur = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conference: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?Animateur ?date ?title " +
                "WHERE  { ?conference conference:nomConf ?title ." +
                "?conference conference:date  ?date ." +
                " ?conference conference:animéPar ?Animateur ." +
                "?Animateur conference:nom \"Kostantin Todorov\"}";

        String req_organisateur = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conference: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?company ?conference " +
                "WHERE  { " +
                " ?conference conference:organiséPar ?company ." +
                "?company conference:nomCompagnie \"Université de Montpellier\"}";

        Query q = QueryFactory.create(req_organisateur);
        QueryExecution qexec = QueryExecutionFactory.create(q, req.getModel());
        try {

            ResultSet rs = qexec.execSelect();
            /*String[] s = ResultSetFormatter.asText(rs).split("\\|");
            for (int i = 3; i < s.length; i++) {
                if ((i % 3 != 0) && (i % 4 == 0)) {
                    System.out.println(s[i]);
                    Resource conf = model.getResource(s[i].split("<")[1].split(">")[0]);
                    for (StmtIterator it = conf.listProperties(); it.hasNext(); ) {
                        Statement statement = it.next();
                        System.out.print("    " + statement.getPredicate().getLocalName() + " -> ");

                        if (statement.getObject().isLiteral()) {
                            System.out.println(statement.getLiteral().getLexicalForm());
                        } else {
                            System.out.println(statement.getObject());
                        }
                    }
                    System.out.println("-------------------------------------------------");
                }
            }*/

            ResultSetFormatter.out(System.out, rs);

        } finally {

            qexec.close();
        }
    }
}
