package application.view;

import java.io.File;

import Util.Alerts;
import application.Main;
import application.resources.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

public class CutsAndArticlesController
{
	@FXML
	private ListView<String> cutsList;
	@FXML
	private TextField cutsTextField;
	@FXML
	private Button cutsAddButton;
	
	@FXML
	private ListView<String> articlesList;
	@FXML
	private TextField articlesTextField;
	@FXML
	private Button articlesAddButton;
	
	private Main main;
	
	public void setMain(Main main)
	{
		this.main = main;
		new ListTools(cutsList, cutsTextField, cutsAddButton, main.getCuts());
		new ListTools(articlesList, articlesTextField, articlesAddButton, main.getArticles());
	}
	
	private class ListTools
	{
		private ListView<String> listView;
		private TextField textField;
		private Button button;
		private ObservableList<String> observableList;
		ListTools(ListView<String> listView, TextField textField, Button button, ObservableList<String> observableList)
		{
			this.listView = listView;
			this.textField = textField;
			this.button = button;
			this.observableList = observableList;
			
			this.listView.setItems(this.observableList);
			addButtonListener();
			addTextFieldListener();
			addListListener();
			addListViewMenuContext();
		}

		private void addButtonListener()
		{
			button.setOnAction((event) -> handleAdd());
		}
		
		private void addTextFieldListener()
		{
			textField.setOnKeyPressed((event) -> 
			{
				if(event.getCode() == KeyCode.ENTER) 
					handleAdd();
			});
		}
		
		private void handleAdd()
		{
			String text = textField.getText().toUpperCase();
			if("".equals(text))
			{
				Alerts.showError("Brak nazwy", "Podana nazwa jest pusta", "Nie mo¿na dodawaæ pustego pola do listy.");
			}
			else if(observableList.contains(text))
			{
				Alerts.showError("B³¹d", "Wybrana nazwa znajduje siê ju¿ na liœcie",
						"Na liœcie nie mog¹ znajdowaæ siê duplikaty.");
			}
			else
			{
				observableList.add(text);
				FXCollections.sort(observableList);
				textField.clear();
			}
		}
		
		private void addListListener()
		{
			listView.setOnKeyPressed((event) ->
			{
				if(event.getCode() == KeyCode.DELETE)
					handleRemove();
			});
		}
	
		private void addListViewMenuContext()
		{
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menuItem = new MenuItem("Usuñ");
			menuItem.setOnAction((ActionEvent event) -> handleRemove());
			contextMenu.getItems().add(menuItem);
			listView.setContextMenu(contextMenu);
		}
		
		private void handleRemove()
		{
			int id = listView.getSelectionModel().getSelectedIndex();
			if(id >= 0)
			{
				listView.getItems().remove(id);
			}
			else
			{
				Alerts.showError("Brak wyboru", "Nie wybrano ¿adnej pozycji", "Proszê zaznaczyæ jedn¹ pozycjê na liœcie.");
			}
		}
	}

	@FXML
    private void handleOpen()
	{
        FileChooser fileChooser = ResTools.makeFileChooser("XML files (*.xml)", "*.xml");

        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null)
        {
            ResLoader.loadCutsAndArticlesFromFile(file, main);
        }
    }
	
    @FXML
    private void handleSave() 
    {
        File cutsAndArticlesFile = ResTools.getCutsAndArticlesFilePath();
        if (cutsAndArticlesFile != null)
        {
            ResSaver.saveCutsAndArticlesToFile(cutsAndArticlesFile, main);
        } 
        else 
        {
            handleSaveAs();
        }
    }
    
    @FXML
    private void handleSaveAs() 
    {
        FileChooser fileChooser = ResTools.makeFileChooser("XML files (*.xml)", "*.xml");
        
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null)
        {
            if (!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
            }
            ResSaver.saveCutsAndArticlesToFile(file, main);
        }
    }
}
