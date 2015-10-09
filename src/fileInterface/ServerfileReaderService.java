package fileInterface;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServerfileReaderService extends Service<Serverfile>{
	final ServerfileReader serverfileReader;
	public ServerfileReaderService(String sharedFolderPath, char player){
		serverfileReader  = new ServerfileReader(sharedFolderPath, player);
	}
	@Override
	protected Task<Serverfile> createTask() {
		 return new Task<Serverfile>() {
                protected Serverfile call() throws IOException {
                     return serverfileReader.readServerfile();
            };
		 };
	}
}