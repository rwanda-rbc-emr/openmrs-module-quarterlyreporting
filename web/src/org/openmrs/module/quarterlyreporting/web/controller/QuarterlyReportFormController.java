/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.quarterlyreporting.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.quarterlyreporting.QuarterlyReportUtil;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * This controller backs the /web/module/basicmoduleForm.jsp page. This
 * controller is tied to that jsp page in the
 * /metadata/moduleApplicationContext.xml file
 */
public class QuarterlyReportFormController extends
		ParameterizableViewController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	protected SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	@SuppressWarnings( { "deprecation", "unchecked" })
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// Map<String,Object> newMap=null;

		List<Integer> years = new ArrayList<Integer>();
		Date date = new Date();
		int annual = date.getYear() + 1900;
		for (int i = 1990; i <= annual + 1; i++) {
			years.add(i);
		}
		map.put("years", years);
		map.put("currentYear", annual);

		if (request.getMethod().equalsIgnoreCase("post")) {
			

			String maleGender = request.getParameter("mGender");
			String femaleGender = request.getParameter("fGender");
			
			String zeroMonthAgeStr = request.getParameter("zeroMonthAge");
			String one80MonAgeStr = request.getParameter("one80MonAge");
			String elevMonAgeStr = request.getParameter("elevMonAge");
			String twlvMonAgeStr = request.getParameter("twlvMonAge");
			String twenty3MonAgeStr = request.getParameter("twenty3MonAge");
			String twentyFoMonAgeStr = request.getParameter("twentyFoMonAge");
			String fifty9MonAgeStr = request.getParameter("fifty9MonAge");
			String sixtyMonAgeStr = request.getParameter("sixtyMonAge");
			String one79MonAgeStr = request.getParameter("one79MonAge");

			int zeroMonthAge = 0;
			int one80MonAge = 0;
			int elevMonAge = 0;
			int twlvMonAge = 0;
			int twenty3MonAge = 0;
			int twentyFoMonAge = 0;
			int fifty9MonAge = 0;
			int sixtyMonAge = 0;
			int one79MonAge = 0;

			if (zeroMonthAgeStr != null)
				zeroMonthAge = Integer.parseInt(zeroMonthAgeStr);
			if (one79MonAgeStr != null)
				one79MonAge = Integer.parseInt(one79MonAgeStr);
			if (one80MonAgeStr != null)
				one80MonAge = Integer.parseInt(one80MonAgeStr);
			if (elevMonAgeStr != null)
				elevMonAge = Integer.parseInt(elevMonAgeStr);
			if (twlvMonAgeStr != null)
				twlvMonAge = Integer.parseInt(twlvMonAgeStr);
			if (twenty3MonAgeStr != null)
				twenty3MonAge = Integer.parseInt(twenty3MonAgeStr);
			if (twentyFoMonAgeStr != null)
				twentyFoMonAge = Integer.parseInt(twentyFoMonAgeStr);
			if (fifty9MonAgeStr != null)
				fifty9MonAge = Integer.parseInt(fifty9MonAgeStr);
			if (sixtyMonAgeStr != null)
				sixtyMonAge = Integer.parseInt(sixtyMonAgeStr);
			if (one79MonAgeStr != null)
				one79MonAge = Integer.parseInt(one79MonAgeStr);

			String quarter = null;
			if (request.getParameterValues("quarter") != null) {
				quarter = request.getParameter("quarter");
			}
			String year = request.getParameter("year");

			String[] temp = new String[2];
			if (quarter != null) {
				temp = quarter.split("To");
			}

			String quarterFromStr = temp[0] + "-" + year;
			String quarterToStr = temp[1] + "-" + year;

			Date quarterFromDate = null;
			Date quarterToDate = null;

			try {
				if (quarterToStr != null) {
					quarterFromDate = df.parse(quarterFromStr);
					quarterToDate = df.parse(quarterToStr);

					map.put("quarterBegin", quarterFromDate);
					map.put("quarterEnd", quarterToDate);
				}
			} catch (ParseException e) {
				log.error("Error generated", e);
			}
			
			// log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<datesformed<<<<<<<<<<<"
			// + quarterFromDate + " To >>>>>>" + quarterToDate);

			QuarterlyReportingService service = Context.getService(QuarterlyReportingService.class);

			map.put("valuesCollection", map);
			map.put("year", year);
			map.put("quarterFromDate", df.format(quarterFromDate));
			map.put("quarterToDate", df.format(quarterToDate));
			map.put("maleGender", maleGender);
//			map.put("zeroAge", zeroAge);
//			map.put("fteenAge", foteenAge);

			// ____________________________ table 1.0(ENROLLMENT)__________________________________________
			// //////////////// male 0-179 months
			List<Object[]> cumMalePatUnder179EnrolledBuyTheBegOfQuarterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,zeroMonthAge, one79MonAge);
			List<Object[]> cumMalePatUnder179EnrolledDuringTheQuarterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge);
			List<Object[]> cumMalePat0To79EnrolledEndOfQterList = (List<Object[]>) service.union(cumMalePatUnder179EnrolledBuyTheBegOfQuarterList,cumMalePatUnder179EnrolledDuringTheQuarterList);

			map.put("cumMalePatUnder179EnrolledBuyTheBegOfQuarterList",cumMalePatUnder179EnrolledBuyTheBegOfQuarterList);
			map.put("cumMalePatUnder179EnrolledDuringTheQuarterList",cumMalePatUnder179EnrolledDuringTheQuarterList);
			map.put("cumMalePat0To79EnrolledEndOfQterList",cumMalePat0To79EnrolledEndOfQterList);

			int cumMalePatUnder179EnrolledBuyTheBegOfQuarter = cumMalePatUnder179EnrolledBuyTheBegOfQuarterList.size();
			int cumMalePatUnder179EnrolledDuringTheQuarter = cumMalePatUnder179EnrolledDuringTheQuarterList.size();
			int cumMalePatUnder179EnrolledByTheEndOfQuarter = cumMalePatUnder179EnrolledBuyTheBegOfQuarter+ cumMalePatUnder179EnrolledDuringTheQuarter;

			map.put("cumMalePatUnder179EnrolledBuyTheBegOfQuarter",cumMalePatUnder179EnrolledBuyTheBegOfQuarter);
			map.put("cumMalePatUnder179EnrolledDuringTheQuarter",cumMalePatUnder179EnrolledDuringTheQuarter);
			map.put("cumMalePatUnder179EnrolledByTheEndOfQuarter",cumMalePatUnder179EnrolledByTheEndOfQuarter);

			// //////////////// female 0-179 months
			List<Object[]> cumFemPat0To179EnrolledBuyTheBegOfQuarterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,zeroMonthAge, one79MonAge);
			List<Object[]> cumFemPat0To179EnrolledDuringTheQuarterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge);
			List<Object[]> cumFemPat0To179EnrolledByTheEndOfQuarterList = (List<Object[]>) service.union(cumFemPat0To179EnrolledBuyTheBegOfQuarterList,cumFemPat0To179EnrolledDuringTheQuarterList);

			map.put("cumFemPatEnrolledTheBegOfQterList",cumFemPat0To179EnrolledBuyTheBegOfQuarterList);
			map.put("cumFemPat0To179EnrolledDuringTheQuarterList",cumFemPat0To179EnrolledDuringTheQuarterList);
			map.put("cumFemPat0To179EnrolledByTheEndOfQuarterList",cumFemPat0To179EnrolledByTheEndOfQuarterList);

			int cumFemPat0To179EnrolledTheBegOfQter = cumFemPat0To179EnrolledBuyTheBegOfQuarterList.size();
			int cumFemPat0To179EnrolledDuringTheQuarter = cumFemPat0To179EnrolledDuringTheQuarterList.size();
			int cumFemPat0To179EnrolledByTheEndOfQuarter = cumFemPat0To179EnrolledByTheEndOfQuarterList.size();

			map.put("cumFemPat0To179EnrolledTheBegOfQter",cumFemPat0To179EnrolledTheBegOfQter);
			map.put("cumFemPat0To179EnrolledDuringTheQuarter",cumFemPat0To179EnrolledDuringTheQuarter);
			map.put("cumFemPat0To179EnrolledByTheEndOfQuarter",cumFemPat0To179EnrolledByTheEndOfQuarter);

			// male >180 months
			List<Object[]> cumMalePatOver180MonEnrolledBegOfQterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,one79MonAge, null);
			List<Object[]> cumMalePatOver180EnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, one79MonAge, null);
			List<Object[]> cumMalePatOver180EnrolledEndOfQterList = (List<Object[]>) service.union(cumMalePatOver180MonEnrolledBegOfQterList,	cumMalePatOver180EnrolledDuringQterList);

			map.put("cumMalePatOver180MonEnrolledBegOfQterList",cumMalePatOver180MonEnrolledBegOfQterList);
			map.put("cumMalePatOver180EnrolledDuringQterList",cumMalePatOver180EnrolledDuringQterList);
			map.put("cumMalePatOver180EnrolledEndOfQterList",cumMalePatOver180EnrolledEndOfQterList);

			int cumMalePatOver180MonEnrolledBegOfQter = cumMalePatOver180MonEnrolledBegOfQterList.size();
			int cumMalePatOver180EnrolledDuringQter = cumMalePatOver180EnrolledDuringQterList.size();
			int cumMalePatOver180EnrolledEndOfQter = cumMalePatOver180EnrolledEndOfQterList.size();

			map.put("cumMalePatOver180MonEnrolledBegOfQter",cumMalePatOver180MonEnrolledBegOfQter);
			map.put("cumMalePatOver180EnrolledDuringQter",cumMalePatOver180EnrolledDuringQter);
			map.put("cumMalePatOver180EnrolledEndOfQter",cumMalePatOver180EnrolledEndOfQter);

			// female >180 months
			List<Object[]> cumFemPatOver180MonEnrolledBegOfQterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,one80MonAge, null);
			List<Object[]> cumFemPatOver180MonEnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, one80MonAge, null);
			List<Object[]> cumFemPatOver180MonEnrolledEndOfQterList = (List<Object[]>) service.union(cumFemPatOver180MonEnrolledBegOfQterList,cumFemPatOver180MonEnrolledDuringQterList);

			map.put("cumFemPatOver180MonEnrolledBegOfQterList",cumFemPatOver180MonEnrolledBegOfQterList);
			map.put("cumFemPatOver180MonEnrolledDuringQterList",cumFemPatOver180MonEnrolledDuringQterList);
			map.put("cumFemPatOver180MonEnrolledEndOfQterList",cumFemPatOver180MonEnrolledEndOfQterList);

			int cumFemPatOver180MonEnrolledBegOfQter = cumFemPatOver180MonEnrolledBegOfQterList.size();
			int cumFemPatOver180MonEnrolledDuringQter = cumFemPatOver180MonEnrolledDuringQterList.size();
			int cumFemPatOver180MonEnrolledEndOfQter = cumFemPatOver180MonEnrolledEndOfQterList.size();

			map.put("cumFemPatOver180MonEnrolledBegOfQter",cumFemPatOver180MonEnrolledBegOfQter);
			map.put("cumFemPatOver180MonEnrolledDuringQter",cumFemPatOver180MonEnrolledDuringQter);
			map.put("cumFemPatOver180MonEnrolledEndOfQter",cumFemPatOver180MonEnrolledEndOfQter);

			// male 0-11 months
			List<Object[]> malePat0To11EnrolledByTheBegOfQterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,	zeroMonthAge, elevMonAge);
			List<Object[]> malePat0To11EnrolledDuringTheQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge);
			List<Object[]> malePat0To11EnrolledByTheEndOfQterList = (List<Object[]>) service.union(malePat0To11EnrolledByTheBegOfQterList,malePat0To11EnrolledDuringTheQterList);

			map.put("malePat0To11EnrolledByTheBegOfQterList",malePat0To11EnrolledByTheBegOfQterList);
			map.put("malePat0To11EnrolledDuringTheQterList",malePat0To11EnrolledDuringTheQterList);
			map.put("malePat0To11EnrolledByTheEndOfQterList",malePat0To11EnrolledByTheEndOfQterList);

			int malePat0To11EnrolledByTheBegOfQter = malePat0To11EnrolledByTheBegOfQterList.size();
			int malePat0To11EnrolledDuringTheQter = malePat0To11EnrolledDuringTheQterList.size();
			int malePat0To11EnrolledByTheEndOfQter = malePat0To11EnrolledByTheEndOfQterList.size();

			map.put("malePat0To11EnrolledByTheBegOfQter",malePat0To11EnrolledByTheBegOfQter);
			map.put("malePat0To11EnrolledDuringTheQter",malePat0To11EnrolledDuringTheQter);
			map.put("malePat0To11EnrolledByTheEndOfQter",malePat0To11EnrolledByTheEndOfQter);
			
			//male 12-23 months
			List<Object[]> malePat12To23BegQterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,twlvMonAge, twenty3MonAge);
			List<Object[]> malePat12To23DuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge);
			List<Object[]> malePat12To23EndQterList = (List<Object[]>) service.union(malePat12To23BegQterList,malePat12To23DuringQterList);

			map.put("malePat12To23BegQterList",malePat12To23BegQterList);
			map.put("malePat12To23DuringQterList",malePat12To23DuringQterList);
			map.put("malePat12To23EndQterList",malePat12To23EndQterList);

			int malePat12To23BegQter = malePat12To23BegQterList.size();
			int malePat12To23DuringQter = malePat12To23DuringQterList.size();
			int malePat12To23EndQter = malePat12To23EndQterList.size();

			map.put("malePat12To23BegQter",malePat12To23BegQter);
			map.put("malePat12To23DuringQter",malePat12To23DuringQter);
			map.put("malePat12To23EndQter",malePat12To23EndQter);

			// male 24-59 months
			List<Object[]> malePat24To59BegQterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,twentyFoMonAge, fifty9MonAge);
			List<Object[]> malePat24To59DuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, twentyFoMonAge, fifty9MonAge);
			List<Object[]> malePat24To59EndQterList = (List<Object[]>) service.union(malePat24To59BegQterList,malePat24To59DuringQterList);

			map.put("malePat24To59BegQterList",malePat24To59BegQterList);
			map.put("malePat24To59DuringQterList",malePat24To59DuringQterList);
			map.put("malePat24To59EndQterList",malePat24To59EndQterList);

			int malePat24To59BegQter = malePat24To59BegQterList.size();
			int malePat24To59DuringQter = malePat24To59DuringQterList.size();
			int malePat24To59EndQter = malePat24To59EndQterList.size();

			map.put("malePat24To59BegQter",malePat24To59BegQter);
			map.put("malePat24To59DuringQter",malePat24To59DuringQter);
			map.put("malePat24To59EndQter",malePat24To59EndQter);

			// male 60-179
			List<Object[]> malePat60To179BegQterList = service.getPatientsEnrolled(quarterFromDate, null, maleGender,	sixtyMonAge, one79MonAge);
			List<Object[]> malePat60To179DuringTheQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,maleGender, sixtyMonAge, one79MonAge);
			List<Object[]> malePat60To179EndQterList = (List<Object[]>) service.union(malePat60To179BegQterList,malePat60To179DuringTheQterList);

			map.put("malePat60To179BegQterList",malePat60To179BegQterList);
			map.put("malePat60To179DuringTheQterList",	malePat60To179DuringTheQterList);
			map.put("malePat60To179EndQterList",malePat60To179EndQterList);

			int malePat60To179BegQter = malePat60To179BegQterList.size();
			int malePat60To179DuringTheQter = malePat60To179DuringTheQterList.size();
			int malePat60To179EndQter = malePat60To179EndQterList.size();

			map.put("malePat60To179BegQter",malePat60To179BegQter);
			map.put("malePat60To179DuringTheQter",malePat60To179DuringTheQter);
			map.put("malePat60To179EndQter",malePat60To179EndQter);

			// female 0-11 months
			List<Object[]> femalePat0To11EnrolledBegQterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,zeroMonthAge, elevMonAge);
			List<Object[]> femalePat0To11EnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge);
			List<Object[]> femalePat0To11EnrolledEndQterList = (List<Object[]>) service.union(femalePat0To11EnrolledBegQterList,femalePat0To11EnrolledDuringQterList);

			map.put("femalePat0To11EnrolledBegQterList",femalePat0To11EnrolledBegQterList);
			map.put("femalePat0To11EnrolledDuringQterList",femalePat0To11EnrolledDuringQterList);
			map.put("femalePat0To11EnrolledEndQterList",femalePat0To11EnrolledEndQterList);

			int femalePat0To11EnrolledBegQter = femalePat0To11EnrolledBegQterList.size();
			int femalePat0To11EnrolledDuringQter = femalePat0To11EnrolledDuringQterList.size();
			int femalePat0To11EnrolledEndQter = femalePat0To11EnrolledEndQterList.size();

			map.put("femalePat0To11EnrolledBegQter",femalePat0To11EnrolledBegQter);
			map.put("femalePat0To11EnrolledDuringQter",femalePat0To11EnrolledDuringQter);
			map.put("femalePat0To11EnrolledEndQter",femalePat0To11EnrolledEndQter);
			
			// female 12-23 months
			List<Object[]> femalePat12To23EnrolledBegQterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,	twlvMonAge, twenty3MonAge);
			List<Object[]> femalePat12To23EnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge);
			List<Object[]> femalePat12To23EnrolledEndQterList = (List<Object[]>) service.union(femalePat12To23EnrolledBegQterList,femalePat12To23EnrolledDuringQterList);

			map.put("femalePat12To23EnrolledBegQterList",femalePat12To23EnrolledBegQterList);
			map.put("femalePat12To23EnrolledDuringQterList",femalePat12To23EnrolledDuringQterList);
			map.put("femalePat12To23EnrolledEndQterList",femalePat12To23EnrolledEndQterList);

			int femalePat12To23EnrolledBegQter = femalePat12To23EnrolledBegQterList.size();
			int femalePat12To23EnrolledDuringQter = femalePat12To23EnrolledDuringQterList.size();
			int femalePat12To23EnrolledEndQter = femalePat12To23EnrolledEndQterList.size();

			map.put("femalePat12To23EnrolledBegQter",femalePat12To23EnrolledBegQter);
			map.put("femalePat12To23EnrolledDuringQter",femalePat12To23EnrolledDuringQter);
			map.put("femalePat12To23EnrolledEndQter",femalePat12To23EnrolledEndQter);

			// female 24-59 months
			List<Object[]> femalePat24To59EnrolledBegQterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,twentyFoMonAge, fifty9MonAge);
			List<Object[]> femalePat24To59EnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge);
			List<Object[]> femalePat24To59EnrolledEndQterList = (List<Object[]>) service.union(femalePat24To59EnrolledBegQterList,femalePat24To59EnrolledDuringQterList);

			map.put("femalePat24To59EnrolledBegQterList",femalePat24To59EnrolledBegQterList);
			map.put("femalePat24To59EnrolledDuringQterList",femalePat24To59EnrolledDuringQterList);
			map.put("femalePat24To59EnrolledEndQterList",femalePat24To59EnrolledEndQterList);

			int femalePat24To59EnrolledBegQter = femalePat24To59EnrolledBegQterList.size();
			int femalePat24To59EnrolledDuringQter = femalePat24To59EnrolledDuringQterList.size();
			int femalePat24To59EnrolledEndQter = femalePat24To59EnrolledEndQterList.size();

			map.put("femalePat24To59EnrolledBegQter",femalePat24To59EnrolledBegQter);
			map.put("femalePat24To59EnrolledDuringQter",femalePat24To59EnrolledDuringQter);
			map.put("femalePat24To59EnrolledEndQter",femalePat24To59EnrolledEndQter);

			// female 60-179
			List<Object[]> femalePat60To17EnrolledBegQterList = service.getPatientsEnrolled(quarterFromDate, null, femaleGender,sixtyMonAge, one79MonAge);
			List<Object[]> femalePat60To179EnrolledDuringQterList = service.getPatientsEnrolled(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge);
			List<Object[]> femalePat60To179EnrolledEndQterList =  (List<Object[]>) service.union(femalePat60To17EnrolledBegQterList,femalePat60To179EnrolledDuringQterList);

			map.put("femalePat60To17EnrolledBegQterList",femalePat60To17EnrolledBegQterList);
			map.put("femalePat60To179EnrolledDuringQterList",femalePat60To179EnrolledDuringQterList);
			map.put("femalePat60To179EnrolledEndQterList",femalePat60To179EnrolledEndQterList);

			int femalePat60To179EnrolledBegQter = femalePat60To17EnrolledBegQterList.size();
			int femalePat60To179EnrolledDuringQter = femalePat60To179EnrolledDuringQterList.size();
			int femalePat60To179EnrolledEndQter = femalePat60To179EnrolledEndQterList.size();

			map.put("femalePat60To179EnrolledBegQter",femalePat60To179EnrolledBegQter);
			map.put("femalePat60To179EnrolledDuringQter",femalePat60To179EnrolledDuringQter);
			map.put("femalePat60To179EnrolledEndQter",femalePat60To179EnrolledEndQter);

			// ============================total enrolled====================================================================
			//############################# total enrolled beginning of the quarter #############################
			List<Object[]> cumMalePatEnrolledBuyTheBegOfQterList = (List<Object[]>) service.union(cumMalePatUnder179EnrolledBuyTheBegOfQuarterList,cumMalePatOver180MonEnrolledBegOfQterList);
			List<Object[]> cumFemalePatEnrolledBuyTheBegOfQuarterList = (List<Object[]>) service.union(cumFemPat0To179EnrolledBuyTheBegOfQuarterList,cumFemPatOver180MonEnrolledBegOfQterList);
			List<Object[]> totalEnrolledBegQterList = (List<Object[]>) service.union(cumMalePatEnrolledBuyTheBegOfQterList,cumFemalePatEnrolledBuyTheBegOfQuarterList);

			int totalEnrolledBegQter = totalEnrolledBegQterList.size();
			map.put("totalEnrolledBegQter", totalEnrolledBegQter);
			map.put("totalEnrolledBegQterList", totalEnrolledBegQterList);

            //############################# total enrolled during the quarter#############################
			List<Object[]> cumMalePatEnrolledDuringTheQuarterList = (List<Object[]>) service.union(cumMalePatUnder179EnrolledDuringTheQuarterList,cumMalePatOver180EnrolledDuringQterList);
			List<Object[]> cumFemalePatEnrolledDuringTheQuarterList = (List<Object[]>) service.union(cumFemPat0To179EnrolledDuringTheQuarterList,cumFemPatOver180MonEnrolledDuringQterList);
			List<Object[]> totalEnrolledDuringQterList = (List<Object[]>) service.union(cumMalePatEnrolledDuringTheQuarterList,cumFemalePatEnrolledDuringTheQuarterList);

			int totalEnrolledDuringQter = totalEnrolledDuringQterList.size();
			map.put("totalEnrolledDuringQter", totalEnrolledDuringQter);
			map.put("totalEnrolledDuringQterList", totalEnrolledDuringQterList);

            //#############################  total enrolled end of the quarter #############################
			List<Object[]> totalEnrolledEndQterList =  (List<Object[]>) service.union(totalEnrolledBegQterList, totalEnrolledDuringQterList);
			int totalEnrolledEndQter = totalEnrolledEndQterList.size();
			map.put("totalEnrolledEndQter", totalEnrolledEndQter);
			map.put("totalEnrolledEndQterList", totalEnrolledEndQterList);

			// _______________patients who recieved HIV Care during the quarter_____________________________________

			// male 0-179 months
			List<Object[]> malePatRecievedCareAge0To179List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge);
			map.put("malePatRecievedCareAge0To179List",malePatRecievedCareAge0To179List);
			int malePatRecievedCareAge0To179 = malePatRecievedCareAge0To179List.size();
			map.put("malePatRecievedCareAge0To179", malePatRecievedCareAge0To179);

			// female 0-179 months
			List<Object[]> femalePatRecievedCareAge0To179List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge);
			map.put("femalePatRecievedCareAge0To179List",femalePatRecievedCareAge0To179List);
			int femalePatRecievedCareAge0To179 = femalePatRecievedCareAge0To179List.size();
			map.put("femalePatRecievedCareAge0To179",femalePatRecievedCareAge0To179);

			// male > 180 months
			List<Object[]> maleOver180ReceivedHIVCareList = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,maleGender, one80MonAge, null);
			map.put("maleOver180ReceivedHIVCareList",maleOver180ReceivedHIVCareList);
			int maleOver180ReceivedHIVCare = maleOver180ReceivedHIVCareList.size();
			map.put("maleOver180ReceivedHIVCare", maleOver180ReceivedHIVCare);

			// female > 180 months
			List<Object[]> femaleOver180ReceivedHIVCareList = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,	femaleGender, one80MonAge, null);
			map.put("femaleOver180ReceivedHIVCareList",femaleOver180ReceivedHIVCareList);
			int femaleOver180ReceivedHIVCare = femaleOver180ReceivedHIVCareList.size();
			map.put("femaleOver180ReceivedHIVCare", femaleOver180ReceivedHIVCare);

			// male 0-11 months
			List<Object[]> malePatRecievedCareAge0To11List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge);
			map.put("malePatRecievedCareAge0To11List",malePatRecievedCareAge0To11List);
			int malePatRecievedCareAge0To11 = malePatRecievedCareAge0To11List.size();
			map.put("malePatRecievedCareAge0To11", malePatRecievedCareAge0To11);
			
			// male 12-23 months 
			List<Object[]> malePatRecievedCareAge12To23List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge);
			map.put("malePatRecievedCareAge12To23List",malePatRecievedCareAge12To23List);
			int malePatRecievedCareAge12To23 = malePatRecievedCareAge12To23List.size();
			map.put("malePatRecievedCareAge12To23", malePatRecievedCareAge12To23);

			// male 24-59 months
			List<Object[]> malePatRecievedCareAge24To59List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,maleGender, twentyFoMonAge, fifty9MonAge);
			map.put("malePatRecievedCareAge24To59List",malePatRecievedCareAge24To59List);
			int malePatRecievedCareAge24To59 = malePatRecievedCareAge24To59List.size();
			map.put("malePatRecievedCareAge24To59", malePatRecievedCareAge24To59);

			// male 60-179  months
			List<Object[]> malePatRecievedCareAge60To179List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,	maleGender, sixtyMonAge, one79MonAge);
			map.put("malePatRecievedCareAge60To179List",malePatRecievedCareAge60To179List);
			int malePatRecievedCareAge60To179 = malePatRecievedCareAge60To179List.size();
			map.put("malePatRecievedCareAge60To179", malePatRecievedCareAge60To179);

			// female 0-11 months
			List<Object[]> femalePatRecievedCareAge0To11List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge);
			map.put("femalePatRecievedCareAge0To11List",femalePatRecievedCareAge0To11List);
			int femalePatRecievedCareAge0To11 = femalePatRecievedCareAge0To11List.size();
			map.put("femalePatRecievedCareAge0To11",femalePatRecievedCareAge0To11);
			
			// female 12-23 months
			List<Object[]> femalePatRecievedCareAge12To23List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge);
			map.put("femalePatRecievedCareAge12To23List",femalePatRecievedCareAge12To23List);
			int femalePatRecievedCareAge12To23 = femalePatRecievedCareAge12To23List.size();
			map.put("femalePatRecievedCareAge12To23",femalePatRecievedCareAge12To23);

			// female 24-59 months
			List<Object[]> femalePatRecievedCareAge24To59List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge);
			map.put("femalePatRecievedCareAge24To59List",femalePatRecievedCareAge24To59List);
			int femalePatRecievedCareAge24To59 = femalePatRecievedCareAge24To59List.size();
			map.put("femalePatRecievedCareAge24To59",femalePatRecievedCareAge24To59);

			// female 60-179 months
			List<Object[]> femalePatRecievedCareAge60To179List = service.getPatientsRecievedHIVCare(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge);
			map.put("femalePatRecievedCareAge60To179List",femalePatRecievedCareAge60To179List);
			int femalePatRecievedCareAge60To179 = femalePatRecievedCareAge60To179List.size();
			map.put("femalePatRecievedCareAge60To179",femalePatRecievedCareAge60To179);

			// total who received HIV Care
			List<Object[]> malePatRecievedCare = (List<Object[]>) service.union(malePatRecievedCareAge0To179List,maleOver180ReceivedHIVCareList);
			List<Object[]> femalePatRecievedCare = (List<Object[]>) service.union(femalePatRecievedCareAge0To179List,femaleOver180ReceivedHIVCareList);
			List<Object[]> totalPatientsRecievedCareList = (List<Object[]>) service.union(malePatRecievedCare, femalePatRecievedCare);

			int totalPatientsRecievedCare = totalPatientsRecievedCareList.size();
			map.put("totalPatientsRecievedCare", totalPatientsRecievedCare);
			map.put("totalPatientsRecievedCareList",totalPatientsRecievedCareList);
			
			
			
			//###################patients who received Cotrimo during the quarter#########################################
			// male 0-179 months
			List<Object[]> malePatRecievedCotrimo0To179List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge);
			map.put("malePatRecievedCotrimo0To179List",malePatRecievedCotrimo0To179List);
			int malePatRecievedCotrimo0To179 = malePatRecievedCotrimo0To179List.size();
			map.put("malePatRecievedCotrimo0To179", malePatRecievedCotrimo0To179);

			// female 0-179 months
			List<Object[]> femalePatRecievedCotrimo0To179List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge);
			map.put("femalePatRecievedCotrimo0To179List",femalePatRecievedCotrimo0To179List);
			int femalePatRecievedCotrimo0To179 = femalePatRecievedCotrimo0To179List.size();
			map.put("femalePatRecievedCotrimo0To179",femalePatRecievedCotrimo0To179);

			// male > 180 months
			List<Object[]> maleOver180ReceivedCotrimoList = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,maleGender, one80MonAge, null);
			map.put("maleOver180ReceivedCotrimoList",maleOver180ReceivedCotrimoList);
			int maleOver180ReceivedCotrimo = maleOver180ReceivedCotrimoList.size();
			map.put("maleOver180ReceivedCotrimo", maleOver180ReceivedCotrimo);

			// female > 180 months
			List<Object[]> femaleOver180ReceivedCotrimoList = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,	femaleGender, one80MonAge, null);
			map.put("femaleOver180ReceivedCotrimoList",femaleOver180ReceivedCotrimoList);
			int femaleOver180ReceivedCotrimo= femaleOver180ReceivedCotrimoList.size();
			map.put("femaleOver180ReceivedCotrimo", femaleOver180ReceivedCotrimo);

			// male 0-11 months
			List<Object[]> malePatRecievedCotrimo0To11List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge);
			map.put("malePatRecievedCotrimo0To11List",malePatRecievedCotrimo0To11List);
			int malePatRecievedCotrimo0To11 = malePatRecievedCotrimo0To11List.size();
			map.put("malePatRecievedCotrimo0To11", malePatRecievedCotrimo0To11);
			
			// male 12-23 months 
			List<Object[]> malePatRecievedCotrimo12To23List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge);
			map.put("malePatRecievedCotrimo12To23List",malePatRecievedCotrimo12To23List);
			int malePatRecievedCotrimo12To23 = malePatRecievedCotrimo12To23List.size();
			map.put("malePatRecievedCotrimo12To23", malePatRecievedCotrimo12To23);

			// male 24-59 months
			List<Object[]> malePatRecievedCotrimo24To59List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,maleGender, twentyFoMonAge, fifty9MonAge);
			map.put("malePatRecievedCotrimo24To59List",malePatRecievedCotrimo24To59List);
			int malePatRecievedCotrimo24To59 = malePatRecievedCotrimo24To59List.size();
			map.put("malePatRecievedCotrimo24To59", malePatRecievedCotrimo24To59);

			// male 60-179  months
			List<Object[]> malePatRecievedCotrimo60To179List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,	maleGender, sixtyMonAge, one79MonAge);
			map.put("malePatRecievedCotrimo60To179List",malePatRecievedCotrimo60To179List);
			int malePatRecievedCotrimo60To179 = malePatRecievedCotrimo60To179List.size();
			map.put("malePatRecievedCotrimo60To179", malePatRecievedCotrimo60To179);

			// female 0-11 months
			List<Object[]> femalePatRecievedCotrimo0To11List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge);
			map.put("femalePatRecievedCotrimo0To11List",femalePatRecievedCotrimo0To11List);
			int femalePatRecievedCotrimo0To11 = femalePatRecievedCotrimo0To11List.size();
			map.put("femalePatRecievedCotrimo0To11",femalePatRecievedCotrimo0To11);
			
			// female 12-23 months
			List<Object[]> femalePatRecievedCotrimo12To23List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge);
			map.put("femalePatRecievedCotrimo12To23List",femalePatRecievedCotrimo12To23List);
			int femalePatRecievedCotrimo12To23 = femalePatRecievedCotrimo12To23List.size();
			map.put("femalePatRecievedCotrimo12To23",femalePatRecievedCotrimo12To23);

			// female 24-59 months
			List<Object[]> femalePatRecievedCotrimo24To59List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge);
			map.put("femalePatRecievedCotrimo24To59List",femalePatRecievedCotrimo24To59List);
			int femalePatRecievedCotrimo24To59 = femalePatRecievedCotrimo24To59List.size();
			map.put("femalePatRecievedCotrimo24To59",femalePatRecievedCotrimo24To59);

			// female 60-179 months
			List<Object[]> femalePatRecievedCotrimo60To179List = service.getPatientsStartedCotrimo(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge);
			map.put("femalePatRecievedCotrimo60To179List",femalePatRecievedCotrimo60To179List);
			int femalePatRecievedCotrimo60To179 = femalePatRecievedCotrimo60To179List.size();
			map.put("femalePatRecievedCotrimo60To179",femalePatRecievedCotrimo60To179);

			// total who received Cotrimo
			List<Object[]> malePatRecievedCotrimo = (List<Object[]>) service.union(malePatRecievedCotrimo0To179List,maleOver180ReceivedCotrimoList);
			List<Object[]> femalePatRecievedCotrimo = (List<Object[]>) service.union(femalePatRecievedCotrimo0To179List,femaleOver180ReceivedCotrimoList);
			List<Object[]> totalPatientsRecievedCotrimoList = (List<Object[]>) service.union(malePatRecievedCotrimo, femalePatRecievedCotrimo);

			int totalPatientsRecievedCotrimo = totalPatientsRecievedCotrimoList.size();
			map.put("totalPatientsRecievedCotrimo", totalPatientsRecievedCotrimo);
			map.put("totalPatientsRecievedCotrimoList",totalPatientsRecievedCotrimoList);
			

			// ____________________________eligible______________________________________________________________________________________________

			List<Object[]> eligiblePatientsList = service.getAllPatientsEligibleForARVsButNotYetStarted(2,quarterFromDate, quarterToDate);
			map.put("eligiblePatientsList", eligiblePatientsList);
			int eligiblePatients = eligiblePatientsList.size();
			map.put("eligiblePatients", eligiblePatients);

			// ___________________patients transferred in during the quarter__________________________________
			// ======================adult transferred in=========================
			// male 0-179
			List<Object[]> male0To179TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge);
			map.put("male0To179TransfInDuringQterList",male0To179TransfInDuringQterList);
			int male0To179TransfInDuringQter = male0To179TransfInDuringQterList.size();
			map.put("male0To179TransfInDuringQter", male0To179TransfInDuringQter);

			// male > 180
			List<Object[]> maleOver180TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,maleGender, one80MonAge, null);
			map.put("maleOver180TransfInDuringQterList",maleOver180TransfInDuringQterList);
			int maleOver180TransfInDuringQter = maleOver180TransfInDuringQterList.size();

			// female 0-179
			List<Object[]> femaleOTo179TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge);
			map.put("femaleOTo179TransfInDuringQterList",femaleOTo179TransfInDuringQterList);
			int femaleOTo179TransfInDuringQter = femaleOTo179TransfInDuringQterList.size();

			// female > 180
			List<Object[]> femaleOver180TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, one80MonAge, null);
			map.put("femaleOver180TransfInDuringQterList",femaleOver180TransfInDuringQterList);
			int femaleOver180TransfInDuringQter = femaleOver180TransfInDuringQterList.size();

			List<Object[]> maleTransfInDuringQterList = (List<Object[]>) service.union(male0To179TransfInDuringQterList,maleOver180TransfInDuringQterList);
			List<Object[]> femaleTransfInDuringQter = (List<Object[]>) service.union(femaleOTo179TransfInDuringQterList,femaleOver180TransfInDuringQterList);
			List<Object[]> transferredInTotalList = (List<Object[]>) service.union(maleTransfInDuringQterList, femaleTransfInDuringQter);
			
