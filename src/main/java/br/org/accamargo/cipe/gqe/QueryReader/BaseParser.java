package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.List;

public interface BaseParser {

	ParsedQuery parse(String query) throws Exception;

}
