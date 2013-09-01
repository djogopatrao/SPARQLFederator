SPARQLFederator
===============

Expand SPARQL queries to perform inference on multiple endpoints.


Compiling:
==========
mvn clean package


Running:
========

mvn exec:java -Dexec.mainClass="br.org.accamargo.cipe.gqe.SPARQLFederator" -Dexec.args="file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/ontocloud2full_ontop.owl http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl# file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/20130713_estudo_clinico.owl  http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl# "