//			log.info("kikikiiiiiiiiiiiiiiiiiiiiiiiiii "+transferredInTotalList);

			int transferredInTotal = transferredInTotalList.size();

			map.put("male0To179TransfInDuringQter",male0To179TransfInDuringQter); 
			map.put("maleOver180TransfInDuringQter",maleOver180TransfInDuringQter);
			map.put("femaleOTo179TransfInDuringQter",femaleOTo179TransfInDuringQter);
			map.put("femaleOver180TransfInDuringQter",femaleOver180TransfInDuringQter);

			map.put("transferredInTotalList", transferredInTotalList);
			map.put("transferredInTotal", transferredInTotal);

			// =================pediatric transferred in========================================

			// male 0-11
			List<Object[]> male0To11TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge);
			int male0To11TransfInDuringQter = male0To11TransfInDuringQterList.size();
			map.put("male0To11TransfInDuringQter", male0To11TransfInDuringQter);
			map.put("male0To1TransfInDuringQterList",male0To11TransfInDuringQterList);

			// male 12-23
			List<Object[]> male12To23TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge);
			int male12To23TransfInDuringQter = male12To23TransfInDuringQterList.size();
			map.put("male12To23TransfInDuringQter", male12To23TransfInDuringQter);
			map.put("male12To23TransfInDuringQterList",male12To23TransfInDuringQterList);
			
			// male 24-59
			List<Object[]> male24To59TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,	maleGender, twentyFoMonAge, fifty9MonAge);
			int male24To59TransfInDuringQter = male24To59TransfInDuringQterList.size();
			map.put("male24To59TransfInDuringQter", male24To59TransfInDuringQter);
			map.put("male24To59TransfInDuringQterList",male24To59TransfInDuringQterList);

			// male 60-179
			List<Object[]> male60To179TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,maleGender, sixtyMonAge, one79MonAge);
			int male60To179TransfInDuringQter = male60To179TransfInDuringQterList.size();
			map.put("male60To179TransfInDuringQter", male60To179TransfInDuringQter);
			map.put("male60To179TransfInDuringQterList",male60To179TransfInDuringQterList);

			// female 0-11
			List<Object[]> female0To11TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge);
			int female0To11TransfInDuringQter = female0To11TransfInDuringQterList	.size();
			map.put("female0To11TransfInDuringQter",female0To11TransfInDuringQter);
			map.put("female0To11TransfInDuringQterList",female0To11TransfInDuringQterList);

			// female 12-23
			List<Object[]> female12To23TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge);
			int female12To23TransfInDuringQter = female12To23TransfInDuringQterList.size();
			map.put("female12To23TransfInDuringQter",female12To23TransfInDuringQter);
			map.put("female12To23TransfInDuringQterList",female12To23TransfInDuringQterList);

			// female 24-59
			List<Object[]> female24To59TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge);
			int female24To59TransfInDuringQter = female24To59TransfInDuringQterList.size();
			map.put("female24To59TransfInDuringQter",female24To59TransfInDuringQter);
			map.put("female24To59TransfInDuringQterList",female24To59TransfInDuringQterList);
			
			// female 60-179
			List<Object[]> female60To179TransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge);
			int female60To179TransfInDuringQter = female60To179TransfInDuringQterList.size();
			map.put("female60To179TransfInDuringQter",female60To179TransfInDuringQter);
			map.put("female60To179TransfInDuringQterList",female60To179TransfInDuringQterList);

			// _______________________________table 2 ====>new on ART_______________________
			// ============================= adult(table 2.0)===================================
			// male 0-179
			List<Object[]> cumMalePat0To179StartedARTBegQterList1 = service.getPatientsStartedART(quarterFromDate, null, maleGender,zeroMonthAge, one79MonAge);
			List<Object[]> cumMalePat0To179StartedARTBegQterList = (List<Object[]>) service.SubtractACollection(cumMalePat0To179StartedARTBegQterList1, transferredInTotalList);
			List<Object[]> cumMalePat0To179StartedDuringQterList = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge);
