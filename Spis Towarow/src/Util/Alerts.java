package Util;

import java.util.Optional;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class Alerts
{
	private static Alert makeAlert(AlertType alertType, String title, String headerText, String contentText)
	{
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		
		return alert;
	}
	
	public static boolean showConfirmationDialog(String title, String headerText)
	{
		Alert alert = makeAlert(AlertType.CONFIRMATION, title, headerText, "");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		    return true;
		else
		    return false;
	}
	
	public static void showInformationAlert(String title, String headerText)
	{
		Alert alert = makeAlert(AlertType.INFORMATION, title, headerText, "");
		alert.showAndWait();
	}
	
	public static void showWarning(String title, String headerText, String contentText)
	{
		Alert alert = makeAlert(AlertType.WARNING, title, headerText, contentText);
        alert.showAndWait();
	}

	public static void showError(String title, String headerText, String contentText)
	{
		Alert alert = makeAlert(AlertType.ERROR, title, headerText, contentText);
        alert.showAndWait();
	}
}
