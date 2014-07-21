SPARQLFederator
===============

Expand SPARQL queries to perform inference on multiple endpoints.


Main documentation
==================

Find more information on our wiki page:

https://github.com/djogopatrao/SPARQLFederator/wiki

Example
=======

You can try our live demo (see wiki page) or use the example contained here

java -jar target/SPARQLFederator-1.1-SNAPSHOT-jar-with-dependencies.jar -domain_ontology examples/domain.owl -federation_ontology examples/federation.owl  -exec print 'http://www.cipe.accamargo.org.br/ontologias/domain.owl#A'

"-exec print" will print the expanded query; "-exec run" would execute it and yield results (that is, if there are working endpoints as defined on example/federation.owl)

"http://www.cipe.accamargo.org.br/ontologias/domain.owl#A" is the full IRI of the class you're querying for (see -domain_ns for saving space); try it with other classes (like B, or C). Add axioms and your own classes to domain.owl, but keep in mind SPARQLFederator implemented semantics (see the wiki).


Arguments
=========

It is mandatory to specify at least the domain ontology file, the federation ontology file, and one or more classes for querying.

usage: SPARQLFederator [options] <DOMAIN_CLASS> [...]

 -domain_ns <arg>             The domain namespace (if specified, will be appended before each of the queryied DOMAIN_CLASSes)
 
 -domain_ontology <arg>       The domain ontology file
 
 -federation_ontology <arg>   The federation ontology file
 
 -help                        Shows the help message
 
 -ontocloud_ns <arg>          The federation namespace (default value: http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl)
 
 -optimizer <arg>             Execute a query optimizer: 'simple' (default) or 'none'
 
 -planner <arg>               Execute a query planner: 'simple' (default) or 'none'
 
 -query_type <arg>            The accepted query type: 'simple' (default) or 'sparql' (not implemented)
 
 -stats                       Display statistics for queries


DEBUG compiling and running:
===========================

mvn clean package 

mvn exec:java -Dexec.mainClass="br.org.accamargo.cipe.gqe.SPARQLFederatorRun" -Dexec.args="-federation_ontology federation_ontology.owl -domain_ontology domain_ontology.owl  -domain_ns domainNamespace# class1 [,classn]"


Production Compiling
====================

mvn clean compile assembly:single