//			List<Object[]> cumMalePat0To179StartedDuringQterList = (List<Object[]>) service.union(cumMalePat0To179StartedDuringQterList1, male0To179TransfInDuringQterList);
			List<Object[]> cumMalePat0To179StartedEndQterList = (List<Object[]>) service.union(cumMalePat0To179StartedARTBegQterList, cumMalePat0To179StartedDuringQterList);

			map.put("cumMalePat0To179StartedARTBegQterList",cumMalePat0To179StartedARTBegQterList);
			map.put("cumMalePat0To179StartedDuringQterList",cumMalePat0To179StartedDuringQterList);
			map.put("cumMalePat0To179StartedEndQterList",cumMalePat0To179StartedEndQterList);

			int cumMalePat0To179StartedARTBegOfQter = cumMalePat0To179StartedARTBegQterList.size();
			int cumMalePat0To179StartedDuringQter = cumMalePat0To179StartedDuringQterList.size();
			int cumMalePat0To179StartedEndQter = cumMalePat0To179StartedEndQterList.size();

			map.put("cumMalePat0To179StartedARTBegOfQter",cumMalePat0To179StartedARTBegOfQter);
			map.put("cumMalePat0To179StartedDuringQter",cumMalePat0To179StartedDuringQter);
			map.put("cumMalePat0To179StartedEndQter",cumMalePat0To179StartedEndQter);

			// female 0-179
			List<Object[]> cumfem0To179StartedARTBegQterList1 = service.getPatientsStartedART(quarterFromDate, null, femaleGender,zeroMonthAge, one79MonAge);
			List<Object[]> cumfem0To179StartedARTBegQterList = (List<Object[]>) service.SubtractACollection(cumfem0To179StartedARTBegQterList1, transferredInTotalList);
			List<Object[]> cumfem0To179StartedARTDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge);
			List<Object[]> cumfem0To179StartedARTDuringQterList = (List<Object[]>) service.union(cumfem0To179StartedARTDuringQterList1, femaleOTo179TransfInDuringQterList);
			List<Object[]> cumfemPat0To179StartedARTEndQterList = (List<Object[]>) service.union(cumfem0To179StartedARTBegQterList, cumfem0To179StartedARTDuringQterList);

			map.put("cumfem0To179StartedARTBegQterList",cumfem0To179StartedARTBegQterList);
			map.put("cumfem0To179StartedARTDuringQterList",cumfem0To179StartedARTDuringQterList);
			map.put("cumfemPat0To179StartedARTEndQterList",cumfemPat0To179StartedARTEndQterList);

			int cumfem0To179StartedARTBegQter = cumfem0To179StartedARTBegQterList.size();
			int cumfem0To179StartedARTDuringQter = cumfem0To179StartedARTDuringQterList.size();
			int cumfemPat0To179StartedARTEndQter = cumfemPat0To179StartedARTEndQterList.size();

			map.put("cumfem0To179StartedARTBegQter",cumfem0To179StartedARTBegQter);
			map.put("cumfem0To179StartedARTDuringQter",cumfem0To179StartedARTDuringQter);
			map.put("cumfemPat0To179StartedARTEndQter",cumfemPat0To179StartedARTEndQter);

			// male >180 transferredInTotalList
			List<Object[]> cumMalePatOver180StartedARTBegQterList1 = service.getPatientsStartedART(quarterFromDate, null, maleGender,one80MonAge, null);
			List<Object[]> cumMalePatOver180StartedARTBegQterList = (List<Object[]>) service.SubtractACollection(cumMalePatOver180StartedARTBegQterList1, transferredInTotalList);
			List<Object[]> cumMalePatOver180StartedARTDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, one80MonAge, null);
			List<Object[]> cumMalePatOver180StartedARTDuringQterList = (List<Object[]>) service.union(cumMalePatOver180StartedARTDuringQterList1, maleOver180TransfInDuringQterList);
			List<Object[]> cumMalePatOver180StartedARTEndQterList = (List<Object[]>) service.union(cumMalePatOver180StartedARTBegQterList, cumMalePatOver180StartedARTDuringQterList);

			map.put("cumMalePatOver180StartedARTBegQterList",cumMalePatOver180StartedARTBegQterList);
			map.put("cumMalePatOver180StartedARTDuringQterList",cumMalePatOver180StartedARTDuringQterList);
			map.put("cumMalePatOver180StartedARTEndQterList",cumMalePatOver180StartedARTEndQterList);

			int cumMalePatOver180StartedARTBegQter = cumMalePatOver180StartedARTBegQterList.size();
			int cumMalePatOver180StartedARTDuringQter = cumMalePatOver180StartedARTDuringQterList.size();
			int cumMalePatOver80StartedARTEndQter = cumMalePatOver180StartedARTEndQterList.size();

			map.put("cumMalePatOver180StartedARTBegQter",cumMalePatOver180StartedARTBegQter);
			map.put("cumMalePatOver180StartedARTDuringQter",cumMalePatOver180StartedARTDuringQter);
			map.put("cumMalePatOver80StartedARTEndQter",cumMalePatOver80StartedARTEndQter);

			// female >180
			List<Object[]> cumFemPatOver180StartedARTBegQterList1 = service.getPatientsStartedART(quarterFromDate, null, femaleGender,one80MonAge, null);
			List<Object[]> cumFemPatOver180StartedARTBegQterList = (List<Object[]>) service.SubtractACollection(cumFemPatOver180StartedARTBegQterList1, transferredInTotalList);
			List<Object[]> cumFemPatOver180StartedARTDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, one80MonAge, null);
			List<Object[]> cumFemPatOver180StartedARTDuringQterList = (List<Object[]>) service.union(cumFemPatOver180StartedARTDuringQterList1, femaleOver180TransfInDuringQterList);
			List<Object[]> cumFemPatOver180StartedARTEndQterList = (List<Object[]>) service.union(cumFemPatOver180StartedARTBegQterList, cumFemPatOver180StartedARTDuringQterList);

			map.put("cumFemPatOver180StartedARTBegQterList",cumFemPatOver180StartedARTBegQterList);
			map.put("cumFemPatOver180StartedARTDuringQterList",cumFemPatOver180StartedARTDuringQterList);
			map.put("cumFemPatOver180StartedARTEndQterList",cumFemPatOver180StartedARTEndQterList);

			int cumFemPatOver180StartedARTBegQter = cumFemPatOver180StartedARTBegQterList.size();
			int cumFemPatOver180StartedARTDuringQter = cumFemPatOver180StartedARTDuringQterList.size();
			int cumFemPatOver180StartedARTEndQter = cumFemPatOver180StartedARTEndQterList.size();

			map.put("cumFemPatOver180StartedARTBegQter",cumFemPatOver180StartedARTBegQter);
			map.put("cumFemPatOver180StartedARTDuringQter",cumFemPatOver180StartedARTDuringQter);
			map.put("cumFemPatOver180StartedARTEndQter",cumFemPatOver180StartedARTEndQter);

			// total
			List<Object[]> cumMalePatStartedARTBuyTheBegOfQterList = (List<Object[]>) service.union(cumMalePat0To179StartedARTBegQterList,cumMalePatOver180StartedARTBegQterList);
			List<Object[]> cumfemPatStartedARTBegQterList = (List<Object[]>) service.union(cumfem0To179StartedARTBegQterList,cumFemPatOver180StartedARTBegQterList);
			List<Object[]> cumStartedARTBegQterList = (List<Object[]>) service.union(cumMalePatStartedARTBuyTheBegOfQterList,cumfemPatStartedARTBegQterList);

			int totalCumBuyBegQter = cumStartedARTBegQterList.size();

			List<Object[]> cumMalePatStartedDuringQterList = (List<Object[]>) service.union(cumMalePat0To179StartedDuringQterList,cumMalePatOver180StartedARTDuringQterList);
			List<Object[]> cumFemPatStartedDuringQterList = (List<Object[]>) service.union(cumfem0To179StartedARTDuringQterList,cumFemPatOver180StartedARTDuringQterList);
			List<Object[]> cumPatStartedDuringQterList = (List<Object[]>) service.union(cumMalePatStartedDuringQterList,cumFemPatStartedDuringQterList);

			int totalCumDuringQter = cumPatStartedDuringQterList.size();

			List<Object[]> cumMalePatStartedEndQterList = (List<Object[]>) service.union(cumMalePat0To179StartedEndQterList,cumMalePatOver180StartedARTEndQterList);
			List<Object[]> cumfemPatStartedARTEndQterList = (List<Object[]>) service.union(cumfemPat0To179StartedARTEndQterList,cumFemPatOver180StartedARTEndQterList);
			List<Object[]> cumPatStartedARTEndQterList = (List<Object[]>) service.union(cumMalePatStartedEndQterList,cumfemPatStartedARTEndQterList);

			int totalCumEndQter = cumPatStartedARTEndQterList.size();

			map.put("totalCumBuyBegQter", totalCumBuyBegQter);
			map.put("totalCumDuringQter", totalCumDuringQter);
			map.put("totalCumEndQter", totalCumEndQter);

			map.put("cumStartedARTBuyTheBegOfQterList",cumStartedARTBegQterList);
			map.put("cumPatStartedDuringTheQterList",cumPatStartedDuringQterList);
			map.put("cumPatStartedARTBuyTheEndOfQterList",cumPatStartedARTEndQterList);

			// ====================2.1 (pediatric)=========================================
			// male 0-11
			List<Object[]> malePat0To11StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, maleGender,zeroMonthAge, elevMonAge);
			List<Object[]> malePat0To11StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge);
			List<Object[]> malePat0To11StartedDuringQterList = (List<Object[]>) service.union(malePat0To11StartedDuringQterList1, male0To11TransfInDuringQterList);
			List<Object[]> malePat0To11StartedEndQterList = (List<Object[]>) service.union(malePat0To11StartedBegQterList, malePat0To11StartedDuringQterList);

			map.put("malePat0To11StartedBegQterList",malePat0To11StartedBegQterList);
			map.put("malePat0To11StartedDuringQterList",malePat0To11StartedDuringQterList);
			map.put("malePat0To11StartedEndQterList",malePat0To11StartedEndQterList);

			int malePat0To11StartedBegQter = malePat0To11StartedBegQterList.size();
			int malePat0To11StartedDuringQter = malePat0To11StartedDuringQterList.size();
			int malePat0To11StartedEndQter = malePat0To11StartedEndQterList.size();

			map.put("malePat0To11StartedBegQter",malePat0To11StartedBegQter);
			map.put("malePat0To11StartedDuringQter",malePat0To11StartedDuringQter);
			map.put("malePat0To11StartedEndQter",malePat0To11StartedEndQter);
			
			// male 12-23
			List<Object[]> malePat12To23StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, maleGender,twlvMonAge, twenty3MonAge);
			List<Object[]> malePat12To23StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge);
			List<Object[]> malePat12To23StartedDuringQterList = (List<Object[]>) service.union(malePat12To23StartedDuringQterList1, male12To23TransfInDuringQterList);
			List<Object[]> malePat12To23StartedEndQterList = (List<Object[]>) service.union(malePat12To23StartedBegQterList, malePat12To23StartedDuringQterList);

			map.put("malePat12To23StartedBegQterList",malePat12To23StartedBegQterList);
			map.put("malePat12To23StartedDuringQterList",malePat12To23StartedDuringQterList);
			map.put("malePat12To23StartedEndQterList",malePat12To23StartedEndQterList);

			int malePat12To23StartedBegQter = malePat12To23StartedBegQterList.size();
			int malePat12To23StartedDuringQter = malePat12To23StartedDuringQterList.size();
			int malePat12To23StartedEndQter = malePat12To23StartedEndQterList.size();

			map.put("malePat12To23StartedBegQter",malePat12To23StartedBegQter);
			map.put("malePat12To23StartedDuringQter",malePat12To23StartedDuringQter);
			map.put("malePat12To23StartedEndQter",malePat12To23StartedEndQter);
			
			// male 24-59
			List<Object[]> malePat24To59StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, maleGender,twentyFoMonAge, fifty9MonAge);
			List<Object[]> malePat24To59StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, twentyFoMonAge, fifty9MonAge);
			List<Object[]> malePat24To59StartedDuringQterList = (List<Object[]>) service.union(malePat24To59StartedDuringQterList1, male24To59TransfInDuringQterList);
			List<Object[]> malePat24To59StartedEndList = (List<Object[]>) service.union(malePat24To59StartedBegQterList, malePat24To59StartedDuringQterList);

			map.put("malePat24To59StartedBegQterList",malePat24To59StartedBegQterList);
			map.put("malePat24To59StartedDuringQterList",malePat24To59StartedDuringQterList);
			map.put("malePat24To59StartedEndList",malePat24To59StartedEndList);

			int malePat24To59StartedBegQter = malePat24To59StartedBegQterList.size();
			int malePat24To59StartedDuringQter = malePat24To59StartedDuringQterList.size();
			int malePat24To59StartedEnd = malePat24To59StartedEndList.size();

			map.put("malePat24To59StartedBegQter",malePat24To59StartedBegQter);
			map.put("malePat24To59StartedDuringQter",malePat24To59StartedDuringQter);
			map.put("malePat24To59StartedEnd", malePat24To59StartedEnd);

			// male 60-179
			List<Object[]> malePat60To179StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, maleGender,sixtyMonAge, one79MonAge);
			List<Object[]> malePat60To179StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,maleGender, sixtyMonAge, one79MonAge);
			List<Object[]> malePat60To179StartedDuringQterList = (List<Object[]>) service.union(malePat60To179StartedDuringQterList1, male60To179TransfInDuringQterList);
			List<Object[]> malePat60To179StartedEndQterList = (List<Object[]>) service.union(malePat60To179StartedBegQterList, malePat60To179StartedDuringQterList);

			map.put("malePat60To179StartedBegQterList",malePat60To179StartedBegQterList);
			map.put("malePat60To179StartedDuringQterList",malePat60To179StartedDuringQterList);
			map.put("malePat60To179StartedEndQterList",malePat60To179StartedEndQterList);

			int malePat60To179StartedBegQter = malePat60To179StartedBegQterList.size();
			int malePat60To179StartedDuringQter = malePat60To179StartedDuringQterList.size();
			int malePat60To179StartedEndQter = malePat60To179StartedEndQterList.size();

			map.put("malePat60To179StartedBegQter",malePat60To179StartedBegQter);
			map.put("malePat60To179StartedDuringQter",malePat60To179StartedDuringQter);
			map.put("malePat60To179StartedEndQter",malePat60To179StartedEndQter);

			// female 0-11
			List<Object[]> femPat0To11StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, femaleGender,zeroMonthAge, elevMonAge);
			List<Object[]> femPat0To11StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge);
			List<Object[]> femPat0To11StartedDuringQterList = (List<Object[]>) service.union(femPat0To11StartedDuringQterList1, female0To11TransfInDuringQterList);
            List<Object[]> femPat0To11StartedEndQterList = (List<Object[]>) service.union(femPat0To11StartedBegQterList, femPat0To11StartedDuringQterList);

			map.put("femPat0To11StartedBegQterList",femPat0To11StartedBegQterList);
			map.put("femPat0To11StartedDuringQterList",femPat0To11StartedDuringQterList);
			map.put("femPat0To11StartedEndQterList",femPat0To11StartedEndQterList);

			int femPat0To11StartedBegQter = femPat0To11StartedBegQterList.size();
			int femPat0To11StartedDuringQter = femPat0To11StartedDuringQterList.size();
			int femPat0T11StartedEndQter = femPat0To11StartedEndQterList.size();

			map.put("femPat0To11StartedBegQter",femPat0To11StartedBegQter);
			map.put("femPat0To11StartedDuringQter",femPat0To11StartedDuringQter);
			map.put("femPat0T11StartedEndQter",femPat0T11StartedEndQter);
			
			// female 12-23
			List<Object[]> femPat12To23StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, femaleGender,twlvMonAge, twenty3MonAge);
			List<Object[]> femPat12To23StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge);
			List<Object[]> femPat12To23StartedDuringQterList = (List<Object[]>) service.union(femPat12To23StartedDuringQterList1, female12To23TransfInDuringQterList);
            List<Object[]> femPat12To23StartedEndQterList = (List<Object[]>) service.union(femPat12To23StartedBegQterList, femPat12To23StartedDuringQterList);

			map.put("femPat12To23StartedBegQterList",femPat12To23StartedBegQterList);
			map.put("femPat12To23StartedDuringQterList",femPat12To23StartedDuringQterList);
			map.put("femPat12To23StartedEndQterList",femPat12To23StartedEndQterList);

			int femPat12To23StartedBegQter = femPat12To23StartedBegQterList.size();
			int femPat12To23StartedDuringQter = femPat12To23StartedDuringQterList.size();
			int femPat12To23StartedEndQter = femPat12To23StartedEndQterList.size();

			map.put("femPat12To23StartedBegQter",femPat12To23StartedBegQter);
			map.put("femPat0To11StartedDuringQter",femPat12To23StartedDuringQter);
			map.put("femPat12To23StartedEndQter",femPat12To23StartedEndQter);

			// female 24-59
			List<Object[]> femPat24To59StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, femaleGender,twentyFoMonAge, fifty9MonAge);
			List<Object[]> femPat24To59StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge);
			List<Object[]> femPat24To59StartedDuringQterList = (List<Object[]>) service.union(femPat24To59StartedDuringQterList1, female24To59TransfInDuringQterList);
			List<Object[]> femPat24To59StartedEndList = (List<Object[]>) service.union(femPat24To59StartedBegQterList, femPat24To59StartedDuringQterList);

			map.put("femPat24To59StartedBegQterList",femPat24To59StartedBegQterList);
			map.put("femPat24To59StartedDuringQterList",femPat24To59StartedDuringQterList);
			map.put("femPat24To59StartedEndList",femPat24To59StartedEndList);

			int femPat24To59StartedBegQter = femPat24To59StartedBegQterList.size();
			int femPat24To59StartedDuringQter = femPat24To59StartedDuringQterList.size();
			int femPat24To59StartedEnd = femPat24To59StartedEndList.size();

			map.put("femPat24To59StartedBegQter",femPat24To59StartedBegQter);
			map.put("femPat24To59StartedDuringQter",femPat24To59StartedDuringQter);
			map.put("femPat24To59StartedEnd",femPat24To59StartedEnd);

			// female 60-179
			List<Object[]> femPat60To179StartedBegQterList = service.getPatientsStartedART(quarterFromDate, null, femaleGender,sixtyMonAge, one79MonAge);
			List<Object[]> femPat60To179StartedDuringQterList1 = service.getPatientsStartedART(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge);
			List<Object[]> femPat60To179StartedDuringQterList = (List<Object[]>) service.union(femPat60To179StartedDuringQterList1, female60To179TransfInDuringQterList);
			List<Object[]> femPat60To179StartedEndQterList = (List<Object[]>) service.union(femPat60To179StartedBegQterList, femPat60To179StartedDuringQterList);

			map.put("femPat60To179StartedBegQterList",femPat60To179StartedBegQterList);
			map.put("femPat60To179StartedDuringQterList",femPat60To179StartedDuringQterList);
			map.put("femPat60To179StartedEndQterList",femPat60To179StartedEndQterList);

			int femPat60To179StartedBegQter = femPat60To179StartedBegQterList.size();
			int femPat60To179StartedDuringQter = femPat60To179StartedDuringQterList.size();
			int femPat60To179StartedEndQter = femPat60To179StartedEndQterList.size();

			map.put("femPat60To179StartedBegQter", femPat60To179StartedBegQter);
			map.put("femPat60To179StartedDuringQter", femPat60To179StartedDuringQter);
			map.put("femPat60To179StartedEndQter", femPat60To179StartedEndQter);

			// ________________________patients stopped ART_____________________________________________

			// male and female aged 0-179 and >180          
			List<Object[]> male0To179StoppedARTList = service.getPatientStoppeART(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, one79MonAge,cumMalePat0To179StartedEndQterList);
			List<Object[]> female0To179StoppedARTList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, one79MonAge,cumfemPat0To179StartedARTEndQterList);

			int male0To179StoppedART = male0To179StoppedARTList.size();
			int female0To179StoppedART = female0To179StoppedARTList.size();
			map.put("male0To179StoppedART", male0To179StoppedART);
			map.put("female0To179StoppedART", female0To179StoppedART);

			map.put("male0To179StoppedARTList", male0To179StoppedARTList);
			map.put("female0To179StoppedARTList", female0To179StoppedARTList);

			// // male and female aged >180
			List<Object[]> maleOver180StoppedList = service.getPatientStoppeART(quarterFromDate, quarterToDate, maleGender, one79MonAge,null, cumMalePatOver180StartedARTEndQterList);
			List<Object[]> femaleOver180StoppedList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, one79MonAge, null,cumFemPatOver180StartedARTEndQterList);

			int maleOver180Stopped = maleOver180StoppedList.size();
			int femaleOver180Stopped = femaleOver180StoppedList.size();
			map.put("maleOver180Stopped", maleOver180Stopped);
			map.put("femaleOver180Stopped", femaleOver180Stopped);

			map.put("maleOver180StoppedList", maleOver180StoppedList);
			map.put("femaleOver180StoppedList", femaleOver180StoppedList);

			// total male and female aged 0 -179 and >180 stopped ART
			List<Object[]> male0To179AndOver15StoppedARTList = (List<Object[]>) service.union(male0To179StoppedARTList, maleOver180StoppedList);
			List<Object[]> female0To179AndOver15StoppedARTList = (List<Object[]>) service.union(female0To179StoppedARTList, femaleOver180StoppedList);
			List totalStoppedARTList = (List) service.union(male0To179AndOver15StoppedARTList,female0To179AndOver15StoppedARTList);

			int totalStoppedART = totalStoppedARTList.size();
			map.put("totalStoppedART", totalStoppedART);
			map.put("totalStoppedARTList", totalStoppedARTList);
			
			//____________pediatric stopped ARV________________________________

			// male 0-11
			List<Object[]> male0To11StoppedARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,maleGender, zeroMonthAge, elevMonAge,malePat12To23StartedEndQterList);
			int male0To11StoppedARV = male0To11StoppedARVList.size();
			map.put("male0To11StoppedARV", male0To11StoppedARV);
			map.put("male0To11StoppedARVList", male0To11StoppedARVList);

			// male 12-23
			List<Object[]> male12To23StoppedARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,maleGender, twlvMonAge, twenty3MonAge,malePat12To23StartedEndQterList);
			int male12To23StoppedARV = male12To23StoppedARVList.size();
			map.put("male12To23StoppedARV", male12To23StoppedARV);
			map.put("male12To23StoppedARVList", male12To23StoppedARVList);
			
			// male 24-59
			List<Object[]> male24To59StoppedARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,maleGender, twentyFoMonAge, fifty9MonAge,malePat24To59StartedEndList);
			int male24To59StoppedARV = male24To59StoppedARVList.size();
			map.put("male24To59StoppedARV", male24To59StoppedARV);
			map.put("male24To59StoppedARVList", male24To59StoppedARVList);

			// male 60-179
			List<Object[]> male60To179StoppedARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,maleGender, sixtyMonAge, one79MonAge,malePat60To179StartedEndQterList);
			int male60To179StoppedARV = male60To179StoppedARVList.size();
			map.put("male60To179StoppedARV", male60To179StoppedARV);
			map.put("male60To179StoppedARVList", male60To179StoppedARVList);

			// female 0-11
			List<Object[]> female0To11StopARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, zeroMonthAge, elevMonAge,femPat0To11StartedEndQterList);
			int female0To11StopARV = female0To11StopARVList.size();
			map.put("female0To11StopARV", female0To11StopARV);
			map.put("female0To11StopARVList", female0To11StopARVList);
			
			// female 12-23
			List<Object[]> fem12To23StopARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, twlvMonAge, twenty3MonAge,femPat12To23StartedEndQterList);
			int female12To23StoppedARV = fem12To23StopARVList.size();
			map.put("female12To23StoppedARV", female12To23StoppedARV);
			map.put("fem12To23StopARVList", fem12To23StopARVList);
			
			// female 24-59
			List<Object[]> fem24To59StopARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, twentyFoMonAge, fifty9MonAge,femPat24To59StartedEndList);
			int fem24To59StopARV = fem24To59StopARVList.size();
			map.put("fem24To59StopARV", fem24To59StopARV);
			map.put("fem24To59StopARVList", fem24To59StopARVList);

			// female 60-179
			List<Object[]> fem60To179StopARVList = service.getPatientStoppeART(quarterFromDate, quarterToDate,femaleGender, sixtyMonAge, one79MonAge,femPat60To179StartedEndQterList);
			int fem60To179StopARV = fem60To179StopARVList.size();
			map.put("fem60To179StopARV", fem60To179StopARV);
			map.put("fem60To179StopARVList", fem60To179StopARVList);

			// ______________________patients transferred out______________________________
			Integer transfOutConceptId = 1744;

