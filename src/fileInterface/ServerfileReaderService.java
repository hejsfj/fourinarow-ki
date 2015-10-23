package fileInterface;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


/**
 * Ein Service für das Lesen der Server-Datei.
 */
public class ServerfileReaderService extends Service<Serverfile>{
	
	/** Der serverfileReader */
	final ServerfileReader serverfileReader;
	
	/**
	 * Instanziiert einen neuen ServerfileReaderService.
	 *
	 * @param sharedFolderPath der Pfad zum Austauschverzeichnis
	 * @param player der Spielername
	 */
	public ServerfileReaderService(String sharedFolderPath, char player){
		serverfileReader  = new ServerfileReader(sharedFolderPath, player);
	}
	
	/* (non-Javadoc)
	 * @see javafx.concurrent.Service#createTask()
	 */
	@Override
	protected Task<Serverfile> createTask() {
		 return new Task<Serverfile>() {
                protected Serverfile call() throws IOException {
                     return serverfileReader.readServerfile();
            };
		 };
	}
}