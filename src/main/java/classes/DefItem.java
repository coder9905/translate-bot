package classes;

import java.util.List;

public class DefItem{
	private String pos;
	private String text;
	private List<TrItem> tr;
	private String ts;

	public String getPos(){
		return pos;
	}

	public String getText(){
		return text;
	}

	public List<TrItem> getTr(){
		return tr;
	}

	public String getTs(){
		return ts;
	}
}