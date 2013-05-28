package org.openmrs.module.quarterlyreporting;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class AddCollection {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		list2.add(3);
		list2.add(4);
		list2.add(1);
		
		ArrayList<Integer> sumList = new ArrayList<Integer>();
		
		for(Integer i:list2){
			for(Integer j:list2){
				if(i!=j)
					sumList.add(i);
			}
		}

		System.out.println("===================list1 "+list1);
		System.out.println("===================list2 "+list2);
		System.out.println("===================sumList "+sumList);
	}

}
