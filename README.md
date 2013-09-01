SPARQLFederator
===============

Expand SPARQL queries to perform inference on multiple endpoints.


Compiling:
==========
mvn clean package


Running:
========

mvn exec:java -Dexec.mainClass="br.org.accamargo.cipe.gqe.SPARQLFederator" -Dexec.args="federation_ontology.owl federation_namespace# domain_ontology.owl  domainNamespace# "

