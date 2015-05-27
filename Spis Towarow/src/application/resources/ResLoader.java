package application.resources;

import java.io.File;

import javax.xml.bind.*;

import Util.Alerts;
import application.Main;
import application.model.DataWrapper;
import javafx.scene.image.Image;

public class ResLoader
{	
	public static void loadCutsAndArticlesFromFile(File file, Main main)
	{
	    try 
	    {
	        JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        DataWrapper wrapper = (DataWrapper) um.unmarshal(file);

	        main.getCuts().clear();
	        main.getCuts().addAll(wrapper.getCuts());
	        main.getArticles().clear();
	        main.getArticles().addAll(wrapper.getArticles());

	        ResTools.setCutsAndArticlesFilePath(file);

	    } catch (Exception e) {
	    	Alerts.showError("B³¹d", "Nie mo¿na wczytaæ danych",
	    			"Nie mo¿na wczytaæ danych z pliku:\n" + file.getPath());
	    }
	}
	
	public static Image getIcon()
	{
		return new Image(ResLoader.class.getResourceAsStream("icon.png"));
	}
}
