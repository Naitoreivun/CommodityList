package application.resources;

import java.io.File;
import java.util.prefs.Preferences;

import application.Main;
import javafx.stage.FileChooser;

public class ResTools
{
	public static FileChooser makeFileChooser(String description, String extension)
	{
		FileChooser fileChooser = new FileChooser();

	    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
	    fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setSelectedExtensionFilter(extFilter);
	    
	    return fileChooser;
	}
	
	public static File getCutsAndArticlesFilePath() 
	{
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    String filePath = prefs.get("filePath", null);
	    
	    if (filePath != null)
	        return new File(filePath);
	    else
	        return null;
	}

	public static void setCutsAndArticlesFilePath(File file) 
	{
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    
	    if (file != null)
	        prefs.put("filePath", file.getPath());
	    else 
	        prefs.remove("filePath");
	}
}
