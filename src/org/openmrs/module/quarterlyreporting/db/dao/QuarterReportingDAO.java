package org.openmrs.module.quarterlyreporting.db.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;

public interface QuarterReportingDAO {
	/**
	 * gives patients enrolled in HIV Program
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @return list of patient ids
	 */
	public List<Object[]> getPatientEnrolled(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge);

	/**
	 * helps to know patients who started tretment
	 */
	public List<Object[]> getPatientsStartedART(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge);

	/**
	 * 
	 * gives patients eligible for ART
	 * 
	 * @param programId
	 * @param startDate
	 * @param endDate
	 * @return list of patient ids
	 */
	public List<Object[]> getAllPatientsEligibleForARVsButNotYetStarted(
			int programId, Date startDate, Date endDate);
	/**
	 * 
	 * gives patients who have recieved HIV Care during the given quarter
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @return list of patients ids
	 */
	public List<Object[]> getPatientsRecievedHIVCare(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge);
	/**
	 * gives  patients lost to follow up in a the quarter
	 * 
	 * @param patientId
	 * @param endDate
	 * @return list of ids
	 */
	public List<Integer> getPatientsLostOnFollowup(Date endDate);
	
	
	/**
	 * 
	 * gives patient who stopped ART
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @return ids
	 */
	public List<Object[]> getPatientStoppeART(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge,List<Object[]> patientIds);
	
	/**
	 * 
	 * helps to get patients exited from care buy giving the concept
	 * 
	 * @param concept
	 * @param quarterFrom
	 * @param quarterTo
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @return ids
	 */
	public List<Object[]> getPatientExitedFromCare(Integer conceptId,Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge,List<Object[]> patientIds);
	
	public List<Object[]> getAllARVPatientsLostOnFollowUp(Date quarterFrom,Date quarterTo,List<Object[]> patientIds);
	
	/**
	 * 
	 * gives patients transferred in during the quarter
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @return List<Object[]>
	 */
	public List<Object[]> getPatientsTransferreIn(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge);
	
	/**
	 * 
	 * gives preART patients at the time when they were transferred in
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @return List<Object[]>
	 */
	public List<Integer> getPreARTpatientsTransferreIn(Date quarterFrom, Date quarterTo);
	
	/**
	 * 
	 * gives patients who were on ART treatment at the time when they were transferred in
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @return List<Integer>
	 */
	public List<Integer> getARTpatientsTransferreIn(Date quarterFrom, Date quarterTo);
	
	/**
	 * 
	 * gives all patients transferred in during the quarter
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @return List<Integer>
	 */
	public List<Integer> getAllPatientsTransferredIn(Date quarterFrom, Date quarterTo);
		
	/**
	 * 	
	 * gives date witch is calculated based on beginning the the given quarter;
	 * going backward
	 * @param quarterFrom
	 * @return date
	 */
	public Date getDateXMonthAgo(Date quarterFrom,int monthAgo);
	
	/**
	 * gives new patients(over 6 years old) on ART on a date 
	 * 
	 * @param date
	 * @return patient ids
	 */
	public List<Object[]> getNewPatientsOver6YearsOnART(Date startDate, Date endDate,String gender,Integer minAge,Integer maxAge);
	
	/**
	 * returns trus when patient with id has a conceptId
	 * 
	 * @param patients
	 * @param conceptId
	 * @return true or false
	 */
	public List<Integer> getPatientsWthConcept(List<Integer> patients,int conceptId,Date date);
	
	/**
	 * 
	 * gives one collection fron two 
	 * 
	 * @param coll1
	 * @param coll2
	 * @return collection
	 */
	public Collection union(Collection coll1, Collection coll2);
	
	
	/**
	 * 
	 * allows to substract a collection from another one
	 * 
	 * @param coll1
	 * @param coll2
	 * @return a collection (the remained)
	 */
	public Collection<Object[]> SubtractACollection(Collection<Object[]> coll1, Collection<Object[]> coll2);
	
	/**
	 * gives patients currently on that given regimen
	 * 
	 * @param listDrugIds
	 * @param patientIds
	 * @return patients ids
	 */
	public List<Object[]> getPatientOnAllDrugs(List<Integer> listDrugIds, List<Integer> patientIds,Date endDate);
	
	/**
	 * 
	 * gives patients on ART during the quarter
	 * 
	 * @param quarterStard
	 * @param quarterEnd
	 * @return patients ids
	 */
	public List<Integer> getPatientOnARTDuringTheQter(Date quarterStart,Date quarterEnd,Integer minAge,Integer maxAge);
	
	/**
	 * helps to export data into excel
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patients
	 * @param filename
	 * @param title
	 */
	public void exportQuarterlyData(HttpServletRequest request, HttpServletResponse response, List<Patient> patients,
            String filename, String title)throws IOException, ParseException ;
	
	/**
	 * 
	 * helps to get all observation values in a list
	 * 
	 * @param patient
	 * @return list of concept values
	 */
	public double[] getPatientsObsValues(Integer patientId,Concept concept) ;
	
	/**
	 * 
	 * calculates median 
	 * 
	 * @param a
	 * @return median in a terms list
	 */
	public double calculateMedian(List<Double> a);
	
	/**
	 * 
	 * gives patients who received ARVs for x month out of x month (====> who did pharmacy visit x/x)
	 * 
	 * @param startDate
	 * @param endDate
	 * @return patient ids
	 */
	public List<Object[]> getPatientsReceivedARVsForXmonthOutOfXmonth(List<Object[]> patientIds,Integer n);
	
	public List<Object[]> getPregnantFemales(Date quarterStart,Date quarterEnd);
	
