#!/bin/bash
java -jar ../target/SPARQLFederator-1.1-SNAPSHOT-jar-with-dependencies.jar -domain_ns 'http://www.cipe.accamargo.org.br/ontologias/domain.owl#' -domain_ontology domain.owl -federation_ontology federation.owl  -exec print A
