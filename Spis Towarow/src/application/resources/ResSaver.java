package application.resources;

import java.io.File;

import javax.xml.bind.*;

import Util.Alerts;
import application.Main;
import application.model.DataWrapper;

public class ResSaver
{
	public static void saveCutsAndArticlesToFile(File file, Main main)
	{
	    try
	    {
	        JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        DataWrapper wrapper = new DataWrapper();
	        wrapper.setCuts(main.getCuts());
	        wrapper.setArticles(main.getArticles());

	        m.marshal(wrapper, file);

	        ResTools.setCutsAndArticlesFilePath(file);
	        
	    } catch (Exception e) {
	    	Alerts.showError("B³¹d", "Nie mo¿na zapisaæ danych",
	    			"Nie mo¿na wczytaæ danych z pliku:\n" + file.getPath());
	    }
	}
}
