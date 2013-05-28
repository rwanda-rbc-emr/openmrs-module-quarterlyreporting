package org.openmrs.module.quarterlyreporting;

import java.util.ArrayList;
import java.util.Collection;

public class SubstractCollection {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList a = new ArrayList();
		a.add("1");
		a.add("3");
		a.add("5");
		 
		ArrayList b = new ArrayList();
		b.add("3");
		b.add("5");
		b.add("1");
		b.add("7");
		b.add("8");
		
		
		
		SubstractCollection s = new SubstractCollection();
		
		System.out.println("coll1 "+a);
		System.out.println("coll2 "+b);
		System.out.println("diff list: " + s.Subtract(b, a));
		System.out.println("diff size: " + s.Subtract(b, a).size());
		System.out.println("mmmmmmmmmmmmmmmmmmmm ");

	}
	
	public Collection Subtract(Collection coll1, Collection coll2)
	{
//	    Collection result = new ArrayList(coll2);
	    coll1.removeAll(coll2);
	    return coll1;
	}

}
