package org.openmrs.module.quarterlyreporting;

import java.util.Arrays;

public class medianeTest {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double myArray1[] = { 2.0, 3.0, 0.0, 9.0, 1.0 };

		double mediane1 = median(myArray1);

		System.out.println(">>>>>>>>>>>>>mediane1>>>>>>>>>>>>> " + mediane1);

	}

	public static double median(double a[]) {
		double[] b = new double[a.length];
		System.arraycopy(a, 0, b, 0, b.length);
		Arrays.sort(b);

		if (a.length % 2 == 0) {
			return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
		} else {
			return b[b.length / 2];
		}
	}

}
