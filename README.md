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

java -jar target/SPARQLFederator-1.1-SNAPSHOT-jar-with-dependencies.jar -domain_ns 'http://www.cipe.accamargo.org.br/ontologias/domain.owl#' -domain_ontology examples/domain.owl -federation_ontology examples/federation.owl  -exec print A

"-exec print" will print the expanded query; "-exec run" would execute it and yield results (that is, if there are working endpoints as defined on example/federation.owl)

"A" is the class name (assume it is concatenated with domain_ns) you're querying for; try it with other classes (like B, or C). Play with domain.owl, but keep in mind SPARQLFederator implemented semantics (see the wiki).


Arguments
=========

It is mandatory specify at least the domain namespace, domain ontology file, federation ontology file, also one or more classes for querying.

usage: SPARQLFederator [options] <DOMAIN_CLASS> [...]

 -domain_ns <arg>             The domain namespace
 
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