//			List<Object[]> male0to179TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,zeroMonthAge, one79MonAge,cumMalePat0To179StartedEndQterList);
//			List<Object[]> fem0to179TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,zeroMonthAge, one79MonAge,cumfemPat0To179StartedARTEndQterList);
//			List<Object[]> maleOver180TransOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,one80MonAge, null,cumMalePatOver180StartedARTEndQterList);
//			List<Object[]> femOver180TransOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,one80MonAge, null,cumFemPatOver180StartedARTEndQterList);

			List<Object[]> male0to179TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender, zeroMonthAge, one79MonAge);
			List<Object[]> fem0to179TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender, zeroMonthAge, one79MonAge);
			List<Object[]> maleOver180TransOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender, one80MonAge, null);
			List<Object[]> femOver180TransOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender, one80MonAge, null);
			
			map.put("male0to179TransfOutList", male0to179TransfOutList);
			map.put("fem0to179TransfOutList", fem0to179TransfOutList);
			map.put("maleOver180TransOutList", maleOver180TransOutList);
			map.put("femOver180TransOutList", femOver180TransOutList);

			int male0to179TransfOut = male0to179TransfOutList.size();
			int fema0to179TransfOut = fem0to179TransfOutList.size();
			int maleOver180TransOut = maleOver180TransOutList.size();
			int femOver180TransOut = femOver180TransOutList.size();

			List<Object[]> list1 = (List<Object[]>) service.union(male0to179TransfOutList, fem0to179TransfOutList);
			List<Object[]> list2 = (List<Object[]>) service.union(maleOver180TransOutList, femOver180TransOutList);
			List<Object[]> totalTransfOutList = (List<Object[]>) service.union(list1, list2);

			int totalTransfOut = totalTransfOutList.size();

			map.put("male0to179TransfOut", male0to179TransfOut);
			map.put("fema0to179TransfOut", fema0to179TransfOut);
			map.put("maleOver180TransOut", maleOver180TransOut);
			map.put("femOver180TransOut", femOver180TransOut);
			map.put("totalTransfOut", totalTransfOut);
			map.put("totalTransfOutList", totalTransfOutList);

			// male 0-11
