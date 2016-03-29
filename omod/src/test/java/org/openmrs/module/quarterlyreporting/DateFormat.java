package org.openmrs.module.quarterlyreporting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		Date today = df.parse("2011/03/24");
		System.out.println("Today = " + df.format(today));
	}
}