/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.base.kv;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FeatureValuePath;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.cas.SofaID;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.admin.CASAdminException;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.LowLevelCAS;
import org.apache.uima.cas.impl.LowLevelIndexRepository;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FloatArray;
import org.apache.uima.jcas.cas.IntegerArray;
import org.apache.uima.jcas.cas.Sofa;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.cs.in.base.HoopDataType;

/**
 * Perhaps the most important data structure in the Hoop system is the
 * document class. Since it is derived from the Hoop KV Class we should
 * think about it as a KV object but with specialized methods and fixed
 * known member variables, such as rank and score. You can freely use 
 * this class as a KV Class and even as an entry in a KV List if so desired.
 * 
 * Our document representation class is directly derived from the Hoop KV 
 * abstract class. For now this should suffice but we might have to derive 
 * from the KV Table instead to allow for better data modeling.
 * 
 * 
 */
public class HoopUIMAKVDocument extends HoopKVDocument implements HoopKVInterface, Serializable, AbstractCas, JCas
{    									
	private static final long serialVersionUID = -6481333217856570903L;

	/**
	 *
	 */
    public HoopUIMAKVDocument ()
    {
    	setDataType (HoopDataType.CLASS);
    	
    }

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFsToIndexes(FeatureStructure arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkArrayBounds(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FeaturePath createFeaturePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FeatureValuePath createFeatureValuePath(String arg0)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends FeatureStructure> FSIterator<T> createFilteredIterator(
			FSIterator<T> arg0, FSMatchConstraint arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public SofaFS createSofa(SofaID arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCas createView(String arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends FeatureStructure> ListIterator<T> fs2listIterator(
			FSIterator<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationIndex<Annotation> getAnnotationIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationIndex<Annotation> getAnnotationIndex(Type arg0)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationIndex<Annotation> getAnnotationIndex(int arg0)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CAS getCas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CASImpl getCasImpl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getCasType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConstraintFactory getConstraintFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TOP getDocumentAnnotationFs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSArray getFSArray0L() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSIndexRepository getFSIndexRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FloatArray getFloatArray0L() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSIndexRepository getIndexRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerArray getIntegerArray0L() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCas getJCas(Sofa arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JFSIndexRepository getJFSIndexRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TOP getJfsFromCaddr(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LowLevelCAS getLowLevelCas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LowLevelIndexRepository getLowLevelIndexRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Feature getRequiredFeature(Type arg0, String arg1)
			throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Feature getRequiredFeatureDE(Type arg0, String arg1, String arg2,
			boolean arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getRequiredType(String arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sofa getSofa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public Sofa getSofa(SofaID arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FeatureStructure getSofaDataArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getSofaDataStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSofaDataString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSofaDataURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSIterator<SofaFS> getSofaIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSofaMimeType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringArray getStringArray0L() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TOP_Type getType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public TOP_Type getType(TOP arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeSystem getTypeSystem() throws CASRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCas getView(SofaFS arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<JCas> getViewIterator() throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<JCas> getViewIterator(String arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getViewName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public void processInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putJfsFromCaddr(int arg0, FeatureStructure arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFsFromIndexes(FeatureStructure arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() throws CASAdminException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLanguage(String arg0) throws CASRuntimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentText(String arg0) throws CASRuntimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSofaDataArray(FeatureStructure arg0, String arg1)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSofaDataString(String arg0, String arg1)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSofaDataURI(String arg0, String arg1)
			throws CASRuntimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void throwFeatMissing(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JCas getView(String arg0) throws CASException {
		// TODO Auto-generated method stub
		return null;
	}		
}