//			List<Object[]> male0to11TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,zeroMonthAge, elevMonAge,malePat12To23StartedEndQterList);
			List<Object[]> male0to11TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender,zeroMonthAge, elevMonAge);
			int male0to11TransfOut = male0to11TransfOutList.size();
			map.put("male0to11TransfOut", male0to11TransfOut);
			map.put("male0to11TransfOutList", male0to11TransfOutList);
			
			// male 12-23
//			List<Object[]> male12to23TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,twlvMonAge, twenty3MonAge,malePat12To23StartedEndQterList);
			
			List<Object[]> male12to23TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender,twlvMonAge, twenty3MonAge);
			int male12to23TransfOut = male12to23TransfOutList.size();
			map.put("male12to23TransfOut", male12to23TransfOut);
			map.put("male12to23TransfOutList", male12to23TransfOutList);

			// male 24-59
//			List<Object[]> male24to59TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,twentyFoMonAge, fifty9MonAge, malePat24To59StartedEndList);
			List<Object[]> male24to59TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender,twentyFoMonAge, fifty9MonAge);
			int male24to59TransfOut = male24to59TransfOutList.size();
			map.put("male24to59TransfOut", male24to59TransfOut);
			map.put("male24to59TransfOutList", male24to59TransfOutList);

			// male 60-179
//			List<Object[]> male60to179TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, maleGender,sixtyMonAge, one79MonAge,malePat60To179StartedEndQterList);
			List<Object[]> male60to179TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, maleGender,sixtyMonAge, one79MonAge);
			int male60to179TransfOut = male60to179TransfOutList.size();
			map.put("male60to179TransfOut", male60to179TransfOut);
			map.put("male60to179TransfOutList", male60to179TransfOutList);

			// female 0-11
//			List<Object[]> fem0to11TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,zeroMonthAge, one79MonAge,femPat0To11StartedEndQterList);
			List<Object[]> fem0to11TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender,zeroMonthAge, one79MonAge);
			int fem0to11TransfOut = fem0to11TransfOutList.size();
			map.put("fem0to11TransfOut", fem0to11TransfOut);
			map.put("fem0to11TransfOutList", fem0to11TransfOutList);
			
			// female 12-23
//			List<Object[]> fem12to23TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,twlvMonAge, twenty3MonAge,femPat12To23StartedEndQterList);
			List<Object[]> fem12to23TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender,twlvMonAge, twenty3MonAge);
			int fem12to23TransfOut = fem12to23TransfOutList.size();
			map.put("fem12to23TransfOut", fem12to23TransfOut);
			map.put("fem12to23TransfOutList", fem12to23TransfOutList);

			// female 24-59
//			List<Object[]> fem24to59TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,twentyFoMonAge, fifty9MonAge, femPat24To59StartedEndList);
			List<Object[]> fem24to59TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender,twentyFoMonAge, fifty9MonAge);
			int fem24to59TransfOut = fem24to59TransfOutList.size();
			map.put("fem24to59TransfOut", fem24to59TransfOut);
			map.put("fem24to59TransfOutList", fem24to59TransfOutList);

			// female 60-179
//			List<Object[]> fem60to179TransfOutList = service.getPatientExitedFromCare(transfOutConceptId,quarterFromDate, quarterToDate, femaleGender,sixtyMonAge, one79MonAge, femPat60To179StartedEndQterList);
			List<Object[]> fem60to179TransfOutList = service.getPatientsTransferredOut(quarterFromDate, quarterToDate, femaleGender,sixtyMonAge, one79MonAge);
			int fem60to179TransfOut = fem60to179TransfOutList.size();
			map.put("fem60to179TransfOut", fem60to179TransfOut);
			map.put("fem60to179TransfOutList", fem60to179TransfOutList);

			// _______________________patients died________________________________________
			Integer deathConceptId = 1742;
//			List<Object[]> male0to179DiedList = service.getPatientExitedFromCare(deathConceptId, quarterFromDate,quarterToDate, maleGender, zeroMonthAge, one79MonAge,cumMalePat0To179StartedEndQterList);
//			List<Object[]> fem0to179DiedList = service.getPatientExitedFromCare(deathConceptId, quarterFromDate,quarterToDate, femaleGender, zeroMonthAge, one79MonAge,cumfemPat0To179StartedARTEndQterList);
//			List<Object[]> maleOver180DiedList = service.getPatientExitedFromCare(deathConceptId, quarterFromDate,quarterToDate, maleGender, one80MonAge, null,cumMalePatOver180StartedARTEndQterList);
//			List<Object[]> femOver180DiedList = service.getPatientExitedFromCare(deathConceptId, quarterFromDate,quarterToDate, femaleGender, one80MonAge, null,cumFemPatOver180StartedARTEndQterList);

			List<Object[]> male0to179DiedList = service.getPatientsDead(quarterFromDate, quarterToDate, maleGender, zeroMonthAge, one79MonAge);
			List<Object[]> fem0to179DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, zeroMonthAge, one79MonAge);
			List<Object[]> maleOver180DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, maleGender, one80MonAge, null);
			List<Object[]> femOver180DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, one80MonAge, null);
			
			map.put("male0to179DiedList", male0to179DiedList);
			map.put("fem0to179DiedList", fem0to179DiedList);
			map.put("maleOver180DiedList", maleOver180DiedList);
			map.put("femOver180DiedList", femOver180DiedList);

			int male0to179Died = male0to179DiedList.size();
			int fem0to179Died = fem0to179DiedList.size();
			int maleOver180Died = maleOver180DiedList.size();
			int femOver180Died = femOver180DiedList.size();

			List<Object[]> array1 = (List<Object[]>) service.union(male0to179DiedList, fem0to179DiedList);
			List<Object[]> array2 = (List<Object[]>) service.union(maleOver180DiedList, femOver180DiedList);
			List<Object[]> totalDiedList = (List<Object[]>) service.union(array1, array2);

			int totalDied = totalDiedList.size();

			map.put("male0to179Died", male0to179Died);
			map.put("fem0to179Died", fem0to179Died);
			map.put("maleOver180Died", maleOver180Died);
			map.put("femOver180Died", femOver180Died);
			map.put("totalDied", totalDied);
			map.put("totalDiedList", totalDiedList);

			// male 0-11
			List<Object[]> male0To11DiedList = service.getPatientsDead(quarterFromDate, quarterToDate, maleGender,zeroMonthAge, elevMonAge); 
			int male0To11Died = male0To11DiedList.size();
			map.put("male0To11Died", male0To11Died);
			map.put("male0To11DiedList", male0To11DiedList);
			
			// male 12-23
			List<Object[]> male12To23DiedList = service.getPatientsDead(quarterFromDate, quarterToDate, maleGender,twlvMonAge, twenty3MonAge);
			int male12To23Died = male12To23DiedList.size();
			map.put("male12To23Died", male12To23Died);
			map.put("male12To23DiedList", male12To23DiedList);
			
			// male 24-59
			List<Object[]> male24To59DiedList = service.getPatientsDead(quarterFromDate, quarterToDate, maleGender,twentyFoMonAge, fifty9MonAge);
			int male24To59Died = male24To59DiedList.size();
			map.put("male24To59Died", male24To59Died);
			map.put("male24To59DiedList", male24To59DiedList);

			// male 60-179
			List<Object[]> male60To179DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, maleGender, sixtyMonAge, one79MonAge);
			int male60To179Died = male60To179DiedList.size();
			map.put("male60To179Died", male60To179Died);
			map.put("male60To179DiedList", male60To179DiedList);

			// female 0-11
			List<Object[]> fem0To11DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, zeroMonthAge, elevMonAge);
			int fem0To11Died = fem0To11DiedList.size();
			map.put("fem0To11Died", fem0To11Died);
			map.put("fem0To11DiedList", fem0To11DiedList);
			
			// female 12-23
			List<Object[]> fem12To23DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, twlvMonAge, twenty3MonAge);
			int fem12To23Died = fem12To23DiedList.size();
			map.put("fem12To23Died", fem12To23Died);
			map.put("fem12To23DiedList", fem12To23DiedList);

			// female 24-59
			List<Object[]> fem24To59DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, twentyFoMonAge, fifty9MonAge);
			int fem24To59Died = fem24To59DiedList.size();
			map.put("fem24To59Died", fem24To59Died);
			map.put("fem24To59DiedList", fem24To59DiedList);

			// female 60-179
			List<Object[]> fem60To179DiedList = service.getPatientsDead(quarterFromDate,quarterToDate, femaleGender, sixtyMonAge, one79MonAge);
			int fem60To179Died = fem60To179DiedList.size();
			map.put("fem60To179Died", fem60To179Died);
			map.put("fem60To179DiedList", fem60To179DiedList);

			// _________________________patients lost to follow up(exited, reason=defaulted)___________________________________
			
			Integer defaultedConceptId = 1743;
//			List<Object[]> male0To179LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, cumMalePat0To179StartedEndQterList);
//			List<Object[]> fem0to179LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate,cumfemPat0To179StartedARTEndQterList);
//			List<Object[]> maleOver180LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate,cumMalePatOver180StartedARTEndQterList);
//			List<Object[]> femOver180LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate,cumFemPatOver180StartedARTEndQterList);
			
			List<Object[]> male0To179LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, maleGender, zeroMonthAge, one79MonAge,cumMalePat0To179StartedEndQterList);
			List<Object[]> fem0to179LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, zeroMonthAge, one79MonAge,femPat60To179StartedEndQterList);
			List<Object[]> maleOver180LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, maleGender, one80MonAge, null,cumMalePatOver180StartedARTEndQterList);
			List<Object[]> femOver180LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, one80MonAge, null,cumFemPatOver180StartedARTEndQterList);


			map.put("male0To179LostList", male0To179LostList);
			map.put("fem0to179LostList", fem0to179LostList);
			map.put("maleOver180LostList", maleOver180LostList);
			map.put("femOver180LostList", femOver180LostList);

			int male0To179Lost = male0To179LostList.size();
			int fem0to179Lost = fem0to179LostList.size();
			int maleOver180Lost = maleOver180LostList.size();
			int femOver180Lost = femOver180LostList.size();

			List<Object[]> ar1 = (List<Object[]>) service.union(male0To179LostList, fem0to179LostList);
			List<Object[]> ar2 = (List<Object[]>) service.union(maleOver180LostList, femOver180LostList);
			List<Object[]> totalLostList = (List<Object[]>) service.union(ar1,ar2);

			int totalLost = totalLostList.size();

			map.put("male0To179Lost", male0To179Lost);
			map.put("fem0to179Lost", fem0to179Lost);
			map.put("maleOver180Lost", maleOver180Lost);
			map.put("femOver180Lost", femOver180Lost);
			map.put("totalLost", totalLost);
			map.put("totalLostList", totalLostList);

			// male 0-11
//			List<Object[]> male0To11LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, malePat0To11StartedEndQterList);
			List<Object[]> male0To11LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate, quarterToDate, maleGender,zeroMonthAge, elevMonAge, malePat0To11StartedEndQterList); 
			int male0To11Lost = male0To11LostList.size();
			map.put("male0To11Lost", male0To11Lost);
			map.put("male0To11LostList", male0To11LostList);
			
			// male 12-23
//			List<Object[]> male12To23LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, malePat12To23StartedEndQterList);
			List<Object[]> male12To23LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate, quarterToDate, maleGender,twlvMonAge, twenty3MonAge, malePat12To23StartedEndQterList);
			int male12To23Lost = male12To23LostList.size();
			map.put("male12To23Lost", male12To23Lost);
			map.put("male12To23LostList", male12To23LostList);

			// male 24-59
//			List<Object[]> male24To59LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, malePat24To59StartedEndList);
			List<Object[]> male24To59LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate, quarterToDate, maleGender,twentyFoMonAge, fifty9MonAge,malePat24To59StartedEndList);
			int male24To59Lost = male24To59LostList.size();
			map.put("male24To59Lost", male24To59Lost);
			map.put("male24To59LostList", male24To59LostList);

			// male 60-179
//			List<Object[]> male60To179LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate,malePat60To179StartedEndQterList);
			List<Object[]> male60To179LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate, quarterToDate, maleGender,twentyFoMonAge, fifty9MonAge,malePat24To59StartedEndList);
			int male60To179Lost = male60To179LostList.size();
			map.put("male60To179Lost", male60To179Lost);
			map.put("male60To179LostList", male60To179LostList);

			// female 0-11
//			List<Object[]> fem0To11LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, femPat0To11StartedEndQterList);
			List<Object[]> fem0To11LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, zeroMonthAge, elevMonAge,femPat0To11StartedEndQterList);
			int fem0To11Lost = fem0To11LostList.size();
			map.put("fem0To11Lost", fem0To11Lost);
			map.put("fem0To11LostList", fem0To11LostList);
			
			// female 12-23
//			List<Object[]> fem12To23LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, femPat12To23StartedEndQterList);
			List<Object[]> fem12To23LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, twlvMonAge, twenty3MonAge,femPat12To23StartedEndQterList);
			int fem12To23Lost = fem12To23LostList.size();
			map.put("fem12To23Lost", fem12To23Lost);
			map.put("fem12To23LostList", fem12To23LostList);

			// female 24-59
//			List<Object[]> fem24To59LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, femPat24To59StartedEndList);
			List<Object[]> fem24To59LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, twentyFoMonAge, fifty9MonAge,femPat24To59StartedEndList);
			int fem24To59Lost = fem24To59LostList.size();
			map.put("fem24To59Lost", fem24To59Lost);
			map.put("fem24To59LostList", fem24To59LostList);

			// female 60-179
