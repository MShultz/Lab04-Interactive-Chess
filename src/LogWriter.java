import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
	File resultFile;
	BufferedWriter results = null;
	FileWriter innerWriter = null;
	
	public LogWriter(){
		createFile();
		initializeWriter();
	}
	private void initializeWriter() {
		try {
			innerWriter = new FileWriter(resultFile);
			results = new BufferedWriter(innerWriter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void createFile() {
		String timeStamp = new SimpleDateFormat("MM-dd_HH_mm_ss").format(new Date());
		try {
			resultFile = new File("src/TranslationResults" + timeStamp + ".log");
			resultFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeToFile(String log) {
		try {
			results.write(log);
			results.flush();
			results.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeLogFile() throws IOException{
			results.close();
	}
}
