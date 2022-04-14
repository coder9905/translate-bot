package classes;

import java.util.List;

public class Response{
	private Head head;
	private List<DefItem> def;

	public Head getHead(){
		return head;
	}

	public List<DefItem> getDef(){
		return def;
	}
}