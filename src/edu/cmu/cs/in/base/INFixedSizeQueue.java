
/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.base;

import java.util.LinkedList;

/**
*
*/
public class INFixedSizeQueue<E> extends LinkedList<E> 
{	
	private static final long serialVersionUID = 1L;
	private int limit;

	/**
	 *
	 */	
    public INFixedSizeQueue(int limit) 
    {
        this.limit = limit;
    }
	/**
	 *
	 */
    @Override
    public boolean add(E o) 
    {
        super.add(o);
        while (size() > limit) 
        { 
        	super.remove(); 
        }
        return true;
    }
}

