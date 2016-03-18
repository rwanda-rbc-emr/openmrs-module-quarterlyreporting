package org.openmrs.module.quarterlyreporting.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ExportQuarterlyData extends SimpleFormController {
protected final Log log = LogFactory.getLog(getClass());

@SuppressWarnings("unchecked")
@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

	
	QuarterlyReportingService service = Context.getService(QuarterlyReportingService.class);
	
	List<Patient> patients = (List<Patient>) request.getSession().getAttribute("patients");
	
	String programIdKey = request.getParameter("checkType");
	
	
	service.exportQuarterlyData(request, response, patients, "patientsInQuarter.csv", programIdKey);
	
	
	return null;
	}

}
