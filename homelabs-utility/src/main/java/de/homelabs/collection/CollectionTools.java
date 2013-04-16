package de.homelabs.collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionTools {
	
	/**
	 * Converts a given untyped list a typed objectt list
	 * 
	 * @param expectedClass 
	 * @param objectList
	 * @return List<expectedClass>
	 * @throws ClassCastException
	 */
	public static <E> List<E> convertToCheckedList(Class<E> expectedClass, Object objectList) throws ClassCastException{
		//result List with checked elements
		List<E> checkedList = new ArrayList<E>();
		
		//provide conversation only for lists
		if (objectList instanceof List){
			//lets have a closer look at our list elements
			Object[] listElements = ((List<?>) objectList).toArray();
			
			//then check each element
			for (Object element : listElements){
				//ensure that the element fits to expected class
				if (expectedClass.isInstance(element)){
					//then add it to our list
					checkedList.add(expectedClass.cast(element));
				} else {
					//can not convert element to expected class
					throw new ClassCastException("Cannot cast "+element.getClass()+" to "+expectedClass);
				}
			}
		}
		
		//return result
		return checkedList;
	}
	
	/**
	 * if objects class equals expectedClass do a checked cast
	 * 
	 * @param Class<E>
	 * @param Object
	 * @return <E> E
	 * @throws Exception
	 */
	public static <E> E checkedCast(Class<E> expectedClass, Object object) throws Exception{
		if (expectedClass.isInstance(object)){
			return expectedClass.cast(object);
		} else {
			throw new Exception("cannot Cast class "+object.getClass()+" to "+expectedClass);
		}
	}
}
