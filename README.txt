Hoop: a text mining and language exploration workbench in Java. 
Primarily designed for event extraction and text-based event
forecasting, the workbench is a generic text and language
machine learning sandbox that can be adapted to a wide variety
of tasks. Hoop is a collection of modules (hoops) each of which 
can take linguistic input from one, process it and pass it onto
another. Combining those modules or hoops allows you to create
complex analysis systems.

Author: Martin van Velsen <vvelsen@cs.cmu.edu>,<vvelsen@gmail.com> 

Background

When analyzing any kind of textual data you will soon find yourself
in a situation where you're writing the same code over and over
again. Each new project requires a version of string tokenizing,
each new effort has some form of custom text filtering and 
cleaning. You feel as if you have to jump through the same hoops
over and over again. Hence the creation of a modular toolkit 
called hoops in which those building blocks are pre-packaged for
you and can be easily customized.

Goals

Within large complex language analysis systems we often focus on
analyzing our results statistically without examining the correctness
of the individual steps. Even worse, we tend to not look at those
cases that get rejected by parsers or have been mis-classified by 
machine learning classifiers. Hoop attempts to provide a means whereby
each hoop in a transformation process or analysis step can be examined
and interrogated as it is doing its job. All in all Hoop attempts to
provide:

- Inspectability

Most systems allow you to do an after-action review of a completed
pipeline (a CPE in UIMA terms). This makes it very difficult to inspect
what data was produced in each step (CASes) and what data was discarded.
Hoop integrates an inspection system which can be activated at any
time during or after the running of a Hoop sequence. By clicking on
the magnifying glass in a selected Hoop panel you can see the data that
was created in that step and you can also inspect what data was 
discarded.

- Explainability

In a complex system that runs on a cluster in which essentially all
the steps happen in a parallel fashion, it can be difficult if not
impossible to understand what exactly is happening to the system and
the data is processes. Hoop aims to provide both visualization tools
to understand how data is managed, manipulated and pushed through
the pipeline, as well as make the results comprehensible through
enhanced text visualizations (e.g. a document wall showing text
highlighting based on likelihood estimates)

- Repeatability

Initially the Hoop code should make it possible to repeat an experiment
hundreds or thousands of times, perhaps in such a way that each time
different permutations are tried of a pipeline. However the ultimate
goal is to create system that can be run indefinitely akin to online
learning but with a strong feedback loop that can integrate previously
discarded data if the system detects faults in previously used assumptions. 

History

Initially written as a set of support code for graduate classes
in Language Technologies and smaller narrative projects, the code 
is slowly growing to encompass a larger text-based data mining framework.

This source set includes code written for other various graduate 
courses and is part of a larger research effort in the field of
interactive narrative (IN). Language Technologies (CMU, LTI 
http://www.lti.cs.cmu.edu/)

Used Packages:

- JDom (included), used as the XML processing and creation workhorse
	http://www.jdom.org/

- Cobra (included), for webpage rendering
	http://lobobrowser.org/cobra.jsp

- JGraph (included), to render the hoop graph and query trees, etc
	http://www.jgraph.com/jgraph.html

- Hadoop (included), for the indexing part in case you're running through a cluster
	http://hadoop.apache.org/
	
- Apache Xerces (included), used to populate certain swing controls from 
	xml, for example the INHoopXMLTreeView. This package is included in the lib
	directory but can be separately downloaded and linked to externally if so 
	desired.	
	http://xerces.apache.org/mirrors.cgi
	
- MySQL jdbc driver (not included), provided as a means to load data directly
	from a database. Please download this package separately and add the jar to 
	your classpath.
	http://dev.mysql.com/downloads/connector/j/	
	
- BerkeleyDB driver (included), mainly integrated to function as a rapid indexing
	and retieval backend that lives alongside an XML document tree
	http://www.oracle.com/technetwork/products/berkeleydb/overview/index.html	
	
- Stanford NLP (included), mostly provided in hoop form for the purpose of 
	tokenizing, parsing and low-level processing of text data
	http://nlp.stanford.edu/software/	
	
- UIMA (included, integrated), used for pipeline and cluster management as well as
	high-level data type specifications
	http://uima.apache.org/downloads.cgi
	
- frej (integrated), fuzzy pattern recognition based on regular expressions
	http://frej.sourceforge.net/index.html

- JDesktop (integrated)

- cjwizard (integrated), used for the generation of wizards such
	as the application builder
	http://code.google.com/p/cjwizard/
	
- Lucene (integrated), used to optionally create a searchable version of the
	Hoop native document data set
	http://lucene.apache.org/
	
Legend:
	- Included, means that the package is simply made available and is usually
	the form of a jar or set of jars
	- Integrated, means that the package is included but also used in various
	classes and most likely altered or customized
	- Not Included, means that the code links to needed jars but additional
	drivers might be needed to make the code operational at runtime 	

Notice!

At some point the IDE might be migrated to the Eclipse workbench,
although it looks like we don't need to go that elaborate and the
current system is almost done anyway.
	