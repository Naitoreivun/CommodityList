package application.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Util.*;
import application.Main;
import application.model.Jacket;
import application.resources.ResTools;
import javafx.beans.value.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class ListMakerController
{
	@FXML
	private TableView<Jacket> jacketTable;
	@FXML
	private ContextMenu jacketTableMenu;
	@FXML
	private TableColumn<Jacket, String> cutColumn;
	@FXML
	private TableColumn<Jacket, String> articleColumn;
	@FXML
	private TableColumn<Jacket, Integer> quantityColumn;
	@FXML
	private TableColumn<Jacket, Integer> valueColumn;
	@FXML
	private TableColumn<Jacket, String> sizesColumn;
	
	@FXML
	private TextField stListNumberField;
	@FXML
	private TextField ndListNumberField;
	@FXML
	private TextField thListNumberField;
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private TextField cutField;
	@FXML
	private ComboBox<String> cutComboBox;
	@FXML
	private TextField articleField;
	@FXML
	private ComboBox<String> articleComboBox;
	@FXML
	private TextField valueField;
	
	@FXML
	private CheckBox S;
	@FXML
	private CheckBox M;
	@FXML
	private CheckBox L;
	@FXML
	private CheckBox XL;
	@FXML
	private CheckBox XL2;
	@FXML
	private CheckBox XL3;
	@FXML
	private CheckBox XL4;
	@FXML
	private CheckBox XL5;
	@FXML
	private CheckBox XL6;
	
	private CheckBox[] sizeCheckBoxes;
	
	private Map<CheckBox, Integer> sizesMap;
	private List<Integer> selectedSizesList;
	
	@FXML
	private CheckBox controlListCheckBox;
	@FXML
	private RadioButton controlListWomenRadioButton;
	@FXML
	private RadioButton controlListMenAndWomenRadioButton;
	@FXML
	private RadioButton controlListMenRadioButton;
	@FXML
	private HBox controlListRadioButtonsBox;
	
	private Main main;
	
	private File excelFile;
	private boolean saved = false;
	
	@FXML
	private Spinner<Integer> pageQuantitySpinner;
	
	public ListMakerController() {}
	
	@FXML
	private void initialize()
	{
		initSizes();
		initTable();
		initComboBoxes();
		initSpinner();
		clearAllComponents();
	}
	
	private void initSizes()
	{
		selectedSizesList = new ArrayList<Integer>();
		
		sizeCheckBoxes  = new CheckBox[] {S, M, L, XL, XL2, XL3, XL4, XL5, XL6};

		sizesMap = new HashMap<>();
		for(int i = 0; i <= 8; ++i)
			sizesMap.put(sizeCheckBoxes[i], i);
		
		for(int i = 0; i <= 8; ++i)
			sizeCheckBoxes[i].selectedProperty().addListener(new CheckBoxListener(sizeCheckBoxes[i]));
	}
	
	private class CheckBoxListener implements ChangeListener<Boolean>
	{
		  private final CheckBox checkBox;
		  CheckBoxListener(CheckBox checkBox)
		  {
		    this.checkBox = checkBox;
		  }
		  @Override
		  public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
		  {
			  int id = sizesMap.get(checkBox);
			  if(newValue)
				  selectedSizesList.add(id);
			  else
				  selectedSizesList.remove((Integer) id);
		  }
	}
	
	private void initTable()
	{
		cutColumn.setCellValueFactory(cellData -> cellData.getValue().cutProperty());
		articleColumn.setCellValueFactory(cellData -> cellData.getValue().articleProperty());
		quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
		valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
		sizesColumn.setCellValueFactory(cellData -> cellData.getValue().selectedSizesProperty());
		
		jacketTable.setOnKeyPressed((event) -> 
		{
			if(event.getCode() == KeyCode.DELETE)
				handleRemoveJacket();
		});
	}
	
	private void initComboBoxes()
	{
		cutComboBox.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> cutField.setText(newValue));
		articleComboBox.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> articleField.setText(newValue));
	}
	
	private void initSpinner()
	{
		pageQuantitySpinner.setValueFactory(new IntegerSpinnerValueFactory(1, 10, 1));
	}

	
	public void setMain(Main main)
	{
		this.main = main;
		
		cutComboBox.setItems(main.getCuts());
		articleComboBox.setItems(main.getArticles());
		jacketTable.setItems(main.getJackets());

		main.getJackets().addListener(new ListChangeListener<Jacket>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends Jacket> pChange) 
            {
                saved = false;
            }
        });
	}
	
	public String getCommodityListNumber()
	{
		return stListNumberField.getText() + "/" + ndListNumberField.getText() + "/" + thListNumberField.getText();
	}
	
	public String getCommodityListDate()
	{
		String datePattern = "dd.MM.yyyy";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
		
		LocalDate date = datePicker.getValue();
		return dateFormatter.format(date) + "r.";
	}
	
	@FXML
	private void handleAddJacket()
	{
		if(!isTextFieldValid(valueField))
			Alerts.showError("Z³a cena", "Niepoprawnie wpisana cena", "Proszê wpisaæ odpowiedni¹ cenê.");
		else if(selectedSizesList.size() > 0)
		{
			String cut = cutField.getText();
			String article = articleField.getText();
			int value = Integer.parseInt(valueField.getText());
			
			Jacket jacket = new Jacket(cut, article, value, selectedSizesList);
			if(main.getJackets().contains(jacket))
				Alerts.showWarning("Ostrze¿enie", "Kurtka o takim samym fasonie i artykule ju¿ widnieje na liœcie", "");
			main.getJackets().add(jacket);
			selectedSizesList = new ArrayList<Integer>();
			clearSizeCheckBoxes();
			
		}
		else
			Alerts.showError("Zero kurtek", "Nie wybrano ¿adnego rozmiaru", "Proszê zaznaczyæ odpowiednie rozmiary.");
	}
	
	@FXML
	private void handleRemoveJacket()
	{
		int selectedId = jacketTable.getSelectionModel().getSelectedIndex();
		if(selectedId >= 0)
		{
			jacketTable.getItems().remove(selectedId);
		}
		else
			Alerts.showError("Brak wyboru", "Nie wybrano ¿adnej kurtki", "Proszê zaznaczyæ kurtkê w tabeli.");
	}
	
	@FXML
	private void handleSelectAllSizes()
	{
		for(CheckBox cb : sizeCheckBoxes)
			cb.setSelected(true);
	}
	
	@FXML
	private void handleSetDisableControllListRadioButtons()
	{
		boolean selected = controlListCheckBox.isSelected();
		controlListRadioButtonsBox.setDisable(!selected);
	}
	
	@FXML
	private boolean handleExportToExcel()
	{
		if(isValid())
		{
			excelFile = showFileChooser();
			
			if(excelFile != null)
			{
				if(controlListCheckBox.isSelected())
				{
					String additionalText = getControlListText();
					new CommodityList(main.getJackets(), stListNumberField.getText(),
							getCommodityListDate(), excelFile, additionalText, true);
				}
				else
				{
					new CommodityList(main.getJackets(), getCommodityListNumber(),
							getCommodityListDate(), excelFile, "", false);
				}
				saved = true;
				Alerts.showInformationAlert("Eksport", "Spis zosta³ wyeksportowany do Excel'a");
				return true;
			}
		}
		return false;
	} 
	
	private String getControlListText()
	{
		String text = "";
		
		if(controlListMenRadioButton.isSelected())
			text = " / MÊSKIE";
		else if(controlListWomenRadioButton.isSelected())
			text = " / DAMSKIE";
		
		return text;
	}

	private File showFileChooser()
	{
		FileChooser fileChooser = ResTools.makeFileChooser("XLS files (*.xls)", "*.xls");
		return fileChooser.showSaveDialog(main.getPrimaryStage());
	}

	@FXML
	private void handlePrint()
	{
		if(saved)
		{
			print();
		}
		else if(handleExportToExcel())
		{
			print();
		}
	}
	
	private void print()
	{
		try
		{
			for(int i = 1; i <= pageQuantitySpinner.getValue(); ++i)
				Desktop.getDesktop().print(excelFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isValid()
	{
		boolean valid = true;
		String errors = "";
		if(!isTextFieldValid(stListNumberField) || !isTextFieldValid(ndListNumberField)
				|| !isTextFieldValid(thListNumberField))
		{
			errors += "Proszê wpisaæ poprawny numer spisu.\n";
			valid = false;
		}
		if(datePicker.getValue() == null)
		{
			errors += "Proszê wybraæ poprawn¹ datê.\n";
			valid = false;
		}
		if(main.getJackets().size() == 0)
		{
			errors += "Proszê dodaæ przynajmniej jedn¹ kurtkê.\n";
			valid = false;
		}
		
		if(!valid)
			Alerts.showError("B³¹d", "Niepoprawnie wype³niony arkusz", errors);
		
		return valid;
	}
	
	private boolean isTextFieldValid(TextField textField)
	{
		String text = textField.getText();
		if("".equals(text))
			return false;
		if(!text.matches("\\d*"))
			return false;
		
		return true;
	}

	public void clearAllComponents()
	{
		clearSizeCheckBoxes();
		clearControlListCheckBox();
		clearTextFields();
		clearDatePicker();
		clearSpinner();
		clearControlListControls();
	}
	
	private void clearSizeCheckBoxes()
	{
		for(CheckBox cb : sizeCheckBoxes)
			cb.setSelected(false);
	}
	
	private void clearControlListCheckBox()
	{
		controlListCheckBox.setSelected(false);
	}
	
	private void clearTextFields()
	{
		stListNumberField.clear();
		String month = String.valueOf((LocalDate.now().getMonthValue()));
		if(month.length() == 1)
			month = "0" + month;
		ndListNumberField.setText(month);
		String year = String.valueOf((LocalDate.now().getYear()));
		thListNumberField.setText(year);
		valueField.setText("0");
	}
	
	private void clearDatePicker()
	{
		datePicker.setValue(LocalDate.now());
	}
	
	private void clearSpinner()
	{
		pageQuantitySpinner.getValueFactory().setValue(2);
	}
	
	private void clearControlListControls()
	{
		controlListCheckBox.setSelected(false);
		controlListRadioButtonsBox.setDisable(true);
		controlListMenAndWomenRadioButton.setSelected(true);
	}
}
