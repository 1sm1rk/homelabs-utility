package de.homelabs.collection.nestedset;

/**
 * Entity Template for use with this package
 * 
 * @author dirk.mueller (d.mueller@homelabs.de)
 * 
 */
public interface INestedSetEntity {

	/*
	 * base elements
	 */
	int getId();
	int getLft();
	int getRgt();
	int getParent();
	
	/*
	 * transient properties, map these values by the corresponding formula
	 */
	
	/**
	 * replace :entity with your entity
	 * @Formula(value="((select count(m.id) from :entity m where lft between m.lft and m.rgt))")
	 */
	int getLevel();
	
	/**
	 * @Formula(value="(((rgt - lft - 1) / 2))")
	 */
	int getOffspring();
	
	void setId(int id);
	void setLft(int lft);
	void setRgt(int rgt);
	void setParent(int parent);
	void setLevel(int level);
	void setOffspring(int offspring);
}