//			List<Object[]> fem60To179LostList = service.getAllARVPatientsLostOnFollowUp(quarterFromDate,quarterToDate, femPat60To179StartedEndQterList);
			List<Object[]> fem60To179LostList = service.getPatientExitedFromCare(defaultedConceptId, quarterFromDate,quarterToDate, femaleGender, twentyFoMonAge, fifty9MonAge,femPat24To59StartedEndList);
			int fem60To179Lost = fem60To179LostList.size();
			map.put("fem60To179Lost", fem60To179Lost);
			map.put("fem60To179LostList", fem60To179LostList);

			// ________TOTAL_________Pediatric ART Care Follow UP________________________________________

			//male 0-11
			List<Object[]> col1_0to11 = (List<Object[]>) service.union(male0To11StoppedARVList, male0to11TransfOutList);
			List<Object[]> col2_0to11 = (List<Object[]>) service.union(male0To11DiedList, male0To11LostList);
			List<Object[]> male0To11NotOnARVTotalList = (List<Object[]>) service.union(col1_0to11, col2_0to11);

			int male0To11NotOnARVTotal = male0To11NotOnARVTotalList.size();
			map.put("male0To11NotOnARVTotal", male0To11NotOnARVTotal);
			map.put("male0To11NotOnARVTotalList", male0To11NotOnARVTotalList);
			
			//male 12-23
			List<Object[]> col1_12to23 = (List<Object[]>) service.union(male12To23StoppedARVList, male12to23TransfOutList);
			List<Object[]> col2_12to23 = (List<Object[]>) service.union(male12To23DiedList, male12To23LostList);
			List<Object[]> male12to23NotOnARVTotalList = (List<Object[]>) service.union(col1_12to23, col2_12to23);

			int male12To23NotOnARVTotal = male12to23NotOnARVTotalList.size();
			map.put("male12To23NotOnARVTotal", male12To23NotOnARVTotal);
			map.put("male12to23NotOnARVTotalList", male12to23NotOnARVTotalList);

			//male 24-59
			List<Object[]> col1_24_59 = (List<Object[]>) service.union(male24To59StoppedARVList, male24to59TransfOutList);
			List<Object[]> col2_24_59 = (List<Object[]>) service.union(male24To59DiedList, male24To59LostList);
			List<Object[]> male24To59NotOnARVTTotalList = (List<Object[]>) service.union(col1_24_59, col2_24_59);

			int male24To59NotOnARVTTotal = male24To59NotOnARVTTotalList.size();
			map.put("male24To59NotOnARVTTotal", male24To59NotOnARVTTotal);
			map.put("male24To59NotOnARVTTotalList", male24To59NotOnARVTTotalList);

			//male 60-179
			List<Object[]> col1_Male60To179 = (List<Object[]>) service.union(male60To179StoppedARVList, male60to179TransfOutList);
			List<Object[]> col2_Male60To179 = (List<Object[]>) service.union(male60To179DiedList, male60To179LostList);
			List<Object[]> male60To179NotOnARVTTotalList = (List<Object[]>) service.union(col1_Male60To179, col2_Male60To179);

			int male60To179NotOnARVTTotal = male60To179StoppedARV+ male60to179TransfOut + male60To179Died + male60To179Lost;
			map.put("male60To179NotOnARVTTotal", male60To179NotOnARVTTotal);
			map.put("male60To179NotOnARVTTotalList", male60To179NotOnARVTTotalList);

			//female 0-11
			List<Object[]> col1_fem0To11 = (List<Object[]>) service.union(female0To11StopARVList, fem12to23TransfOutList);
			List<Object[]> col2_fem0To11 = (List<Object[]>) service.union(fem0To11DiedList, fem0To11LostList);
			List<Object[]> female0To11NotOnARVTotalList = (List<Object[]>) service.union(col1_fem0To11, col2_fem0To11);

			int female0To11NotOnARVTotal = female0To11NotOnARVTotalList.size();
			map.put("female0To11NotOnARVTotal", female0To11NotOnARVTotal);
			map.put("female0To11NotOnARVTotalList", female0To11NotOnARVTotalList);

			//female 12-23
			List<Object[]> col1_fem12To23 = (List<Object[]>) service.union(fem12To23StopARVList, fem12to23TransfOutList);
			List<Object[]> col2_fem12To23 = (List<Object[]>) service.union(fem12To23DiedList, fem12To23LostList);
			List<Object[]> female12To23NotOnARVTotalList = (List<Object[]>) service.union(col1_fem12To23, col2_fem12To23);

			int female12To23NotOnARVTotal = female12To23NotOnARVTotalList.size();
			map.put("female12To23NotOnARVTotal", female12To23NotOnARVTotal);
			map.put("female12To23NotOnARVTotalList", female12To23NotOnARVTotalList);
			
			//female 24-59
			List<Object[]> col1_fem24To59 = (List<Object[]>) service.union(fem24To59StopARVList, fem24to59TransfOutList);
			List<Object[]> col2_fem24To59 = (List<Object[]>) service.union(fem24To59DiedList, fem24To59LostList);
			List<Object[]> fem24To59NotOnARVTotalList = (List<Object[]>) service.union(col1_fem24To59, col2_fem24To59);

			int female24To59NotOnARVTotal = fem24To59NotOnARVTotalList.size();
			map.put("female24To59NotOnARVTotal", female24To59NotOnARVTotal);
			map.put("fem24To59NotOnARVTotalList", fem24To59NotOnARVTotalList);

			//female 60-179
			List<Object[]> col1_fem60To179 = (List<Object[]>) service.union(fem60To179StopARVList, fem60to179TransfOutList);
			List<Object[]> col2_fem60To179 = (List<Object[]>) service.union(fem60To179DiedList, fem60To179LostList);
			List<Object[]> female60To179NotOnARVTotalList = (List<Object[]>) service.union(col1_fem60To179, col2_fem60To179);

			int female60To179NotOnARVTotal = female60To179NotOnARVTotalList.size();
			map.put("female60To179NotOnARVTotal", female60To179NotOnARVTotal);
			map.put("female60To179NotOnARVTotalList",female60To179NotOnARVTotalList);

			

			// ___________________________________________________________________________________
			// Nbre de nouveau patients ayant demarre le ARV pendant ce trimestre(2.0 colonne5)
			// only New non TRANSFER-IN
			// ____________________________________________________________________________________
			
			// =============================adult=======================================================
			// male 0-179
			List<Object[]> male0To179NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(cumMalePat0To179StartedDuringQterList,male0To179TransfInDuringQterList);
			int male0To179NewOnArtDuringQterNoTransferIn = male0To179NewOnARTDuringQterNoTransfList.size();
			map.put("male0To179NewOnArtDuringQterNoTransferIn",	male0To179NewOnArtDuringQterNoTransferIn);
			map.put("male0To179NewOnARTDuringQterNoTransfList",male0To179NewOnARTDuringQterNoTransfList);

			// male >180
			List<Object[]> maleOver179NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(cumMalePatOver180StartedARTDuringQterList,maleOver180TransfInDuringQterList);
			int maleOver180NewOnArtDuringQterNoTransferIn = maleOver179NewOnARTDuringQterNoTransfList.size();
			map.put("maleOver180NewOnArtDuringQterNoTransferIn",maleOver180NewOnArtDuringQterNoTransferIn);
			map.put("maleOver179NewOnARTDuringQterNoTransfList",maleOver179NewOnARTDuringQterNoTransfList);

			// female 0-179
			List<Object[]> fem0To179NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(cumfem0To179StartedARTDuringQterList,femaleOTo179TransfInDuringQterList);
			int fem0To179OnArtDuringQterNoTransferIn = fem0To179NewOnARTDuringQterNoTransfList.size();
			map.put("fem0To179OnArtDuringQterNoTransferIn",fem0To179OnArtDuringQterNoTransferIn);
			map.put("fem0To179NewOnARTDuringQterNoTransfList",fem0To179NewOnARTDuringQterNoTransfList);

			// female >180
			List<Object[]> cumFemPatOver180NewOnARTDuringQterList = (List<Object[]>) service.SubtractACollection(cumFemPatOver180StartedARTDuringQterList,femaleOver180TransfInDuringQterList);
			int femOver180NewOnARTDuringQterNoTransfIn = cumFemPatOver180NewOnARTDuringQterList.size();
			map.put("femOver180NewOnARTDuringQterNoTransfIn",femOver180NewOnARTDuringQterNoTransfIn);
			map.put("cumFemPatOver180NewOnARTDuringQterList",cumFemPatOver180NewOnARTDuringQterList);

			// total
			List<Object[]> maleNewOnArtDuringQterNoTransferInList = (List<Object[]>) service.union(male0To179NewOnARTDuringQterNoTransfList,maleOver179NewOnARTDuringQterNoTransfList);
			List<Object[]> femNewOnArtDuringQterNoTransferInList = (List<Object[]>) service.union(fem0To179NewOnARTDuringQterNoTransfList,cumFemPatOver180NewOnARTDuringQterList);
			List<Object[]> newOnArtDuringQterNoTransferInList = (List<Object[]>) service.union(maleNewOnArtDuringQterNoTransferInList,femNewOnArtDuringQterNoTransferInList);

			int totalActiveOnART = newOnArtDuringQterNoTransferInList.size();
			map.put("totalActiveOnART", totalActiveOnART);
			map.put("newOnArtDuringQterNoTransferInList",newOnArtDuringQterNoTransferInList);

			// ======================pediatric==========================================================
			// male 0-11
			List<Object[]> male0To11NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(malePat0To11StartedDuringQterList,male0To11TransfInDuringQterList);
			int male0To11NewOnARTDuringQterNoTransferIn = male0To11NewOnARTDuringQterNoTransfList.size();
			map.put("male0To11NewOnARTDuringQterNoTransferIn",male0To11NewOnARTDuringQterNoTransferIn);
			map.put("male0To11NewOnARTDuringQterNoTransfList",male0To11NewOnARTDuringQterNoTransfList);

			// male 12-23
			List<Object[]> male12To23NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(malePat12To23StartedDuringQterList,male12To23TransfInDuringQterList);
			int male12To23NewOnARTDuringQterNoTransferIn = male12To23NewOnARTDuringQterNoTransfList.size();
			map.put("male12To23NewOnARTDuringQterNoTransferIn",male12To23NewOnARTDuringQterNoTransferIn);
			map.put("male12To23NewOnARTDuringQterNoTransfList",male12To23NewOnARTDuringQterNoTransfList);
			
			// male 24-59
			List<Object[]> male24To59NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(malePat24To59StartedDuringQterList,male24To59TransfInDuringQterList);
			int male24To59NewOnARTDuringQterNoTransferIn = male24To59NewOnARTDuringQterNoTransfList.size();
			map.put("male24To59NewOnARTDuringQterNoTransferIn",male24To59NewOnARTDuringQterNoTransferIn);
			map.put("male24To59NewOnARTDuringQterNoTransfList",male24To59NewOnARTDuringQterNoTransfList);

			// male 60-179
			List<Object[]> male60To179NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(malePat60To179StartedDuringQterList,male60To179TransfInDuringQterList);
			int male60To179NewOnARTDuringQterNoTransferIn = male60To179NewOnARTDuringQterNoTransfList.size();
			map.put("male60To179NewOnARTDuringQterNoTransferIn",male60To179NewOnARTDuringQterNoTransferIn);
			map.put("male60To179NewOnARTDuringQterNoTransfList",male60To179NewOnARTDuringQterNoTransfList);

			// female 0-11
			List<Object[]> fem0To11NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(femPat0To11StartedDuringQterList,female0To11TransfInDuringQterList);
			int fem0To11NewOnARTDuringQterNoTransferIn = fem0To11NewOnARTDuringQterNoTransfList.size();
			map.put("fem0To11NewOnARTDuringQterNoTransferIn",fem0To11NewOnARTDuringQterNoTransferIn);
			map.put("fem0To11NewOnARTDuringQterNoTransfList",fem0To11NewOnARTDuringQterNoTransfList);

			// female 12-23
			List<Object[]> fem12To23NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(femPat12To23StartedDuringQterList,female0To11TransfInDuringQterList);
			int fem12To23NewOnARTDuringQterNoTransferIn = fem12To23NewOnARTDuringQterNoTransfList.size();
			map.put("fem12To23NewOnARTDuringQterNoTransferIn",fem12To23NewOnARTDuringQterNoTransferIn);
			map.put("fem12To23NewOnARTDuringQterNoTransfList",fem12To23NewOnARTDuringQterNoTransfList);
			
			// female 24-59
			List<Object[]> fem24To59NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(femPat24To59StartedDuringQterList,female24To59TransfInDuringQterList);
			int fem24To59NewOnARTDuringQterNoTransferIn = fem24To59NewOnARTDuringQterNoTransfList.size();
			map.put("fem24To59NewOnARTDuringQterNoTransferIn",fem24To59NewOnARTDuringQterNoTransferIn);
			map.put("fem24To59NewOnARTDuringQterNoTransfList",fem24To59NewOnARTDuringQterNoTransfList);

			// female 60-179
			List<Object[]> fem60To179NewOnARTDuringQterNoTransfList = (List<Object[]>) service.SubtractACollection(femPat60To179StartedDuringQterList,female60To179TransfInDuringQterList);
			int fem60To179NewOnARTDuringQterNoTransferIn = fem60To179NewOnARTDuringQterNoTransfList.size();
			map.put("fem60To179NewOnARTDuringQterNoTransferIn",fem60To179NewOnARTDuringQterNoTransferIn);
			map.put("fem60To179NewOnARTDuringQterNoTransfList",fem60To179NewOnARTDuringQterNoTransfList);

			// ____________________________pregnant females_________________________________________
			// cumulative number buy the beginning of qter
			List<Object[]> cumFemPregnantInTheBegOfQterList = service.getPregnantFemales(quarterFromDate, null);
			List<Object[]> cumFemPregnantNewOnARTInTheBegOfQterList = new ArrayList<Object[]>();
			for (Object[] id : cumPatStartedARTEndQterList) {
				if (cumFemPregnantInTheBegOfQterList.contains(id[0]))
					cumFemPregnantNewOnARTInTheBegOfQterList.add(new Object[] {
							id[0], "" });

			}
			int cumFemPregnantNewOnARTInTheBegOfQter = cumFemPregnantNewOnARTInTheBegOfQterList.size();
			map.put("cumFemPregnantNewOnARTInTheBegOfQter",cumFemPregnantNewOnARTInTheBegOfQter);
			map.put("cumFemPregnantNewOnARTInTheBegOfQterList",cumFemPregnantNewOnARTInTheBegOfQterList);

			// pregnant female new on ART during the quarter
			List<Object[]> pregnantFemalesDuringQterList = service.getPregnantFemales(quarterFromDate, quarterToDate);
			List<Object[]> newOnARTPregnantList = new ArrayList<Object[]>();
			for (Object[] id : pregnantFemalesDuringQterList) {
				if (cumPatStartedDuringQterList.contains(id[0])) {
					newOnARTPregnantList.add(new Object[] { id[0], "" });
				}
			}
			int newOnARTPregnant = newOnARTPregnantList.size();
			map.put("newOnARTPregnant", newOnARTPregnant);
			map.put("newOnARTPregnantList", newOnARTPregnantList);

			// pregnant female transferred in during quarter
			List<Object[]> allPatTransfInDuringQterList = service.getPatientsTransferreIn(quarterFromDate, quarterToDate,null, null, null);
			List<Object[]> pregnantFemTransfInList = new ArrayList<Object[]>();
			for (Object[] d : allPatTransfInDuringQterList) {
				if (pregnantFemalesDuringQterList.contains(d[0]))
					pregnantFemTransfInList.add(new Object[] { d[0], "" });
			}
			int pregnantFemTransfIn = pregnantFemTransfInList.size();
			map.put("pregnantFemTransfIn", pregnantFemTransfIn);
			map.put("pregnantFemTransfInList", pregnantFemTransfInList);

			// number started on ART during the quarter(new and transferred in)
			List newOnARTPlusTransInPregnantFemList = (List) service.union(newOnARTPregnantList, pregnantFemTransfInList);
			int newOnARTPlusTransInPregnantFem = newOnARTPlusTransInPregnantFemList.size();
			map.put("newOnARTPlusTransInPregnantFem",newOnARTPlusTransInPregnantFem);
			map.put("newOnARTPlusTransInPregnantFemList",newOnARTPlusTransInPregnantFemList);

			// cumulative number started on ART buy the end of the quarter =
			// table 2 col2+col3
			List<Object[]> cumPregnantFemEndQterList = (List<Object[]>) service.union(cumFemPregnantNewOnARTInTheBegOfQterList,newOnARTPlusTransInPregnantFemList);
			int cumPregnantFemEndQter = cumPregnantFemEndQterList.size();
			map.put("cumPregnantFemEndQter", cumPregnantFemEndQter);
			map.put("cumPregnantFemEndQterList", cumPregnantFemEndQterList);
			
			
			//==================================================================================================
			//
			//
			//==================================================================================================
			
			// ________________________total not on ART____________________________________

						List<Object[]> male0To179StoppedTransfOutList = (List<Object[]>) service.union(male0To179StoppedARTList, male0to179TransfOutList);
						List<Object[]> male0To179DiedAndLostList = (List<Object[]>) service.union(male0to179DiedList, male0To179LostList);
						List<Object[]> male0To179NotOnARTList = (List<Object[]>) service.union(male0To179StoppedTransfOutList,male0To179DiedAndLostList);
						

						int male0To179NotOnARTTotal = male0To179NotOnARTList.size();

						List<Object[]> maleOver180StoppedAndTransOutList = (List<Object[]>) service.union(maleOver180StoppedList, maleOver180TransOutList);
						List<Object[]> maleOver180LostAndDiedList = (List<Object[]>) service.union(maleOver180DiedList, maleOver180LostList);
						List<Object[]> maleOver180NotOnARTList = (List<Object[]>) service.union(maleOver180StoppedAndTransOutList,maleOver180LostAndDiedList);

						int maleOver180NotOnARTTotal = maleOver180NotOnARTList.size();

						List<Object[]> fem0To179StoppedARTAndTransOutList = (List<Object[]>) service.union(female0To179StoppedARTList, fem0to179TransfOutList);
						List<Object[]> fem0to179DiedAndLostList = (List<Object[]>) service.union(fem0to179DiedList, fem0to179LostList);
						List<Object[]> fem0to179NotAtARTList = (List<Object[]>) service.union(fem0To179StoppedARTAndTransOutList,fem0to179DiedAndLostList);

						int female0To179NotOnARTTotal = fem0to179NotAtARTList.size();

						List<Object[]> femOver180StoppedAndDiedList = (List<Object[]>) service.union(femaleOver180StoppedList, femOver180TransOutList);
						List<Object[]> femOver180DiedAndLostList = (List<Object[]>) service.union(femOver180DiedList, femOver180LostList);
						List<Object[]> femOver180NotOnARTList = (List<Object[]>) service.union(femOver180StoppedAndDiedList,femOver180DiedAndLostList);

						int femOver180NotOnARTTotal = femOver180NotOnARTList.size();

						map.put("male0To179NotOnARTTotal", male0To179NotOnARTTotal);
						map.put("maleOver180NotOnARTTotal", maleOver180NotOnARTTotal);
						map.put("female0To179NotOnARTTotal", female0To179NotOnARTTotal);
						map.put("femOver180NotOnARTTotal", femOver180NotOnARTTotal);

						map.put("male0To179NotOnARTList", male0To179NotOnARTList);
						map.put("femOver180NotOnARTList", femOver180NotOnARTList);
						map.put("maleOver180NotOnARTList", maleOver180NotOnARTList);
						map.put("fem0to179NotAtARTList", fem0to179NotAtARTList);

						List<Object[]> totalStoppedARTList1 = (List<Object[]>) service.union(male0To179NotOnARTList, maleOver180NotOnARTList);
						List<Object[]> totalStoppedARTList2 = (List<Object[]>) service.union(fem0to179NotAtARTList, femOver180NotOnARTList);
						List<Object[]> bigTotalList = (List<Object[]>) service.union(totalStoppedARTList1, totalStoppedARTList2);

						int bigTotal = bigTotalList.size();
						map.put("bigTotal", bigTotal);
						map.put("bigTotalList", bigTotalList);

			//=============================================================================
			// Total number on ART at the end of the
			// quarter(current)====>cumulative end of quarter - not active
			// ==>Table 2 col4- Total Table 6
			//============================================================================

			// ____________________________________total active(new in the quarter and tranferred in):table 2 column 7______________________
			// ======table 2 column 4 - table 6(non active patients)
			
			int male0To179ActiveOnARTTotalEndQter = 0;
			int maleOver180ActiveOnARTTotalEndQter = 0;
			int fem0To179ActiveOnARTTotalEndQter = 0;
			int femOver180ActiveOnARTTotalEndQter = 0;
			
			
						
			// male 0-179
			List<Object[]> male0To179ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(cumMalePat0To179StartedEndQterList,bigTotalList); ///
			male0To179ActiveOnARTTotalEndQter = male0To179ActiveOnARTTotalEndQterList.size();
			map.put("male0To179ActiveOnARTTotalEndQter",male0To179ActiveOnARTTotalEndQter);
			map.put("male0To179ActiveOnARTTotalEndQterList",male0To179ActiveOnARTTotalEndQterList);

			// male > 180
			List<Object[]> maleOver180ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(cumMalePatOver180StartedARTEndQterList,bigTotalList);
			map.put("maleOver180ActiveOnARTTotalEndQterList",maleOver180ActiveOnARTTotalEndQterList);

			maleOver180ActiveOnARTTotalEndQter = maleOver180ActiveOnARTTotalEndQterList.size();
			map.put("maleOver180ActiveOnARTTotalEndQter",maleOver180ActiveOnARTTotalEndQter);

			// female 0-179
			List<Object[]> female0To179ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(cumfemPat0To179StartedARTEndQterList,bigTotalList);
			map.put("female0To179ActiveOnARTTotalEndQterList",female0To179ActiveOnARTTotalEndQterList);

			fem0To179ActiveOnARTTotalEndQter = female0To179ActiveOnARTTotalEndQterList.size();
			map.put("fem0To179ActiveOnARTTotalEndQter",fem0To179ActiveOnARTTotalEndQter);

			// female>180
			List<Object[]> femaleOver180ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(cumFemPatOver180StartedARTEndQterList,bigTotalList);
			map.put("femaleOver180ActiveOnARTTotalEndQterList",femaleOver180ActiveOnARTTotalEndQterList);

			femOver180ActiveOnARTTotalEndQter = femaleOver180ActiveOnARTTotalEndQterList.size();
			map.put("femOver180ActiveOnARTTotalEndQter",femOver180ActiveOnARTTotalEndQter);

			// =================== total adult=================================================
			List<Object[]> coll1 = (List<Object[]>) service.union(male0To179ActiveOnARTTotalEndQterList,maleOver180ActiveOnARTTotalEndQterList);
			List<Object[]> coll2 = (List<Object[]>) service.union(female0To179ActiveOnARTTotalEndQterList,femaleOver180ActiveOnARTTotalEndQterList);
			List<Object[]> coll3 = (List<Object[]>) service.union(coll1, coll2);

			List<Object[]> col4 = (List<Object[]>) service.SubtractACollection(cumPatStartedARTEndQterList, totalLostList);

			int totalAdultActiveInART = coll3.size();
			map.put("totalAdultActiveInART", totalAdultActiveInART);
			map.put("coll3", coll3);
			

			// ======================= total active pregnants females==========================================
			// pregnants female minus STOPPED,TRANSFERRED OUT,DEATH and Lost Patients
			List<Object[]> activePregnantsList1 = new ArrayList<Object[]>();
			for (Object[] id : cumPregnantFemEndQterList) {
				if (!service.getPatientsLostOnFollowup(quarterFromDate)
						.contains(id[0]))
					if (!service.getPatientsExitedInThePeriod(null,
							quarterToDate).contains(id[0]))
						activePregnantsList1.add(new Object[] { id[0] });
			}

			int activePregnants = activePregnantsList1.size();
			map.put("activePregnants", activePregnants);
			map.put("activePregnantsList", activePregnantsList1);

			// male 0-11
			List<Object[]> male0To11ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(malePat0To11StartedEndQterList,bigTotalList);
			int male0To11ActiveOnARTTotalEndQter = male0To11ActiveOnARTTotalEndQterList.size();
			map.put("male0To11ActiveOnARTTotalEndQter",male0To11ActiveOnARTTotalEndQter);
			map.put("male0To11ActiveOnARTTotalEndQterList",male0To11ActiveOnARTTotalEndQterList);

			// male 12-23
			List<Object[]> male12To23ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(malePat12To23StartedEndQterList,bigTotalList); 
			int male12To23ActiveOnARTTotalEndQter = male12To23ActiveOnARTTotalEndQterList.size();
			map.put("male12To23ActiveOnARTTotalEndQter",male12To23ActiveOnARTTotalEndQter);
			map.put("male12To23ActiveOnARTTotalEndQterList",male12To23ActiveOnARTTotalEndQterList);
			
			// male 24-59
			List<Object[]> male24To59ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(malePat24To59StartedEndList,bigTotalList);
			int male24To59ActiveOnARTTotalEndQter = male24To59ActiveOnARTTotalEndQterList.size();
			map.put("male24To59ActiveOnARTTotalEndQter",male24To59ActiveOnARTTotalEndQter);
			map.put("male2To4ActiveOnARTTotalEndQterList",male24To59ActiveOnARTTotalEndQterList);

			// male 60-179
			List<Object[]> male60To179ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(malePat60To179StartedEndQterList,bigTotalList);
			int male5To14ActiveOnARTTotalEndQter = male60To179ActiveOnARTTotalEndQterList.size();
			map.put("male5To14ActiveOnARTTotalEndQter",male5To14ActiveOnARTTotalEndQter);
			map.put("male5To14ActiveOnARTTotalEndQterList",male60To179ActiveOnARTTotalEndQterList);

			// female 0-11
			List<Object[]> female0To11ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(femPat0To11StartedEndQterList,bigTotalList);
			int female0To11ActiveOnARTTotalEndQter = female0To11ActiveOnARTTotalEndQterList.size();
			map.put("female0To11ActiveOnARTTotalEndQter",female0To11ActiveOnARTTotalEndQter);
			map.put("female0To1ActiveOnARTTotalEndQterList",female0To11ActiveOnARTTotalEndQterList);
			
			// female 12-23
			List<Object[]> female12To23ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(femPat12To23StartedEndQterList,bigTotalList);
			int female12To23ActiveOnARTTotalEndQter = female12To23ActiveOnARTTotalEndQterList.size();
			map.put("female12To23ActiveOnARTTotalEndQter",female12To23ActiveOnARTTotalEndQter);
			map.put("female12To23ActiveOnARTTotalEndQterList",female12To23ActiveOnARTTotalEndQterList);
			
			// female 24-59
			List<Object[]> fem24To59ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(femPat24To59StartedEndList,bigTotalList);
			int fem24To59ActiveOnARTTotalEndQter = fem24To59ActiveOnARTTotalEndQterList.size();
			map.put("fem24To59ActiveOnARTTotalEndQter",fem24To59ActiveOnARTTotalEndQter);
			map.put("fem24To59ActiveOnARTTotalEndQterList",fem24To59ActiveOnARTTotalEndQterList);

			// female 60-179
			List<Object[]> fem60To179ActiveOnARTTotalEndQterList = (List<Object[]>) service.SubtractACollection(femPat60To179StartedEndQterList,bigTotalList);
			int fem60To179ActiveOnARTTotalEndQter = fem60To179ActiveOnARTTotalEndQterList.size();
			map.put("fem60To179ActiveOnARTTotalEndQter",fem60To179ActiveOnARTTotalEndQter);
			map.put("fem60To179ActiveOnARTTotalEndQterList",fem60To179ActiveOnARTTotalEndQterList);
			
			
			//USD Funded
			map.put("usdFundedList",coll3);

			// ________________________ table 4
			//##################calculate 6 months ago
			Date dateWhenCohortStartedARTSix = service.getDateXMonthAgo(quarterFromDate, 6);
			Date twoMonthsBeforeDateWhenCohortStartedARTSix = service.getFirstDayOfMonth(service.getDateInterval(dateWhenCohortStartedARTSix, 2));

			String dateWhenCohortStartedARTSixStr = QuarterlyReportUtil.getDateFormat(dateWhenCohortStartedARTSix);
			String twoMonthsBeforedateWhenCohortStartedARTSixStr = QuarterlyReportUtil.getDateFormat(twoMonthsBeforeDateWhenCohortStartedARTSix);
			map.put("monthsWhenCohortStartedARTSix",twoMonthsBeforedateWhenCohortStartedARTSixStr + " - "+ dateWhenCohortStartedARTSixStr);

			//###################calculate 12 month ago
			Date dateWhenCohortStartedARTTwelve = service.getDateXMonthAgo(quarterFromDate, 12);
			Date twoMonthBeforeDateWhenCohortStartedARTTwelve = service.getFirstDayOfMonth(service.getDateInterval(dateWhenCohortStartedARTTwelve, 2));
			
			String dateWhenCohortStartedARTTwelveStr = QuarterlyReportUtil.getDateFormat(dateWhenCohortStartedARTTwelve);
			String twoMonthBeforedateWhenCohortStartedARTTwelveStr = QuarterlyReportUtil.getDateFormat(twoMonthBeforeDateWhenCohortStartedARTTwelve);
			map.put("monthsWhenCohortStartedARTTwelve",					twoMonthBeforedateWhenCohortStartedARTTwelveStr + " - "
							+ dateWhenCohortStartedARTTwelveStr);

			// ===============================6 month==================================================
			// ........ BASELINE
			// 1.Number of person in cohort of 6month ==>
			//#########################################################################################################################
			//###who started ART in cohort(beginning of quarter to its end i.e from dateWhenCohortStartedARTSix to quarterFromDate)   #
			//#########################################################################################################################
			
			List<Object[]> patientsInCohortBaselineSixList = service.getNewPatientsOver6YearsOnART(twoMonthsBeforeDateWhenCohortStartedARTSix,dateWhenCohortStartedARTSix, null, null, null);
