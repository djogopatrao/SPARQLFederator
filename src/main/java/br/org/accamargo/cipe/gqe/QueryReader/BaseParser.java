package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;

public interface BaseParser {

	ParsedQuery parse(String query, OntModel model) throws Exception;

	ParsedQuery parse(String query, OntModel model, String defaultUri) throws Exception;

}
