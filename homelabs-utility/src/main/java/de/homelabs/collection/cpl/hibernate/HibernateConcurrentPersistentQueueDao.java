package de.homelabs.collection.cpl.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.homelabs.collection.CollectionTools;
import de.homelabs.collection.cpl.IConcurrentPersistentListDao;

public class HibernateConcurrentPersistentQueueDao<E> implements
		IConcurrentPersistentListDao<E> {

	SessionFactory sessionFactory;
	Logger log = LoggerFactory.getLogger(getClass());
	
	public boolean addElement(E element)
			throws Exception {
		Transaction ta = null;
		boolean result = false;
		try {
			ta = sessionFactory.getCurrentSession().beginTransaction();
			if (sessionFactory.getCurrentSession().save(element) != null){
				result = true;
			}
			ta.commit();
			return result;
		} catch (RuntimeException re){
			log.error("Save failed, rollback : ", re);
			try {
				if (ta != null) ta.rollback();
			} catch (HibernateException he){
				log.error("Transaction rollback failed: ", he);
			}
			throw re;
		}
	}

	public boolean removeElement(E element)
			throws Exception {
		Transaction ta = null;
		try {
			ta = sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().delete(element);
			ta.commit();
			return true;
		} catch (RuntimeException re){
			log.error("Delete failed, rollback : ", re);
			try {
				if (ta != null) ta.rollback();
			} catch (HibernateException he){
				log.error("Transaction rollback failed: ", he);
			}
			throw re;
		}
	}

	public List<E> loadAllElements(Class<E> elementClass, String order) throws Exception {
		Transaction ta = null;
		List<E> elements = null;
		try {
			ta = sessionFactory.getCurrentSession().beginTransaction();
			elements = CollectionTools.convertToCheckedList(elementClass, sessionFactory.getCurrentSession().createQuery("from "+elementClass.getSimpleName() + " order by "+order).list());
			ta.commit();
			return elements;
		} catch (RuntimeException re){
			log.error("Loading elements failed, rollback : ", re);
			try {
				if (ta != null) ta.rollback();
			} catch (HibernateException he){
				log.error("Transaction rollback failed: ", he);
			}
			throw re;
		}
	}

	public boolean updateElement(E element){
		Transaction ta = null;
		try {
			ta = sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().update(element);
			ta.commit();
			return true;
		} catch (RuntimeException re){
			log.error("Delete failed, rollback : ", re);
			try {
				if (ta != null) ta.rollback();
			} catch (HibernateException he){
				log.error("Transaction rollback failed: ", he);
			}
			throw re;
		}
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