//			List<Object[]> patientsInCohortBaselineSixList = service.getNewPatientsOver6YearsOnART(twoMonthsBeforeDateWhenCohortStartedARTSix,quarterFromDate, null, null, null);
			int patientsInCohortBaselineSix = patientsInCohortBaselineSixList.size();
			map.put("patientsInCohortBaselineSix", patientsInCohortBaselineSix);
			map.put("patientsInCohortBaselineSixList",patientsInCohortBaselineSixList);

			// 2.patients in cohort of 6 month who have CD4 Count
			//#########################################################################################################################
			//###                 who started ART in cohort but not active by the end of the querter                                  #
			//#########################################################################################################################
			List<Object[]> patientIdAndCD4AtM0 = service.getPatientsWithCD4AtMo(patientsInCohortBaselineSixList);
			List<Object[]> patients6MonthsAgoWithCD4BaselineValueList = new ArrayList<Object[]>();
			List<Double> cd4AtM0List = new ArrayList<Double>();

			
			if (patientIdAndCD4AtM0 != null)
				for (Object[] ob : patientIdAndCD4AtM0) {
					Integer patientId = (Integer) ob[0];
					Double cd4Value = (Double) ob[1];
					Date obsDate = (Date) ob[2];
					if (patientId != null && cd4Value != null) {
						Patient patient = Context.getPatientService().getPatient(patientId);
						Date whenStarted = service.getWhenPatientStarted(patient);
						if(cd4Value!=null && obsDate!=null){
						patients6MonthsAgoWithCD4BaselineValueList.add(new Object[] { patientId,df.format(whenStarted), "Start ARVs","M0|obs date", cd4Value,df.format(obsDate) });
						cd4AtM0List.add(cd4Value);
						}
					}
				}
			
			int patients6MonthsAgoWithCD4BaselineValue = 0;
			if (patients6MonthsAgoWithCD4BaselineValueList != null)
				patients6MonthsAgoWithCD4BaselineValue = patients6MonthsAgoWithCD4BaselineValueList.size();
			map.put("patients6MonthsAgoWithCD4BaselineValue",patients6MonthsAgoWithCD4BaselineValue);
			map.put("patients6MonthsAgoWithCD4BaselineValueList",patients6MonthsAgoWithCD4BaselineValueList);

			// ........at 6th month
			// 1.Number of person
			//#########################################################################################################################
			//###           who started ART in cohort of 6 months and still active in six months from when he/started ARV             #
			//###           in six months from when he/she started ARV                                                                    #
			//#########################################################################################################################
//			List<Object[]> exitedPatientsBeforeTheBegOfQterCohort6Obj = service.getPatientsExitedInThePeriod(twoMonthsBeforeDateWhenCohortStartedARTSix, quarterFromDate);
			List<Object[]> patientsInCohortAt6thMonthList = new ArrayList<Object[]>();
			
			for (Object[] obj : patientsInCohortBaselineSixList) {
				Integer patientId = (Integer) obj[0];
				Patient p = Context.getPatientService().getPatient(patientId);
				Date arvStartDate = service.getWhenPatientStarted(p);
				Date sixMonthAfter = service.getXMonthAfterDate(arvStartDate, 6);
				List<Integer> exited = QuarterlyReportUtil.getConvertToType(service.getPatientsExitedInThePeriod(arvStartDate, sixMonthAfter));
				if(!exited.contains(patientId))
				 patientsInCohortAt6thMonthList.add(new Object[] {patientId, "",""});
			}
			
			
			int patientsInCohortAt6thMonth = patientsInCohortAt6thMonthList.size();
			map.put("patientsInCohortAt6thMonth", patientsInCohortAt6thMonth);
			map.put("patientsInCohortAt6thMonthList",patientsInCohortAt6thMonthList);

			// 2.Number of person at 6th month who have CD4 Count
			//#########################################################################################################################
			//###          who had cd4 count and completed 6 months                                                                   #
			//#########################################################################################################################
			List<Object[]> patientsAt6thMonthWithCD4List = new ArrayList<Object[]>();
			List<Double> cd4AtM6List = new ArrayList<Double>();
			List<Object[]> patientIdAndCD4AtM6 = service.getPatientsHadCD4CountAfter6Months(patientsInCohortAt6thMonthList);
			
			if (patientIdAndCD4AtM6 != null)
				for (Object[] ob : patientIdAndCD4AtM6) {
					Integer patientId = (Integer) ob[0];
					Double cd4Value = (Double) ob[1];
					Date obsDate = (Date) ob[2];

					if (patientId != null && cd4Value != null) {
						Patient patient = Context.getPatientService().getPatient(patientId);
						Date start = service.getWhenPatientStarted(patient);
						patientsAt6thMonthWithCD4List.add(new Object[] {patientId, df.format(start), "Start ARVs","M6|obs date", cd4Value, df.format(obsDate) });
						cd4AtM6List.add(cd4Value);
					}
				}
			int patientsAt6thMonthWithCD4 = 0;
			if (patientsAt6thMonthWithCD4List != null)
				patientsAt6thMonthWithCD4 = patientsAt6thMonthWithCD4List.size();
			map.put("patientsAt6thMonthWithCD4", patientsAt6thMonthWithCD4);
			map.put("patientsAt6thMonthWithCD4List",patientsAt6thMonthWithCD4List);

			//################################################################################################
			//##                 patients who received ARVs for 6 out of 6 months                            #
			//################################################################################################
			List<Object[]> patWhoReceivedARVsFor6OutOf6MonthList = service.getPatientsReceivedARVsForXmonthOutOfXmonth(patientsInCohortAt6thMonthList, 6);
			int nberPatReceivedARVsFor6OutOf6Month = patWhoReceivedARVsFor6OutOf6MonthList.size();
			map.put("nberPatReceivedARVsFor6OutOf6Month",nberPatReceivedARVsFor6OutOf6Month);
			map.put("patWhoReceivedARVsFor6OutOf6MonthList",patWhoReceivedARVsFor6OutOf6MonthList);

			//################################################################################################
			//###                ..........BASELINE-------12 month                                           #
			//###                   1.person in cohort of 12 month                                           #
			//################################################################################################
			List<Object[]> patientsInCohortBaselineTwelveList = service.getNewPatientsOver6YearsOnART(twoMonthBeforeDateWhenCohortStartedARTTwelve,dateWhenCohortStartedARTTwelve, null, null, null);
