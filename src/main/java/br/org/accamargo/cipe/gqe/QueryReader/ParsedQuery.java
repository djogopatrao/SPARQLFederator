package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.Iterator;
import java.util.List;

public class ParsedQuery {

	private List <String> data;
		
	public ParsedQuery() {
		super();
	}

	public void addIntersectionClass(String string) {
		data.add(string);
	}

	public Iterator<String> iterator() {
		return data.iterator();
	}

}
