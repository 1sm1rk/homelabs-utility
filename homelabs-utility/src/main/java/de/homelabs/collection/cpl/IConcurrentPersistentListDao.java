package de.homelabs.collection.cpl;

import java.util.List;

/**
 * Interface for Dao classes
 * 
 * @author Dirk M&uuml;ller (d.mueller@homelabs.de)
 * @version 0.1
 * @param <E> Payload inside the Queue
 * @see {@link IPersistentList}
 * @see {@link ConcurrentPersistentList} 
 * @see {@link ConcurrentPersistentListNode}
 * @see {@link IConcurrentPersistentListDao}
 */

public interface IConcurrentPersistentListDao<E> {

	/**
	 * Persist an element from the queue (not only the payload) to an persistent storage.
	 * 
	 * @param element 
	 * @return boolean
	 * @throws Exception
	 * @see {@link ConcurrentPersistentListNode}
	 */
	boolean addElement(E element) throws Exception;
	
	/**
	 * Remove element from from persistent storage
	 * 
	 * @param element
	 * @return boolean
	 * @throws Exception
	 * @see {@link ConcurrentPersistentListNode}
	 */
	boolean removeElement(E element) throws Exception;
	
	/**
	 * Load all elements from perstistent storage 
	 * 
	 * @return List&lt;ConcurrentPersistentQueueNode&lt;E&gt;&gt;
	 * @throws Exception
	 * @see {@link ConcurrentPersistentListNode} 
	 */
	List<E> loadAllElements(Class<E> elementClass, String order) throws Exception;
	
	/**
	 * update an element
	 * 
	 * @param element
	 * @return boolean
	 * @throws Exception
	 */
	boolean updateElement(E element) throws Exception;
}
