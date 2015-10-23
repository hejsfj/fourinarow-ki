package fileInterface;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Dient dem Lesen und Verarbeiten der Text-Datei vom Server.
 */
public class ServerfileReader {
	private String sharedFolderPath;
	private char player;
	private Serverfile serverFile;	
	
	/**
	 * Instanziiert einen neuen ServerfileReader.
	 *
	 * @param sharedFolderPath der Pfad zum Austauschverzeichnis
	 * @param player der Spielername
	 */
	public ServerfileReader(String sharedFolderPath, char player){
		this.sharedFolderPath = sharedFolderPath;
		this.player = player;
	}
	
	/**
	 * Lieﬂt die Server-Textdatei.
	 *
	 * @return ein ServerFile-Objekt
	 */
	public Serverfile readServerfile(){			
		File xmlFile = new File(sharedFolderPath + "/server2spieler" + player + ".xml");
		
		while (!xmlFile.exists()){
			try {
				Thread.sleep(300);
				System.out.println("wartet auf XML von Server");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("XML empfangen. Verarbeite XML");
		try {
			serverFile = new Serverfile();
			setServerfileValuesFromXML(xmlFile);			
			xmlFile.delete();
		}
		catch (Exception e) { 
			System.out.println("Error:");
			System.out.println(e.getMessage());
		}
		return serverFile;	
	}

	private void setServerfileValuesFromXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		Document xmlDoc = getDocFromXmlFile(xmlFile);
		Node contentNode = getNodeFromXmlDoc(xmlDoc, "content");	
		Element element = (Element) contentNode;		

		boolean freigegeben = Boolean.parseBoolean(element.getElementsByTagName("freigabe").item(0).getTextContent());
		
		serverFile.setFreigabe(freigegeben);				
		serverFile.setSatzstatus(element.getElementsByTagName("satzstatus").item(0).getTextContent());				
		serverFile.setGegnerzug(Integer.parseInt(element.getElementsByTagName("gegnerzug").item(0).getTextContent()));				
		serverFile.setSieger(element.getElementsByTagName("sieger").item(0).getTextContent());		
	}

	private Node getNodeFromXmlDoc(Document xmlDoc, String node) {
		NodeList nodeList = xmlDoc.getElementsByTagName(node);
		Node contentNode = nodeList.item(0);
		
		return contentNode;		
	}

	private Document getDocFromXmlFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		return documentBuilder.parse(xmlFile);
	}
}