//			List<Object[]> patientsInCohortBaselineTwelveList = service.getNewPatientsOver6YearsOnART(twoMonthBeforeDateWhenCohortStartedARTTwelve,quarterFromDate, null, null, null);
			int patientsInCohortBaselineTwelve = patientsInCohortBaselineTwelveList.size();
			map.put("patientsInCohortBaselineTwelve",patientsInCohortBaselineTwelve);
			map.put("patientsInCohortBaselineTwelveList",patientsInCohortBaselineTwelveList);

			//################################################################################################
			//# 2.patients in cohort of 12 month who have CD4 Count                                          #
			//################################################################################################
			List<Object[]> patients12MonthsAgoWithCD4BaselineValueList = new ArrayList<Object[]>();
			List<Object[]> patientIdAndCD4AtM12 = service.getPatientsWithCD4AtMo(patientsInCohortBaselineTwelveList);
			List<Double> cd4AtM12List = new ArrayList<Double>();
			if (patientIdAndCD4AtM12 != null)
				for (Object[] ob : patientIdAndCD4AtM12) {
					Integer patientId = (Integer) ob[0];
					Double cd4Value = (Double) ob[1];
					Date obsDate = (Date) ob[2];

					if (patientId != null && cd4Value != null) {
						Patient p = Context.getPatientService().getPatient(patientId);
						Date start = service.getWhenPatientStarted(p);
						if(cd4Value!=null && obsDate!=null){
						patients12MonthsAgoWithCD4BaselineValueList.add(new Object[] { patientId,df.format(start), "Start ARVs","M12|obs date", cd4Value,df.format(obsDate) });
 						cd4AtM12List.add(cd4Value);
						}
					}
				}

			int patients12MonthsAgoWithCD4BaselineValue = 0;
			if (patients12MonthsAgoWithCD4BaselineValueList != null)
				patients12MonthsAgoWithCD4BaselineValue = patients12MonthsAgoWithCD4BaselineValueList.size();
			map.put("patients12MonthsAgoWithCD4BaselineValue",patients12MonthsAgoWithCD4BaselineValue);
			map.put("patients12MonthsAgoWithCD4BaselineValueList",patients12MonthsAgoWithCD4BaselineValueList);

			
			// ........at 12th month.........
			//################################################################################################
			//# number of person at 12th month: someone who                                                  #
			//# completed 12 months from when he/she started ARV                                             #
			//################################################################################################
			List<Object[]> patientsInCohortAt12thMonthList = new ArrayList<Object[]>();
			
			for (Object[] obj : patientsInCohortBaselineTwelveList) { 
				Integer patientId = (Integer) obj[0];
				Patient p = Context.getPatientService().getPatient(patientId);
				Date arvStartDate = service.getWhenPatientStarted(p);
				Date sixMonthAfter = service.getXMonthAfterDate(arvStartDate, 6);
				Date twlvMonthAfter = service.getXMonthAfterDate(arvStartDate, 12);
				List<Integer> exitedInSecondSixMonths=QuarterlyReportUtil.getConvertToType(service.getPatientsExitedInThePeriod(arvStartDate, twlvMonthAfter));
				if(!exitedInSecondSixMonths.contains(patientId))
					patientsInCohortAt12thMonthList.add(new Object[] {patientId, "",""});
			}
			
			int patientsInCohortAt12thMonth = patientsInCohortAt12thMonthList.size();
			map.put("patientsInCohortAt12thMonth", patientsInCohortAt12thMonth);
			map.put("patientsInCohortAt12thMonthList",patientsInCohortAt12thMonthList);

			//################################################################################################
			// 2.number of person who have CD4 Count
			//################################################################################################
			List<Double> cd4CountOfActivePatsAtM12 = new ArrayList<Double>();
			List<Object[]> patientsAt12thMonthWithCD4List = new ArrayList<Object[]>();
			patientIdAndCD4AtM12=service.getPatientsHadCD4CountAfter12Months(patientsInCohortAt12thMonthList); 
			if (patientIdAndCD4AtM12 != null)
				for (Object[] ob : patientIdAndCD4AtM12) {
					Integer patientId = (Integer) ob[0];
					Double cd4Value = (Double) ob[1];
					Date obsDate = (Date) ob[2];

					if (patientId != null && cd4Value != null) {
						Patient patient = Context.getPatientService().getPatient(patientId);
						Date start = service.getWhenPatientStarted(patient);
						if(cd4Value!=null && obsDate!=null){
						patientsAt12thMonthWithCD4List.add(new Object[] {patientId, df.format(start), "Start ARVs","M12|obs date", cd4Value, df.format(obsDate) });
						cd4CountOfActivePatsAtM12.add(cd4Value);
						}
					}
				}

			int patientsAt12thMonthWithCD4 = 0;
			if (patientsAt12thMonthWithCD4List != null)
				patientsAt12thMonthWithCD4 = patientsAt12thMonthWithCD4List.size();
			map.put("patientsAt12thMonthWithCD4", patientsAt12thMonthWithCD4);
			map.put("patientsAt12thMonthWithCD4List",patientsAt12thMonthWithCD4List);

			//################################################################################################
			//##            patients who received ARVs for 12 out of 12 months                               #
			//################################################################################################
			List<Object[]> patWhoReceivedARVsFor12OutOf2MonthList = service.getPatientsReceivedARVsForXmonthOutOfXmonth(patientsInCohortAt12thMonthList, 12);
			int nberPatReceivedARVsFor12OutOf12Month = patWhoReceivedARVsFor12OutOf2MonthList.size();
			map.put("nberPatReceivedARVsFor12OutOf12Month",nberPatReceivedARVsFor12OutOf12Month);
			map.put("patWhoReceivedARVsFor12OutOf2MonthList",patWhoReceivedARVsFor12OutOf2MonthList);
			
			//------------------------------------------------------------------------------------------------
			//##              calculate median of patients CD4s Count                                        #
			//------------------------------------------------------------------------------------------------
			Concept cd4Count = Context.getConceptService().getConcept(5497);
			//################################################################################################
			// ================cohort of 6 months=====================================
			//################################################################################################
			// baseline
			double medianBaselineSixMonth = 0.0;
			if (cd4AtM0List != null)
				medianBaselineSixMonth = service.calculateMedian(cd4AtM0List);
			map.put("medianBaselineSixMonth", medianBaselineSixMonth);

			// at 6th month
			double medianSixthMonth = 0.0;
			if (cd4AtM6List != null)
				medianSixthMonth = service.calculateMedian(cd4AtM6List);
			map.put("medianSixthMonth", medianSixthMonth);

			//################################################################################################
			//##                      cohort of 12 months                                                    #
			//################################################################################################
			// baseline
			double medianBaseline12Months = 0.0;
			if (cd4AtM12List != null)
				medianBaseline12Months = service.calculateMedian(cd4AtM12List);
			map.put("medianBaseline12Months", medianBaseline12Months);

			// at 12th month
			double medianAt12thMonth = 0.0;

			try {
				if (cd4CountOfActivePatsAtM12 != null)
					medianAt12thMonth = service.calculateMedian(cd4CountOfActivePatsAtM12);
				    map.put("medianAt12thMonth", medianAt12thMonth);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// ___________________________number of patients buy regimens_______________________________

			List<Integer> activePatientsOnART = QuarterlyReportUtil.getConvertToType(coll3);	
			List<List<Integer>> regimens = new ArrayList<List<Integer>>();
			regimens = service.getRegimenComposition(activePatientsOnART,quarterToDate);
			List<String> regimenNames = new ArrayList<String>();
			List<Object[]> objects = new ArrayList<Object[]>();

			// loop through regimens collection and get patients on each one
			// -------------------------------------------------------------
			int i = 1;
			int total0To11 = 0;
			int total12To23 = 0;
			int total24To59 = 0;
			int total60To179 = 0;
			int total0To179 = 0;
			int total180plus = 0; 
			int totalTotal = 0;
			
			List<List<Integer>> allPatientsOnRegimens =new ArrayList<List<Integer>>();

			List<Integer> regimenCompositions = new ArrayList<Integer>();

			String regimenName = "";
			
			regimens = QuarterlyReportUtil.getRemoveEmptyList(regimens);
			
//			List<Object[]> diff = (List<Object[]>) service.SubtractACollection(totalPatientsRecievedCareList, totalPatientsRecievedCotrimoList);
//			log.info("rererereeeeeeeeeeeeeeeeeeeeeeeeeeeeeee "+QuarterlyReportUtil.getConvertToType(diff));
				
				
			for (List<Integer> reg : regimens) {
					
					regimenCompositions = reg;
					regimenName = service.getRegimenName(regimenCompositions);


					// age 0-11
					// male
					List<Integer> artActiveMalePatients0To11 = QuarterlyReportUtil.getConvertToType(male0To11ActiveOnARTTotalEndQterList);
					List<Integer> artActiveFemalePatients0To11 = QuarterlyReportUtil.getConvertToType(female0To11ActiveOnARTTotalEndQterList);
					List<Integer> patient0To1 = (List<Integer>) service.union(artActiveMalePatients0To11, artActiveFemalePatients0To11);

					List<Object[]> patientsAged0To11onRegimen_i_List = service.getPatientOnAllDrugs(regimenCompositions, patient0To1,quarterToDate);
						
					if(patientsAged0To11onRegimen_i_List.size()==0){
						patientsAged0To11onRegimen_i_List= service.getPatientOnAllDrugsByConceptIds(regimenCompositions, patient0To1,quarterToDate);
					}
					
					int patientsAged0To11onRegimen_i = 0;
					patientsAged0To11onRegimen_i = patientsAged0To11onRegimen_i_List.size();
					map.put("patientsAged0To1onRegimen_i",patientsAged0To11onRegimen_i);

					map.put("position_" + i + "," + 0,patientsAged0To11onRegimen_i_List);
					total0To11 = total0To11 + patientsAged0To11onRegimen_i;

					// age 12-23
					List<Integer> artActiveMalePatients12To23 = QuarterlyReportUtil.getConvertToType(male12To23ActiveOnARTTotalEndQterList);
					List<Integer> artActiveFemalePatients12To23 = QuarterlyReportUtil.getConvertToType(female12To23ActiveOnARTTotalEndQterList);
					List<Integer> totActiveARTPatient12To23 = (List<Integer>) service.union(artActiveMalePatients12To23,artActiveFemalePatients12To23);
									

					List<Object[]> patientsAged12To23onRegimen_i_List = service.getPatientOnAllDrugs(regimenCompositions,totActiveARTPatient12To23,quarterToDate);
					if(patientsAged12To23onRegimen_i_List.size()==0){
						patientsAged12To23onRegimen_i_List= service.getPatientOnAllDrugsByConceptIds(regimenCompositions, totActiveARTPatient12To23,quarterToDate);
					}


					int patientsAged12To23onRegimen_i = 0;
					patientsAged12To23onRegimen_i = patientsAged12To23onRegimen_i_List.size();
					map.put("patientsAged12To23onRegimen_i",patientsAged12To23onRegimen_i);

					map.put("position_" + i + "," + 1,patientsAged12To23onRegimen_i_List);
					total12To23 = total12To23 + patientsAged12To23onRegimen_i;

					// age 24-59
					List<Integer> artActiveMalePatients24To59 = QuarterlyReportUtil.getConvertToType(male24To59ActiveOnARTTotalEndQterList);
					List<Integer> artActiveFemalePatients24To59 = QuarterlyReportUtil.getConvertToType(fem24To59ActiveOnARTTotalEndQterList);
					List<Integer> totActiveARTPatient24To59 = (List<Integer>) service.union(artActiveMalePatients24To59,artActiveFemalePatients24To59);

					List<Object[]> patientsAged24To59onRegimen_i_List = service.getPatientOnAllDrugs(regimenCompositions,totActiveARTPatient24To59,quarterToDate);
					if(patientsAged24To59onRegimen_i_List.size()==0){
						patientsAged24To59onRegimen_i_List= service.getPatientOnAllDrugsByConceptIds(regimenCompositions, totActiveARTPatient24To59,quarterToDate);
					}
					int patientsAged24To59onRegimen_i = patientsAged24To59onRegimen_i_List.size();
					map.put("patientsAged24To59onRegimen_i",patientsAged24To59onRegimen_i);

					map.put("position_" + i + "," + 2,patientsAged24To59onRegimen_i_List);
					total24To59 = total24To59 + patientsAged24To59onRegimen_i;
					
					// age 60-179
					List<Integer> artActiveMalePatients60To179 = QuarterlyReportUtil.getConvertToType(male60To179ActiveOnARTTotalEndQterList);
					List<Integer> artActiveFemalePatients60To179 = QuarterlyReportUtil.getConvertToType(fem60To179ActiveOnARTTotalEndQterList);
					List<Integer> totActiveARTPatient60To179 = (List<Integer>) service.union(artActiveMalePatients60To179,artActiveFemalePatients60To179);

					List<Object[]> patientsAged60To179onRegimen_i_List = service.getPatientOnAllDrugs(regimenCompositions,totActiveARTPatient60To179,quarterToDate);
					if(patientsAged60To179onRegimen_i_List.size()==0){
						patientsAged60To179onRegimen_i_List= service.getPatientOnAllDrugsByConceptIds(regimenCompositions, totActiveARTPatient60To179,quarterToDate);
					}

					int patientsAged60To179OnRegimen_i = patientsAged60To179onRegimen_i_List.size();
					map.put("patientsAged60To179OnRegimen_i",patientsAged60To179OnRegimen_i);

					map.put("position_" + i + "," + 3,patientsAged60To179onRegimen_i_List);
					total60To179 = total60To179 + patientsAged60To179OnRegimen_i;

					// total 0-179
					List<Object[]> patientsAged0To11And12To23onRegimen_i_List = (List<Object[]>) service.union(patientsAged0To11onRegimen_i_List,patientsAged12To23onRegimen_i_List);
					List<Object[]> patient0To11_12To23_24To59OnRegimenList = (List<Object[]>) service.union(patientsAged0To11And12To23onRegimen_i_List,patientsAged24To59onRegimen_i_List);
					List<Object[]> patient0To179OnRegimenList = (List<Object[]>) service.union(patient0To11_12To23_24To59OnRegimenList,patientsAged60To179onRegimen_i_List);
					int patient0To179OnRegimen = patient0To179OnRegimenList.size();
					map.put("patient0To179OnRegimen", patient0To179OnRegimen);

					map.put("position_" + i + "," + 4,patient0To179OnRegimenList);
					total0To179 = total0To179 + patient0To179OnRegimen;

					// age >180

					List<Integer> artActiveMalePatientsOver180 = QuarterlyReportUtil.getConvertToType(maleOver180ActiveOnARTTotalEndQterList);
					List<Integer> artActiveFemalePatientsOver180 = QuarterlyReportUtil.getConvertToType(femaleOver180ActiveOnARTTotalEndQterList);
					List<Integer> totActiveARTPatientsOver180 = (List<Integer>) service.union(artActiveMalePatientsOver180,artActiveFemalePatientsOver180);
					

					List<Object[]> patientsOver180OnRegimen_i_List = service.getPatientOnAllDrugs(regimenCompositions,totActiveARTPatientsOver180,quarterToDate);
					if(patientsOver180OnRegimen_i_List.size()==0){
						patientsOver180OnRegimen_i_List= service.getPatientOnAllDrugsByConceptIds(regimenCompositions, totActiveARTPatientsOver180,quarterToDate);
						
					}
					int patientsOver180OnRegimen_i = patientsOver180OnRegimen_i_List.size();
					map.put("patientsOver180OnRegimen_i", patientsOver180OnRegimen_i);

					map.put("position_" + i + "," + 5,patientsOver180OnRegimen_i_List);
					total180plus = total180plus + patientsOver180OnRegimen_i;

					// total 0-179 and >180 on regimen at index i
					List<Object[]> pediatricAndAdultPatientsOnRegimenList = (List<Object[]>) service.union(patient0To179OnRegimenList,patientsOver180OnRegimen_i_List);
					int pediatricAndAdultPatientsOnRegimen = pediatricAndAdultPatientsOnRegimenList.size();
					map.put("pediatricAndAdultPatientsOnRegimen",pediatricAndAdultPatientsOnRegimen);

					map.put("position_" + i + "," + 6,pediatricAndAdultPatientsOnRegimenList);
					totalTotal = totalTotal + pediatricAndAdultPatientsOnRegimen;
					
					allPatientsOnRegimens.add(QuarterlyReportUtil.getConvertToType(pediatricAndAdultPatientsOnRegimenList));

					i++;
					// numbers					
					objects.add(new Object[] {regimenName,patientsAged0To11onRegimen_i,patientsAged12To23onRegimen_i,patientsAged24To59onRegimen_i,patientsAged60To179OnRegimen_i, patient0To179OnRegimen,patientsOver180OnRegimen_i,pediatricAndAdultPatientsOnRegimen, i });
				}
			 
			List<Object[]> patOnRegObj=new ArrayList<Object[]>(); 
			for (List<Integer> list : allPatientsOnRegimens) {
				for (Integer d : list) {
					Patient p = Context.getPatientService().getPatient(d);
					patOnRegObj.add(new Object[]{d,"",""});
				}
			}
			
			
			
//			log.info("difffffffffffffffffffffffffffffffff"+QuarterlyReportUtil.getConvertToType((List<Object[]>) service.SubtractACollection(coll3,patOnRegObj )));
			
			// ________________________________________________________________________________ 
			//                       objects is used on jsp to display patients of tab 5
			// ________________________________________________________________________________
			map.put("regimens", regimenNames);
			map.put("objects", objects);

			map.put("total0To11", total0To11);
			map.put("total12To23", total12To23);
			map.put("total24To59", total24To59);
			map.put("total60To179", total60To179);
			map.put("total0To179", total0To179);
			map.put("total180plus", total180plus);
			map.put("totalTotal", totalTotal);
			map.put("totalPatientsOnReg", patOnRegObj);
			map.put("today", df.format(new Date()));

			// ______set all patient ids list into one session_____________

			request.getSession().setAttribute("mapOfCollections", map);
			
		}
		return new ModelAndView(getViewName(), map);
	}

}
