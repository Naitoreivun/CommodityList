package application.model;

import java.util.*;

import javafx.beans.property.*;

public class Jacket
{
	private StringProperty cut;
	private StringProperty article;
	private IntegerProperty quantity;
	private IntegerProperty value;
	private StringProperty selectedSizes;
	private List<Integer> selectedSizesList;
	
	public static String SIZES[] = {"S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL", "6XL"};
	
	public Jacket(String cut, String article, int value, List<Integer> selectedSizesList)
	{
		this.cut = new SimpleStringProperty(cut);
		this.article = new SimpleStringProperty(article);
		this.value = new SimpleIntegerProperty(value);
		this.quantity = new SimpleIntegerProperty(selectedSizesList.size());
		this.selectedSizes = new SimpleStringProperty();
		this.selectedSizesList = selectedSizesList;
		setSelectedSizes();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((article == null) ? 0 : article.get().hashCode());
		result = prime * result + ((cut == null) ? 0 : cut.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jacket other = (Jacket) obj;
		if (article == null)
		{
			if (other.article.get() != null)
				return false;
		} else if (!article.get().equals(other.article.get()))
			return false;
		if (cut == null)
		{
			if (other.cut.get() != null)
				return false;
		} else if (!cut.get().equals(other.cut.get()))
			return false;
		return true;
	}

	public void setCut(String cut) {
		this.cut.set(cut);
	}
	public String getCut() {
		return cut.get().toUpperCase();
	}
	public StringProperty cutProperty() {
		return cut;
	}
	
	public void setArticle(String article) {
		this.article.set(article);
	}
	public String getArticle() {
		return article.get().toUpperCase();
	}
	public StringProperty articleProperty() {
		return article;
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	public int getQuantity() {
		return quantity.get();
	}
	public IntegerProperty quantityProperty() {
		return quantity;
	}
	
	public void setValue(int value) {
		this.value.set(value);
	}
	public int getValue() {
		return value.get();
	}
	public IntegerProperty valueProperty() {
		return value;
	}
	
	public void setSelectedSizes()
	{
		Collections.sort(selectedSizesList);
		String selectedSizes = SIZES[selectedSizesList.get(0)];
		for(int i = 1; i < selectedSizesList.size(); ++i)
			selectedSizes += ", " + SIZES[selectedSizesList.get(i)];
		this.selectedSizes.set(selectedSizes);
	}
	
	public StringProperty selectedSizesProperty(){
		return selectedSizes;
	}

	public List<Integer> getSelectedSizesList() {
		return selectedSizesList;
	}
}
