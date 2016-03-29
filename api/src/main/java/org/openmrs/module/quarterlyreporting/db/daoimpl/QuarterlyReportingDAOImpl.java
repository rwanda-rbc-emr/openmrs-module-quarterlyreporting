package org.openmrs.module.quarterlyreporting.db.daoimpl;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;
import org.openmrs.module.quarterlyreporting.QuarterlyReportUtil;
import org.openmrs.module.quarterlyreporting.db.dao.QuarterReportingDAO;
import org.openmrs.module.quarterlyreporting.regimenhistory.Regimen;
import org.openmrs.module.quarterlyreporting.regimenhistory.RegimenComponent;
import org.openmrs.module.quarterlyreporting.regimenhistory.RegimenHistory;
import org.openmrs.module.quarterlyreporting.regimenhistory.RegimenUtils;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;

public class QuarterlyReportingDAOImpl implements QuarterReportingDAO {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;

	}

	public String getQueryPortionForProphylaxis() {
		List<String> gpProphylaxisDrugIds = QuarterlyReportUtil.gpGetProphylaxisAsList();
		String quaryPortion = "";
		for (String id : gpProphylaxisDrugIds) {
			quaryPortion = quaryPortion + " AND dro.drug_inventory_id <> " + id;
		}

		return quaryPortion;
	}

	public String getQueryPortionForProphylaxisConceptIds() {
		List<String> gpProphylaxisDrugIds = QuarterlyReportUtil.gpGetProphylaxisAsList();
		String quaryPortion = "";
		for (String id : gpProphylaxisDrugIds) {
			quaryPortion = quaryPortion + " AND o.concept_id <> " + id;
		}

		return quaryPortion;
	}

	public String getQueryPortionForTBDrugs() {
		List<String> gpTBDrugIds = QuarterlyReportUtil.gpGetTBDrugsConceptIdsAsList();
		String quaryPortion = "";
		for (String id : gpTBDrugIds) {
			quaryPortion = quaryPortion + " AND o.concept_id <> " + id;
		}

		return quaryPortion;
	}

	/**
	 * 
	 * get exited conceptId from global properties
	 * 
	 * @return String
	 */
	public Integer gpGetExitedFromCareConceptId() {
		Integer exitedConcept = Integer.parseInt(QuarterlyReportUtil.gpGetExitedFromCareConceptId());
		return exitedConcept;
	}

	// table 1.0 and table 1.1
	public List<Object[]> getPatientEnrolled(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {
		SQLQuery query = null;

		StringBuffer strbuf = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		queryPortion.append(" FROM patient_program pg ");
		queryPortion.append(" INNER JOIN program pm on pg.program_id=pg.program_id ");
		queryPortion.append("INNER JOIN patient pat on pat.patient_id = pg.patient_id ");
		queryPortion.append("INNER JOIN person ps on ps.person_id=pat.patient_id and gender = '" + gender + "' ");
		// queryPortion.append(" INNER JOIN obs obs on
		// obs.person_id=ps.person_id ");
		queryPortion.append(" AND pg.program_id = " + QuarterlyReportUtil.gpGetHIVProgramId());

		if (quarterFrom != null && quarterTo == null) {
			strbuf.append("SELECT pat.patient_id , ");
			strbuf.append("IF((select min(pg.date_enrolled)) < ");
			strbuf.append("'" + df.format(quarterFrom) + "'" + " , " + " true " + " ," + "false)  ");
			strbuf.append(queryPortion);
			strbuf.append(" and pg.date_enrolled <= '" + df.format(quarterFrom)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' "
					+ df.format(quarterFrom) + " ')) ");
			strbuf.append(" group by pat.patient_id ");

		}
		if (quarterFrom == null && quarterTo != null) {
			strbuf.append("SELECT pat.patient_id , ");
			strbuf.append("IF((select min(pg.date_enrolled)) < ");
			strbuf.append("'" + df.format(quarterTo) + "'" + " , " + " true " + " ," + "false)  ");
			strbuf.append(queryPortion);
			strbuf.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(quarterTo)
					+ " ')) ");
			strbuf.append(" group by pat.patient_id ");
		}
		if (quarterFrom != null && quarterTo != null) {
	
			strbuf.append("SELECT pat.patient_id , ");
			strbuf.append("IF((select min(pg.date_enrolled)) BETWEEN ");
			strbuf.append("'" + df.format(quarterFrom) + "' AND '" + df.format(quarterTo) + "'" + " , " + " true "
					+ " ," + "false)  ");
			strbuf.append(queryPortion);
			// strbuf.append(" and pg.date_enrolled <= '"+ df.format(quarterTo)
			// + "' and (pg.date_completed is null OR pg.date_completed >=
			// '"+df.format(quarterTo)+"') ");
			strbuf.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(quarterTo)
					+ " ')) ");
			strbuf.append(" group by pat.patient_id ");

			log.info("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr3 " + strbuf.toString());
		}

		query = sessionFactory.getCurrentSession().createSQLQuery(strbuf.toString());

		List<Object[]> records = query.list();

		List<Integer> patIds = new ArrayList<Integer>();

		int add = 0;

		for (Object[] obj : records) {
			add++;
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			patientStartedWhenMap.put(booleanValue, id);
		}

		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				patIds.add(patientStartedWhenMap.get(key));
			}
		}
		List<Integer> patIds2 = new ArrayList<Integer>();

		int patientAge = 0;

		for (Integer id : patIds) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (YEAR(pg.date_enrolled) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(pg.date_enrolled) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(CURDATE()) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  person s ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=s.person_id ");
			strbuf2.append(" INNER JOIN patient_program pg on pg.patient_id=p.patient_id and p.patient_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<BigInteger> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					patIds2.add(id);
			}
		}

		QuarterlyReportingService service = Context.getService(QuarterlyReportingService.class);

		List<Object[]> objs = new ArrayList<Object[]>();

		List<Integer> transferreInPatients = new ArrayList<Integer>();

		if (quarterFrom != null && quarterTo == null)
			transferreInPatients = service.getAllPatientsTransferredIn(quarterFrom, null);
		if (quarterFrom == null && quarterTo != null)
			transferreInPatients = service.getAllPatientsTransferredIn(null, quarterTo);
		if (quarterFrom != null && quarterTo != null)
			transferreInPatients = service.getAllPatientsTransferredIn(quarterFrom, quarterTo);

		for (Integer id : patIds2) {
			Patient p = Context.getPatientService().getPatient(id);
			if (!transferreInPatients.contains(id)) {
				objs.add(new Object[] { id, getWhenEnrolled(p), "Enrollment Date" });
			}

		}
		return objs;
	}

	/*********************************************************************************************************************************/
	public List<Object[]> getPatientsStartedART(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {

		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		StringBuffer strBuffer = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		List<Object[]> objs = new ArrayList<Object[]>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		if (gender != null) {
			queryPortion.append(" FROM orders o ");
			queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			queryPortion.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			queryPortion.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			queryPortion.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
			queryPortion.append(" AND p.gender= '" + gender + "'");
		} else {
			queryPortion.append(" FROM orders o ");
			queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			queryPortion.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			queryPortion.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			queryPortion.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
		}

		SQLQuery query = null;

		if (quarterFrom != null && quarterTo == null) {

			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) < ");
			strBuffer.append("'" + df.format(quarterFrom) + "'" + " , " + " true " + " ," + "false)  ");
			strBuffer.append(queryPortion);
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(quarterFrom)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' "
					+ df.format(quarterFrom) + " ')) ");
			strBuffer.append(" group by o.patient_id ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		}
		if (quarterFrom == null && quarterTo != null) {

			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) < ");
			strBuffer.append("'" + df.format(quarterTo) + "'" + " , " + " true " + " ," + "false) ");
			strBuffer.append(queryPortion);
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(quarterTo)
					+ " ')) ");
			strBuffer.append(" group by o.patient_id ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		}
		if (quarterFrom != null && quarterTo != null) {

			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) >= ");
			strBuffer.append("'" + df.format(quarterFrom) + "'" + " AND (select min(o.date_activated)) <='"
					+ df.format(quarterTo) + "'" + " , " + " true " + " ," + "false)");
			strBuffer.append(queryPortion);
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(quarterTo)
					+ " ')) ");
			strBuffer.append(" group by o.patient_id ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		}
		if (quarterFrom == null && quarterTo == null) {
			quarterTo = new Date();
			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) < ");
			strBuffer.append("'" + df.format(quarterTo) + "'" + " , " + " true" + " ," + "false) ");
			strBuffer.append(queryPortion);
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(quarterTo)
					+ " ')) ");
			strBuffer.append(" group by o.patient_id ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
		}

		List<Object[]> records = query.list();

		List<Integer> patId1 = new ArrayList<Integer>();

		int add = 0;

		for (Object[] obj : records) {
			add++;
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			patientStartedWhenMap.put(booleanValue, id);
		}

		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				patId1.add(patientStartedWhenMap.get(key));
			}
		}
		double patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();

		for (Integer id : patId1) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(o.date_activated) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(MIN(o.date_activated)) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(MIN(o.date_activated)) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  orders o ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=o.patient_id ");
			strbuf2.append(" INNER JOIN person s on s.person_id=p.patient_id  ");
			strbuf2.append(" AND p.patient_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					patIds2.add(id);
			}
		}
		for (Integer id : patIds2) {
			Patient p = Context.getPatientService().getPatient(id);
			Date when = getWhenPatientStarted(p);
			if (when != null)
				objs.add(new Object[] { id, when, "Start ARV" });
			else
				objs.add(new Object[] { id, "", "Start ARV" });
		}
		return objs;
	}

	/*************************************************************************************************************************************/
	@Override
	public List<Object[]> getAllPatientsEligibleForARVsButNotYetStarted(int programId, Date startDate, Date endDate) {

		 Session session = getSessionFactory().getCurrentSession();

		Date threeMonthsBeforeEndDate = getTreeMonthBefore(endDate);

		List<Object[]> eligiblePatients = new ArrayList<Object[]>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SQLQuery query1 = session.createSQLQuery("select DISTINCT pg.patient_id from patient_program pg "
				+ "inner join person pe on pg.patient_id = pe.person_id "
				+ "inner join patient pa on pg.patient_id = pa.patient_id "
				+ "inner join obs o on pg.patient_id = o.person_id "
				+ "inner join encounter en on pg.patient_id = en.patient_id " + "where o.concept_id <> "
				+ gpGetExitedFromCareConceptId() + " and (pg.voided = 0 and pe.voided = 0 and o.voided = 0 "
				+ " and pa.voided = 0 and en.voided = 0) and (o.concept_id =  "
				+ Integer.parseInt(QuarterlyReportUtil.gpGetCD4CountConceptId())
				+ " and (cast(o.obs_datetime as DATE)) >= '" + df.format(startDate)
				+ "' and (cast(o.obs_datetime as DATE)) <= '" + df.format(endDate) + "' ) " + "and pg.program_id= "
				+ programId + " and pg.date_completed is null ");

		List<Integer> patientIds1 = query1.list();

		List<Date> maxReturnVisitDay = new ArrayList<Date>();

		for (Integer patientId : patientIds1) {

			SQLQuery query2 = session.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
					+ gpGetExitedFromCareConceptId() + " and o.person_id=" + patientId);

			List<Integer> patientIds2 = query2.list();

			if (patientIds2.size() == 0) {

				try {

					SQLQuery query2Date = session
							.createSQLQuery("select cast(max(obs_datetime) as DATE) from obs where concept_id = "
									+ Integer.parseInt(QuarterlyReportUtil.gpGetCD4CountConceptId())
									+ " and (select cast(max(obs_datetime) as DATE)) >= '" + df.format(startDate)
									+ "' and (select cast(max(obs_datetime) as DATE)) <= '" + df.format(endDate)
									+ "' and value_numeric is not null and concept_id <>  "
									+ gpGetExitedFromCareConceptId() + " and voided=0 and person_id = " + patientId);

					Date maxObsDateTimeCD4Count = (Date) query2Date.uniqueResult();

					String dateStr = "(" + maxObsDateTimeCD4Count.toString() + ")";

					SQLQuery query3 = session.createSQLQuery("select value_numeric from obs where concept_id = "
							+ Integer.parseInt(QuarterlyReportUtil.gpGetCD4CountConceptId()) + " and obs_datetime = '"
							+ maxObsDateTimeCD4Count + "' and value_numeric is not null and concept_id <>  "
							+ gpGetExitedFromCareConceptId() + " and voided=0 and person_id = " + patientId);

					List<Double> maxValueNumericCD4Count = query3.list();

					SQLQuery queryDate1 = session.createSQLQuery(
							"select cast(max(encounter_datetime)as DATE) from encounter where (select cast(max(encounter_datetime)as DATE))<= '"
									+ df.format(endDate) + "' and patient_id = " + patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session.createSQLQuery("select cast(max(value_datetime) as DATE )"
							+ "from obs where (select cast(max(value_datetime)as DATE))<= '" + df.format(endDate)
							+ "' and concept_id = "
							+ Integer.parseInt(QuarterlyReportUtil.gpGetReturnVisitDateConceptId())
							+ " and value_numeric is not null and person_id = " + patientId);

					maxReturnVisitDay = queryDate2.list();

					Double val = (maxValueNumericCD4Count.size() > 0) ? maxValueNumericCD4Count.get(0) : 400;

					if (val < 350.0) {

						SQLQuery query4 = session
								.createSQLQuery("select distinct pg.patient_id from patient_program pg "
										+ "inner join person pe on pg.patient_id = pe.person_id "
										+ "inner join patient pa on pg.patient_id = pa.patient_id "
										+ "inner join obs o on pe.person_id = o.person_id "
										+ "inner join encounter en on pg.patient_id = en.patient_id "
										+ "inner join orders ord on pg.patient_id = ord.patient_id "
										+ "inner join drug_order do on ord.order_id = do.order_id "
										+ "inner join drug d on do.drug_inventory_id = d.drug_id "
										+ "where d.concept_id in (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") "
										+ "and (cast(ord.date_activated as DATE)) <= '" + df.format(endDate)
										+ "' and pg.program_id= " + programId
										+ " and pg.date_completed is null and ord.patient_id =  " + patientId);

						List<Integer> patientIds4 = query4.list();

						if (patientIds4.size() == 0) {

							try {

								if ((maxReturnVisitDay.get(0)) != null && maxEnocunterDateTime.get(0) != null) {

									if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate.getTime()
											&& (maxEnocunterDateTime.get(0).getTime()) <= endDate.getTime())
											|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
													.getTime()
													&& (maxReturnVisitDay.get(0).getTime()) <= endDate.getTime())) {
										eligiblePatients.add(new Object[] { patientId, val.toString() + dateStr,
												"CD4 COUNT(Date)" });
									}
								}

								else if ((maxReturnVisitDay.get(0)) == null && maxEnocunterDateTime.get(0) != null) {

									if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate.getTime()
											&& (maxEnocunterDateTime.get(0).getTime()) <= endDate.getTime()) {

										eligiblePatients.add(new Object[] { patientId, val.toString() + dateStr,
												"CD4 COUNT(Date)" });

									}
								} else if ((maxReturnVisitDay.get(0).getTime() > endDate.getTime())
										&& maxReturnVisitDay.get(0) != null) {

									eligiblePatients.add(
											new Object[] { patientId, val.toString() + dateStr, "CD4 COUNT(Date)" });
								}

							}

							catch (Exception e) {
								// TODO: handle exception
							}

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return eligiblePatients;
	}

	/************************************************************************************************************************/
	public static Date addDaysToDate(Date date, int days) {
		// Initialized with date if specified
		Calendar cal = new GregorianCalendar();
		if (date != null)
			cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, days);
		return cal.getTime();
	}

	/*****************************************************************************************************************************************/
	/**
	 * concatenates drugs with the same startdate and separates those witch
	 * don't have the same start date
	 * 
	 * @param RegimenComponent
	 * @return String
	 */
	public String getRegimensAsString(Set<RegimenComponent> regimens) {
		StringBuffer sb = new StringBuffer();
		RegimenComponent components[] = regimens.toArray(new RegimenComponent[0]);
		for (int r = 0; r < components.length; r++) {
			RegimenComponent reg = components[r];
			RegimenComponent nextReg = r >= components.length - 1 ? null : components[r + 1];
			if (nextReg == null || !reg.getStartDate().equals(nextReg.getStartDate()))
				sb.append((new StringBuilder(String.valueOf(reg.toString()))).append(" ").toString());

			else {
				if (reg.getDrug() != null)
					sb.append((new StringBuilder(String.valueOf(reg.getDrug().getName()))).append("-").toString());
				else
					sb.append((new StringBuilder(String.valueOf(reg.getGeneric().getName()))).append("-").toString());
			}
		}

		return sb.toString();
	}

	/*****************************************************************************************************************************************/
	// logic is to look for patients who have
	// received encounter in the selected quarter
	// age is calculated basing on when the patient's last encounter's date
	@Override
	public List<Object[]> getPatientsRecievedHIVCare(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {

		StringBuffer strBuffer = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		SQLQuery query = null;

		SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd");

		if (gender != null) {
			queryPortion.append("select distinct pg.patient_id from patient_program pg ");
			queryPortion.append(" inner join person pe on pg.patient_id = pe.person_id ");
			queryPortion.append(" and pe.gender = '" + gender + "'");
			queryPortion.append(" inner join patient pa on pg.patient_id = pa.patient_id ");
			queryPortion.append(" inner join orders o on o.patient_id = pa.patient_id ");
			queryPortion.append(" inner join encounter en on pa.patient_id = en.patient_id ");
			queryPortion.append(" inner join obs obs on obs.person_id = pe.person_id ");
			queryPortion.append(" and pg.program_id =  " + Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			// queryPortion.append(" and en.encounter_datetime <= '
			// "+df.format(quarterTo)+"'" );
			queryPortion.append(" and en.voided=0 and pg.voided=0 and pa.voided=0 and o.voided=0 ");
			queryPortion.append(
					" and o.concept_id<>1811 and  (cast(obs.obs_datetime as DATE))<='" + df.format(quarterTo) + "' ");
			queryPortion.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' and (pg.date_completed is null OR pg.date_completed >= '" + df.format(quarterTo) + "') ");
		} else {
			queryPortion.append("select distinct pg.patient_id from patient_program pg ");
			queryPortion.append(" inner join person pe on pg.patient_id = pe.person_id ");
			queryPortion.append(" inner join patient pa on pg.patient_id = pa.patient_id ");
			queryPortion.append(" inner join orders o on o.patient_id = pa.patient_id ");
			// queryPortion.append(" inner join encounter en on pa.patient_id =
			// en.patient_id ");
			queryPortion.append(" inner join obs obs on obs.person_id = pe.person_id ");
			queryPortion.append(" and pg.program_id =  " + Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			queryPortion.append(" and en.encounter_datetime <= ' " + df.format(quarterTo) + "'");
			queryPortion.append(" and en.voided=0 and pg.voided=0 and pa.voided=0 and o.voided=0 ");
			queryPortion.append(
					" and o.concept_id<>1811 and (cast(obs.obs_datetime as DATE))<='" + df.format(quarterTo) + "' ");
			queryPortion.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' and (pg.date_completed is null OR pg.date_completed >= '" + df.format(quarterTo) + "') ");
		}

		if (minAge != null && maxAge != null) {
			strBuffer.append(queryPortion);
			strBuffer.append(" AND TIMESTAMPDIFF(MONTH,pe.birthdate,CURDATE()) BETWEEN " + minAge + " AND " + maxAge);

		}
		if (minAge != null && maxAge == null) {
			strBuffer.append(queryPortion);
			strBuffer.append(" AND TIMESTAMPDIFF(MONTH,pe.birthdate,CURDATE()) >= " + minAge);
		}
		if (minAge == null && maxAge == null) {
			strBuffer.append(queryPortion);
		}

		// log.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
		// "+strBuffer.toString());

		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		List<Integer> patientsReceivedHIVCare = query.list();
		List<Integer> exitedInThePeriod = QuarterlyReportUtil
				.getConvertToType(getPatientsExitedInThePeriod(null, quarterTo));
		// log.info("eaaaaaaaaaaaaaaaaaaaaaaaaaa "+exitedInThePeriod);
		List<Object[]> obj = new ArrayList<Object[]>();
		for (Integer id : patientsReceivedHIVCare) {
			Patient patient = Context.getPatientService().getPatient(id);
			// if(!exitedInThePeriod.contains(id))
			obj.add(new Object[] { id, df.format(getMaxEncounter(patient, quarterTo)),
					"Last Encounter During The Quarter" });
		}
		return obj;
	}

	/******************************************************************************************************************************/
	public List<Integer> getPatientsLostOnFollowup(Date endDate) {

		StringBuffer strBuffer = new StringBuffer();
		SQLQuery query = null;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Integer> patients = new HashMap<String, Integer>();

		// Date threeMonthsBeforeEndDate = addDaysToDate(endDate, -92);

		if (endDate == null) {
			endDate = new Date();
		}

		strBuffer.append(
				" SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id = pe.person_id ");
		strBuffer.append(" INNER JOIN obs ob ON ob.person_id = pe.person_id WHERE ob.concept_id =  "
				+ gpGetExitedFromCareConceptId());
		strBuffer.append(" AND value_coded=1743");
		strBuffer.append(checkObsDate(null, endDate));

		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		List<Integer> records = query.list();

		// int add = 0;
		//
		// for (Object[] obj : records) {
		// add++;
		//
		// Integer id = (Integer) obj[0];
		// String booleanValue = obj[1].toString() + add + "";
		//
		// patients.put(booleanValue, id);
		// }
		// List<Integer> patientIds = new ArrayList<Integer>();
		// for (String key : patients.keySet()) {
		// if (key.charAt(0) == '1') {
		// patientIds.add(patients.get(key));
		// }
		// }

		return records;

	}

	/*************************************************************************************************************/

	// its used to see the get the patients started ART at HC but who left at
	// the end of quarter
	//
	public List<Integer> getAllNewPatientOnARTStartedAtTheOfQter(Date start, Date end) {
		StringBuffer strBuffer = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		strBuffer.append("SELECT o.patient_id, ");
		strBuffer.append("IF((select min(o.date_activated)) BETWEEN ");
		strBuffer.append("'" + df.format(start) + "'" + " AND " + "'" + df.format(end) + "'" + " , " + " true " + " ,"
				+ "false)");
		strBuffer.append(" FROM orders o ");
		strBuffer.append(" INNER JOIN patient p on o.patient_id=p.patient_id");
		strBuffer.append(" INNER JOIN obs obs on p.patient_id=obs.person_id and o.concept_id<> "
				+ Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId()));
		strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
		// strBuffer.append(" INNER JOIN drug d on
		// dro.drug_inventory_id=d.drug_id ");
		strBuffer.append(" AND o.concept_id in  (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
		strBuffer.append(" group by o.patient_id ");

		SQLQuery newOnARTDuringTheQterQuery = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		List<Object[]> newOnARTDuringTheQter = newOnARTDuringTheQterQuery.list();

		int add = 0;
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		for (Object[] obj : newOnARTDuringTheQter) {
			add++;

			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";

			patientStartedWhenMap.put(booleanValue, id);
		}
		List<Integer> patientIds = new ArrayList<Integer>();
		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				patientIds.add(patientStartedWhenMap.get(key));
			}
		}

		return patientIds;
	}

	/*************************************************************************************************************/
	// ///////////////////////who stopped all ART buy the end of the quarter
	@Override
	public List<Object[]> getPatientStoppeART(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge, List<Object[]> patientIds) {

		List<Object[]> patientsStoppeARV = new ArrayList<Object[]>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		StringBuffer portion = new StringBuffer();
		if (!gender.equals("")) {
			portion.append(" select DISTINCT p.patient_id FROM patient_program pg ");
			portion.append(" INNER JOIN patient p on p.patient_id=pg.patient_id ");
			portion.append(" INNER JOIN orders o on o.patient_id = p.patient_id ");
			portion.append(" INNER JOIN person pe on pe.person_id=p.patient_id and pe.gender = '" + gender + "'");
			portion.append(" INNER JOIN obs ob on ob.person_id=pe.person_id AND ob.concept_id = "
					+ gpGetExitedFromCareConceptId());
			portion.append(" AND ob.value_coded IN(1067,3580,3490) ");
			portion.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id  ");
			portion.append(" AND o.concept_id in (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			portion.append("AND o.date_stopped <= '" + df.format(quarterTo) + "'");

		} else if (gender.equals("")) {
			portion.append(" select DISTINCT p.patient_id FROM patient_program pg ");
			portion.append(" INNER JOIN patient p on p.patient_id=pg.patient_id ");
			portion.append(" INNER JOIN orders o on o.patient_id = p.patient_id ");
			portion.append(" INNER JOIN person pe on pe.person_id=p.patient_id ");
			portion.append(" INNER JOIN obs ob on ob.person_id=pe.person_id AND ob.concept_id = "
					+ gpGetExitedFromCareConceptId());
			portion.append(" AND ob.value_coded IN(1067,3580,3490) ");
			portion.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id ");
			portion.append(" AND o.concept_id in (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			portion.append("AND o.date_stopped <= '" + df.format(quarterTo) + "'  ");
		}

		SQLQuery queryStop = sessionFactory.getCurrentSession().createSQLQuery(portion.toString());

		List<Integer> patientsWhoStopped1 = queryStop.list();

		List<Object> o = new ArrayList<Object>();
		for (Integer id : patientsWhoStopped1) {
			o.add(new Object[] { id, "", "" });
		}

		List<Object[]> patsNoLongerPregnant = getPatientsNoLongerPreg(quarterTo);

		@SuppressWarnings("unchecked")
		List<Object[]> noLongerPregAndStopped = (List<Object[]>) union(o, patsNoLongerPregnant);

		List<Integer> stoppedPatients = new ArrayList<Integer>();

		List<Integer> transOutPats = QuarterlyReportUtil
				.getConvertToType(getPatientsTransferredOut(quarterFrom, quarterTo, gender, minAge, maxAge));

		List<Integer> lostPatients = getPatientsLostOnFollowup(quarterTo);

		List<Integer> exitedPatients = QuarterlyReportUtil
				.getConvertToType(getPatientsExitedInThePeriod(null, quarterTo));

		int patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();
		for (Object[] id : noLongerPregAndStopped) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(o.date_stopped) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(o.date_stopped) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(o.date_stopped) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  orders o ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=o.patient_id ");
			strbuf2.append(" INNER JOIN person s on s.person_id=p.patient_id  ");
			strbuf2.append(" AND p.patient_id= " + id[0]);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					if (!lostPatients.contains(id))
						patIds2.add((Integer) id[0]);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					if (!lostPatients.contains(id[0]))
						patIds2.add((Integer) id[0]);
			}
		}
		// log.info("vpppppppppppppppppppppppppppppppp "+transOutPats);
		for (Integer id : patIds2) {
			if (!transOutPats.contains(id))
				patientsStoppeARV.add(new Object[] { id, "", "" });
		}
		return patientsStoppeARV;
	}

	/*************************************************************************************************************/

	public List<Object[]> getPatientsNoLongerPreg(Date endqter) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SQLQuery queryNoLongerPreg = sessionFactory.getCurrentSession()
				.createSQLQuery("select patient_id from orders where date_stopped < 'CURDATE()' "
						+ "and order_reason=1748 and date_stopped <=' " + df.format(endqter) + "'");
		List<Integer> patsNoLongerPregnant = queryNoLongerPreg.list();
		// log.info("ddddddddddddddddddddddddddddd "+patsNoLongerPregnant);
		List<Object[]> obj = new ArrayList<Object[]>();
		for (Object[] ob : obj) {
			obj.add(new Object[] { ob[0], "", "" });
		}
		return obj;
	}

	public Boolean isAllDrugsStopped(Patient patient, Date when) {

		Boolean isStopped = false;
		SQLQuery query = null;

		StringBuffer strb = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			strb.append("SELECT DISTINCT o.patient_id , ");
			strb.append("IF((select date_activated < 'CURDATE()' AND date_stopped > 'CURDATE()' and o.date_stopped<='" + df.format(when) + "') , 1 , 0)");
			strb.append("FROM drug_order d ");
			strb.append("INNER JOIN orders o on o.order_id=d.order_id ");
			// strb.append("INNER JOIN drug g on g.drug_id=d.drug_inventory_id
			// ");
			strb.append(" AND o.patient_id= " + patient.getPatientId());
			strb.append(" and o.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");

			List<Integer> values = new ArrayList<Integer>();

			query = sessionFactory.getCurrentSession().createSQLQuery(strb.toString());

			List<Object[]> records = query.list();

			for (Object[] obj : records) {
				String booleanValue = obj[1].toString();

				values.add(Integer.parseInt(booleanValue));
			}

			if (!values.contains(0)) {
				isStopped = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isStopped;

	}

	/********************************************************************************************************************/
	@Override
	public List<Object[]> getPatientExitedFromCare(Integer conceptId, Date quarterFrom, Date quarterTo, String gender,
			Integer minAge, Integer maxAge, List<Object[]> patientIds) {

		StringBuffer strBuf = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		strBuf.append(
				" SELECT DISTINCT pa.patient_id FROM patient pa inner join person ps on pa.patient_id=ps.person_id ");
		strBuf.append(" INNER JOIN obs ob on ob.person_id=ps.person_id and ob.concept_id= "
				+ gpGetExitedFromCareConceptId() + " and ob.value_coded = " + conceptId);
		strBuf.append(" AND ob.obs_datetime <= '" + df.format(quarterTo) + "'");
		strBuf.append(" AND ob.voided=0 AND ps.gender='" + gender + "'");

		SQLQuery queryExited = sessionFactory.getCurrentSession().createSQLQuery(strBuf.toString());

		List<Integer> patientsExited = queryExited.list();

		List<Integer> lostPatients = getPatientsLostOnFollowup(quarterTo);

		List<Object[]> objs = new ArrayList<Object[]>();

		int patientAge = 0;

		for (Object[] ob : patientIds) {
			Patient patient = Context.getPatientService().getPatient((Integer) ob[0]);

			Date cal = null;
			Date deathdate = patient.getDeathDate();
			Date transOutDate = getWhenTransOut(patient);
			Date lostDate = getWhenLost(patient);

			if (deathdate != null)
				cal = deathdate;
			else if (transOutDate != null)
				cal = transOutDate;
			else if (lostDate != null)
				cal = lostDate;

			Date birthdate = patient.getBirthdate();

			try {
				if (cal != null)
					patientAge = QuarterlyReportUtil.calculateAge(cal, birthdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge) {
					if (patientsExited.contains(ob[0]))
						objs.add(new Object[] { ob[0], "", "" });
				}
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge) {
					if (patientsExited.contains(ob[0]))
						objs.add(new Object[] { ob[0], "", "" });
				}
			}
		}

		return objs;

	}

	/********************************************************************************************************************/
	@Override
	public List<Object[]> getAllARVPatientsLostOnFollowUp(Date quarterFrom, Date quarterTo, List<Object[]> patientIds) {

		List<Object[]> patientIdsLost = new ArrayList<Object[]>();

		Date threeMonthsBeforeEndDate = getTreeMonthBefore(quarterTo);

		if (quarterTo == null) {
			quarterTo = new Date();
		}

		List<Object[]> deadPatientsObj = getPatientExitedFromCare(1742, null, quarterTo, null, null, null, patientIds);
		List<Object[]> transfOutPatientsObj = getPatientExitedFromCare(1744, null, quarterTo, null, null, null,
				patientIds);
		List<Object[]> patientsStoppedArvObj = getPatientStoppeART(null, quarterTo, "", null, null, patientIds);

		List<Integer> deadPatientIds = QuarterlyReportUtil.getConvertToType(deadPatientsObj);
		List<Integer> transfOutPatientIds = QuarterlyReportUtil.getConvertToType(transfOutPatientsObj);
		List<Integer> stoppedArvPatientIds = QuarterlyReportUtil.getConvertToType(patientsStoppedArvObj);

		for (Object[] id : patientIds) {

			Integer patientId = (Integer) id[0];

			SQLQuery queryDate1 = sessionFactory.getCurrentSession()
					.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
							+ "(select(cast(max(encounter_datetime)as Date))) <= '"
							+ QuarterlyReportUtil.getDateFormat(quarterTo) + "' and patient_id = " + patientId);

			Date maxEncounterDateTime = (Date) queryDate1.uniqueResult();

			SQLQuery queryDate2 = sessionFactory.getCurrentSession()
					.createSQLQuery("select cast(max(value_datetime) as DATE ) "
							+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
							+ QuarterlyReportUtil.getDateFormat(quarterTo) + "' and concept_id = "
							+ Integer.parseInt(QuarterlyReportUtil.gpGetReturnVisitDateConceptId())
							+ " and person_id = " + patientId);

			Date maxReturnVisitDay = (Date) queryDate2.uniqueResult();

			List<Object[]> notLostPatients = new ArrayList<Object[]>();

			if ((maxReturnVisitDay != null) && (maxEncounterDateTime != null)) {

				if (((maxEncounterDateTime.getTime()) >= threeMonthsBeforeEndDate.getTime()
						&& (maxEncounterDateTime.getTime()) <= quarterTo.getTime())
						|| ((maxReturnVisitDay.getTime()) >= threeMonthsBeforeEndDate.getTime()
								&& (maxReturnVisitDay.getTime()) <= quarterTo.getTime())) {

					notLostPatients.add(new Object[] { patientId, "", "" });

				} else {
					if (!transfOutPatientIds.contains(patientId))
						if (!deadPatientIds.contains(patientId))
							if (!stoppedArvPatientIds.contains(patientId))
								// if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId),
								// quarterTo))
								// if(isLastRegimenProphy(Context.getPatientService().getPatient(patientId)))
								patientIdsLost.add(new Object[] { patientId, "", "" });
				}
			}

			else if ((maxReturnVisitDay == null) && (maxEncounterDateTime != null)) {

				if ((maxEncounterDateTime.getTime()) >= threeMonthsBeforeEndDate.getTime()
						&& (maxEncounterDateTime.getTime()) <= quarterTo.getTime()) {
					notLostPatients.add(new Object[] { patientId, "", "" });

				} else {
					if (!transfOutPatientIds.contains(patientId))
						if (!deadPatientIds.contains(patientId))
							if (!stoppedArvPatientIds.contains(patientId))
								// if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId),
								// quarterTo))
								// if(isLastRegimenProphy(Context.getPatientService().getPatient(patientId)))
								patientIdsLost.add(new Object[] { patientId, "", "" });
				}
			}

			else if (maxEncounterDateTime == null && maxReturnVisitDay == null) {
				if (!transfOutPatientIds.contains(patientId))
					if (!deadPatientIds.contains(patientId))
						if (!stoppedArvPatientIds.contains(patientId))
							// if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId),
							// quarterTo))
							// if(isLastRegimenProphy(Context.getPatientService().getPatient(patientId)))
							patientIdsLost.add(new Object[] { patientId, "", "" });

			}

		}

		return patientIdsLost;
	}

	/*******************************************************************************************************************************/

	public Date getTreeMonthBefore(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT DATE_SUB(CAST('" + df.format(date) + "' AS DATE), INTERVAL 3 MONTH)");

		return ((Date) query.uniqueResult());
	}

	/*******************************************************************************************************************************/

	public Date getSixMonthBefore(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT DATE_SUB(CAST('" + df.format(date) + "' AS DATE), INTERVAL 6 MONTH)");

		return ((Date) query.uniqueResult());
	}

	/*******************************************************************************************************************************/

	public Date getTwelveMonthBefore(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT DATE_SUB(CAST('" + df.format(date) + "' AS DATE), INTERVAL 12 MONTH)");

		return ((Date) query.uniqueResult());
	}

	/********************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsTransferreIn(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {

		StringBuffer strBuf = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		List<Object[]> objs = new ArrayList<Object[]>();

		if (quarterTo == null) {
			quarterTo = new Date();
		}

		List<Integer> patientsOnART = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		StringBuffer artBuf = new StringBuffer();

		artBuf.append("SELECT distinct o.patient_id FROM orders o  ");
		artBuf.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
		artBuf.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
		artBuf.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
		artBuf.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
		artBuf.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
		artBuf.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
		artBuf.append(" AND o.date_activated <= '" + df.format(quarterTo) + "'");

		SQLQuery artQuery = sessionFactory.getCurrentSession().createSQLQuery(artBuf.toString());
		
		patientsOnART = artQuery.list();

		if (gender == null) {
			queryPortion.append(" SELECT DISTINCT o.person_id FROM obs o ");
			queryPortion.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
			queryPortion.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
			queryPortion.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
					+ " AND o.value_coded=1065  ");
		} else {

			queryPortion.append(" SELECT DISTINCT  o.person_id FROM obs o ");
			queryPortion.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
			queryPortion.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
			queryPortion.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
					+ " AND o.value_coded=1065 ");
			queryPortion.append(" AND pe.gender = '" + gender + "' ");

		}

		if (quarterFrom != null && quarterTo != null) {
			strBuf.append(queryPortion);
			strBuf.append(" and o.obs_datetime BETWEEN  '" + df.format(quarterFrom) + "' ");
			strBuf.append(" AND '" + df.format(quarterTo) + "' ");

		}
		if (quarterFrom != null && quarterTo == null) {
			strBuf.append(queryPortion);
			strBuf.append(" and o.obs_datetime <  '" + df.format(quarterFrom) + "' ");

		}
		if (quarterFrom == null && quarterTo != null) {
			strBuf.append(queryPortion);
			strBuf.append(" and o.obs_datetime <  '" + df.format(quarterTo) + "' ");

		}

		// log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvvv "+strBuf.toString());

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strBuf.toString());

		List<Integer> patientTransferIn = query.list();
		List<Integer> patIds2 = new ArrayList<Integer>();

		double patientAge = 0;

		// List<Integer> patIds2 = new ArrayList<Integer>();
		for (Integer id : patientTransferIn) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(o.date_activated) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(MIN(o.date_activated)) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(MIN(o.date_activated)) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  orders o ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=o.patient_id ");
			strbuf2.append(" INNER JOIN person s on s.person_id=p.patient_id  ");
			strbuf2.append(" AND p.patient_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					patIds2.add(id);
			}
		}
		for (Integer id : patIds2) {
			Patient patient = Context.getPatientService().getPatient(id);
			Date whenTransferredInd = getMaxTransferInDate(patient, quarterFrom, quarterTo);
			if (patientsOnART.contains(id)) {
				if (whenTransferredInd != null) {
					objs.add(new Object[] { id, df.format(whenTransferredInd), "Transfer-In Date" });
				} else {
					objs.add(new Object[] { id, "-", "Transfer-In Date" });
				}

			}
		}
		return objs;
	}

	/********************************************************************************************************************/
	public Date getDateXMonthAgo(Date quarterFrom, int monthAgo) {
		StringBuffer strBuf = new StringBuffer();
		SQLQuery query = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		strBuf.append("SELECT CAST(DATE_SUB( '" + df.format(quarterFrom) + "'" + " ,INTERVAL " + monthAgo
				+ " MONTH)AS DATE)");
		query = sessionFactory.getCurrentSession().createSQLQuery(strBuf.toString());
		Date date = (Date) query.uniqueResult();

		SQLQuery query1 = sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT LAST_DAY('" + df.format(date) + "');");

		Date lastDay = (Date) query1.uniqueResult();
		return lastDay;
	}

	/********************************************************************************************************************/
	@Override
	public List<Object[]> getNewPatientsOver6YearsOnART(Date startDate, Date endDate, String gender, Integer minAge,
			Integer maxAge) {
		StringBuffer strBuffer = new StringBuffer();

		List<Integer> patientsTransInDuringQter = QuarterlyReportUtil
				.getConvertToType(getPatientsTransferreIn(startDate, endDate, null, null, null));

		StringBuffer queryPortion = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Object[]> objs = new ArrayList<Object[]>();
		if (gender != null) {
			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) BETWEEN ");
			strBuffer.append(" '" + df.format(startDate) + "' AND '" + df.format(endDate) + "' " + " , " + " true "
					+ " ," + "false) ");
			strBuffer.append(" FROM orders o ");
			strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			strBuffer.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			strBuffer.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			strBuffer.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
			strBuffer.append(" AND p.gender= '" + gender + "'");
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(endDate)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)
					+ " ')) ");
			strBuffer.append(" group by o.patient_id ");
		} else {
			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) BETWEEN ");
			strBuffer.append(" '" + df.format(startDate) + "' AND '" + df.format(endDate) + "' " + " , " + " true "
					+ " ," + "false) ");
			strBuffer.append(" FROM orders o ");
			strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			strBuffer.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			strBuffer.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			strBuffer.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
			strBuffer.append(" and pg.date_enrolled <= '" + df.format(endDate)
					+ "' AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)
					+ " ')) ");
			strBuffer.append(" group by o.patient_id ");
		}
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		// >>>>>>>>>>>>>>>>>>>>SEE IF PATIENTS IS NOT ART TRANFER-IN
		SQLQuery transferInStr = sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT DISTINCT o.person_id FROM obs o "
						+ "INNER JOIN person pe ON pe.person_id=o.person_id "
						+ "INNER JOIN patient pat ON pe.person_id=pat.patient_id "
						+ "INNER JOIN orders d ON d.patient_id=pat.patient_id " + "AND o.concept_id= "
						+ Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId()) + " AND value_coded=1065 "
						+ " AND o.obs_datetime BETWEEN  ' " + df.format(startDate) + "' AND ' " + df.format(endDate)
						+ "' " + " AND d.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");

		List<Integer> transferredInDuringQter = transferInStr.list();

		List<Object[]> newOnARTDuringTheQter = query.list();

		List<Integer> patId1 = new ArrayList<Integer>();

		int add = 0;
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		for (Object[] obj : newOnARTDuringTheQter) {
			add++;
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			patientStartedWhenMap.put(booleanValue, id);
		}

		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				// if
				// (!patientsTransInDuringQter.contains(patientStartedWhenMap.get(key)))
				if (!transferredInDuringQter.contains(patientStartedWhenMap.get(key)))
					patId1.add(patientStartedWhenMap.get(key));
			}
		}
		double patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();
		for (Integer id : patId1) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(o.date_activated) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(MIN(o.date_activated)) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(MIN(o.date_activated))  < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  orders o ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=o.patient_id ");
			strbuf2.append(" INNER JOIN person s on s.person_id=p.patient_id  ");
			strbuf2.append(" AND p.patient_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if ((patientAge / 12) > 6) {
				Patient p = Context.getPatientService().getPatient(id);
				Date when = getWhenPatientStarted(p);
				objs.add(new Object[] { p.getPatientId(), df.format(when), "Start ARVs" });

			}
		}

		return objs;

	}

	/********************************************************************************************************************/
	// will be used to get number of patients on a date but who have concept
	// like CD4 Count
	public List<Integer> getPatientsWthConcept(List<Integer> patients, int conceptId, Date date) {

		List<Integer> patientsWithCD4Count = getAllPatientWithObs(conceptId, date);
		List<Integer> newOnARTWithConcept = new ArrayList<Integer>();

		for (Integer patientId : patients) {
			if (patientsWithCD4Count.contains(patientId)) {
				newOnARTWithConcept.add(patientId);
			}
		}

		return newOnARTWithConcept;
	}

	/************************************************************************************************************************/
	public List<Integer> getAllPatientWithObs(int conceptId, Date obsdate) {

		StringBuffer strBuffer = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = null;

		strBuffer.append(" select DISTINCT p.patient_id, ");
		strBuffer.append(
				" IF(obs.obs_datetime <= " + "'" + df.format(obsdate) + "'" + " , " + " true " + " ," + "false)");
		strBuffer.append(" FROM obs obs INNER JOIN person ps on ps.person_id=obs.person_id ");
		strBuffer.append(" INNER JOIN patient p on p.patient_id=ps.person_id ");
		strBuffer.append(" AND DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(ps.birthdate)), '%Y')+0 >= 6 ");
		strBuffer.append(" AND  obs.concept_id=  " + conceptId);
		strBuffer.append(" ORDER BY obs.obs_datetime ASC ");

		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		List<Object[]> newOnARTDuringTheQter = query.list();

		int add = 0;
		Map<String, Integer> newPatientsMap = new HashMap<String, Integer>();

		for (Object[] obj : newOnARTDuringTheQter) {
			add++;

			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";

			newPatientsMap.put(booleanValue, id);
		}
		List<Integer> patientIds = new ArrayList<Integer>();
		for (String key : newPatientsMap.keySet()) {
			if (key.charAt(0) == '1') {
				Patient patient = Context.getPatientService().getPatient(newPatientsMap.get(key));

				patientIds.add(patient.getPatientId());
			}
		}

		return patientIds;
	}

	/*****************************************************************************************************************************************/

	@Override
	public List<Integer> getAllPatientWithObsInPeriod(int conceptId, Date startDate, Date endDate) {
		List<Integer> patientIds = new ArrayList<Integer>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer strBuffer = new StringBuffer();

			SQLQuery query = null;

			// if (startDate!=null && endDate!=null && conceptId!=0) {
			strBuffer.append(" select DISTINCT p.patient_id, ");
			strBuffer.append(" IF(obs.obs_datetime BETWEEN '" + df.format(startDate) + "' AND '" + df.format(endDate)
					+ "' , " + " true " + " ," + "false)");
			strBuffer.append(" FROM obs obs INNER JOIN person ps on ps.person_id=obs.person_id ");
			strBuffer.append(" INNER JOIN patient p on p.patient_id=ps.person_id ");
			strBuffer.append(" AND DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(ps.birthdate)), '%Y')+0 >= 6 ");
			strBuffer.append(" AND  obs.concept_id=  " + conceptId);
			strBuffer.append(" ORDER BY obs.obs_datetime ASC ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

			List<Object[]> newOnARTDuringTheQter = query.list();

			int add = 0;
			Map<String, Integer> newPatientsMap = new HashMap<String, Integer>();

			for (Object[] obj : newOnARTDuringTheQter) {
				add++;

				Integer id = (Integer) obj[0];
				String booleanValue = obj[1].toString() + add + "";

				newPatientsMap.put(booleanValue, id);
			}

			for (String key : newPatientsMap.keySet()) {
				if (key.charAt(0) == '1') {

					patientIds.add(newPatientsMap.get(key));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("!!!!!!!!!!!!!!!!!!!!!!!!!! " + e.getMessage());
		}

		return patientIds;
	}

	/********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public Collection union(Collection coll1, Collection coll2) {
		Set<Object[]> union = new HashSet<Object[]>(coll1);
		union.addAll(new HashSet<Object[]>(coll2));
		return new ArrayList(union);

	}

	/********************************************************************************************************************/
	@Override
	public List<Object[]> getPatientOnAllDrugs(List<Integer> listDrugIds, List<Integer> patientIds, Date endDate) {

		List<Object[]> objects = new ArrayList<Object[]>();

		int regimenSize = 1;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (Integer patientId : patientIds) {
				SQLQuery query = null;
				 Session session = sessionFactory.getCurrentSession();

				StringBuffer strbuf = new StringBuffer();

				strbuf.append(" SELECT DISTINCT d.drug_inventory_id, ");
				strbuf.append(" IF(((o.date_activated <='" + df.format(endDate) + "' AND (date_activated < 'CURDATE()' AND date_stopped > 'CURDATE()')) ");
				strbuf.append(" OR (o.date_activated <='" + df.format(endDate) + "' AND o.date_stopped >='"
						+ df.format(endDate) + "')),1,0) ");
				strbuf.append(" FROM orders o ");
				strbuf.append(" INNER JOIN drug_order d ON o.order_id=d.order_id AND o.patient_id= " + patientId);
				strbuf.append(" AND o.concept_id IN(" + QuarterlyReportUtil.gpGetARVConceptIds() + ")"
				// strbuf.append(" AND o.concept_id NOT IN("+
				// QuarterlyReportUtil.gpGetProphylaxisDrugConceptIds() + ")"
				);

				query = session.createSQLQuery(strbuf.toString());

				List<Object[]> records = query.list();
				Map<String, Integer> map = new HashMap<String, Integer>();
				List<Integer> regimenDrugs = new ArrayList<Integer>();

				int add = 0;

				for (Object[] obj : records) {
					add++;

					Integer id1 = (Integer) obj[0];
					String booleanValue = obj[1].toString() + add + "";

					map.put(booleanValue, id1);
				}

				for (String key : map.keySet()) {
					if (key.charAt(0) == '1') {
						regimenDrugs.add(map.get(key));
					}
				}

				if (getPatientsOnAll(listDrugIds, regimenDrugs)) {
					// patientOnDrugs.add(p.getPatientId());
					Patient patient = Context.getPatientService().getPatient(patientId);
					Date when = getWhenPatientStarted(patient);
					objects.add(new Object[] { patient.getPatientId(), df.format(when), "Start ARV" });

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return objects;
	}

	/*********************************************************************************************************************/
	/**
	 * allows to compare to arraylists of integers
	 * 
	 * @param drugSelected
	 * @param regimenDrugs
	 * @return boolean value
	 */
	public boolean getPatientsOnAll(List<Integer> drugSelected, List<Integer> regimenDrugs) {
		boolean found = false;

		if (regimenDrugs.size() >= drugSelected.size() && regimenDrugs.containsAll(drugSelected)) {
			for (Integer r : regimenDrugs) {
				if (drugSelected.contains(r) || QuarterlyReportUtil.gpGetProphylaxisAsIntegers().contains(r)
						|| QuarterlyReportUtil.gpGetTBDrugIds().contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}

		}

		return found;
	}

	/*********************************************************************************************************************/
	/**
	 * allows to compare two arraylists of drug concept ids
	 * 
	 * @param drugSelected
	 * @param regimenDrugs
	 * @return boolean value
	 */
	public boolean getPatientsOnAllByConceptIds(List<Integer> drugSelectedByConcept,
			List<Integer> regimenDrugsByConcept) {
		boolean found = false;
		if (regimenDrugsByConcept.size() >= drugSelectedByConcept.size()
				&& regimenDrugsByConcept.containsAll(drugSelectedByConcept)) {
			for (Integer r : regimenDrugsByConcept) {
				if (drugSelectedByConcept.contains(r) || QuarterlyReportUtil.gpGetProphylaxisConceptIds().contains(r)
						|| QuarterlyReportUtil.gpGetTBDrugIds().contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}

		return found;
	}

	/*********************************************************************************************************************/
	public List<Integer> getPatientOnARTDuringTheQter(Date quarterStart, Date quarterEnd, Integer minAge,
			Integer maxAge) {
		SQLQuery query = null;

		List<Integer> records = null;
		StringBuffer strBuffer = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		queryPortion.append(
				"SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id in(SELECT DISTINCT p.patient_id FROM patient p ");
		queryPortion.append(
				"INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0 and o.date_stopped < 'CURDATE()' ");
		queryPortion.append(" INNER JOIN person ps on o.patient_id=ps.person_id ");
		queryPortion.append(
				" INNER JOIN drug_order dr ON dr.order_id = o.order_id " + checkInputDate(quarterStart, quarterEnd));
		queryPortion.append(" INNER JOIN drug d on d.drug_id= dr.drug_inventory_id ");
		queryPortion.append(" AND o.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");

		if (minAge != null && maxAge != null) {
			strBuffer.append(queryPortion);
			strBuffer.append(" AND DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(ps.birthdate)), '%Y')+0 BETWEEN  "
					+ minAge + " AND " + maxAge + " )");

		}
		if (minAge != null && maxAge == null) {
			strBuffer.append(queryPortion);
			strBuffer.append(
					" AND DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(ps.birthdate)), '%Y')+0 >=  " + minAge + " )");
		}

		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		records = query.list();

		return records;
	}

	/*********************************************************************************************************************/
	public String checkInputDate(Date startDate, Date endDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (startDate != null && endDate != null) {
			return "AND (o.date_activated BETWEEN '" + df.format(startDate) + "' AND '" + df.format(endDate) + "' )";
		} else if (endDate == null && startDate != null) {
			return "AND (o.date_activated > '" + df.format(startDate) + "' )";
		} else if (startDate == null && endDate != null) {
			return "AND (o.date_activated < '" + df.format(endDate) + "' )";
		} else if (startDate == null && endDate == null) {
			endDate = new Date();
			return "AND (o.date_activated < '" + df.format(endDate) + "' )";
		}
		return "";

	}

	/*********************************************************************************************************************/
	@Override
	public void exportQuarterlyData(HttpServletRequest request, HttpServletResponse response, List<Patient> patients,
			String filename, String title) throws IOException {
		ServletOutputStream op = response.getOutputStream();
		List<Object[]> listPatientHistory = new ArrayList<Object[]>();
		// List<Regimen> regimens = new ArrayList<Regimen>();
		List<PatientProgram> patientPrograms = new ArrayList<PatientProgram>();
		Patient patientsObj = new Patient();

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		if (Context.hasPrivilege("View Patient Names"))
			op.print("TRACnet ID,COHORT ID,FAMILY NAME,GIVEN NAME,GENDER,AGE,PROGRAM(Enrollment Date-dd/mm/yyyy");
		else
			op.print("TRACnet ID,COHORT ID,GENDER,AGE,PROGRAM(Enrollment Date-dd/mm/yyyy");

		HashMap<Patient, List<Regimen>> patientRegLists = new HashMap<Patient, List<Regimen>>();
		int maxRegHistorySize = 1;

		Concept CD4CountConcept = Context.getConceptService()
				.getConcept(Integer.parseInt(QuarterlyReportUtil.gpGetCD4CountConceptId()));
		Concept hivViralLoad = Context.getConceptService().getConcept(856);
		Concept weightConcept = Context.getConceptService().getConcept(5089);

		int patientMaxCD4Count = 0;
		int patientMaxVL = 0;

		for (Patient patient : patients) {
			RegimenHistory history = RegimenUtils.getRegimenHistory(patient);
			List<Regimen> regimenList = history.getRegimenList();
			patientRegLists.put(patient, regimenList);
			if (regimenList.size() > maxRegHistorySize)
				maxRegHistorySize = regimenList.size();

			List<String> patientCD4Count = getAllPatientObsList(patient, CD4CountConcept);
			List<String> patientVL = getAllPatientObsList(patient, hivViralLoad);

			if (patientCD4Count.size() > patientMaxCD4Count) {
				patientMaxCD4Count = patientCD4Count.size();
			}
			if (patientVL.size() > patientMaxVL) {
				patientMaxVL = patientVL.size();
			}

		}
		op.print(",FIRST VIRAL LOAD,LAST VIRAL LOAD");

		op.print(",FIRST WEIGHT(kg),LAST WEIGHT(kg)");

		for (int i = 0; i < patientMaxCD4Count - 1; i++) {
			op.print(",CD4 COUNT,Obs_Datetime ");
		}

		if (maxRegHistorySize != 1) {
			op.print(",REGIMEN START DATE,REGIMEN END DATE");
		}

		// regimen row (display regimen in excel sheet)
		int maxValue = patientMaxCD4Count * 2 + 15;

		op.println();

		QuarterlyReportingService service = Context.getService(QuarterlyReportingService.class);

		for (Patient patient : patients) {
			listPatientHistory.add(new Object[] { patient,
					Context.getProgramWorkflowService().getPatientPrograms(patient, null, null, null, null, null,
							false),
					patientRegLists.get(patient), service.getAllPatientObs(patient, CD4CountConcept),
					service.getAllPatientObsList(patient, hivViralLoad),
					service.getAllPatientObsList(patient, weightConcept), patient.getPatientIdentifier(4) });

		}

		if (listPatientHistory.size() > 0) {
			for (Object objects[] : listPatientHistory) {
				patientsObj = (Patient) objects[0];

				if (Context.hasPrivilege("View Patient Names")) {
					op.print(patientsObj.getPatientIdentifier() + "," + patientsObj.getPatientIdentifier(4) + ","
							+ patientsObj.getGivenName() + "," + patientsObj.getFamilyName() + ","
							+ patientsObj.getGender() + "," + patientsObj.getAge());
				} else {
					op.print(patientsObj.getPatientIdentifier() + "," + patientsObj.getPatientIdentifier(4) + ","
							+ patientsObj.getGender() + "," + patientsObj.getAge());
				}

				String patientProgramsAsString = "";
				ArrayList<String> pPrograms = new ArrayList<String>();
				patientPrograms = (List<PatientProgram>) objects[1];
				for (PatientProgram patientProgram : patientPrograms) {

					if (patientProgram.getDateEnrolled() != null) {
						pPrograms.add(patientProgram.getProgram().getName() + " ("
								+ QuarterlyReportUtil.getDateFormat(patientProgram.getDateEnrolled()) + ")");
					} else {
						pPrograms.add(patientProgram.getProgram().getName());
					}
					patientProgramsAsString = getStringFromArrayListOfString(pPrograms);
					patientProgramsAsString = patientProgramsAsString.substring(0,
							patientProgramsAsString.lastIndexOf(";"));
				}

				op.print("," + patientProgramsAsString);

				String CD4CountAndDate = (String) objects[3];
				ArrayList<String> HIViralLoads = (ArrayList<String>) objects[4];
				ArrayList<String> patientWeights = (ArrayList<String>) objects[5];

				String firstVL = null;
				String lastVL = null;
				if (HIViralLoads != null && HIViralLoads.size() != 0) {
					firstVL = HIViralLoads.get(0).toString();
					lastVL = HIViralLoads.get(HIViralLoads.size() - 1).toString();
				} else {
					firstVL = "-";
					lastVL = "-";
				}

				String firstWeight = null;
				String lastWeight = null;

				if (patientWeights != null && patientWeights.size() != 0) {
					firstWeight = patientWeights.get(0).toString();
					lastWeight = patientWeights.get(patientWeights.size() - 1).toString();
				} else {
					firstWeight = "-";
					lastWeight = "-";
				}

				op.print("," + firstVL + "," + lastVL);
				op.print("," + firstWeight + "," + lastWeight);

				// patient CD4 COUNT
				if (CD4CountAndDate.length() != 0) {
					op.print("," + CD4CountAndDate);

				}

				op.println();

				// empy string before displaying regimens
				String emptyStr = new String();
				for (int i = 1; i < maxValue - 1; i++) {
					emptyStr += ",";
				}

				Set<RegimenComponent> regimenComponent = new HashSet<RegimenComponent>();

				String patRegimens = "";

				SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
				Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();

				RegimenHistory history = RegimenUtils.getRegimenHistory(patientsObj);
				List<Regimen> regimens = history.getRegimenList();

				for (Regimen r : regimens) {
					regimenComponent = r.getComponents();

					if (r.getEndDate() == null) {
						r.setEndDate(new Date());
					}
					for (RegimenComponent rc : regimenComponent) {
						if (rc.getStopDate() != null)
							if (rc.getStopDate().getTime() <= r.getStartDate().getTime()) {
								componentsStopped.add(rc);

							}
					}

					regimenComponent.removeAll(componentsStopped);

					patRegimens = getRegimensAsString(regimenComponent);

					if (!patRegimens.toString().equals("")) {
						op.print(emptyStr + f.format(r.getStartDate()) + "," + f.format(r.getEndDate()) + ","
								+ patRegimens);
						op.println();
					} else {
						op.print(emptyStr + f.format(r.getStartDate()) + "," + f.format(r.getEndDate()) + "," + "-");
						op.println();
					}

				}

				op.println();

			}

		}

		op.flush();
		op.close();
	}

	/*********************************************************************************************************************/
	@Override
	public double[] getPatientsObsValues(Integer patientId, Concept concept) {

		SQLQuery query = null;

		double[] patientObsInArray = null;

		try {

			StringBuffer strb = new StringBuffer();

			strb.append(" SELECT DISTINCT obs.value_numeric ");
			strb.append(" FROM obs obs WHERE obs.person_id = " + patientId + " AND obs.concept_id= "
					+ concept.getConceptId());
			strb.append(" ORDER BY obs.value_numeric ASC ");

			query = sessionFactory.getCurrentSession().createSQLQuery(strb.toString());

			List<Double> patientObs = query.list();
			if (patientObs != null)
				patientObsInArray = new double[patientObs.size()];

			for (int i = 0; i < patientObs.size(); i++) {
				patientObsInArray[i] = patientObs.get(i);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return patientObsInArray;
	}

	/*********************************************************************************************************************/
	@Override
	public double calculateMedian(List<Double> a) {
		Collections.sort(a);
		double lower = 0;
		double upper = 0;

		if (a.size() % 2 == 1)
			return a.get((a.size() + 1) / 2 - 1);
		else {
			if (a.size() != 0)
				lower = a.get(a.size() / 2 - 1);
			if (a.size() != 0)
				upper = a.get(a.size() / 2);
			return (lower + upper) / 2.0;
		}

	}

	/*********************************************************************************************************************/
	@Override
	// who have done PHARMACY VISITE n months out of n
	public List<Object[]> getPatientsReceivedARVsForXmonthOutOfXmonth(List<Object[]> patientIds, Integer n) {

		List<Object[]> patients = new ArrayList<Object[]>();

		for (Object[] id : patientIds) {
			List<Date> nMonths = new ArrayList<Date>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(getWhenPatientStarted(Context.getPatientService().getPatient((Integer) id[0])));
			for (int co = 0; co < n; co++) {
				c.add(Calendar.MONTH, 1);
				nMonths.add(c.getTime());

			}
			if (hasPharmacyVisitInThePeriod(Context.getPatientService().getPatient((Integer) id[0]), nMonths) == true) {
				Patient p = Context.getPatientService().getPatient((Integer) id[0]);
				patients.add(new Object[] { id[0], sdf.format(getWhenPatientStarted(p)), "Start ARVs" });
			}

		}
		return patients;
	}

	/***************************************************************************************************************************/
	public Boolean hasPharmacyVisitInThePeriod(Patient p, List<Date> nMonths) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> obs = null;
		List<Integer> checkList = new ArrayList<Integer>();

		for (int i = 0; i < nMonths.size() - 1; i++) {

			/*
			 * see if the patient had pharmacy visit between a given month and
			 * the next month
			 */
			query = session.createSQLQuery("SELECT concept_id FROM obs where person_id= " + p.getPatientId()
					+ " AND concept_id=6189 AND value_coded=6191 AND obs_datetime BETWEEN '" + df.format(nMonths.get(i))
					+ "' AND '" + df.format(nMonths.get(i + 1)) + "'");

			obs = query.list();

			if (obs.size() != 0 ? checkList.add(1) : checkList.add(0))
				;
		}

		Boolean ret = false;
		if (!checkList.contains(0))
			ret = true;
		return ret;

	}

	/***************************************************************************************************************************/
	@Override
	public List<Object[]> getPregnantFemales(Date quarterStart, Date quarterEnd) {

		SQLQuery query = null;
		StringBuffer strb = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		queryPortion.append(" SELECT DISTINCT pa.patient_id FROM patient pa ");
		queryPortion.append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id and pg.program_id=1 ");
		queryPortion.append(" INNER JOIN person ps on ps.person_id=pa.patient_id and ps.gender='f' ");
		queryPortion.append(
				" INNER JOIN obs ob on ob.person_id=ps.person_id AND pg.date_completed is null AND pg.voided=0");

		// List<Integer>
		// newPatientsOnART=QuarterlyReportUtil.getConvertToType(getNewPatientsOnART(null,
		// quarterEnd, null, null, null));

		if (quarterStart != null && quarterEnd == null) {
			strb.append(queryPortion);
			strb.append(" AND pg.date_enrolled < '" + df.format(quarterStart) + "' ");
		}
		if (quarterStart == null && quarterEnd != null) {
			strb.append(queryPortion);
			strb.append(" AND pg.date_enrolled < '" + df.format(quarterEnd) + "' ");
		}
		if (quarterStart != null && quarterEnd != null) {
			strb.append(queryPortion);
			strb.append(" AND pg.date_enrolled BETWEEN '" + df.format(quarterStart) + "' AND '" + df.format(quarterEnd)
					+ "' ");
		}

		query = sessionFactory.getCurrentSession().createSQLQuery(strb.toString());

		List<Integer> pregnantPatientIds = query.list();

		List<Object[]> patientsNewOnARVPregnant = new ArrayList<Object[]>();

		for (Integer id : pregnantPatientIds) {
			patientsNewOnARVPregnant.add(new Object[] { id, "", "" });
		}

		return patientsNewOnARVPregnant;
	}

	/***************************************************************************************************************************/
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Object[]> SubtractACollection(Collection<Object[]> coll1, Collection<Object[]> coll2) {

		List<Integer> list1 = QuarterlyReportUtil.getConvertToType((List<Object[]>) coll1);
		List<Integer> list2 = QuarterlyReportUtil.getConvertToType((List<Object[]>) coll2);
		//
		//// List<Integer> remain = new ArrayList<Integer>();
		//
		List<Object[]> objects = new ArrayList<Object[]>();
		//
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> activePatientsOnART = QuarterlyReportUtil.subtract(list1, list2);

		for (Integer id : activePatientsOnART) {
			Date start = getWhenPatientStarted(Context.getPatientService().getPatient(id));
			if (start != null)
				objects.add(new Object[] { id, df.format(start), "Start ARVs" });
			else
				objects.add(new Object[] { id, "", "Start ARVs" });
		}

		return objects;
	}

	/***************************************************************************************************************************/
	@Override
	public List<List<Integer>> getRegimenComposition(List<Integer> patientIds, Date endDate) {

		List<List<Integer>> listOfRegimens = new ArrayList<List<Integer>>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (Integer id : patientIds) {
			SQLQuery query = null;
			SQLQuery query1 = null;
			 Session session = sessionFactory.getCurrentSession();

			StringBuffer strbuf = new StringBuffer();
			StringBuffer strbuf1 = new StringBuffer();

			// OPTION I. Consider patient drug orders by drug_inventory_id
			strbuf.append(" SELECT DISTINCT d.drug_inventory_id, ");
			strbuf.append(" IF((o.date_activated <='" + df.format(endDate)
					+ "' AND (date_activated < 'CURDATE()' AND date_stopped > 'CURDATE()') OR o.date_stopped >='" + df.format(endDate) + "') ");
			strbuf.append(" ,1,0) ");
			strbuf.append(" FROM orders o ");
			strbuf.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
			strbuf.append(" INNER JOIN person ps on ps.person_id=pa.patient_id ");
			strbuf.append(" INNER JOIN obs obs on obs.person_id=ps.person_id ");
			strbuf.append(" INNER JOIN drug_order d ON o.order_id=d.order_id AND o.patient_id= " + id);
			strbuf.append(" AND o.concept_id IN(" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");
			// strbuf.append(" AND d.drug_inventory_id NOT IN("+
			// QuarterlyReportUtil.gpGetProphylaxisDrugIds() + ")");

			query = session.createSQLQuery(strbuf.toString());

			// OPTION III. Considider patient drug orders concept_id
			strbuf1.append(" SELECT DISTINCT o.concept_id, ");
			strbuf1.append(" IF((o.date_activated <='" + df.format(endDate)
					+ "' AND (date_activated < 'CURDATE()' AND date_stopped > 'CURDATE()') OR o.date_stopped>='" + df.format(endDate) + "') ");
			strbuf1.append(" ,1,0) ");
			strbuf1.append(" FROM orders o ");
			strbuf1.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
			strbuf1.append(" INNER JOIN person ps on ps.person_id=pa.patient_id ");
			strbuf1.append(" INNER JOIN obs obs on obs.person_id=ps.person_id ");
			strbuf1.append(" INNER JOIN drug_order d ON o.order_id=d.order_id AND o.patient_id= " + id);
			strbuf1.append(" AND o.concept_id IN(" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");
			strbuf1.append(" AND o.concept_id ");

			// log.info("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
			// "+QuarterlyReportUtil.gpGetARVConceptIds());

			query1 = session.createSQLQuery(strbuf1.toString());

			List<Object[]> records = query1.list();
			// if (query.list().size() == 0)
			// records = query1.list();

			Map<String, Integer> map = new HashMap<String, Integer>();
			List<Integer> drugOfEachPatients = new ArrayList<Integer>();

			int add = 0;

			for (Object[] obj : records) {
				add++;

				Integer id1 = (Integer) obj[0];
				String booleanValue = obj[1].toString() + add + "";

				map.put(booleanValue, id1);
			}

			for (String key : map.keySet()) {
				// log.info("tttttttttttttttttttttttttttttttttttttttmap
				// "+map.size());
				if (key.charAt(0) == '1') {
					drugOfEachPatients.add(map.get(key));
				}
				// else if(key.charAt(0)=='0' && map.size()<=4)
				// drugOfEachPatients.add(map.get(key));

			}
			// if(drugOfEachPatients.size()>=2)
			listOfRegimens.add(drugOfEachPatients);
		}
		List<List<Integer>> listWithNoDuplicates = QuarterlyReportUtil
				.removeDuplcatedListInListOfIntegers(listOfRegimens);
		return listWithNoDuplicates;
	}

	/*******************************************************************************************************************************/

	@Override
	public String getRegimenName(List<Integer> composition) {
		String regimenName = "";
		int count = 1;
		try {
			for (Integer id : composition) {
				// if (Context.getConceptService().getDrug(id) != null) {
				// regimenName +=
				// Context.getConceptService().getDrug(id).getName();
				// regimenName += count < composition.size() ? "+" : "";
				// } else {
				// arrangement(I had to get the drug generic instead of using
				// the element at index 0)
				// log.info("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
				// "+Context.getConceptService().getConcept(id).getName());
				// regimenName +=
				// Context.getConceptService().getDrugsByConcept(Context.getConceptService().getConcept(id)).get(0).getConcept().getName();
				// regimenName +=
				// Context.getConceptService().getDrug(id).getConcept().getName();
				regimenName += Context.getConceptService().getConcept(id).getName();
				regimenName += count < composition.size() ? "+" : "";
				// }

				count++;
			}
		} catch (IndexOutOfBoundsException e) {
			log.info("ssssssssssssssssssssssssError " + e.getMessage());
		}
		return regimenName;
	}

	/***************************************************************************************************************************/
	public String getStringFromArrayListOfString(ArrayList<String> listOfIds) {
		String listVal = "";
		Iterator itr = listOfIds.iterator();
		while (itr.hasNext()) {
			listVal += itr.next().toString() + ";";
		}
		return listVal;

	}

	/***************************************************************************************************************************/
	@Override
	public String getAllPatientObs(Patient p, Concept c) {
		SQLQuery query = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SortedMap<Date, Double> cd4CountAndDateSorted = new TreeMap<Date, Double>();

		Date minDate = getWhenPatientStarted(p);

		String values = new String();

		if (c != null && p != null)
			query = sessionFactory.getCurrentSession()
					.createSQLQuery("(select obs_datetime,value_numeric from obs where  person_id= " + p.getPatientId()
							+ " and concept_id= " + c.getConceptId() + ")ORDER BY obs_datetime asc");

		List<Object[]> obj = query.list();

		for (Object[] ob : obj) {
			Date date = (Date) ob[0];

			Double conceptValue = (Double) ob[1];
			cd4CountAndDateSorted.put(date, conceptValue);
		}
		cd4CountAndDateSorted = getSubMap(cd4CountAndDateSorted, minDate);
		for (Date d : cd4CountAndDateSorted.keySet()) {
			values += cd4CountAndDateSorted.get(d) + "," + df.format(d) + ",";
		}

		return values;
	}

	/*********************************************************************************************************************************/
	@Override
	public List<String> getAllPatientObsList(Patient p, Concept c) {
		SQLQuery query = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SortedMap<Date, Double> cd4CountAndDateSorted = new TreeMap<Date, Double>();

		Date whenPatientHasStarted = getWhenPatientStarted(p);

		if (c != null && p != null) {
			query = sessionFactory.getCurrentSession()
					.createSQLQuery("(select obs_datetime,value_numeric from obs where  person_id= " + p.getPatientId()
							+ " and concept_id= " + c.getConceptId() + " )ORDER BY obs_datetime asc");
		}
		List<Object[]> obj = query.list();

		List<String> conceptValueAndDate = new ArrayList<String>();

		for (Object[] ob : obj) {
			Date date = (Date) ob[0];

			Double conceptValue = (Double) ob[1];

			cd4CountAndDateSorted.put(date, conceptValue);

			conceptValueAndDate.add(cd4CountAndDateSorted.get(date) + "(" + df.format(date) + ")");
		}

		cd4CountAndDateSorted = getSubMap(cd4CountAndDateSorted, whenPatientHasStarted);
		return conceptValueAndDate;
	}

	/*********************************************************************************************************************************/
	@Override
	public Date getWhenPatientStarted(Patient patient) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT min(o.date_activated) FROM orders o  ");
		strbuf.append("INNER JOIN drug_order dro on dro.order_id = o.order_id  AND ");
		strbuf.append("o.concept_id NOT IN(" + QuarterlyReportUtil.gpGetProphylaxisDrugConceptIds() + ") ");
		strbuf.append(" AND o.patient_id = ");
		strbuf.append(patient.getPatientId());

		query = session.createSQLQuery(strbuf.toString());

		Date whenPatientStarted = (Date) query.uniqueResult();

		return whenPatientStarted;
	}

	/*********************************************************************************************************************************/
	/**
	 * takes a map of all patients'concepts and gives a sub map starting with
	 * one the patient had when he started treatment and ends with one he has
	 * currently
	 * 
	 * @param SortedMap
	 *            <Date,Double>
	 * @param date
	 *            i.e when the patient has started the treatment return a submap
	 */
	public SortedMap<Date, Double> getSubMap(SortedMap<Date, Double> map, Date dateToSearch) {
		SortedMap<Date, Double> newMap = new TreeMap<Date, Double>();

		Iterator it = map.keySet().iterator();
		List<Date> dates = new ArrayList<Date>();

		while (it.hasNext()) {
			dates.add((Date) it.next());

		}
		int index = 0;
		for (int i = 0; i < dates.size(); i++) {
			index = i;
			if (dates != null && dateToSearch != null)
				if (dates.get(i).getTime() == dateToSearch.getTime()
						|| dates.get(i).getTime() > dateToSearch.getTime()) {
					break;
				}
		}
		if (dates.size() != 0 && dates != null) {
			// sub map starts with the first concept value(ex:cd4count)
			// up to the last value (or last cd4 cout) i.e the cd4 count he has
			// now

			// submap(firstkey,laskey)
			if (index != 0)
				newMap = map.subMap(dates.get(index - 1), new Date());
			else if (index == 0)
				newMap = map.subMap(dates.get(index), new Date());
		}

		return newMap;

	}

	/*********************************************************************************************************************************/
	@Override
	public Date getDateInterval(Date baseline, Integer months) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		// SELECT DATE_SUB('2004-01-01', INTERVAL 6 MONTH);
		strbuf.append("SELECT CAST(DATE_SUB( '" + df.format(baseline) + "', INTERVAL " + months + " MONTH) AS DATE )");

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*********************************************************************************************************************************/
	@Override
	public Date getXMonthAfterDate(Date baseline, Integer months) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		strbuf.append("SELECT CAST(DATE_ADD('" + df.format(baseline) + "', INTERVAL " + months + " MONTH) AS DATE) ");

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*********************************************************************************************************************************/
	@Override
	public Date getTwoWeeksAfterDate(Date baseline, Integer week) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT CAST(DATE_ADD('" + df.format(baseline) + "', INTERVAL " + week + " WEEK) AS DATE)");

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*********************************************************************************************************************************/

	@Override
	public List<Object[]> getPatientsExitedInThePeriod(Date startDate, Date endDate) {
		SQLQuery patientsExitedInThePeriodQuery = null;
		// List<Object[]> patientsExitedInThePeriod = new ArrayList<Object[]>();
		if (startDate != null && endDate != null)
			patientsExitedInThePeriodQuery = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id = pe.person_id "
							+ "INNER JOIN orders z on z.patient_id=pa.patient_id "
							+ " INNER JOIN obs ob ON ob.person_id = pe.person_id " + " AND z.concept_id IN ("
							+ QuarterlyReportUtil.gpGetARVConceptIds() + ")" + " AND ob.concept_id =  "
							+ gpGetExitedFromCareConceptId() + checkObsDate(startDate, endDate));

		else if (startDate == null && endDate != null)
			patientsExitedInThePeriodQuery = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id = pe.person_id "
							+ "INNER JOIN orders z on z.patient_id=pa.patient_id "
							+ " INNER JOIN obs ob ON ob.person_id = pe.person_id " + " AND z.concept_id IN ("
							+ QuarterlyReportUtil.gpGetARVConceptIds() + ")" + " AND ob.concept_id =  "
							+ gpGetExitedFromCareConceptId() + checkObsDate(null, endDate));

		List<Integer> records = patientsExitedInThePeriodQuery.list();

		List<Object[]> obj = new ArrayList<Object[]>();

		for (Integer id : records) {
			obj.add(new Object[] { id, "", "" });
		}

		return obj;

	}

	/*****************************************************************************************************************************************/
	/**
	 * helps to check when the observation occurred
	 * 
	 * @param startDate
	 * @param endDate
	 * @return String
	 */
	public String checkObsDate(Date startDate, Date endDate) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (startDate != null && endDate != null) {
			return " AND (ob.obs_datetime BETWEEN '" + df.format(startDate) + "' AND '" + df.format(endDate) + "' )";
		} else if (endDate == null && startDate != null) {
			return " AND (ob.obs_datetime < '" + df.format(startDate) + "' )";
		} else if (startDate == null && endDate != null) {
			return " AND (ob.obs_datetime < '" + df.format(endDate) + "' )";
		} else if (startDate == null && endDate == null) {
			endDate = new Date();
			return " AND (ob.obs_datetime < '" + df.format(endDate) + "' )";
		}
		return "";
	}

	/****************************************************************************************************************************/
	@Override
	public Date getFirstDayOfMonth(Date date) {
		SQLQuery query = null;
		Date dat = null;
		 Session session = sessionFactory.getCurrentSession();
		StringBuffer strbuf = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		strbuf.append("SELECT CAST(DATE_SUB('" + df.format(date) + "',INTERVAL (DAY('" + df.format(date)
				+ "')-1) DAY) AS DATE) ");
		query = session.createSQLQuery(strbuf.toString());
		dat = (Date) query.uniqueResult();

		return dat;
	}

	/*************************************************************************************************************************/
	@SuppressWarnings("null")
	@Override
	public List<Object[]> getCD4CountAfterXMonths(List<Object[]> patientIds, int xthMonth) {
		SQLQuery query = null;
		SQLQuery maxObsDateQuery = null;
		List<Object[]> objects = null;

		// CD4 M0
		Date arvStartedDate = null;
		Date sixMonthsAfterARVDate = null;

		// CD4 M12
		Date twlvMonthsAfterARVDate = null;

		for (Object[] id : patientIds) {

			arvStartedDate = getWhenPatientStarted(Context.getPatientService().getPatient((Integer) id[0]));
			sixMonthsAfterARVDate = getSixMonthBefore(arvStartedDate);
			twlvMonthsAfterARVDate = getXMonthAfterDate(sixMonthsAfterARVDate, 6);

			if (xthMonth == 6) {
				query = sessionFactory.getCurrentSession()
						.createSQLQuery("select MAX(obs_datetime) from obs where  person_id= " + id[0]
								+ " and concept_id = " + QuarterlyReportUtil.gpGetCD4CountConceptId()
								+ " and obs_datetime <= '" + QuarterlyReportUtil.getDateFormat(arvStartedDate) + "' ");

			}
			if (xthMonth == 12) {
				query = sessionFactory.getCurrentSession().createSQLQuery(
						"select MAX(obs_datetime) from obs where  person_id = " + id[0] + " and concept_id = "
								+ QuarterlyReportUtil.gpGetCD4CountConceptId() + " and obs_datetime BETWEEN '"
								+ QuarterlyReportUtil.getDateFormat(sixMonthsAfterARVDate) + "'" + " AND '"
								+ QuarterlyReportUtil.getDateFormat(twlvMonthsAfterARVDate) + "'");
			}
			List<Date> date = query.list();

			Date d = null;
			SQLQuery cd4Value = null;

			if (date != null) {
				d = date.get(0);

				cd4Value = sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT bs.value_numeric FROM obs bs " + "where bs.person_id = " + id[0]
								+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
								+ " AND bs.obs_datetime = '" + QuarterlyReportUtil.getDateFormat(d) + "'");

				// log.info("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu2
				// "+cd4Value.list().get(0));
				// log.info("ppppppppppppppppppppppppppppppppppppppp "+id[0]);

				// objects.add(new Object[] { id[0], "","" });

			}
			if (cd4Value != null)
				objects.add(new Object[] { id[0], "", "" });
		}

		return objects;
	}

	/****************************************************************************************************************************/
	@Override
	public Date getWhenEnrolled(Patient p) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT CAST(MIN(pg.date_enrolled) AS DATE)  FROM patient_program pg  ");
		strbuf.append("INNER JOIN program p on p.program_id=pg.program_id ");
		strbuf.append(" AND p.program_id=2  ");
		strbuf.append(" AND pg.patient_id= " + p.getPatientId());

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/********************************************************************************************************************/
	@Override
	public Date getMaxEncounter(Patient p, Date maxDate) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT CAST(MAX(encounter_datetime)AS DATE) FROM encounter  ");
		strbuf.append("where patient_id = " + p.getPatientId());
		strbuf.append(" and encounter_datetime <= '" + df.format(maxDate) + "' ");

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();
		return date;
	}

	/********************************************************************************************************************/
	@Override
	public Date getMaxTransferInDate(Patient p, Date minDate, Date maxDate) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer strbuf = new StringBuffer();

		strbuf.append(" SELECT MAX(obs_datetime) FROM obs o  ");
		strbuf.append(" where concept_id = " + QuarterlyReportUtil.gpTransferInConceptId()
				+ " AND value_coded=1065 and person_id = " + p.getPatientId());
		strbuf.append(" and obs_datetime BETWEEN '" + df.format(minDate) + "' AND '" + df.format(maxDate) + "' ");

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();
		return date;
	}

	/****************************************************************************************************************************/
	public List<Integer> getPatientsExitedFromCare() {
		List<Integer> patientsExitedIds = null;

		 Session session = sessionFactory.getCurrentSession();
		SQLQuery patientsExitedFromCare = session.createSQLQuery("SELECT distinct pa.patient_id FROM patient pa "
				+ "INNER JOIN person pe ON pa.patient_id = pe.person_id "
				+ "INNER JOIN obs ob ON ob.person_id = pe.person_id WHERE ob.concept_id = 1811");
		patientsExitedIds = patientsExitedFromCare.list();

		return patientsExitedIds;
	}

	/****************************************************************************************************************************/
	public List<Integer> getPatientsOnDapsone(Date quarterTo) {
		 Session session = sessionFactory.getCurrentSession();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SQLQuery patientsOnDapsone = session.createSQLQuery("SELECT distinct o.patient_id  " + " FROM orders o "
				+ " INNER JOIN drug_order dro on dro.order_id=o.order_id  "
				+ " INNER JOIN patient pat on pat.patient_id=o.patient_id "
				+ " INNER JOIN encounter en on en.patient_id=pat.patient_id "
				+ " INNER JOIN patient_program pg on pat.patient_id=pg.patient_id "
				+ " INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = "
				+ Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()) + " AND o.concept_id =92  "
				+ " AND o.voided=0 AND pat.voided=0 AND pg.voided=0 "
				+ " INNER JOIN person p on o.patient_id = p.person_id   "
				+ " INNER JOIN obs obs on obs.person_id = p.person_id   " + " AND pg.date_enrolled < '"
				+ df.format(quarterTo) + "'" + " AND en.encounter_datetime <= '" + df.format(quarterTo) + "' "
				+ " AND o.date_activated <= '" + df.format(quarterTo) + "' ");

		List<Integer> patientIds = patientsOnDapsone.list();

		return patientIds;
	}

	/****************************************************************************************************************************/
	public boolean isPatientOnProphylaxisOnly(Patient p) {
		List<Regimen> regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();

		for (Regimen r : regimens) {
			components = r.getComponents();
		}

		List<Integer> regimenDrugs = new ArrayList<Integer>();

		for (RegimenComponent rc : components) {
			if (rc.getDrugOrder().isActive())
				regimenDrugs.add(rc.getDrug().getDrugId());
		}

		boolean found = false;
		List<Integer> prophylaxisIds = QuarterlyReportUtil.gpGetProphylaxisAsIntegers();
		if (prophylaxisIds.size() >= regimenDrugs.size() && prophylaxisIds.containsAll(regimenDrugs)) {
			for (Integer r : regimenDrugs) {
				if (prophylaxisIds.contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}

		return found;
	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getRemoveDuplicates(List<Object[]> list1, List<Object[]> list2) {
		List<Object[]> result = new ArrayList<Object[]>();

		List<Integer> listt1 = QuarterlyReportUtil.getConvertToType(list1);
		List<Integer> listt2 = QuarterlyReportUtil.getConvertToType(list2);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		for (Integer d : listt1) {
			Date when = getWhenPatientStarted(Context.getPatientService().getPatient(d));
			if (!listt2.contains(d))
				result.add(new Object[] { d, df.format(when), "Start ARVs" });
		}

		return result;
	}

	/*******************************************************************************************************************************/

	public Boolean isLastRegimenProphy(Patient p) {
		List<Regimen> regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();

		if (regimens.size() != 0)
			components = regimens.get(regimens.size() - 1).getComponents();

		List<Integer> regimenDrugs = new ArrayList<Integer>();

		for (RegimenComponent rc : components) {
			if (rc.getDrugOrder().isActive())
				regimenDrugs.add(rc.getDrug().getDrugId());
		}

		boolean found = false;
		List<Integer> prophylaxisIds = QuarterlyReportUtil.gpGetProphylaxisAsIntegers();
		if (prophylaxisIds.size() >= regimenDrugs.size() && prophylaxisIds.containsAll(regimenDrugs)) {
			for (Integer r : regimenDrugs) {
				if (prophylaxisIds.contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}
		return found;

	}

	public Boolean isLastProphylaxisTakenCotrimo(Integer id) {
		Patient p = Context.getPatientService().getPatient(id);
		List<Regimen> regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();

		if (regimens.size() != 0)
			components = regimens.get(regimens.size() - 1).getComponents();

		List<Integer> regimenDrugs = new ArrayList<Integer>();

		for (RegimenComponent rc : components) {
			if (rc.getDrug() != null && rc.getDrugOrder().isActive())
				//// regimenDrugs.add(rc.getDrug().getDrugId());
				// if(rc.getDrug()!=null)
				regimenDrugs.add(rc.getDrug().getConcept().getConceptId());
		}

		boolean found = false;

		if (regimenDrugs.contains(92)) {
			found = true;
		}

		return found;

	}

	public List<Integer> getRetrieveARVDrugIdsOnly(List<Integer> drugIds) {
		List<Integer> arvIds = new ArrayList<Integer>();
		List<Integer> tbDrugIds = QuarterlyReportUtil.gpGetTBDrugIds();
		List<Integer> prophylaxisIds = QuarterlyReportUtil.gpGetProphylaxisAsIntegers();
		for (Integer id : drugIds) {
			if (!tbDrugIds.contains(id) && !prophylaxisIds.contains(id))
				arvIds.add(id);
		}
		return arvIds;
	}

	/******************************************************************************************************************************/
	@Override
	public Boolean isOnTriTherapy(Integer patientId) {
		Boolean checked = false;

		List<Regimen> regimens = RegimenUtils.getRegimenHistory(Context.getPatientService().getPatient(patientId))
				.getRegimenList();
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();

		for (Regimen r : regimens) {
			components = r.getComponents();
		}

		List<Integer> currentRegimen = new ArrayList<Integer>();

		for (RegimenComponent rc : components) {
			// if (!rc.getDrugOrder().getDiscontinued() ||
			// rc.getDrugOrder().getDiscontinuedDate().getTime()<endDate.getTime())
			if (!QuarterlyReportUtil.gpGetProphylaxisConceptIds().contains(rc.getDrug().getConcept().getConceptId()))
				currentRegimen.add(rc.getDrug().getConcept().getConceptId());

		}
		if (currentRegimen.size() >= 3)
			checked = true;

		return checked;

	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientOnAllDrugsByConceptIds(List<Integer> listDrugIdsByConcept, List<Integer> patientIds,
			Date endDate) {
		List<Object[]> objects = new ArrayList<Object[]>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			for (Integer patientId : patientIds) {

				SQLQuery query = null;
				 Session session = sessionFactory.getCurrentSession();

				StringBuffer strbuf = new StringBuffer();

				strbuf.append(" SELECT DISTINCT o.concept_id, ");
				strbuf.append(" IF(((o.date_activated <='" + df.format(endDate)
						+ "' AND (date_activated < 'CURDATE()' AND date_stopped > 'CURDATE()') OR o.date_stopped>='" + df.format(endDate) + "' ) ");
				strbuf.append("  ),1,0) ");
				strbuf.append(" FROM orders o ");
				strbuf.append(" INNER JOIN drug_order d ON o.order_id=d.order_id AND o.patient_id= " + patientId);
				strbuf.append(" AND o.concept_id IN(" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");

				query = session.createSQLQuery(strbuf.toString());

				List<Object[]> records = query.list();
				Map<String, Integer> map = new HashMap<String, Integer>();
				List<Integer> regimenDrugsByConcept = new ArrayList<Integer>();

				int add = 0;

				for (Object[] obj : records) {
					add++;

					Integer id1 = (Integer) obj[0];
					String booleanValue = obj[1].toString() + add + "";
					map.put(booleanValue, id1);
				}

				for (String key : map.keySet()) {
					if (key.charAt(0) == '1') {
						if (!regimenDrugsByConcept.contains(map.get(key)))
							regimenDrugsByConcept.add(map.get(key));
					}
				}

				if (getPatientsOnAllByConceptIds(listDrugIdsByConcept, regimenDrugsByConcept)) {
					Patient patient = Context.getPatientService().getPatient(patientId);
					Date when = getWhenPatientStarted(patient);
					// if(!exited.contains(patient.getPatientId()))
					objects.add(new Object[] { patient.getPatientId(), df.format(when), "Start ARV" });

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return objects;
	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsStartedCotrimo(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {

		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		StringBuffer strBuffer = new StringBuffer();

		StringBuffer queryPortion = new StringBuffer();

		List<Object[]> objs = new ArrayList<Object[]>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SQLQuery query = null;

		if (gender != null) {
			queryPortion.append("select distinct pg.patient_id from patient_program pg ");
			queryPortion.append(" inner join person pe on pg.patient_id = pe.person_id ");
			queryPortion.append(" and pe.gender = '" + gender + "'");
			queryPortion.append(" inner join patient pa on pg.patient_id = pa.patient_id ");
			queryPortion.append(" inner join orders o on o.patient_id = pa.patient_id ");
			queryPortion.append(" inner join encounter en on pa.patient_id = en.patient_id ");
			queryPortion.append(" inner join obs obs on obs.person_id = pe.person_id ");
			queryPortion.append(" and pg.program_id =  " + Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			queryPortion.append(" and en.voided=0 and pg.voided=0 and pa.voided=0 and o.voided=0 ");
			queryPortion.append(" and o.concept_id in( " + QuarterlyReportUtil.gpCotrimoConceptIds() + ")");
			queryPortion.append(
					" and o.concept_id<>1811 and  (cast(obs.obs_datetime as DATE))<='" + df.format(quarterTo) + "' ");
			// queryPortion.append(" and o.date_stopped < 'CURDATE()' ");
			queryPortion.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' and (pg.date_completed is null OR pg.date_completed >= '" + df.format(quarterTo) + "') ");

		} else {
			queryPortion.append("select distinct pg.patient_id from patient_program pg ");
			queryPortion.append(" inner join person pe on pg.patient_id = pe.person_id ");
			queryPortion.append(" inner join patient pa on pg.patient_id = pa.patient_id ");
			queryPortion.append(" inner join orders o on o.patient_id = pa.patient_id ");
			queryPortion.append(" inner join encounter en on pa.patient_id = en.patient_id ");
			queryPortion.append(" inner join obs obs on obs.person_id = pe.person_id ");
			queryPortion.append(" and pg.program_id =  " + Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			queryPortion.append(" and o.concept_id in( " + QuarterlyReportUtil.gpCotrimoConceptIds() + ")");
			queryPortion.append(" and en.voided=0 and pg.voided=0 and pa.voided=0 and o.voided=0 ");
			queryPortion.append(
					" and o.concept_id<>1811 and  (cast(obs.obs_datetime as DATE))<='" + df.format(quarterTo) + "' ");
			// queryPortion.append(" and o.date_stopped < 'CURDATE()' ");
			queryPortion.append(" and pg.date_enrolled <= '" + df.format(quarterTo)
					+ "' and (pg.date_completed is null OR pg.date_completed >= '" + df.format(quarterTo) + "') ");

		}

		if (minAge != null && maxAge != null) {
			strBuffer.append(queryPortion);
			strBuffer.append(" AND TIMESTAMPDIFF(MONTH,pe.birthdate,CURDATE()) BETWEEN " + minAge + " AND " + maxAge);

		}
		if (minAge != null && maxAge == null) {
			strBuffer.append(queryPortion);
			strBuffer.append(" AND TIMESTAMPDIFF(MONTH,pe.birthdate,CURDATE()) >= " + minAge);
		}
		if (minAge == null && maxAge == null) {
			strBuffer.append(queryPortion);
		}

		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		List<Integer> records = query.list();

		double patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();

		List<Integer> patsOnCotrimo = new ArrayList<Integer>();
		List<Integer> patsOnDapsone = getPatientsOnDapsone(quarterTo);

		for (Integer g : records) {
			if (!patsOnDapsone.contains(g)) {
				patsOnCotrimo.add(g);
			}
		}

		List<Integer> exitedInThePeriod = QuarterlyReportUtil
				.getConvertToType(getPatientsExitedInThePeriod(null, quarterTo));

		for (Integer g : patsOnCotrimo) {
			// if(patientsReceivedHIVCare.contains(g) &&
			// isLastProphylaxisTakenCotrimo(g))
			// if(!exitedInThePeriod.contains(g))
			objs.add(new Object[] { g, "", "" });
		}

		return objs;
	}

	/*******************************************************************************************************************************/
	@Override
	public Date getWhenStopART(Patient p) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT CAST(MIN(o.date_stopped) AS DATE)  FROM orders o  ");
		strbuf.append(" where o.date_stopped < 'CURDATE()' ");
		strbuf.append(" AND o.patient_id= " + p.getPatientId());

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*******************************************************************************************************************************/
	@Override
	public Date getWhenTransOut(Patient p) {
		SQLQuery query = null;
		 Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT CAST(MIN(o.obs_datetime) AS DATE)  FROM obs o  ");
		strbuf.append(" where o.person_id= " + p.getPatientId());
		strbuf.append(" AND o.concept_id= " + 1811 + " AND o.value_coded=" + 1744);

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*******************************************************************************************************************************/
	@Override
	public Date getWhenLost(Patient p) {

		SQLQuery query = null;
		Session session = sessionFactory.getCurrentSession();

		StringBuffer strbuf = new StringBuffer();

		strbuf.append("SELECT CAST(MIN(o.obs_datetime) AS DATE)  FROM obs o  ");
		strbuf.append(" where o.person_id= " + p.getPatientId());
		strbuf.append(" AND o.concept_id= " + 1811 + " AND o.value_coded=" + 1743);

		query = session.createSQLQuery(strbuf.toString());

		Date date = (Date) query.uniqueResult();

		return date;
	}

	/*******************************************************************************************************************************/
	@Override
	public Integer getCalculageAge(Patient p, Date date1, Date date2) {
		SQLQuery query2 = null;
		StringBuffer strbuf2 = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		strbuf2.append("SELECT (YEAR('" + df.format(date1) + "') - YEAR('" + df.format(date2) + "')) * 12 ");
		strbuf2.append(" + (MONTH('" + df.format(date1) + "') - MONTH('" + df.format(date2) + "'))");
		strbuf2.append("  - IF(DAYOFMONTH(CURDATE()) < DAYOFMONTH(s.birthdate),1,0) ");
		strbuf2.append(" FROM  person s ");
		strbuf2.append(" INNER JOIN patient p on p.patient_id=s.person_id ");
		strbuf2.append(
				" INNER JOIN patient_program pg on pg.patient_id=p.patient_id and p.patient_id= " + p.getPatientId());

		query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

		List<BigInteger> record = query2.list();
		int patientAge = 0;
		if (record.get(0) != null)
			patientAge = record.get(0).intValue();
		return patientAge;
	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsTransferredOut(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {
		StringBuffer strBuf = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		strBuf.append(
				" SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN orders os on os.patient_id=pa.patient_id"
						+ " INNER JOIN drug_order dor on dor.order_id=os.order_id "
						+ " inner join person ps on pa.patient_id=ps.person_id ");
		strBuf.append(" INNER JOIN obs ob on ob.person_id=ps.person_id and ob.concept_id= "
				+ gpGetExitedFromCareConceptId() + " and ob.value_coded = 1744");
		strBuf.append(" AND ob.obs_datetime <= '" + df.format(quarterTo) + "'");
		strBuf.append(" AND ob.voided=0 AND ps.gender='" + gender + "'");
		strBuf.append(" AND os.date_activated <= '" + df.format(quarterTo) + "' ");
		strBuf.append(" AND os.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strBuf.toString());

		List<Integer> patientsTransOut = query.list();

		List<Integer> lostPatients = getPatientsLostOnFollowup(quarterTo);

		List<Object[]> objs = new ArrayList<Object[]>();

		int patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();

		for (Integer id : patientsTransOut) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(ob.obs_datetime) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(ob.obs_datetime) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(ob.obs_datetime) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  person s ");
			strbuf2.append(" INNER JOIN obs ob on ob.person_id=s.person_id ");
			strbuf2.append(" AND ob.concept_id=" + gpGetExitedFromCareConceptId()
					+ " AND ob.value_coded=1744 AND ob.obs_datetime<=  '" + df.format(quarterTo) + "'");
			strbuf2.append(" AND s.person_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					if (!lostPatients.contains(id))
						patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					if (!lostPatients.contains(id))
						patIds2.add(id);
			}
		}

		for (Integer id : patIds2) {
			objs.add(new Object[] { id, "", "" });
		}
		return objs;

	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsDead(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {
		StringBuffer strBuf = new StringBuffer();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		strBuf.append(
				" SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN orders os on os.patient_id=pa.patient_id"
						+ " INNER JOIN drug_order dor on dor.order_id=os.order_id "
						+ " inner join person ps on pa.patient_id=ps.person_id ");
		strBuf.append(" INNER JOIN obs ob on ob.person_id=ps.person_id and ob.concept_id= "
				+ gpGetExitedFromCareConceptId() + " and ob.value_coded = 1742");
		strBuf.append(" AND ob.obs_datetime <= '" + df.format(quarterTo) + "'");
		strBuf.append(" AND ob.voided=0 AND ps.gender='" + gender + "'");
		strBuf.append(" AND os.date_activated <= '" + df.format(quarterTo) + "' ");
		strBuf.append(" AND os.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strBuf.toString());

		List<Integer> patientsTransOut = query.list();

		List<Integer> lostPatients = getPatientsLostOnFollowup(quarterTo);

		List<Object[]> objs = new ArrayList<Object[]>();

		int patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();

		for (Integer id : patientsTransOut) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(ob.obs_datetime) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(ob.obs_datetime) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(ob.obs_datetime) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  person s ");
			strbuf2.append(" INNER JOIN obs ob on ob.person_id=s.person_id ");
			strbuf2.append(" AND ob.concept_id=" + gpGetExitedFromCareConceptId()
					+ " AND ob.value_coded=1742 AND ob.obs_datetime<=  '" + df.format(quarterTo) + "'");
			strbuf2.append(" AND s.person_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					if (!lostPatients.contains(id))
						patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					if (!lostPatients.contains(id))
						patIds2.add(id);
			}
		}

		for (Integer id : patIds2) {
			objs.add(new Object[] { id, "", "" });
		}
		return objs;

	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsWithCD4CountInThePeriod(List<Integer> cohort, Date startdate, Date enddate) {

		SQLQuery maxObsDateQuery = null;
		List<Object[]> objs = new ArrayList<Object[]>();
		for (Integer id : cohort) {
			if (startdate != null && enddate != null) {
				maxObsDateQuery = sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT max(bs.obs_datetime) FROM orders o "
								+ "INNER JOIN drug_order dro on dro.order_id = o.order_id "
								+ "INNER JOIN patient p on p.patient_id=o.patient_id "
								+ "INNER JOIN obs bs on bs.person_id=p.patient_id " + "AND o.patient_id = " + id
								+ " and bs.concept_id=  " + QuarterlyReportUtil.gpGetCD4CountConceptId()
								+ " AND obs_datetime BETWEEN '" + QuarterlyReportUtil.getDateFormat(startdate) + "'"
								+ " AND '" + QuarterlyReportUtil.getDateFormat(enddate) + "'");
			} else if (startdate != null && enddate == null) {
				maxObsDateQuery = sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT max(bs.obs_datetime) FROM orders o "
								+ "INNER JOIN drug_order dro on dro.order_id = o.order_id "
								+ "INNER JOIN patient p on p.patient_id=o.patient_id "
								+ "INNER JOIN obs bs on bs.person_id=p.patient_id " + "AND o.patient_id = " + id
								+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
								+ " AND obs_datetime <= '" + QuarterlyReportUtil.getDateFormat(startdate) + "'");
			}

			Date obsDateTime = (Date) maxObsDateQuery.list().get(0);

			// log.info("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu1
			// "+maxObsDateQuery.toString());

			SQLQuery cd4Value = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT bs.value_numeric FROM bs obs " + "where bs.person_id = " + id
							+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
							+ " AND bs.obs_starttime = '" + QuarterlyReportUtil.getDateFormat(obsDateTime) + "'");

			// log.info("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu2
			// "+cd4Value.toString());

			if (cd4Value != null)
				objs.add(new Object[] { id, "", "" });

		}

		// log.info("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk "+objs);
		return objs;
	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsHadCD4CountAfter6Months(List<Object[]> patientIds) {

		Date arvStartedDate = null;

		List<Object[]> objects = new ArrayList<Object[]>();

		for (Object[] id : patientIds) {

			arvStartedDate = getWhenPatientStarted(Context.getPatientService().getPatient((Integer) id[0]));

			SQLQuery query = null;

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			Date sixMonthsAfter = getXMonthAfterDate(arvStartedDate, 6);
			Date twlvMonthsAfter = getXMonthAfterDate(arvStartedDate, 12);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sixMonthsAfter);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date dateWithLastDayOfTheMonth = calendar.getTime();

			query = sessionFactory.getCurrentSession()
					.createSQLQuery("select MAX(obs_datetime) from obs where  person_id= " + id[0]
							+ " and concept_id = " + QuarterlyReportUtil.gpGetCD4CountConceptId()
							+ " and obs_datetime BETWEEN '" + QuarterlyReportUtil.getDateFormat(arvStartedDate)
							+ "' AND '" + QuarterlyReportUtil.getDateFormat(dateWithLastDayOfTheMonth) + "'");

			List<Date> date = query.list();

			Date d = date.get(0);
			SQLQuery cd4ValueQuery = null;
			Double value = null;
			if (d != null) {

				cd4ValueQuery = sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT bs.value_numeric FROM obs bs " + "where bs.person_id = " + id[0]
								+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
								+ " AND bs.obs_datetime = '" + QuarterlyReportUtil.getDateFormat(d) + "'");

				value = (Double) cd4ValueQuery.list().get(0);

				if (value != null)
					objects.add(new Object[] { id[0], value, d });
			}

		}

		return objects;

	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsHadCD4CountAfter12Months(List<Object[]> patientIds) {

		// Date arvStartedDate=null;
		// Date sixMonthsAfter=null;
		// Date twelveMonthAfter=null;
		Double value = null;

		List<Object[]> objects = new ArrayList<Object[]>();

		for (Object[] id : patientIds) {

			Date arvStartedDate = getWhenPatientStarted(Context.getPatientService().getPatient((Integer) id[0]));
			Date twelveMonthAfter = getXMonthAfterDate(arvStartedDate, 12);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(twelveMonthAfter);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date dateWithLastDayOfTheMonth = calendar.getTime();

			Date oneMonthBefore = getDateInterval(dateWithLastDayOfTheMonth, 1);
			Date oneMonthAfter = getXMonthAfterDate(dateWithLastDayOfTheMonth, 1);

			SQLQuery query = sessionFactory.getCurrentSession()
					.createSQLQuery("select MAX(obs_datetime) from obs where  person_id= " + id[0]
							+ " and concept_id = " + QuarterlyReportUtil.gpGetCD4CountConceptId()
							+ " and obs_datetime BETWEEN '" + QuarterlyReportUtil.getDateFormat(oneMonthBefore)
							+ "' AND '" + QuarterlyReportUtil.getDateFormat(oneMonthAfter) + "' ");

			Date d = (Date) query.list().get(0);

			SQLQuery cd4Value = null;

			try {
				if (d != null) {
					cd4Value = sessionFactory.getCurrentSession()
							.createSQLQuery("SELECT bs.value_numeric FROM obs bs " + "where bs.person_id = " + id[0]
									+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
									+ " AND bs.obs_datetime = '" + QuarterlyReportUtil.getDateFormat(d) + "'");
				}

				if (cd4Value != null) {
					value = (Double) cd4Value.list().get(0);

					if (value != null)
						objects.add(new Object[] { id[0], value, new Date() });
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return objects;
	}

	/*******************************************************************************************************************************/
	@Override
	public List<Object[]> getPatientsWithCD4AtMo(List<Object[]> patientIds) {

		Date arvStartedDate = null;

		List<Object[]> objects = new ArrayList<Object[]>();

		for (Object[] id : patientIds) {

			arvStartedDate = getWhenPatientStarted(Context.getPatientService().getPatient((Integer) id[0]));

			SQLQuery query = null;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(arvStartedDate);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date dateWithLastDayOfTheMonth = calendar.getTime();

			query = sessionFactory.getCurrentSession().createSQLQuery(
					"select MAX(obs_datetime) from obs where  person_id= " + id[0] + " and concept_id = "
							+ QuarterlyReportUtil.gpGetCD4CountConceptId() + " and obs_datetime <= '"
							+ QuarterlyReportUtil.getDateFormat(dateWithLastDayOfTheMonth) + "' ");
			List<Date> date = query.list();

			// Date d=null;
			SQLQuery cd4ValueQuery = null;
			Double value = null;
			if (date != null) {
				Date d = date.get(0);

				try {
					cd4ValueQuery = sessionFactory.getCurrentSession()
							.createSQLQuery("SELECT bs.value_numeric FROM obs bs " + "where bs.person_id = " + id[0]
									+ " and bs.concept_id= " + QuarterlyReportUtil.gpGetCD4CountConceptId()
									+ " AND bs.l_datetime = '" + QuarterlyReportUtil.getDateFormat(d) + "'");
					value = (Double) cd4ValueQuery.list().get(0);

					if (value != null)
						objects.add(new Object[] { id[0], value, d });
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}

		return objects;
	}

	@Override
	public List<Integer> getPreARTpatientsTransferreIn(Date quarterFrom, Date quarterTo) {
		StringBuffer transferInBuf = new StringBuffer();
		StringBuffer preArtBuf = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		transferInBuf.append(" SELECT DISTINCT o.person_id FROM obs o ");
		transferInBuf.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
		transferInBuf.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
		transferInBuf.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
				+ " AND value_coded=1065  ");
		transferInBuf.append(
				" and o.obs_datetime BETWEEN '" + df.format(quarterFrom) + "' AND '" + df.format(quarterTo) + "'");

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(transferInBuf.toString());

		List<Integer> transferredInPatients = query.list();

		List<Integer> preArtTransferredInPatients = new ArrayList<Integer>();

		preArtBuf.append("SELECT o.patient_id FROM orders o  ");
		preArtBuf.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
		preArtBuf.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
		preArtBuf.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
		preArtBuf.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
		preArtBuf.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetProphylaxisDrugConceptIds() + ") ");
		preArtBuf.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");

		SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(preArtBuf.toString());

		List<Integer> preArtPatients = query1.list();

		for (Integer id : preArtPatients) {
			if (transferredInPatients.contains(id))
				preArtTransferredInPatients.add(id);
		}

		return preArtTransferredInPatients;
	}

	@Override
	public List<Integer> getARTpatientsTransferreIn(Date quarterFrom, Date quarterTo) {

		StringBuffer transferInBuf = new StringBuffer();
		StringBuffer artBuf = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		transferInBuf.append(" SELECT DISTINCT o.person_id FROM obs o ");
		transferInBuf.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
		transferInBuf.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
		transferInBuf.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
				+ " AND value_coded=1065  ");

		if (quarterFrom != null && quarterTo != null)
			transferInBuf.append(
					" and o.obs_datetime BETWEEN '" + df.format(quarterFrom) + "' AND '" + df.format(quarterTo) + "'");
		if (quarterFrom != null && quarterTo == null)
			transferInBuf.append(" and o.obs_datetime < '" + df.format(quarterFrom) + "'");
		if (quarterFrom == null && quarterTo != null)
			transferInBuf.append(" and o.obs_datetime < '" + df.format(quarterTo) + "'");

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(transferInBuf.toString());

		List<Integer> transferredInPatients = query.list();

		List<Integer> artTransferredInPatients = new ArrayList<Integer>();

		artBuf.append("SELECT o.patient_id FROM orders o  ");
		artBuf.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
		artBuf.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
		artBuf.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
		artBuf.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
		artBuf.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
		artBuf.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");

		SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(artBuf.toString());

		List<Integer> artPatients = query1.list();

		for (Integer id : artPatients) {
			if (transferredInPatients.contains(id))
				artTransferredInPatients.add(id);
		}

		return artTransferredInPatients;
	}

	@Override
	public List<Integer> getAllPatientsTransferredIn(Date quarterFrom, Date quarterTo) {
		StringBuffer transferInBuf = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		if (quarterFrom != null && quarterTo == null) {
			transferInBuf.append(" SELECT DISTINCT o.person_id FROM obs o ");
			transferInBuf.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
			transferInBuf.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
			transferInBuf.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
					+ " AND o.value_coded=1065  ");
			transferInBuf.append(" and o.obs_datetime <= '" + df.format(quarterFrom) + "'");
		}
		if (quarterFrom == null && quarterTo != null) {
			transferInBuf.append(" SELECT DISTINCT o.person_id FROM obs o ");
			transferInBuf.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
			transferInBuf.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
			transferInBuf.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
					+ " AND o.value_coded=1065  ");
			transferInBuf.append(" and o.obs_datetime <= '" + df.format(quarterTo) + "'");
		}
		if (quarterFrom != null && quarterTo != null) {
			transferInBuf.append(" SELECT DISTINCT o.person_id FROM obs o ");
			transferInBuf.append(" INNER JOIN person pe ON pe.person_id=o.person_id ");
			transferInBuf.append(" INNER JOIN patient pa ON pa.patient_id=pe.person_id ");
			transferInBuf.append(" AND o.concept_id= " + Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId())
					+ " AND o.value_coded=1065  ");
			transferInBuf.append(
					" and o.obs_datetime BETWEEN '" + df.format(quarterFrom) + "' AND '" + df.format(quarterTo) + "'");

			log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv " + transferInBuf.toString());
		}

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(transferInBuf.toString());

		List<Integer> transferredInPatients = query.list();

		return transferredInPatients;
	}

	@Override
	public List<Object[]> getNewOnArtTransferInExcluded(Date quarterFrom, Date quarterTo, String gender, Integer minAge,
			Integer maxAge) {
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();

		StringBuffer strBuffer = new StringBuffer();

		List<Object[]> objs = new ArrayList<Object[]>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		if (gender != null) {
			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) BETWEEN ");
			strBuffer.append("'" + df.format(quarterFrom) + "'" + " AND '" + df.format(quarterTo) + "'" + " , "
					+ " true " + " ," + "false)");
			strBuffer.append(" FROM orders o ");
			strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			strBuffer.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			strBuffer.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			strBuffer.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
			strBuffer.append(" AND p.gender= '" + gender + "'");
			strBuffer.append(" group by o.patient_id ");
		} else {
			strBuffer.append("SELECT o.patient_id , ");
			strBuffer.append("IF((select min(o.date_activated)) BETWEEN ");
			strBuffer.append("'" + df.format(quarterFrom) + "'" + " AND '" + df.format(quarterTo) + "'" + " , "
					+ " true " + " ," + "false)");
			strBuffer.append(" FROM orders o ");
			strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
			strBuffer.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = ");
			strBuffer.append(Integer.parseInt(QuarterlyReportUtil.gpGetHIVProgramId()));
			strBuffer.append(" AND o.concept_id in( " + QuarterlyReportUtil.gpGetARVConceptIds() + ") ");
			strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
			strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id   ");
			strBuffer.append(" group by o.patient_id ");
		}

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());

		log.info("ppppppppppppppppppppppppppppppppp " + strBuffer.toString());

		List<Object[]> records = query.list();

		List<Integer> patId1 = new ArrayList<Integer>();

		int add = 0;

		for (Object[] obj : records) {
			add++;
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			patientStartedWhenMap.put(booleanValue, id);
		}

		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				patId1.add(patientStartedWhenMap.get(key));
			}
		}

		// >>>>>>>>>>>>>>>>>>>>SEE IF PATIENTS IS NOT TRANFER-IN
		SQLQuery transferInStr = sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT DISTINCT o.person_id FROM obs o "
						+ "INNER JOIN person pe ON pe.person_id=o.person_id "
						+ "INNER JOIN patient pat ON pe.person_id=pat.patient_id "
						+ "INNER JOIN orders d ON d.patient_id=pat.patient_id " + "AND o.concept_id= "
						+ Integer.parseInt(QuarterlyReportUtil.gpTransferInConceptId()) + " AND value_coded=1065 "
						+ " AND o.obs_datetime BETWEEN  ' " + df.format(quarterFrom) + "' AND ' " + df.format(quarterTo)
						+ "' " + " AND d.concept_id IN (" + QuarterlyReportUtil.gpGetARVConceptIds() + ")");

		List<Integer> transferredInDuringQter = transferInStr.list();

		// >>>>>>>>>>>>>>>>>>>>SEE IF PATIENTS IS NOT exited from care
		SQLQuery exitedInStr = sessionFactory.getCurrentSession().createSQLQuery(
				"SELECT DISTINCT o.person_id FROM obs o " + "INNER JOIN person pe ON pe.person_id=o.person_id "
						+ "INNER JOIN patient pat ON pe.person_id=pat.patient_id "
						+ "INNER JOIN orders d ON d.patient_id=pat.patient_id " + "AND o.concept_id= "
						+ Integer.parseInt(QuarterlyReportUtil.gpGetExitedFromCareConceptId())
						+ " AND o.obs_datetime <=  ' " + df.format(quarterTo) + "' ");

		List<Integer> exitedPatients = transferInStr.list();

		double patientAge = 0;

		List<Integer> patIds2 = new ArrayList<Integer>();

		for (Integer id : patId1) {

			SQLQuery query2 = null;
			StringBuffer strbuf2 = new StringBuffer();

			strbuf2.append("SELECT (MIN(o.date_activated) - YEAR(s.birthdate)) * 12 ");
			strbuf2.append(" + (MONTH(MIN(o.date_activated)) - MONTH(s.birthdate)) ");
			strbuf2.append("  - IF(DAYOFMONTH(MIN(o.date_activated)) < DAYOFMONTH(s.birthdate),1,0) ");
			strbuf2.append(" FROM  orders o ");
			strbuf2.append(" INNER JOIN patient p on p.patient_id=o.patient_id ");
			strbuf2.append(" INNER JOIN person s on s.person_id=p.patient_id  ");
			strbuf2.append(" AND p.patient_id= " + id);

			query2 = sessionFactory.getCurrentSession().createSQLQuery(strbuf2.toString());

			List<Double> record = query2.list();

			if (record.get(0) != null)
				patientAge = record.get(0).intValue();

			if (patientAge < 1)
				patientAge = patientAge * (-1);

			if (minAge != null && maxAge != null) {
				if (patientAge >= minAge && patientAge <= maxAge)
					if (!transferredInDuringQter.contains(id))
						if (!exitedPatients.contains(id))
							patIds2.add(id);
			} else if (minAge != null && maxAge == null) {
				if (patientAge >= minAge)
					if (!transferredInDuringQter.contains(id))
						if (!exitedPatients.contains(id))
							patIds2.add(id);
			}
		}
		for (Integer id : patIds2) {
			Patient p = Context.getPatientService().getPatient(id);
			Date when = getWhenPatientStarted(p);
			if (when != null)
				objs.add(new Object[] { id, when, "Start ARV" });
			else
				objs.add(new Object[] { id, "", "Start ARV" });
		}
		return objs;
	}

}
