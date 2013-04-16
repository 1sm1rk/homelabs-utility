package de.homelabs.collection.cpl;


/**
 * A concurrent queue where every element in the queue can be persisted into a persistent storage.
 * 
 * @author Dirk M&uuml;ller (d.mueller@homelabs.de)
 * @version 0.1
 * @param <E> Payload inside the Queue
 * @see {@link IPersistentList}
 * @see {@link ConcurrentPersistentList} 
 * @see {@link ConcurrentPersistentListNode}
 * @see {@link IConcurrentPersistentListDao}
 */

public interface IPersistentList<E> {

	/**
	 * Add an element to the end of the queue
	 * 
	 * @param element payload to be queued
	 * @return boolean 
	 * @throws Exception encapsuls occuring exception
	 */
	boolean add(E element) throws Exception;
	
	/**
	 * Add an element at a specific position to the queue
	 * 
	 * @param element payload to be queued
	 * @return boolean 
	 * @throws Exception encapsuls occuring exception
	 */
	//boolean addAt(long key) throws Exception;
	
	/**
	 * Add all elements to the end of the Queue
	 * 
	 * @param element payload to be queued
	 * @return boolean 
	 * @throws Exception encapsuls occuring exception
	 */
	//boolean addAll(Collection<E> elements) throws Exception;
	
	/**
	 * Remove all elements from the queueu
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	boolean clearQueue() throws Exception;
	
	/**
	 * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this object 
	 * is less than, equal to, or greater than the specified object.
	 * 
	 * @param element payload
	 * @return int
	 */
	int compareTo(E element);
	
	/**
	 * Returns true if this queue contains the specified element.
	 * @param element payload
	 * @return boolean
	 */
	boolean contains(E element);
	
	/**
	 * Returns true if this queue contains all the specified elements.
	 * @param List<E> payloads to check
	 * @return boolean
	 */
	//boolean containsAll(List<E> elements);
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	//E getAt(long key) throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	E getHead() throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	E getTail() throws Exception;
	
	/**
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	//E getSuccessor(E element) throws Exception;
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	//E getSuccessorAt(long key) throws Exception;
	
	/**
	 * Returns the class of the payload Element. Queue must at least contain one Elements, otherwise null.
	 * 
	 * @return Class&lt;E&gt; payload Class
	 */
	Class<?> getPayLoadClass();
	
	/**
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	//E getPredecessor(E element) throws Exception;
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	//E getPredecessorAt(long key) throws Exception;
	
	/**
	 * returns true if queue is empty
	 * @return boolean
	 */
	boolean isEmpty();
	
    /**
     * returns the key of the element
     * @param element payload
     * @return long
     */
	long keyOf(E element);
	
	/**
	 * Remove an element from the Queue
	 * 
	 * @param element payload to be removed
	 * @return boolean
	 * @throws Exception
	 */
	boolean remove(E element) throws Exception;
	
	/**
	 * returns true if elements with key has been removed
	 * @param key the key of the element to remove
	 * @return boolean
	 * @throws Exception
	 */
	//boolean removeAt(long key) throws Exception;
	
	/**
	 * remove a collection from queue
	 * @param elements
	 * @return
	 * @throws Exception
	 */
	//boolean removeAll(Collection<E> elements) throws Exception;	
	
	/**
	 * Number of Elements in Queue
	 * @return int 
	 */
	int size(); 
	
	/**
	 * initalize the list and force load from databse. exception could occur so extracted from constructor 
	 * @throws Exception
	 */
	void initialize() throws Exception;
	
	/**
	 * load last state (listelements) from database and rebuild list, so we start where we left
	 * @throws Exception
	 */
	void loadLastState() throws Exception; 
	 
	/**
	 * to override the order of the listelements on loadLastState
	 * @param orderOnInitialize
	 */
	void setOrderOnInitialize(String orderOnInitialize);
	
	/**
	 * used by loadLastState to get the listelements in the right order. 
	 * default: nextelement
	 * @see IConcurrentPersistentListNode#getNextElement()
	 */
	String getOrderOnInitialize();
}
