package edu.cmu.lti.oaqa.ecd.util;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public final class CasUtils extends HoopRoot 
{
	/**
	 * 
	 */
	private CasUtils() 
	{
		setClassName ("CasUtils");
		debug ("CasUtils ()");  
	}
	/**
	 * 
	 * @param jcas
	 * @param typeName
	 * @return
	 */
	public static Annotation getFirst(JCas jcas, String typeName) 
	{
		TypeSystem ts = jcas.getTypeSystem();
		Type type = ts.getType(typeName);
		AnnotationIndex<Annotation> index = jcas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) 
		{
			return (Annotation) iterator.next();
		}
		
		return null;
	}
	/**
	 * 
	 * @param jcas
	 * @param type
	 * @return
	 */
	public static Annotation getFirst(JCas jcas, Type type) 
	{
		AnnotationIndex<Annotation> index = jcas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		
		if (iterator.hasNext())
			return (Annotation) iterator.next();
		
		return null;
	}
	/**
	 * 
	 * @param jcas
	 * @param typeName
	 * @return
	 */
	public static Type getType(JCas jcas, String typeName) 
	{
		TypeSystem ts = jcas.getTypeSystem();
		return ts.getType(typeName);
	}
}
