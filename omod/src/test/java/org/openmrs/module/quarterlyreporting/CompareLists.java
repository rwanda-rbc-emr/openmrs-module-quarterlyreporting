package org.openmrs.module.quarterlyreporting;

import java.util.ArrayList;
import java.util.List;

public class CompareLists {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Integer> lists1=new ArrayList<Integer>();
		lists1.add(9);
		lists1.add(7);
		lists1.add(1);
		lists1.add(null);

		
		List<Integer> listr2=new ArrayList<Integer>();
		listr2.add(22);
		listr2.add(9);
		listr2.add(7);
		listr2.add(1);
		listr2.add(23);
		listr2.add(24);
		listr2.add(27);
		listr2.add(37);
		
		
		
		boolean test=false;
		
		for (int i=0;i<listr2.size();i++) {
			for (int j=0;j<lists1.size(); j++) {
				if(listr2.contains(lists1.get(j))||listr2.get(i)==22||listr2.get(i)==23||listr2.get(i)==24||listr2.get(i)==27||listr2.get(i)==37||lists1.get(j)==null)
					test=true;
				else{
					test=false;
					break;
				}

			}
		}
		System.out.println("list  :"+test);

		
	}
	

}
