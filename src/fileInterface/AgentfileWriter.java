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
	private char player;
	public AgentfileWriter(String sharedFolderPath, char player){
		this.sharedFolderPath = sharedFolderPath;		
		this.player = player;
	}
	public void writeAgentfile(Agentfile agentFile) throws IOException{					
			String filePath = sharedFolderPath + "/spieler" + player + "2server.txt";
			
			final Charset charset=Charset.forName("US-ASCII");
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charset));
			writer.write(String.valueOf(agentFile.getMove()));
			writer.close();		
	}
}
