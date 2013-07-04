package de.homelabs.collection.nestedset;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("nestetSetService")
@Transactional
public class NesteSetService implements INestedSetService {
	// SLF4J Logger
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	INestedSetDao nestedSetDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.service.INestedSetService#getFullTree(java
	 * .lang.Class)
	 */
	public <E> List<E> getFullTree(Class<E> entity) throws Exception {
		logger.debug("get full tree for {} " + entity);
		return nestedSetDao.getFullTree(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.service.INestedSetService#addEntity(de.
	 * homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> INestedSetEntity addEntity(Class<E> entityClass, INestedSetEntity entity) throws Exception {
		logger.debug("add entity '{}' : {}  ", entity.getClass(), entity);

		// get parent element
		// first check for root
		if (entity.getParent() != 0) {
			INestedSetEntity parent = nestedSetDao.getEntityById(
					entity.getClass(), entity.getParent());

			// set lft and rgt of parent to child
			entity.setLft(parent.getRgt());
			entity.setRgt(parent.getRgt() + 1);

			// free space in tree and add entity
			nestedSetDao.prepareInsertRgt(entityClass, parent);
			nestedSetDao.prepareInsertlft(entityClass, parent);
		} else {
			// root element
			entity.setLft(1);
			entity.setRgt(2);
		}

		return nestedSetDao.insertEntity(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.service.INestedSetService#deleteCompleteSubtree
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> void deleteCompleteSubtree(Class<E> entityClass, int id) throws Exception {
		logger.debug("delete complete subtree: '{}' : {}  ", entityClass,id);

		//
		INestedSetEntity entity = (INestedSetEntity) getEntityById(entityClass, id);
		
		// delete subtree
		nestedSetDao.deleteSubtree(entityClass, entity);

		// adjust left and right values
		nestedSetDao.updateAfterDeleteSubtreeLft(entityClass, entity);
		nestedSetDao.updateAfterDeleteSubtreeRgt(entityClass, entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.service.INestedSetService#getEntityById
	 * (java.lang.Class, int)
	 */
	public <E> E getEntityById(Class<E> entity, int id) throws Exception {
		logger.debug("get entity by id {} : {}", entity, id);
		return nestedSetDao.getEntityById(entity, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.homelabs.utility.nestedset.service.INestedSetService#deleteSingleElement
	 * (de.homelabs.utility.nestedset.INestedSetEntity)
	 */
	public <E> void deleteSingleElement(Class<E> entityClass, INestedSetEntity entity) throws Exception {
		logger.debug("delete single element: '{}' : {}  ", entity.getClass(),
				entity);

		//delete element
		nestedSetDao.deleteSingleElement(entity);
		//adjust elements in between
		nestedSetDao.updateAfterDeleteSingleElement(entityClass, entity);
		
		//adjust left and right values
		nestedSetDao.updateAfterDeleteSingleElementLft(entityClass, entity);
		nestedSetDao.updateAfterDeleteSingleElementRgt(entityClass, entity); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.homelabs.utility.nestedset.INestedSetService#getSubTree(java.lang.Class)
	 */
	public <E> List<E> getSubTree(Class<E> entityClass, int id) throws Exception {
		return nestedSetDao.getSubTree(entityClass, id);
	}
}
