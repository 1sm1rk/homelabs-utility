package de.homelabs.collection.nestedset;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.homelabs.collection.CollectionTools;

/**
 * 
 * @author D.Mueller
 * 
 */
@Repository("nestedSetDao")
public class NestedSetHibernateDao implements INestedSetDao {

	// SLF4J Logger
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#getFullTree(java.lang.Class)
	 */
	public <E> List<E> getFullTree(Class<E> entity) {		
		return CollectionTools.convertToCheckedList(entity, sessionFactory
				.getCurrentSession().createQuery("from "+entity.getSimpleName()+" order by lft")
				.list());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#getEntityById(java.lang.Class
	 * , int)
	 */
	public <E> E getEntityById(Class<E> entity, int id) throws Exception {
		return CollectionTools.checkedCast(entity, sessionFactory
				.getCurrentSession().load(entity, id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#prepareInsertRgt(de.homelabs
	 * .utility.nestedset.INestedSetEntity)
	 */
	public <E> int prepareInsertRgt(Class<E> entityClass, INestedSetEntity entity) throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" set rgt=rgt+2 where rgt >= :entityRgt")
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#prepareInsertlft(de.homelabs
	 * .utility.nestedset.INestedSetEntity)
	 */
	public <E> int prepareInsertlft(Class<E> entityClass, INestedSetEntity entity) throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" set lft=lft+2 where lft > :entityRgt")
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#insertEntity(de.homelabs.
	 * utility.nestedset.INestedSetEntity)
	 */
	public INestedSetEntity insertEntity(INestedSetEntity entity)
			throws Exception {
		sessionFactory.getCurrentSession().save(entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#deleteSubtree(de.homelabs
	 * .utility.nestedset.INestedSetEntity)
	 */
	public <E> int deleteSubtree(Class<E> entityClass, INestedSetEntity entity) throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"delete from "+entityClass.getSimpleName()+" WHERE lft BETWEEN :entityLft and :entityRgt")
				.setInteger("entityLft", entity.getLft())
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#deleteSingleElement(de.homelabs
	 * .utility.nestedset.INestedSetEntity)
	 */
	public void deleteSingleElement(INestedSetEntity entity) throws Exception {
		sessionFactory.getCurrentSession().delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#updateAfterDeleteSingleElement
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> int updateAfterDeleteSingleElement(Class<E> entityClass, INestedSetEntity entity)
			throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" SET lft=lft-1, rgt=rgt-1 WHERE lft BETWEEN :entityLft and :entityRgt")
				.setInteger("entityLft", ((INestedSetEntity)entity).getLft())
				.setInteger("entityRgt", ((INestedSetEntity)entity).getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#updateAfterDeleteSubtreeRgt
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> int updateAfterDeleteSubtreeRgt(Class<E> entityClass, INestedSetEntity entity)
			throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" SET rgt=rgt-ROUND((:entityRgt - :entityLft + 1)) where rgt > :entityRgt")
				.setInteger("entityLft", entity.getLft())
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#updateAfterDeleteSubtreeLft
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> int updateAfterDeleteSubtreeLft(Class<E> entityClass, INestedSetEntity entity)
			throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" SET lft=lft-ROUND((:entityRgt - :entityLft + 1)) where lft > :entityRgt")
				.setInteger("entityLft", entity.getLft())
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#updateAfterDeleteSingleElementLft
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> int updateAfterDeleteSingleElementLft(Class<E> entityClass, INestedSetEntity entity)
			throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" set lft=lft-2 where lft > :entityRgt")
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.INestedSetDao#updateAfterDeleteSingleElementRgt
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> int updateAfterDeleteSingleElementRgt(Class<E> entityClass, INestedSetEntity entity)
			throws Exception {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"update "+entityClass.getSimpleName()+" SET rgt=rgt-2 WHERE rgt> :entityRgt")
				.setInteger("entityRgt", entity.getRgt()).executeUpdate();
	}
	
	public <E> List<E> getSubTree(Class<E> entityClass) throws Exception {
			return CollectionTools.convertToCheckedList(entityClass, sessionFactory
					.getCurrentSession()
					.createQuery(
							"from "+entityClass.getSimpleName()+" n "
							+"left join "+entityClass.getSimpleName()+" c on c.id = 18 "
							+"where "
							+"( "
							+"	(n.lft <= c.lft and n.rgt >=c.rgt) or (n.lft >=c.lft and n.rgt <= c.rgt) "
							+") or "
							+"( "
							+" n.parent in (select parent from "+entityClass.getSimpleName()+" o where ((o.lft <= c.lft and o.rgt >=c.rgt) or (o.lft >=c.lft and o.rgt <= c.rgt))) "
							+") "
							+"order by n.lft ").list());
	}
}
