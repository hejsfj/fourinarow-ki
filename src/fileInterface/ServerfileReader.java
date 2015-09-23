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

public class ServerfileReader {
	private String sharedFolderPath;
	private String player;
	
	public ServerfileReader(String sharedFolderPath, String player){
		this.sharedFolderPath = sharedFolderPath;
		this.player = player;
	}
	
	public Serverfile readServerfile(){		
		Serverfile serverFile = new Serverfile();		
	
		File xmlFile = new File(sharedFolderPath + "/server2spielerx.xml");
//		File xmlFile = new File(sharedFolderPath + "/server2spielero.xml");
		
		try {
			Document xmlDoc = getDocFromXmlFile(xmlFile);
			Node contentNode = getNodeFromXmlDoc(xmlDoc, "content");	
			Element element = (Element) contentNode;		

			boolean freigegeben = Boolean.parseBoolean(element.getElementsByTagName("freigabe").item(0).getTextContent());
			
			serverFile.setFreigabe(freigegeben);				
			serverFile.setSatzstatus(element.getElementsByTagName("satzstatus").item(0).getTextContent());				
			serverFile.setGegnerzug(Integer.parseInt(element.getElementsByTagName("gegnerzug").item(0).getTextContent()));				
			serverFile.setSieger(element.getElementsByTagName("sieger").item(0).getTextContent());
		}
		catch (Exception e) { 
			System.out.println("Error:");
			e.getMessage();
		}		
		return serverFile;	
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
