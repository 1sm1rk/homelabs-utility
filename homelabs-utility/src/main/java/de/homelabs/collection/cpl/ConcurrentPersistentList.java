package de.homelabs.collection.cpl;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.homelabs.collection.CollectionTools;

/**
 * A concurrent list where every element in the queue can be persisted into a
 * persistent storage.
 * 
 * @author Dirk M&uuml;ller (d.mueller@homelabs.de)
 * @version 0.1
 * @param <E>
 *            Payload inside the Queue
 * @see {@link IPersistentList}
 * @see {@link ConcurrentPersistentList}
 * @see {@link ConcurrentPersistentListNode}
 * @see {@link IConcurrentPersistentListDao}
 */

public class ConcurrentPersistentList<E extends IConcurrentPersistentListNode<E>>
		implements IPersistentList<E>, Iterable<E> {

	/* Attributes */
	private E head = null;
	private E tail = null;
	private IConcurrentPersistentListDao<E> persistentQueueDao = null;
	private int count = 0;
	transient final ReentrantLock lock = new ReentrantLock();
	Logger log = LoggerFactory.getLogger(getClass());
	private Class<E> elementClass;
	private boolean lastStateLoaded = false;
	private String orderOnInitialize = "nextelement";

	public ConcurrentPersistentList(
			IConcurrentPersistentListDao<E> persistentQueueDao,
			Class<E> elementClass) throws Exception {
		super();
		this.persistentQueueDao = persistentQueueDao;
		this.elementClass = elementClass;
	}

	
	/*
	 * 
	 */
	public void initialize() throws Exception {
		loadLastState();
	}

	/**
	 * load all elements from db and add to list.
	 * 
	 * @throws Exception
	 */
	public void loadLastState() throws Exception {
		if (!lastStateLoaded) {
			// load last state from db
			List<E> savedElements = CollectionTools.convertToCheckedList(
					this.elementClass, persistentQueueDao.loadAllElements(
							this.elementClass, getOrderOnInitialize()));
			for (E e : savedElements) {
				// add to list
				add(e, false);
			}
			lastStateLoaded = true;
		}
	}

	public boolean add(E element) throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			return add(element, true);
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#add(java.lang.Object)
	 */
	public boolean add(E element, boolean persist) throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// persist flag
			if (persist) {
				persistentQueueDao.addElement(element);
			}
			if (head == null) {
				// first element
				head = element;
				tail = element;
			} else {
				tail.setNextElement(element);
				// update reference in db
				if (persist) {
					persistentQueueDao.updateElement(tail);
				}

				// bend
				tail = element;

			}

			count++;
			return true;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#clearQueue()
	 */
	@Override
	public boolean clearQueue() throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			if (!isEmpty()) {

				// from head to tail
				E successor = null;

				while (head != null) {
					// remove from storage
					// persistentQueueDao.removeElement(head);
					// get child
					successor = head.getNextElement();
					// remove from db
					persistentQueueDao.removeElement(head);
					// remove reference to next element
					head.setNextElement(null);
					// bend head, clear reference
					head = successor;
				}
				return true;
			}

			// queue is already empty
			return true;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(E element) {
		throw new UnsupportedOperationException("not supported yet, sorry");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(E element) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			if (!isEmpty()) {

				// begin with head element
				E cursor = head;

				while (cursor != null) {
					// test current element
					if (cursor.equals(element)) {
						// element found
						return true;
					} else {
						// next element
						cursor = cursor.getNextElement();
					}
				}

				// nothing found
				return false;
			} else {
				// queue is empty
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			return new ConcurrentPersistentListIterator<E>(head);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#getHead()
	 */
	@Override
	public E getHead() throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			return (E) head;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#getPayLoadClass()
	 */
	public Class<?> getPayLoadClass() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return elementClass;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#getTail()
	 */
	@Override
	public E getTail() throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			return (E) tail;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			if (head == null)
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#keyOf(java.lang.Object)
	 */
	@Override
	public long keyOf(E element) {
		throw new UnsupportedOperationException("not supported yet, sorry");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(E element) throws Exception {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			// only of queue is not empty and element is in queue
			if (!isEmpty()) {

				E cursor = head;
				E predecessor = null;

				while (cursor != null) {
					if (cursor.equals(element)) {
						// remove from storage
						// persistentQueueDao.removeElement(head);

						// remove from db
						persistentQueueDao.removeElement(element);

						// bend predecessor and succcessor
						if (predecessor == null) {
							// head
							head = cursor.getNextElement();
						} else {
							// bend
							predecessor.setNextElement(cursor.getNextElement());
							// update node
							persistentQueueDao.updateElement(predecessor);
						}
						// remove successor link
						cursor.setNextElement(null);
						count--;
						return true;
					} else {
						predecessor = cursor;
						cursor = cursor.getNextElement();
					}
				}
				// not found
				return false;
			} else
				return false;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#size()
	 */
	@Override
	public int size() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			// last state should be loaded
			if (!lastStateLoaded)
				initialize();

			return count;
		} catch (Exception e) {
			log.error(e.getMessage());
			return -1;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#getOrderInitialize()
	 */
	public String getOrderOnInitialize() {
		return orderOnInitialize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.sag.threadtest.IPersistentQueue#setOrderInitialize()
	 */
	public void setOrderOnInitialize(String orderOnInitialize) {
		this.orderOnInitialize = orderOnInitialize;
	}
}
