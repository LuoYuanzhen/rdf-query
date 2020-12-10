package view;

import handler.SparqlListenerHandler;

import javax.swing.*;
import java.awt.*;

public class SparqlViewer extends JFrame {

    private JTextPane filePathPane;
    private JTextPane queryPane;
    private JTextPane resultPane;

    private JButton queryBtn;

    private Dimension dimension;

    public SparqlViewer(){
        this.setTitle("Xpath query window");
        this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = dimension.width / 2, windowHeight = dimension.height / 2 + 100;
        this.setSize(windowWidth, windowHeight);
        this.setLayout(new GridLayout(1, 2, 5, 5));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.add(initLeft(windowWidth / 2, windowHeight));
        this.add(initRight(windowWidth / 2, windowHeight));

        this.setVisible(true);
    }

    private JPanel initLeft(int width, int height){
        JPanel mainLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        Dimension dimension = new Dimension(width, height - 200);
        Font font = new Font(null, 0 ,25);

        JLabel fileLabel = new JLabel("filePath:");
        this.filePathPane = new JTextPane();
        this.setTextPane(this.filePathPane, font, new Dimension(width-50, 40), Color.BLACK, true);
        this.filePathPane.setText("/home/tolyzot/validate_file/NBA.owl");

        this.queryPane = new JTextPane();
        this.setTextPane(this.queryPane, font, dimension, Color.BLACK, true);
        this.queryPane.setText("PREFIX NBA:<urn:absolute:NBA#>\n" +
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n\n");

        this.queryBtn = new JButton("query");
        this.queryBtn.addActionListener(new SparqlListenerHandler(this));

        JScrollPane filePathScroll = new JScrollPane(this.filePathPane);
        JScrollPane queryScroll = new JScrollPane(this.queryPane);
        filePathScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        queryScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainLeftPanel.add(fileLabel);
        mainLeftPanel.add(filePathScroll);
        mainLeftPanel.add(queryScroll);
        mainLeftPanel.add(this.queryBtn);

        return mainLeftPanel;
    }

    private JPanel initRight(int width, int height){
        JPanel mainRightPane = new JPanel(new FlowLayout(FlowLayout.LEADING));

        Dimension dimension = new Dimension(width, height - 100);
        Font font = new Font(null, 0 ,25);

        this.resultPane = new JTextPane();
        this.setTextPane(this.resultPane, font, dimension, Color.BLACK, false);

        JScrollPane resultScroll = new JScrollPane(this.resultPane);
        resultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainRightPane.add(resultScroll);

        return mainRightPane;
    }

    private void setTextPane(JTextPane textPane, Font font, Dimension dimension, Color color, boolean editable){
        if(font != null)
            textPane.setFont(font);
        textPane.setForeground(color);
        textPane.setPreferredSize(dimension);
        textPane.setEditable(editable);
    }

    public JTextPane getFilePathPane() {
        return filePathPane;
    }

    public JTextPane getResultPane() {
        return resultPane;
    }

    public JTextPane getQueryPane() {
        return queryPane;
    }

    public static void main(String[] args) {
        new SparqlViewer();
    }
}