	/**
	 * 
	 * helps to get all drugs(called regimen composition) from the table
	 * gives all drugs not stopped before the reporting period
	 * 
	 * @return names
	 */
	public List<List<Integer>> getRegimenComposition(List<Integer> list,Date endDate);
	
	
	/**
	 * allows to get all patient observation's values and the date of the event 
	 * 
	 * @param p
	 * @param c
	 * @return list of the objects
	 */
	public String getAllPatientObs(Patient p, Concept c);
	
	/**
	 * does the same thing with getAllPatientObs(Patient p, Concept c) but its used to know the size of a list
	 * Auto generated method comment
	 * 
	 * @param p
	 * @param c
	 * @return list of concept value and its obs datetime
	 */
	public List<String> getAllPatientObsList(Patient p, Concept c);
	
	/**
	 * gives when patient started treatment for the first time using query
	 * @param patient
	 * @return date
	 */
	
	public Date getWhenPatientStarted(Patient patient);
	
	/**
	 * helps to calculate interval basing on a given date
	 * 
	 * @param baseline
	 * @param months
	 * @return date
	 */
	public Date getDateInterval(Date baseline,Integer months);
	
	/**
	 * helps to calculate the date corresponding to the x months after a given date
	 * 
	 * @param baseline
	 * @param months
	 * @return date
	 */
	public Date getXMonthAfterDate(Date baseline,Integer months);
	
	/**
	 * gives exited patients in the period between
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object[]> getPatientsExitedInThePeriod(Date startDate, Date endDate);
	
	/**
	 * 
	 * calculates weeks after when patients started ARV
	 * 
	 * @param baseline
	 * @param week
	 * @return
	 */
	public Date getTwoWeeksAfterDate(Date baseline,Integer week);
	
	/**
	 * 
	 * gives obs in the given period
	 * 
	 * @param conceptId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Integer> getAllPatientWithObsInPeriod(int conceptId, Date startDate,Date endDate);
	
	/**
	 * 
	 * forms a name basing on drugs that compose it
	 * 
	 * @param composition
	 * @return name 
	 */
	public String getRegimenName(List<Integer> composition);

	/**
	 * returns the first day of the month
	 * 
	 * @param date
	 * @return date
	 */
	public Date getFirstDayOfMonth(Date date);
	
	/**
	 * helps to get M0,M6,M12,.....
	 * 
	 * @param startDate
	 * @return CD4
	 */
	public List<Object[]> getCD4CountAfterXMonths(List<Object[]> patientIds,int xthMonth);
	
	/**
	 * 
	 * gives the date when patient started taking ARV
	 * 
	 * @param p
	 * @return date
	 */
	public Date getWhenEnrolled(Patient p);
	
	/**
	 * 
	 * gives the maximum encounter
	 * 
	 * @param p
	 * @return date
	 */
	public Date getMaxEncounter(Patient p,Date maxDate);
	
	/**
	 * 
	 *gives the date maximum when the given patient has been transferred in from another hospital or Health Center
	 * 
	 * @param p
	 * @param minDate
	 * @param maxDate
	 * @return date
	 */
	public Date getMaxTransferInDate(Patient p, Date minDate, Date maxDate);
	
	/**
	 * 
	 * takes two list and removes duplicates
	 * 
	 * @param list1
	 * @param list2
	 * @return list<Object[]>
	 */
	public List<Object[]> getRemoveDuplicates(List<Object[]> list1,List<Object[]> list2);
	
	/**
	 * helps to know if patient is taking at least 3 HIV drugs 
	 * 
	 * @param p
	 * @return boolean
	 */
	public Boolean isOnTriTherapy(Integer patientId);

	/**
	 * 
	 * helps to know patient regimen by concept ids
	 * 
	 * @param listDrugIds
	 * @param patientIds
	 * @param endDate
	 * @return
	 */
	public List<Object[]> getPatientOnAllDrugsByConceptIds(List<Integer> listDrugIds,List<Integer> patientIds,Date endDate);
	
	public List<Object[]> getPatientsStartedCotrimo(Date quarterFrom,Date quarterTo, String gender, Integer minAge, Integer maxAge) ;
	
	/**
	 * 
	 *gives the date when the patient stopped ART
	 * 
	 * @param p
	 * @return
	 */
	public Date getWhenStopART(Patient p);
	
	/**
	 * 
	 * gives the date when the patient has been transferred out
	 * 
	 * @param p
	 * @return
	 */
	public Date getWhenTransOut(Patient p);
	/**
	 * 
	 * gives the date when the patient has been lost to follow-up
	 * 
	 * @param p
	 * @return
	 */
	
	public Date getWhenLost(Patient p);
	
	public Integer getCalculageAge(Patient p,Date date1,Date date2);
	
	public List<Object[]> getPatientsTransferredOut(Date quarterFrom, Date quarterTo, String gender, Integer minAge,Integer maxAge) ;
	
	public List<Object[]> getPatientsDead(Date quarterFrom, Date quarterTo, String gender, Integer minAge,Integer maxAge) ;
	
	public List<Object[]> getPatientsWithCD4CountInThePeriod(List<Integer> cohort,Date startdate,Date enddate);
	
	public List<Object[]> getPatientsHadCD4CountAfter6Months(List<Object[]> patientIds);

	public List<Object[]> getPatientsHadCD4CountAfter12Months(List<Object[]> patientIds);
	
	/**
	 * 
	 * gets patients who had cd4 count at ARV starttime
	 * 
	 * @param patientIds
	 * @return
	 */
	public List<Object[]> getPatientsWithCD4AtMo(List<Object[]> patientIds) ;
	
	/**
	 * 
	 * new enrolled
	 * 
	 * @param quarterFrom
	 * @param quarterTo
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @return List<Object[]>
	 */
	public List<Object[]> getNewEnrolled(Date quarterFrom, Date quarterTo,String gender, Integer minAge, Integer maxAge);
	
	
}
