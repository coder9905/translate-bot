package classes;

import java.util.List;

public class TrItem{
	private String pos;
	private List<MeanItem> mean;
	private String text;
	private int fr;

	public String getPos(){
		return pos;
	}

	public List<MeanItem> getMean(){
		return mean;
	}

	public String getText(){
		return text;
	}

	public int getFr(){
		return fr;
	}
}