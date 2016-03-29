package org.openmrs.module.quarterlyreporting.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface QuarterlyReportingService extends OpenmrsService {

	public List<Object[]> getPatientsEnrolled(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge);

	public List<Object[]> getPatientsStartedART(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge);

	public List<Object[]> getAllPatientsEligibleForARVsButNotYetStarted(
			int programId, Date startDate, Date endDate);

	public List<Object[]> getPatientsRecievedHIVCare(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge);

	public List<Integer> getPatientsLostOnFollowup(Date endDate);
	public List<Object[]> getPatientStoppeART(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge,List<Object[]> patientIds);
	
	public List<Object[]> getPatientExitedFromCare(Integer conceptId,Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge,List<Object[]> patientIds);
	
	public List<Object[]> getAllARVPatientsLostOnFollowUp(Date quarterFrom, Date quarterTo,List<Object[]> patientIds);
	
	public List<Object[]> getPatientsTransferreIn(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge);
	public Date getDateXMonthAgo(Date quarterFrom,int monthAgo);
	
	public List<Object[]> getNewPatientsOver6YearsOnART(Date startDate, Date endDate,String gender,Integer minAge,Integer maxAge);
	public List<Integer> getPatientsWthConcept(List<Integer> patients,int conceptId,Date date);
	
	public double[] getPatientsObsValues(Integer patientId,Concept concept) ;
	
	public double calculateMedian(List<Double> a) ;
	
	public Collection union(Collection coll1, Collection coll2);
	
	public List<Object[]> getPatientOnAllDrugs(List<Integer> listDrugIds, List<Integer> patientIds,Date endDate);
	
	public List<Integer> getPatientOnARTDuringTheQter(Date quarterStart,Date quarterEnd,Integer minAge,Integer maxAge);
	
	public void exportQuarterlyData(HttpServletRequest request, HttpServletResponse response, List<Patient> patients,
            String filename, String title)throws IOException, ParseException;
	
	public List<Object[]> getPatientsReceivedARVsForXmonthOutOfXmonth(List<Object[]> patientIds,Integer n);
	
	public List<Object[]> getPregnantFemales(Date quarterStart,Date quarterEnd);
	
	public Collection<Object[]> SubtractACollection(Collection<Object[]> coll1, Collection<Object[]> coll2);
	
	public List<List<Integer>> getRegimenComposition(List<Integer> list,Date endDate);
	
	public String getAllPatientObs(Patient p, Concept c);
	
	public List<String> getAllPatientObsList(Patient p, Concept c);
	
	public Date getWhenPatientStarted(Patient patient);
	
	public Date getDateInterval(Date baseline,Integer months);
	
	public List<Object[]> getPatientsExitedInThePeriod(Date startDate, Date endDate);
	
	public Date getTwoWeeksAfterDate(Date baseline,Integer week);
	
	public List<Integer> getAllPatientWithObsInPeriod(int conceptId, Date startDate,Date endDate);
	
	public Date getXMonthAfterDate(Date baseline,Integer months);
	
	public String getRegimenName(List<Integer> composition);
	
	public Date getFirstDayOfMonth(Date date);
	
	public List<Object[]> getCD4CountAfterXMonths(List<Object[]> patientIds,int xthMonth);
	
	public Date getWhenEnrolled(Patient p);
	
	public Date getMaxEncounter(Patient p,Date maxDate);
	

	public Date getMaxTransferInDate(Patient p,Date minDate,Date maxDate);
	
	public List<Object[]> getRemoveDuplicates(List<Object[]> list1,List<Object[]> list2);
	
	public Boolean isOnTriTherapy(Integer patientId);
	
	public List<Object[]> getPatientOnAllDrugsByConceptIds(List<Integer> listDrugIds,List<Integer> patientIds,Date endDate);
	
	public List<Object[]> getPatientsStartedCotrimo(Date quarterFrom,Date quarterTo, String gender, Integer minAge, Integer maxAge);
	
	public Date getWhenStopART(Patient p);

	public Date getWhenTransOut(Patient p);

	
	public Date getWhenLost(Patient p);
	
	public Integer getCalculageAge(Patient p,Date date1,Date date2);
	
	public List<Object[]> getPatientsTransferredOut(Date quarterFrom, Date quarterTo, String gender, Integer minAge,Integer maxAge) ;
	
	public List<Object[]> getPatientsDead(Date quarterFrom, Date quarterTo, String gender, Integer minAge,Integer maxAge);
	
	public List<Object[]> getPatientsWithCD4CountInThePeriod(List<Integer> cohort,Date startdate,Date enddate);
	
	public List<Object[]> getPatientsHadCD4CountAfter6Months(List<Object[]> patientIds);
	
	public List<Object[]> getPatientsHadCD4CountAfter12Months(List<Object[]> patientIds);
	
    public List<Object[]> getPatientsWithCD4AtMo(List<Object[]> patientIds) ;
    
    public List<Integer> getARTpatientsTransferreIn(Date quarterFrom, Date quarterTo);
    
    public List<Integer> getPreARTpatientsTransferreIn(Date quarterFrom,Date quarterTo);
    
    public List<Integer> getAllPatientsTransferredIn(Date quarterFrom, Date quarterTo);
    
    public List<Object[]> getNewOnArtTransferInExcluded(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge);
}