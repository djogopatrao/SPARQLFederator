package br.org.accamargo.cipe.gqe.QueryReader;

public class ParserFactory {
	
	public static BaseParser create( String query_type ) throws Exception {
		if ( query_type.equals("simple") ) {
			return new SimpleParser();
		}
		if ( query_type.equals("dl") ) {
			return new DlParser();
		}
		throw new Exception("Query type '"+query_type+"' is not supported");
	}

}
