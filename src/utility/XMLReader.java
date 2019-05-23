//TODO need to create a default setting if a parameter is left empty (like Gridheight, or if there are no types)

package utility;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.util.*;

/**
 * A class to parse Cellular Automata XML files which obtains initial parameters, the name of the simulation, and dimensions of the grid
 * @author: José San Martin (js665), Joyce Zhou (jyz11)
 * Resources used: https://www.baeldung.com/java-xml, https://coursework.cs.duke.edu/CompSci308_2018Fall/spike_cellsociety/blob/master/src/xml/XMLParser.java
 */

public class XMLReader {
    Document docTree;
    private String xmlContent;
    private ResourceBundle myErrors;
    private DocumentBuilder dBuilder;


    public XMLReader(File file){
        myErrors = ResourceBundle.getBundle("resources/Errors");
        try {
            //File fXmlFile = new File("data/" + file);
            File fXmlFile = file;
            dBuilder = getDocumentBuilder();
            docTree = dBuilder.parse(fXmlFile);
            docTree.getDocumentElement().normalize();
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            xmlContent = new String(data, "UTF-8");
        }
        catch (Exception e){
            new Alert(Alert.AlertType.ERROR, myErrors.getString("NotXML")).showAndWait();
            throw new XMLException(myErrors.getString("NotXML"));
        }
    }


    public DocumentBuilder getDocumentBuilder() {
        try{
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch(ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }

    public String getXMLText(){
        return xmlContent;
    }

    public String getName(){
        NodeList name = docTree.getElementsByTagName("name");
        return name.item(0).getTextContent();
    }

    public int getHeight(){
        NodeList hNode = docTree.getElementsByTagName("gridHeight");
        return (int)Double.parseDouble(hNode.item(0).getTextContent());
    }

    public int getWidth(){
        NodeList wNode = docTree.getElementsByTagName("gridWidth");
        return (int)Double.parseDouble(wNode.item(0).getTextContent());
    }

    public String getShape() {
        NodeList wNode = docTree.getElementsByTagName("gridShape");
        return (wNode.item(0).getTextContent());
    }

    public String getOutline() {
        NodeList wNode = docTree.getElementsByTagName("gridOutline");
        return (wNode.item(0).getTextContent());
    }

    public String getCellFormat(){
        NodeList settings = docTree.getElementsByTagName("inputFormat");
        return settings.item(0).getTextContent();
    }

    public String getEdge(){
        NodeList settings = docTree.getElementsByTagName("edgeType");
        return settings.item(0).getTextContent();
    }

    public Map<String, Integer> getStates(){
        NodeList params = docTree.getElementsByTagName("cellState");
        HashMap<String, Integer> stateTypes = new HashMap<>();
        for (int i = 0; i < params.getLength(); i++){
            Node param = params.item(i);
            if(param.getNodeType() == Node.ELEMENT_NODE) {
                Element eParam = (Element) param;
                stateTypes.put(eParam.getAttribute("type"), Integer.parseInt(eParam.getTextContent()));
            }
        }
        return stateTypes;
    }

    /**
     * Next section basically returns a bunch of maps containing the rules/initial cell states/cell color
     * to be sent over to the XMLCheck, which will see if the info inside the maps are wrong
     */
    public Map<String, String> getMap(String tagName, String attribute){
        NodeList nodes = docTree.getElementsByTagName(tagName);
        Map map = new HashMap<String, String>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                try {
                    map.put(element.getAttribute(attribute), (element.getTextContent()));
                } catch(Exception e) {
                    //if value not a double, set init values elsewhere
                }
            }
        }
        return map;
    }

    public String getOriginalDescription() {
        NodeList params = docTree.getElementsByTagName("description");
        return params.item(0).getTextContent();
    }

    //used to convert a map<string,string> to a map<string,Double>, so that they can be used by
    //certain methods
    public Map<String, Double> getDoubleMap(Map<String,String> inputMap){
        Map map = new HashMap<String, Double>();
        for (String s: inputMap.keySet()) {
            try {
                map.put(s, Double.parseDouble(inputMap.get(s)));
            } catch(Exception e) {
                //if value not a double, set init values elsewhere
            }
        }
        return map;
    }

    public Map<String, Double> getThresholds() {
        return getDoubleMap(getMap("cellRule", "type"));
    }
    public Map<String,Double> getInitCells() {
        return getDoubleMap(getMap("cellState","type"));
    }
    public Map<String,Double> getInitPercent() {
        return getDoubleMap(getMap("cellState","type"));
    }
    public Map<String,String> getColors() {
        return getMap("cellColor","type");
    }

    public ArrayList<String> getInitPosition() {
        NodeList nodes = docTree.getElementsByTagName("row");
        ArrayList<String> rowN = new ArrayList();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                try {
                    rowN.add(element.getTextContent());
                } catch(Exception e) {
                    rowN.add("nothing");
                }
            }
        }
        return rowN;
    }


    /**
     * METHOD TESTING
     *
     */
    public static void main(String argv[]) {
        File fXmlFile = new File("data/Segregation.xml");
        XMLReader reader = new XMLReader(fXmlFile);
        System.out.println("Simulation: " + reader.getName());
        System.out.println("Height of grid: " + reader.getHeight());
        System.out.println("Width of grid: " + reader.getWidth());
        System.out.println("Rules are: " + reader.getThresholds());
        System.out.println("States are: " + reader.getStates());
        System.out.println("Content is: " + reader.getXMLText());
    }
}
