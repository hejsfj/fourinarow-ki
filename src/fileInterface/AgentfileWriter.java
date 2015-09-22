package fileInterface;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
// TO DO: Umstellen auf AgentFilewriter und Agentfile (OO)
public class AgentfileWriter{

	private String sharedFolderPath;
	public AgentfileWriter(String sharedFolderPath){
		this.sharedFolderPath = sharedFolderPath;		
	}
	public void writeAgentfile(Agentfile agentFile) throws IOException{					
		// TO DO: Wie die Angabe des Spielers reinpacken?? für korrekte Bestimmung des Dateinamens
			String filePath = sharedFolderPath + "/spielero2server.txt";
			// oder:
			// String filePath = sharedFolderPath + "/spielerx2server.txt";
			
			final Charset charset=Charset.forName("US-ASCII");
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charset));
			writer.write(String.valueOf(agentFile.getMove()));
			writer.close();		
	}
}
