/* Generated By:JavaCC: Do not edit this line. BIFv01Constants.java */
/* This parser uses the data structures in the JavaBayes core *
 * engine (package BayesianNetworks); other implementations   *
 * may use different data structures                          */
package edu.cmu.cs.in.ml.bayesiannet.Parsers.BIFv01;

public interface BIFv01Constants {

  int EOF = 0;
  int NETWORK = 8;
  int VARIABLE = 9;
  int PROBABILITY = 10;
  int PROPERTY = 11;
  int VARIABLETYPE = 12;
  int DISCRETE = 13;
  int DEFAULTVALUE = 14;
  int TABLEVALUES = 15;
  int PROPERTYSTRING = 16;
  int WORD = 17;
  int LETTER = 18;
  int DIGIT = 19;
  int NUMBER = 20;
  int EXPONENT = 21;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<token of kind 5>",
    "<token of kind 6>",
    "\",\"",
    "\"network\"",
    "\"variable\"",
    "\"probability\"",
    "\"property\"",
    "\"type\"",
    "\"discrete\"",
    "\"default\"",
    "\"table\"",
    "<PROPERTYSTRING>",
    "<WORD>",
    "<LETTER>",
    "<DIGIT>",
    "<NUMBER>",
    "<EXPONENT>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\"(\"",
    "\")\"",
    "\"|\"",
  };

}
