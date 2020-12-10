package queryer;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SparqlQueryer {

    private List<QuerySolution> resultList;
    private String report;

    public void query(String filePath, String sparQL){
        this.resultList = new LinkedList<>();
        this.report = "SparQL result:\n";
        try {
            Model model = ModelFactory.createDefaultModel();
            InputStream file = FileManager.get().open(filePath);
            model.read(file, "", "RDF/XML");

            Query query = QueryFactory.create(sparQL);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();

            while(resultSet.hasNext()){
                QuerySolution next = resultSet.next();
                this.resultList.add(next);
                Iterator iter = next.varNames();
                while (iter.hasNext()){
                    String varName = (String)iter.next();
                    this.report += (varName + " : " + next.get(varName) + "\n");
                }
                //System.out.println(next);
            }
            qe.close();

        }catch (Exception e){
            report += e.getMessage();
        }
    }

    public List<QuerySolution> getResultList(){
        return this.resultList;
    }

    public String getReport(){
        return this.report;
    }

    public static void main(String[] args) {
        String filename = "/home/tolyzot/IdeaProjects/rdf-ql/src/main/resources/vc-db-1.rdf";
        Model model = ModelFactory.createDefaultModel();
        InputStream file = FileManager.get().open(filename);

        model.read(file, "", "RDF/XML");
        String queryString = "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "\n" +
                "SELECT ?fn ?givenName\n" +
                "WHERE\n" +
                "  { ?y  vcard:Family  \"Smith\" ;\n" +
                "        vcard:FN  ?fn ;\n" +
                "        vcard:Given  ?givenName .\n" +
                "  }";
        //创建查询
        Query query = QueryFactory.create(queryString);
        //执行查询
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        //返回查询结果
        ResultSet results = qe.execSelect();
        //输出查询结果
        System.out.println("遍历结果集依次输出结果：");
        while(results.hasNext()){
            QuerySolution next = results.next();
            Iterator iter = next.varNames();
            while (iter.hasNext()){
                String varName = (String)iter.next();

                System.out.println("varName:" + varName + ", " + next.get(varName));
            }
            System.out.println(next);

        }

        qe.close();

    }
}
