package de.homelabs.collection.cpl;

import java.util.Iterator;

/**
 * Iterator for the ConcurrentPersistentQueue.
 * 
 * @author Dirk M&uuml;ller (d.mueller@homelabs.de)
 * @version 0.1
 * @param <E> Payload inside the Queue
 * @see {@link IPersistentList}
 * @see {@link ConcurrentPersistentList} 
 * @see {@link ConcurrentPersistentListNode}
 * @see {@link IConcurrentPersistentListDao}
 */

public class ConcurrentPersistentListIterator<E extends IConcurrentPersistentListNode<E>> implements Iterator<E> {

	/* Attributes */
	private E head = null;
	private E cursor = null;
	
	/**
	 * constructor
	 * 
	 * @param list
	 */
	protected ConcurrentPersistentListIterator(E head) {
		this.head = head;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {	
		if (cursor == null){
			//just starting, cursor points to nowhere
			return (head == null) ? false : true;								//false(null) if queue is empty
		} else {
			//queue is not empty
			return (cursor.getNextElement() == null) ? false : true;			//false(null) when tail
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public E next() {
		if (cursor == null){
			//just starting, cursor points to nowhere, use head
			cursor = head; 
		} else {
			//move cursor to next element
			E next = cursor.getNextElement();
			cursor = next;
		}
		//return payload
		return (E) this.cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Sorry but this operation is only available within the ConcurrentPersistenQueue, not over the Iterator!");
	}

}
