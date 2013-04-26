package de.homelabs.collection.nestedset;

import java.util.List;

/**
 * Service class for the nested set package. 
 * 
 * @author dirk.mueller (d.mueller@homelabs.de)
 *
 */
public interface INestedSetService {

	/**
	 * returns the full nested set tree with all elements
	 * 
	 * @param entity
	 * @return List&lt;E&gt; 
	 * @throws Exception
	 */
	 <E> List<E> getFullTree(Class<E> entity) throws Exception;
	 
	 /**
	  * return an entity bases on its id
	  * 
	  * @param entity
	  * @param id
	  * @return &lt;E&gt;
	  * @throws Exception
	  */
	 <E> E getEntityById(Class<E> entity, int id) throws Exception;
	 
	 /**
	  * Add an entity to the set. Left and right values get set based
	  * on the parent attribute.  
	  * @param entity
	  * @return
	  * @throws Exception
	  */
	 <E> INestedSetEntity addEntity(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * delete a complete subtree under the entity (inclusive the entity)
	  * @param entity
	  * @throws Exception
	  */
	 <E> void deleteCompleteSubtree(Class<E> entityClass, int id) throws Exception;
	 
	 /**
	  * delete a single element in the set
	  * @param entity
	  * @throws Exception
	  */
	 <E> void deleteSingleElement(Class<E> entityClass, INestedSetEntity entity) throws Exception;
	 
	 /**
	  * returns a complete subtree
	  * @param entityClass
	  * @return List&lt;E&gt;
	  */
	 <E> List<E> getSubTree(Class<E> entityClass) throws Exception;
}
