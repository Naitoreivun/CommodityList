package application.model;

import java.util.List;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "lists")
public class DataWrapper
{
	private List<String> cuts;

	private List<String> articles;
	
	@XmlElement(name = "cut")
	public List<String> getCuts() {
		return cuts;
	}
	
	@XmlElement(name = "article")
	public List<String> getArticles() {
		return articles;
	}
	
	public void setCuts(List<String> cuts) {
		test(cuts);
		this.cuts = cuts;
	}

	public void setArticles(List<String> articles) {
		test(articles);
		this.articles = articles;
	}

	private void test(List<String> list)
	{
		if(list.size() == 0)
			list.add("XXX");
	}
}
