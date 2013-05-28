package org.openmrs.module.quarterlyreporting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;

public class QuarterlyReportUtil {

	/** Logger for this class and subclasses */
	protected static final Log log = LogFactory
			.getLog(QuarterlyReportUtil.class);

	/**
	 * 
	 * helps to give a format to date
	 * 
	 * @param dateObject
	 * @return date formated
	 */
	public static String getDateFormat(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/**
	 * global properties for ARVs Concept Ids
	 * 
	 * @return string
	 */
	public static String gpGetARVConceptIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.arvConceptIds");
	}

	/**
	 * gets the list of concept ids and split them
	 * 
	 * @return list
	 */
	public static List<Integer> gpGetARVConceptAsList() {
		ArrayList<Integer> arvConceptIds = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetARVConceptIds(),
				",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			arvConceptIds.add(id);
		}
		return arvConceptIds;
	}

	/**
	 * global properties for HIV Program Id
	 * 
	 * @return string
	 */
	public static String gpGetHIVProgramId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.hivProgramId");
	}

	/**
	 * global properties for prophylaxis drugs
	 * 
	 * @return string
	 */
	public static String gpGetProphylaxisDrugIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.prophylaxisDrugIds");
	}
	
	/**
	 * global properties for prophylaxis concept ids
	 * 
	 * @return string
	 */
	public static String gpGetProphylaxisDrugConceptIds() {
		return Context.getAdministrationService().getGlobalProperty("quarterlyreporting.prophylaxisDrugConceptIds");
	}

	/**
	 * gets the list of ids and split them
	 * 
	 * @return list
	 */
	public static List<String> gpGetProphylaxisAsList() {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetProphylaxisDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			String id = (String) tokenizer.nextToken();
			list.add(id);
		}
		// log.debug("getProphylaxisAsList " +list);
		return list;
	}
	
	/**
	 * gets the list of prophylaxis concept ids and split them
	 * 
	 * @return list
	 */
	public static List<String> gpGetProphylaxisConceptIdsAsList() {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetProphylaxisDrugConceptIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			String id = (String) tokenizer.nextToken();
			list.add(id);
		}
		// log.debug("getProphylaxisAsList " +list);
		return list;
	}


	public static List<String> gpGetTBDrugsConceptIdsAsList() {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(gpTBDrugsConceptIds(),
				",");
		while (tokenizer.hasMoreTokens()) {
			String id = (String) tokenizer.nextToken();
			list.add(id);
		}
		return list;
	}
	
	public static List<Integer> gpGetTBDrugIds() {
		ArrayList<Integer> tbDrugIds = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(gpTBDrugsConceptIds(),
				",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			tbDrugIds.add(id);
		}
		return tbDrugIds;
	}

	/**
	 * get prophylaxis ids as integers
	 * 
	 * @return
	 */
	public static List<Integer> gpGetProphylaxisAsIntegers() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(
				gpGetProphylaxisDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}
	
	/**
	 * get prophylaxis concept ids as integers in the list
	 * 
	 * @return
	 */
	public static List<Integer> gpGetProphylaxisConceptIds() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetProphylaxisDrugConceptIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}

	/**
	 * global properties for prophylaxis drugs
	 * 
	 * @return string
	 */
	public static String gpGetExitedFromCareConceptId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.exitedConceptId");
	}

	/**
	 * global properties for prophylaxis drugs
	 * 
	 * @return string
	 */
	public static String gpGetCD4CountConceptId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.CD4CountConceptId");
	}

	/**
	 * global properties for Return Visit Date
	 * 
	 * @return string
	 */
	public static String gpGetReturnVisitDateConceptId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.returnVisitDateConceptId");
	}

	/**
	 * global properties for Transfer In
	 * 
	 * @return string
	 */
	public static String gpTransferInConceptId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.transferInConceptId"); 
	}

	/**
	 * global properties for Transfer In From
	 * 
	 * @return string
	 */
	public static String gpTransferFromInConceptId() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.transferInFromConceptId");
	}

	public static String gpTBDrugsConceptIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.tbDrugIds");
	}
	
	public static String gpTBDrugsIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.tbIds");
	}
	
	public static String gpCotrimoConceptIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.bactrimConceptIds");
	}
	public static String gpArvAndPreArvDrugConceptIds() {
		return Context.getAdministrationService().getGlobalProperty(
				"quarterlyreporting.arvAndPreArvDrugConceptIds");
	}
	

	/**
	 * helps to convert List<Object[]> to List<Integer>
	 * 
	 * @param objects
	 * @return List<Integer>
	 */
	public static List<Integer> getConvertToType(List<Object[]> objects) {
		List<Integer> list = new ArrayList<Integer>();
		for (Object[] obj : objects) {
			list.add((Integer) obj[0]);
		}
		return list;
	}

	public static List<Integer> getNumbersFromStr(String s) {
		ArrayList<Integer> drugIds = new ArrayList<Integer>();
		try {
			StringTokenizer tokenizer = new StringTokenizer(s, "-");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				drugIds.add(id);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return drugIds;
	}

	/**
	 * 
	 * takes List<List<Integer>> and remove repeated list for example [1,3,4]
	 * and [4,1,3] are the same and we must count them as one list
	 * 
	 * @param listOfLists
	 * @return
	 */
	public static List<List<Integer>> removeDuplcatedListInListOfIntegers(
			List<List<Integer>> listOfLists) {

		List<List<Integer>> lists = new ArrayList<List<Integer>>();
		List<String> str = new ArrayList<String>();

		for (int i = 0; i < listOfLists.size(); i++) {
			List<Integer> list = listOfLists.get(i);
			
			Collections.sort(list);
			HashSet<Integer> setToRemoveDuplic = new HashSet<Integer>(list);
			List<Integer> duplicRemoved = new ArrayList<Integer>(setToRemoveDuplic);

			String listString = "";
			for (int j = 0; j < duplicRemoved.size(); j++) {
				listString += duplicRemoved.get(j) + "-";
			}

			str.add(listString);
		}
		HashSet<String> hashSet = new HashSet<String>(str);
		List<String> newListStr = new ArrayList<String>(hashSet);
		
		
		for (String s : newListStr) 
			lists.add(getNumbersFromStr(s));
		
		return lists;
	}
	
	 public static List<List<Integer>> getRemoveEmptyList (List<List<Integer>> list){
   	  try {
   		 for (List<Integer> l : list) {
 			if(l.size()==0)
 				list.remove(l);
 		}
	     } catch (Exception e) {
		log.equals("error ============================= "+e);
	      }
		return list;
		
   }
	 
	 public static Integer calculateAge(Date date1,Date birthdate) throws ParseException {
		    if (birthdate == null) {
		        return null;
		    }
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date1);
		    
		    Calendar birthCal = Calendar.getInstance();
		    birthCal.setTime(birthdate);
		    
			boolean isMonthGreater = birthCal.get(Calendar.MONTH) > cal.get(Calendar.MONTH);
			boolean isMonthLess = birthCal.get(Calendar.MONTH) < cal.get(Calendar.MONTH);
			boolean isMonthSameButDayGreater = birthCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)	&& birthCal.get(Calendar.DAY_OF_MONTH) > cal.get(Calendar.DAY_OF_MONTH);
			boolean isMonthSameButDayLess = birthCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)	&& birthCal.get(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH);
			boolean isMonthSameButDaySame = birthCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)	&& birthCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH);
			
			int age=0;	
			if(isMonthGreater)
				age= ((date1.getYear()-birthdate.getYear())*12)+(cal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH));
			if(isMonthLess)
				age= ((date1.getYear()-birthdate.getYear())*12)+(birthCal.get(Calendar.MONTH) - cal.get(Calendar.MONTH));
			if(isMonthSameButDayGreater)
				age=(date1.getYear()-birthdate.getYear())*12;
			if(isMonthSameButDayLess)
				age=((((date1.getYear()-birthdate.getYear()))*12)-1);
			if(isMonthSameButDaySame)
				age=(((date1.getYear()-birthdate.getYear()))*12);

	       return age;
		}
	 
	 
	 public static <T> List<T> subtract(List<T> list1, List<T> list2) {
		    List<T> result = new ArrayList<T>();
		    Set<T> set2 = new HashSet<T>(list2);
		    for (T t1 : list1) {
		        if( !set2.contains(t1) ) {
		            result.add(t1);
		        }
		    }
		    return result;
	 }
}


