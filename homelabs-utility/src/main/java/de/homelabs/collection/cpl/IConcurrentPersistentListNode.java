package de.homelabs.collection.cpl;

import java.io.Serializable;

/**
 * The Structur for a ConcurrentPersistendNode
 * 
 * @author D.Mueller
 * @version 0.1
 * @param E Node Type
 * @see {@link IPersistentList}
 * @see {@link ConcurrentPersistentList} 
 * @see {@link ConcurrentPersistentListNode}
 * @see {@link IConcurrentPersistentListDao}
 */
public interface IConcurrentPersistentListNode<E> extends Serializable {

	/**
	 * return the reference to the next element in the list
	 * @return E
	 */
	E getNextElement();
	
	/**
	 * set the reference to the next element 
	 * @param element
	 */
	void setNextElement(E element);
}
