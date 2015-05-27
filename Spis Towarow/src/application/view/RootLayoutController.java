package application.view;


import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import Util.Alerts;
import application.Main;

public class RootLayoutController
{
	@FXML
	private Tab commodityListTab;
	@FXML
	private Tab cutsAndArticlesTab;
	
	private Main main;
	
	public void setMain(Main main)
	{
		this.main = main;
	}
	
	@FXML
	private void initialize() {}
	
	@FXML
	private void handleNew()
	{
		if(Alerts.showConfirmationDialog("Nowy spis", "Dane w tabeli zostan¹ wyczyszczone. Niezapisane zmiany zostan¹ utracone."))
		{
			main.getJackets().clear();
			main.getListMakerController().clearAllComponents();
		}
	}

	@FXML
	private void handleExit()
	{
		main.handleExit();
	}

	public Tab getCommodityListTab() {
		return commodityListTab;
	}

	public Tab getCutsAndArticlesTab() {
		return cutsAndArticlesTab;
	}
	
	
}
