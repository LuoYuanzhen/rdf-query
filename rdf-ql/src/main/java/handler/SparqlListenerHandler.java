package handler;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import queryer.SparqlOwlQueryer;
import queryer.SparqlQueryer;
import view.SparqlViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class SparqlListenerHandler implements ActionListener {

    private SparqlViewer sparqlViewer;
    private SparqlOwlQueryer queryer;

    public SparqlListenerHandler(SparqlViewer sparqlViewer){
        this.sparqlViewer = sparqlViewer;
        this.queryer = new SparqlOwlQueryer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filePath = this.sparqlViewer.getFilePathPane().getText();
        String sparQL = this.sparqlViewer.getQueryPane().getText();

        this.queryer.query(filePath, sparQL);
        formatOutput();
    }

    private void formatOutput(){
        System.out.println(this.queryer.getReport());

        String html = "<h1>SparQL result:</h1>\n<table border='1'>\n";
        List<QuerySolution> resultList = this.queryer.getResultList();
        for (int i = 0; i < resultList.size(); i++){
            QuerySolution qs = resultList.get(i);
            Iterator iter = qs.varNames();
            if (i == 0){
                html += getHeadRow(qs.varNames());
            }
            html += getValueRow(iter, qs);
        }
        html += "</table>";

        //System.out.println(html);
        this.sparqlViewer.getResultPane().setContentType("text/html");
        this.sparqlViewer.getResultPane().setText(html);

    }

    private String getHeadRow(Iterator iter){
        String row = "<tr>\n";
        while (iter.hasNext()){
            String varName = (String) iter.next();
            row += "<th>" + varName + "</th>\n";
        }
        row += "</tr>\n";

        return row;
    }

    private String getValueRow(Iterator iter, QuerySolution qs){
        String valueRow = "<tr>\n";
        while (iter.hasNext()){
            String varName = (String) iter.next();
            valueRow += "<td>" + qs.get(varName) + "</td>\n";
        }
        valueRow += "</tr>\n";

        return valueRow;
    }
}
