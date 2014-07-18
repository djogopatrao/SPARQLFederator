SPARQLFederator
===============

Expand SPARQL queries to perform inference on multiple endpoints.


Main documentation
==================

Find more information on our wiki page:

https://github.com/djogopatrao/SPARQLFederator/wiki


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


mvn exec:java -Dexec.mainClass="br.org.accamargo.cipe.gqe.SPARQLFederator" -Dexec.args="-federation_ontology federation_ontology.owl -domain_ontology domain_ontology.owl  -domain_ns domainNamespace# class1 [,classn]"

- SPARQLFederator class will create a federated query.
- SPARQLFederatorRun class will create the query and run it.


(sort of) PRODUCTION compiling and running( jar with all dependencies included)
====================

mvn clean compile assembly:single

java -jar jar-with-dependencies.jar -federation_ontology federation_ontology.owl -domain_ontology domain_ontology.owl  -domain_ns domainNamespace# class1 [...]
