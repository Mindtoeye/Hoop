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

History

Initially written as a set of support code for graduate classes
in Language Technologies and smaller narrative projects, the code 
is slowly growing to encompass a larger text-based data mining framework.

This source set includes code written for other various graduate 
courses and is part of a larger research effort in the field of
interactive narrative (IN). Language Technologies (CMU, LTI 
http://www.lti.cs.cmu.edu/) Courses that have contribute to this 
source base are:
 
05-834 Applied Machine Learning
11-719 Computational Models of Discourse Analysis
11-741 Information Retrieval

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
	directory but can be separately downloaded and linked to externally	if so desired.	
	http://xerces.apache.org/mirrors.cgi

Notice!

At some point the IDE might be migrated to the Eclipse workbench,
although it looks like we don't need to go that elaborate and the
current system is almost done anyway.
	