import com.hp.hpl.jena.util.FileManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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

    public void pastConference() {
        String req_pastConf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s conf:date ?date ." +
                " ?s conf:nomConf ?nom ." +
                "BIND((NOW()) as ?nowTime ) " +
                "FILTER ( ?date < ?nowTime )}";

        Query q = QueryFactory.create(req_pastConf);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
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

    public void soonConference() {
        String req_1 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s conf:date ?date ." +
                " ?s conf:nomConf ?nom ." +
                "BIND((NOW()) as ?nowTime ) " +
                "BIND(((DAY(?nowTime))+7) as ?nextWeek) " +
                "FILTER ( ?date > ?nowTime && DAY(?date) <= ?nextWeek && MONTH(?date) = MONTH(?nowTime) && YEAR(?date) = YEAR(?nowTime))}";

        Query q = QueryFactory.create(req_1);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
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

    public void conferenceByAnimateur(String animateur) {
        String req_animateur = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conference: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?title ?date  " +
                "WHERE  { ?conference conference:nomConf ?title ." +
                "?conference conference:date  ?date ." +
                " ?conference conference:animéPar ?Animateur ." +
                "?Animateur conference:nom \"" + animateur + "\"}";

        try {
            Query q = QueryFactory.create(req_animateur);
            QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
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
        } catch (Exception e) {
            System.out.println("Une erreur est survenue");
        }
    }

    public void conferenceByOrganisation(String organisation) {
        String req_organisateur = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conference: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?name ?date " +
                "WHERE  { " +
                " ?conference conference:organiséPar ?company . " +
                " ?conference conference:nomConf ?name ." +
                " ?conference conference:date ?date ." +
                " ?company conference:nomCompagnie \"" + organisation + "\"}";
        try {
            Query q = QueryFactory.create(req_organisateur);
            QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
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

            } catch (Exception e) {
                System.out.println("Une erreur est survenue...");
            } finally {

                qexec.close();
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue");
        }
    }

    public void cheapConference() {
        String req_cheapConference = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s rdf:type <http://www.semanticweb.org/adolp/ontologies/2021/Conference#BasPrixConference> ." +
                " ?s conf:nomConf ?nom ." +
                " ?s conf:date ?data ." +
                "}";
        Query q = QueryFactory.create(req_cheapConference);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
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

    public void longConference() {
        String req_longConference = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s rdf:type conf:LongueConference . " +
                " ?s conf:nomConf ?nom ." +
                " ?s conf:date ?date" +
                "}";
        Query q = QueryFactory.create(req_longConference);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
        try {

            ResultSet rs = qexec.execSelect();
//            String[] s = ResultSetFormatter.asText(rs).split("\\|");
//            for (int i = 3; i < s.length-1; i++) {
////                    System.out.println(s[i]);
//                    Resource conf = model.getResource(s[i].split("<")[0].split(">")[0]);
//                    for (StmtIterator it = conf.listProperties(); it.hasNext(); ) {
//                        Statement statement = it.next();
//                        System.out.print("    " + statement.getPredicate().getLocalName() + " -> ");
//
//                        if (statement.getObject().isLiteral()) {
//                            System.out.println(statement.getLiteral().getLexicalForm());
//                        } else {
//                            System.out.println(statement.getObject());
//                        }
//                    }
//                    System.out.println("-------------------------------------------------");
//            }
            ResultSetFormatter.out(System.out, rs);

        } finally {

            qexec.close();
        }
    }

    public void shortConference() {
        String req_shortConference = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s rdf:type conf:CourteConference ." +
                " ?s conf:nomConf ?nom ." +
                " ?s conf:date ?date" +
                "}";
        Query q = QueryFactory.create(req_shortConference);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
        try {

            ResultSet rs = qexec.execSelect();
//            String[] s = ResultSetFormatter.asText(rs).split("\\|");
//            for (int i = 3; i < s.length; i++) {
//                if ((i % 3 != 0) && (i % 4 == 0)) {
//                    System.out.println(s[i]);
//                    Resource conf = model.getResource(s[i].split("<")[1].split(">")[0]);
//                    for (StmtIterator it = conf.listProperties(); it.hasNext(); ) {
//                        Statement statement = it.next();
//                        System.out.print("    " + statement.getPredicate().getLocalName() + " -> ");
//
//                        if (statement.getObject().isLiteral()) {
//                            System.out.println(statement.getLiteral().getLexicalForm());
//                        } else {
//                            System.out.println(statement.getObject());
//                        }
//                    }
//                    System.out.println("-------------------------------------------------");
//                }
//            }

            ResultSetFormatter.out(System.out, rs);

        } finally {

            qexec.close();
        }
    }

    public void TypeConference(String indice) {
        String[] types = {"VulgarisationConference", "ProConference", "HybrideConference", "PresentielConference", "DistancielConference", "MeilleurConference", "MauvaiseConference"};
        String req_TypeSort = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX conf: <http://www.semanticweb.org/adolp/ontologies/2021/Conference#>" +
                "SELECT ?nom ?date " +
                "WHERE {?s rdf:type conf:" + types[Integer.parseInt(indice)] + " ." +
                " ?s conf:nomConf ?nom ." +
                " ?s conf:date ?date" +
                "}";
        Query q = QueryFactory.create(req_TypeSort);
        QueryExecution qexec = QueryExecutionFactory.create(q, this.model);
        try {

            ResultSet rs = qexec.execSelect();
//            String[] s = ResultSetFormatter.asText(rs).split("\\|");
//            for (int i = 3; i < s.length; i++) {
//                if ((i % 3 != 0) && (i % 4 == 0)) {
//                    System.out.println(s[i]);
//                    Resource conf = model.getResource(s[i].split("<")[1].split(">")[0]);
//                    for (StmtIterator it = conf.listProperties(); it.hasNext(); ) {
//                        Statement statement = it.next();
//                        System.out.print("    " + statement.getPredicate().getLocalName() + " -> ");
//
//                        if (statement.getObject().isLiteral()) {
//                            System.out.println(statement.getLiteral().getLexicalForm());
//                        } else {
//                            System.out.println(statement.getObject());
//                        }
//                    }
//                    System.out.println("-------------------------------------------------");
//                }
//            }

            ResultSetFormatter.out(System.out, rs);

        } finally {

            qexec.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String DataSet = "src/main/resources/Conferences";
        InputStream in = FileManager.get().open(DataSet);
        OntModel model = ModelFactory.createOntologyModel();
        model.read(in, null, "RDF/XML");
        requetes req = new requetes(model);
        while (true) {
            System.out.println("Que souhaitez-vous savoir?(Écrivez le numero correspondant)");
            System.out.println("0. Afficher les conférences passées");
            System.out.println("1. Afficher les conferences qui vont avoir lieu prochainement");
            System.out.println("2. Afficher les conferences d'un certain animateur");
            System.out.println("3. Afficher les conferences d'une certaine organisation");
            System.out.println("4. Afficher les conferences à bas prix");
            System.out.println("5. Afficher les conférences selon la durée");
            System.out.println("6. Afficher les conférences selon leurs types");
            System.out.println("E. Quittez l'application");
            Scanner user = new Scanner(System.in);
            switch (user.nextLine()) {
                case "0":
                    req.pastConference();
                    break;
                case "1":
                    req.soonConference();
                    break;
                case "2":
                    System.out.println("Quel est le nom de cet animateur?");
                    String animateur = user.nextLine();
                    req.conferenceByAnimateur(animateur);
                    break;
                case "3":
                    System.out.println("Quel est le nom de cette organisation?");
                    String organisation = user.nextLine();
                    req.conferenceByOrganisation(organisation);
                    break;
                case "4":
                    System.out.flush();
                    req.cheapConference();
                    break;
                case "5":
                    System.out.println("Choisissez la durée:");
                    System.out.println("0. Longues conférences");
                    System.out.println("1. Courtes conférences");
                    switch (user.nextLine()) {
                        case "0":
                            req.longConference();
                            break;
                        case "1":
                            req.shortConference();
                            break;
                    }
                    break;
                case "6":
                    System.out.println("Choisissez le type");
                    System.out.println("0. Vulgaire");
                    System.out.println("1. Pro");
                    System.out.println("2. Hybride");
                    System.out.println("3. Présentiel");
                    System.out.println("4. Distanciel");
                    System.out.println("5. Meilleure");
                    System.out.println("6. Mauvaise");
                    req.TypeConference(user.nextLine());
                    break;
                case "E":
                    return;
            }
        }
    }
}
