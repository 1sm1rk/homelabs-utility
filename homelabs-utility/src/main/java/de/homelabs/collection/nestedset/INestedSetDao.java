package de.homelabs.collection.nestedset;

import java.util.List;

/**
 * 
 * @author dirk.mueller (d.mueller@homelabs.de)
 *
 */
public interface INestedSetDao {

	/**
	 * return the full nested set 
	 * @param entity
	 * @return List&lt;E&gt;
	 * @throws Exception
	 */
	 <E> List<E> getFullTree(Class<E> entity) throws Exception;
	 
	 /**
	  * return a single element
	  * @param entity
	  * @param id
	  * @return E
	  * @throws Exception
	  */
	 <E> E getEntityById(Class<E> entity, int id) throws Exception;
	 
	 /**
	  * insert a single element
	  * @param entity
	  * @return {@link INestedSetEntity}
	  * @throws Exception
	  */
	 INestedSetEntity insertEntity(INestedSetEntity entity) throws Exception;
	 
	 /**
	  * delete a subtree within the entity
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int deleteSubtree(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * delete a single elements
	  * @param entity
	  * @throws Exception
	  */
	 void deleteSingleElement(INestedSetEntity entity) throws Exception;
	 
	 /*
	  * all these functions the insert and delete function and should only be called from the service
	  */
	 
	 /**
	  * adjust right values before adding an entity
	  * @param entity
	  * @return in
	  * @throws Exception
	  */
	 <E> int prepareInsertRgt(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * adjust left values before adding an entity
	  * @param entity
	  * @return int
	  * @throws Exception
	  */
	 <E> int prepareInsertlft(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * update tree after deleting a single entity
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int updateAfterDeleteSingleElement(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * update right values after deleting a subtree
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int updateAfterDeleteSubtreeRgt(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * update left values after deleting a subtree
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int updateAfterDeleteSubtreeLft(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * update left values after deleting a single entity
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int updateAfterDeleteSingleElementLft(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * update right values after deleting a single entity
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> int updateAfterDeleteSingleElementRgt(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * returns a complete subtree
	  * @param entityClass
	  * @return List&lt;E&gt;
	  * @throws Exception
	  */
	 <E> List<E> getSubTree(Class<E> entityClass) throws Exception;
}