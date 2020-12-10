package queryer;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SparqlOwlQueryer {

    private List<QuerySolution> resultList;
    private String report;

    public void query(String filePath, String sparQL){

        this.resultList = new ArrayList<>();
        this.report = "";
        try{

            Model model = ModelFactory.createDefaultModel();
            InputStream is = new FileInputStream(filePath);
            model.read(is, null);

            Query query = QueryFactory.create(sparQL);
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, model);

            QueryExecution qe = QueryExecutionFactory.create(query, infModel);
            ResultSet resultSet = qe.execSelect();

            while(resultSet.hasNext()){
                QuerySolution next = resultSet.next();
                this.resultList.add(next);
                Iterator iter = next.varNames();
                while (iter.hasNext()){
                    String varName = (String)iter.next();
                    this.report += (varName + " : " + next.get(varName) + "\n");
                }
            }
            qe.close();

        }catch (FileNotFoundException fe){
            this.report += "File Not found : " + filePath;
        }
    }

    public List<QuerySolution> getResultList(){
        return this.resultList;
    }

    public String getReport(){
        return this.report;
    }

    public static void main(String[] args) {
        /*String schemaPath = "/home/tolyzot/validate_file/nbaSchema.owl";
        String dataPath = "/home/tolyzot/validate_file/nbaData.rdf";
        */
        String ql = "PREFIX NBA: <urn:absolute:NBA#> \n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                    "select ?p \n" +
                    "where{?p rdf:type NBA:player.}";
        try {
            String filePath = "/home/tolyzot/validate_file/NBA.owl";

            Model model = ModelFactory.createDefaultModel();
            InputStream is = new FileInputStream(filePath);
            model.read(is, null);

            Query query = QueryFactory.create(ql);
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, model);

            QueryExecution qe = QueryExecutionFactory.create(query, infModel);
            ResultSet resultSet = qe.execSelect();

            while(resultSet.hasNext()){
                QuerySolution next = resultSet.next();
                Iterator iter = next.varNames();
                while (iter.hasNext()){
                    String varName = (String)iter.next();
                    System.out.println(varName + " : " + next.get(varName) + "\n");
                }
            }
            qe.close();

        }catch (FileNotFoundException fe){
            System.out.println("file not found.");
        }
    }
}
