//http://code.makery.ch/library/javafx-8-tutorial/part5/

package application;

import java.io.File;
import java.io.IOException;

import Util.Alerts;
import application.model.Jacket;
import application.resources.*;
import application.view.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application 
{
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ListMakerController listMakerController;
	private RootLayoutController  rootLayoutController;
	
	private ObservableList<String> cuts = FXCollections.observableArrayList();
	private ObservableList<String> articles = FXCollections.observableArrayList();
	private ObservableList<Jacket> jackets = FXCollections.observableArrayList();
	
	public Main()
	{
		File file = ResTools.getCutsAndArticlesFilePath();
		if(file != null)
			ResLoader.loadCutsAndArticlesFromFile(ResTools.getCutsAndArticlesFilePath(), this);
	}
	
	public ObservableList<String> getCuts(){
		return cuts;
	}
	public ObservableList<String> getArticles(){
		return articles;
	}
	public ObservableList<Jacket> getJackets(){
		return jackets;
	}

	@Override
	public void start(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
		
		initPrimaryStage();
		
		initRootLayout();
		
		showListMaker();
		
		showCutsAndArticles();
	}
	
	private void initPrimaryStage()
	{
		primaryStage.setTitle("Spis towarów do przechowania");
		
		primaryStage.getIcons().add(ResLoader.getIcon()); //http://olawolska.com/
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	              if(!handleExit())
	            	  we.consume();
	          }
	      }); 
	}
	
	public boolean handleExit()
	{
		if(Alerts.showConfirmationDialog("Wyjœcie",
				"Program zostanie zamkniêty. Niezapisane zmiany zostan¹ utracone."))
			System.exit(0);
		return false;
	}

	private void initRootLayout()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			rootLayoutController = loader.getController();
			rootLayoutController.setMain(this);
			
			primaryStage.show();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void showListMaker()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ListMaker.fxml"));
			BorderPane borderPane = (BorderPane) loader.load();
			
			rootLayoutController.getCommodityListTab().setContent(borderPane);
			
			listMakerController = loader.getController();
			listMakerController.setMain(this);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showCutsAndArticles()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CutsAndArticles.fxml"));
			BorderPane borderPane = (BorderPane) loader.load();
			
			rootLayoutController.getCutsAndArticlesTab().setContent(borderPane);
			
			CutsAndArticlesController controller = loader.getController();
			controller.setMain(this);
			
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public ListMakerController getListMakerController()
	{
		return listMakerController;
	}
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
