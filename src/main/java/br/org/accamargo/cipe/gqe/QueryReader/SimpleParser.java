package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.List;

public class SimpleParser implements BaseParser {

	@Override
	public ParsedQuery parse(String query) throws Exception {
		String[] queryArray = query.split("\\s+");
		if ( queryArray.length == 0 ) {
			throw new Exception("Query is empty!");
		}
		ParsedQuery result = new ParsedQuery();
		for( int i=0; i<queryArray.length; i++ ) {
			result.addIntersectionClass( queryArray[i] );
		}
		return result;
	}

}
