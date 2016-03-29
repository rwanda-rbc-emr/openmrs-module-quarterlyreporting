package org.openmrs.module.quarterlyreporting.serviceimpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.quarterlyreporting.db.dao.QuarterReportingDAO;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;

public class QuarterlyReportingServiceImpl extends BaseOpenmrsService implements QuarterlyReportingService {
	private QuarterReportingDAO quarterlyreportingDAO;

	public QuarterReportingDAO getQuarterlyreportingDAO() {
		return quarterlyreportingDAO;
	}

	public void setQuarterlyreportingDAO(
			QuarterReportingDAO quarterlyreportingDAO) {
		this.quarterlyreportingDAO = quarterlyreportingDAO;
	}
	/************************************************************************************************************************/
	public List<Object[]> getPatientsEnrolled(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge) {

		return quarterlyreportingDAO.getPatientEnrolled(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	public List<Object[]> getPatientsStartedART(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {

		return quarterlyreportingDAO.getPatientsStartedART(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	public List<Object[]> getAllPatientsEligibleForARVsButNotYetStarted(int programId, Date startDate, Date endDate){
		return quarterlyreportingDAO.getAllPatientsEligibleForARVsButNotYetStarted(programId, startDate, endDate);
	}

	@Override
	public List<Object[]> getPatientsRecievedHIVCare(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {
		return quarterlyreportingDAO.getPatientsRecievedHIVCare(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	@Override
	public List<Integer> getPatientsLostOnFollowup(Date endDate) {
		return quarterlyreportingDAO.getPatientsLostOnFollowup(endDate);
	}

	@Override
	public List<Object[]> getPatientStoppeART(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge,List<Object[]> patientIds) {

		return quarterlyreportingDAO.getPatientStoppeART(quarterFrom, quarterTo, gender, minAge, maxAge,patientIds);
	}


	@Override
	public List<Object[]> getPatientExitedFromCare(Integer conceptId,
			Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge,List<Object[]> patientIds) {

		return quarterlyreportingDAO.getPatientExitedFromCare(conceptId, quarterFrom, quarterTo, gender, minAge, maxAge,patientIds);
	}

	@Override
	public List<Object[]> getAllARVPatientsLostOnFollowUp(Date quarterFrom,Date quarterTo,List<Object[]> patientIds) {
		return quarterlyreportingDAO.getAllARVPatientsLostOnFollowUp(quarterFrom, quarterTo,patientIds);
	}

	@Override
	public List<Object[]> getPatientsTransferreIn(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {
		return quarterlyreportingDAO.getPatientsTransferreIn(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	@Override
	public Date getDateXMonthAgo(Date quarterFrom,int monthAgo) {
		return quarterlyreportingDAO.getDateXMonthAgo(quarterFrom,monthAgo);
	}

	@Override
	public List<Object[]> getNewPatientsOver6YearsOnART(Date startDate, Date endDate,String gender,Integer minAge,Integer maxAge) {
		return quarterlyreportingDAO.getNewPatientsOver6YearsOnART(startDate,endDate,gender,minAge,maxAge);
	}

	@Override
	public List<Integer> getPatientsWthConcept(List<Integer> patients, int conceptId,Date date) {
		return quarterlyreportingDAO.getPatientsWthConcept(patients, conceptId,date);
	}

	@Override
	public Collection union(Collection coll1, Collection coll2) {
		return quarterlyreportingDAO.union(coll1, coll2);
	}

	@Override
	public List<Object[]> getPatientOnAllDrugs(List<Integer> listDrugIds,
			List<Integer> patientIds,Date endDate) {
		return quarterlyreportingDAO.getPatientOnAllDrugs(listDrugIds, patientIds,endDate);
	}

	@Override
	public List<Integer> getPatientOnARTDuringTheQter(Date quarterStart,
			Date quarterEnd,Integer minAge,Integer maxAge) {
		return quarterlyreportingDAO.getPatientOnARTDuringTheQter(quarterStart, quarterEnd,minAge,maxAge);
	}

	@Override
	public void exportQuarterlyData(HttpServletRequest request,
			HttpServletResponse response, List<Patient> patients,
			String filename, String title) throws IOException, ParseException {
		quarterlyreportingDAO.exportQuarterlyData(request, response, patients, filename, title);
		
	}

	@Override
	public double[] getPatientsObsValues(Integer patientId,Concept concept)  {

		return quarterlyreportingDAO.getPatientsObsValues(patientId, concept);
	}

	@Override
	public double calculateMedian(List<Double> a) {
		return quarterlyreportingDAO.calculateMedian(a);
	}

	@Override
	public List<Object[]> getPatientsReceivedARVsForXmonthOutOfXmonth(List<Object[]> patientIds,
			 Integer n) {
		return quarterlyreportingDAO.getPatientsReceivedARVsForXmonthOutOfXmonth(patientIds, n);
	}

	@Override
	public List<Object[]> getPregnantFemales(Date quarterStart, Date quarterEnd) {
		return quarterlyreportingDAO.getPregnantFemales(quarterStart, quarterEnd);
	}

	@Override
	public Collection<Object[]> SubtractACollection(Collection<Object[]> coll1, Collection<Object[]> coll2) {

		return quarterlyreportingDAO.SubtractACollection(coll1, coll2);
	}

	@Override
	public List<List<Integer>> getRegimenComposition(List<Integer> list,Date endDate) {

		return quarterlyreportingDAO.getRegimenComposition(list,endDate);
	}

	@Override
	public String getAllPatientObs(Patient p, Concept c) {

		return quarterlyreportingDAO.getAllPatientObs(p, c);
	}

	@Override
	public List<String> getAllPatientObsList(Patient p, Concept c) {

		return quarterlyreportingDAO.getAllPatientObsList(p, c)
		;
	}

	@Override
	public Date getWhenPatientStarted(Patient patient) {

		return quarterlyreportingDAO.getWhenPatientStarted(patient);
	}

	@Override
	public Date getDateInterval(Date baseline, Integer months) {

		return quarterlyreportingDAO.getDateInterval(baseline, months);
	}

	@Override
	public List<Object[]> getPatientsExitedInThePeriod(Date startDate,
			Date endDate) {

		return quarterlyreportingDAO.getPatientsExitedInThePeriod(startDate, endDate);
	}

	@Override
	public Date getTwoWeeksAfterDate(Date baseline, Integer week) {

		return quarterlyreportingDAO.getTwoWeeksAfterDate(baseline, week);
	}

	@Override
	public List<Integer> getAllPatientWithObsInPeriod(int conceptId,
			Date startDate, Date endDate) {

		return quarterlyreportingDAO.getAllPatientWithObsInPeriod(conceptId, startDate, endDate);
	}

	@Override
	public Date getXMonthAfterDate(Date baseline, Integer months) {

		return quarterlyreportingDAO.getXMonthAfterDate(baseline, months);
	}

	@Override
	public String getRegimenName(List<Integer> composition) {

		return quarterlyreportingDAO.getRegimenName(composition);
	}

	@Override
	public Date getFirstDayOfMonth(Date date) {
		return quarterlyreportingDAO.getFirstDayOfMonth(date);
	}

	@Override
	public List<Object[]> getCD4CountAfterXMonths(List<Object[]> patientIds,int xthMonth) {
		return quarterlyreportingDAO.getCD4CountAfterXMonths(patientIds,xthMonth);
	}

	@Override
	public Date getWhenEnrolled(Patient p) {
		return quarterlyreportingDAO.getWhenEnrolled(p);
	}

	@Override
	public Date getMaxEncounter(Patient p,Date maxDate) {

		return quarterlyreportingDAO.getMaxEncounter(p,maxDate);
	}

	@Override
	public Date getMaxTransferInDate(Patient p, Date minDate, Date maxDate) {

		return quarterlyreportingDAO.getMaxTransferInDate(p,minDate,maxDate);
	}

	@Override
	public List<Object[]> getRemoveDuplicates(List<Object[]> list1,
			List<Object[]> list2) {

		return quarterlyreportingDAO.getRemoveDuplicates(list1, list2);
	}

	@Override
	public Boolean isOnTriTherapy(Integer patientId) {
		return quarterlyreportingDAO.isOnTriTherapy(patientId);
	}

	@Override
	public List<Object[]> getPatientOnAllDrugsByConceptIds(
			List<Integer> listDrugIds, List<Integer> patientIds, Date endDate) {

		return quarterlyreportingDAO.getPatientOnAllDrugsByConceptIds(listDrugIds, patientIds, endDate);
	}

	@Override
	public List<Object[]> getPatientsStartedCotrimo(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {

		return quarterlyreportingDAO.getPatientsStartedCotrimo(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	@Override
	public Date getWhenStopART(Patient p) {
		return quarterlyreportingDAO.getWhenStopART(p);
	}

	@Override
	public Date getWhenTransOut(Patient p) {
		return quarterlyreportingDAO.getWhenTransOut(p);
	}

	@Override
	public Date getWhenLost(Patient p) {
		return quarterlyreportingDAO.getWhenLost(p);
	}

	@Override
	public Integer getCalculageAge(Patient p,Date date1,Date date2) {
		return quarterlyreportingDAO.getCalculageAge(p,date1,date2);
	}

	@Override
	public List<Object[]> getPatientsTransferredOut(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {
		return quarterlyreportingDAO.getPatientsTransferredOut(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	@Override
	public List<Object[]> getPatientsDead(Date quarterFrom, Date quarterTo,
			String gender, Integer minAge, Integer maxAge) {
		return quarterlyreportingDAO.getPatientsDead(quarterFrom, quarterTo, gender, minAge, maxAge);
	}

	@Override
	public List<Object[]> getPatientsWithCD4CountInThePeriod(List<Integer> cohort,Date startdate,
			Date enddate) {
		return quarterlyreportingDAO.getPatientsWithCD4CountInThePeriod(cohort,startdate,enddate);
	}

	@Override
	public List<Object[]> getPatientsHadCD4CountAfter6Months(List<Object[]> patientIds) {
		return quarterlyreportingDAO.getPatientsHadCD4CountAfter6Months(patientIds);
	}

	@Override
	public List<Object[]> getPatientsHadCD4CountAfter12Months(List<Object[]> patientIds) {
		// TODO Auto-generated method stub
		return quarterlyreportingDAO.getPatientsHadCD4CountAfter12Months(patientIds);
	}

	@Override
	public List<Object[]> getPatientsWithCD4AtMo(List<Object[]> patientIds) {
		return quarterlyreportingDAO.getPatientsWithCD4AtMo(patientIds);
	}

	@Override
	public List<Integer> getARTpatientsTransferreIn(Date quarterFrom,
			Date quarterTo) {
		return quarterlyreportingDAO.getARTpatientsTransferreIn(quarterFrom, quarterTo);
	}

	@Override
	public List<Integer> getPreARTpatientsTransferreIn(Date quarterFrom,
			Date quarterTo) {
		return quarterlyreportingDAO.getPreARTpatientsTransferreIn(quarterFrom, quarterTo);
	}


	@Override
	public List<Integer> getAllPatientsTransferredIn(Date quarterFrom,
			Date quarterTo) {
		return quarterlyreportingDAO.getAllPatientsTransferredIn(quarterFrom, quarterTo);
	}

	@Override
	public List<Object[]> getNewOnArtTransferInExcluded(Date quarterFrom,
			Date quarterTo, String gender, Integer minAge, Integer maxAge) {
		return quarterlyreportingDAO.getNewOnArtTransferInExcluded(quarterFrom, quarterTo, gender, minAge, maxAge);
	}
}
