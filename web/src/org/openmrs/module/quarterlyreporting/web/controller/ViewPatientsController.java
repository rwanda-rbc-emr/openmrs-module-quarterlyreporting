package org.openmrs.module.quarterlyreporting.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.quarterlyreporting.service.QuarterlyReportingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ViewPatientsController extends ParameterizableViewController {
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String linkId="";
		String title="";
		if(request.getParameter("linkId")!=null){
			linkId=request.getParameter("linkId");
//			log.info(">>>>>>>>>>>>>>>>>>>>>linkId>>>>> " + linkId);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = (Map<String, Object>) request.getSession().getAttribute("mapOfCollections");

		
		List<Object[]> currentList = new ArrayList<Object[]>();
		
		String dateTitle = "";
		String cd4Title = "";


		
		//______________________________table 1:ENROLLMENT______________________________________________________________
		//adult
		if(linkId.equals("cumMalePatUnder179EnrolledBuyTheBegOfQuarter")){
			currentList= (List<Object[]>) map.get("cumMalePatUnder179EnrolledBuyTheBegOfQuarterList");
			title = "Cumulative Patients(Male aged 0 to 179 Months) Enrolled By The Begenning of the Quarter";
		}
		if(linkId.equals("cumMalePatUnder179EnrolledDuringTheQuarter")){
			currentList=(List<Object[]>) map.get("cumMalePatUnder179EnrolledDuringTheQuarterList");
			title = " NEW enrollees(Male aged 0 to 179 Months) in HIV care during the quarter ";
		}
		
		if(linkId.equals("cumMalePatUnder179EnrolledByTheEndOfQuarter")){
			currentList=(List<Object[]>) map.get("cumMalePat0To79EnrolledEndOfQterList");
			title = " Cumulative Patients(Male aged 0 to 179 months) Enrolled By The End Of The quarter ";
		}
		
		if(linkId.equals("cumFemPat0To179EnrolledTheBegOfQter")){
			currentList=(List<Object[]>) map.get("cumFemPatEnrolledTheBegOfQterList");
			title = " Cumulative Patients(Female aged 0 to 179 months) Enrolled By The Begenning of the Quarter ";
		}

		if(linkId.equals("cumFemPat0To179EnrolledDuringTheQuarter")){
			currentList=(List<Object[]>) map.get("cumFemPat0To179EnrolledDuringTheQuarterList");
			title = "Cumulative Patients(Female aged 0 to 179 months) Enrolled During the Quarter";
		}
		if(linkId.equals("cumFemPat0To179EnrolledByTheEndOfQuarter")){
			currentList=(List<Object[]>) map.get("cumFemPat0To179EnrolledByTheEndOfQuarterList");
			title = "Cumulative Patient (Female aged 0 to 179 months) enrolled at by End of Quarter";
		}
		if(linkId.equals("cumMalePatOver180MonEnrolledBegOfQter")){ 
			currentList=(List<Object[]>) map.get("cumMalePatOver180MonEnrolledBegOfQterList");
			title = "Cumulative Patients (Male Aged Over 180 months) Enrolled By the Beginning of Quarter";
		}
		if(linkId.equals("cumMalePatOver180EnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("cumMalePatOver180EnrolledDuringQterList");
			title = "Cumulative Patients(Male aged Over 180 months) Enrolled During the Quarter";
		}
		
		if(linkId.equals("cumMalePatOver180EnrolledEndOfQter")){
			currentList=(List<Object[]>) map.get("cumMalePatOver180EnrolledEndOfQterList");
			title = "Cumulative Patients(Male Aged Over 180 mmonths) Enrolled By the End of the Quarter ";
		}
		if(linkId.equals("cumFemPatOver180MonEnrolledBegOfQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180MonEnrolledBegOfQterList");
			title = "Cumulative Patients(Female aged over 180 months) Enrolled by the Begenning of the Quarter ";
		}
		if(linkId.equals("cumFemPatOver180MonEnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180MonEnrolledDuringQterList");
			title = "Cumulative Patients(Female aged over 180 months) Enrolled During the Quarter";
		}
		if(linkId.equals("cumFemPatOver180MonEnrolledEndOfQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180MonEnrolledEndOfQterList");
			title = "Cumulative Patients(Female aged Over 180) Enrolled by the End of the Quarter";
		}
		if(linkId.equals("totalEnrolledBegQter")){
			currentList=(List<Object[]>) map.get("totalEnrolledBegQterList");
			title = "Total Patients Enrolled By the beginning of the Quarter ";
		}
		if(linkId.equals("totalEnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("totalEnrolledDuringQterList");
			title=" Total Patients Enrolled During the Quarter";
		}
		if(linkId.equals("totalEnrolledEndQter")){
			currentList=(List<Object[]>) map.get("totalEnrolledEndQterList");
			title=" Total Patients Enrolled Buy the end of the Quarter";
		}
		
		
		//____________________________patients who received HIV Care___________________________________________________
		//--------------adult who received HIV Care-------------------------------------
		if(linkId.equals("malePatRecievedCareAge0To179")){
			currentList=(List<Object[]>) map.get("malePatRecievedCareAge0To179List");
			title=" Male Patients(Age between 0 to 179 months) Received HIV Care ";
		}
		if(linkId.equals("maleOver180ReceivedHIVCare")){
			currentList=(List<Object[]>) map.get("maleOver180ReceivedHIVCareList");
			title=" Male Patients(Aged Over 180 months) who received HIV Care";
		}
		if(linkId.equals("femalePatRecievedCareAge0To179")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCareAge0To179List");
			title=" Female Patients(Age between 0 to 179 months) Received HIV Care";
		}
		if(linkId.equals("femaleOver180ReceivedHIVCare")){
			currentList=(List<Object[]>) map.get("femaleOver180ReceivedHIVCareList");
			title=" Female Patients(Aged Over 180 months) who received HIV Care";
		}
		if(linkId.equals("totalPatientsRecievedCare")){
			currentList=(List<Object[]>) map.get("totalPatientsRecievedCareList");
			title=" Total Patients who received HIV Care";
		}
		
		//--------------pediatric who received HIV Care---------------------------------
		if(linkId.equals("malePatRecievedCareAge0To11")){
			currentList=(List<Object[]>) map.get("malePatRecievedCareAge0To11List");
			title=" Male Patients(Aged Between 0 and 11 months) who received HIV Care";
		}
		if(linkId.equals("malePatRecievedCareAge12To23")){
			currentList=(List<Object[]>) map.get("malePatRecievedCareAge12To23List");
			title = "male Patients(Aged between 12 and 23 months) received HIV care";
		}
		if(linkId.equals("malePatRecievedCareAge24To59")){
			currentList=(List<Object[]>) map.get("malePatRecievedCareAge24To59List");
			title = "male Patients(Aged between 24 and 59 months) received HIV care";
		}
		
		if(linkId.equals("malePatRecievedCareAge60To179")){
			currentList=(List<Object[]>) map.get("malePatRecievedCareAge60To179List");
			title=" Male Patients(Aged Between 60 and 179) who received HIV Care";
		}
		if(linkId.equals("femalePatRecievedCareAge0To11")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCareAge0To11List");
			title=" Female Patients(Aged Between 0 and 11) who received HIV Care";
		}
		if(linkId.equals("femalePatRecievedCareAge12To23")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCareAge12To23List");
			title = "male Patients(Aged between 12 and 23 months) received HIV care";
		}
		if(linkId.equals("femalePatRecievedCareAge24To59")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCareAge24To59List");
			title=" Female Patients(Aged Between 24 and 59) who received HIV Care";
		}
		if(linkId.equals("femalePatRecievedCareAge60To179")){ 
			currentList=(List<Object[]>) map.get("femalePatRecievedCareAge60To179List");
			title=" Female Patients(Aged Between 60 and 179) who received HIV Care";
		}		
		
		//____________________________patients who received Cotrimo this quarter_____________________
		//--------------adult who received Cotrimo-------------------------------------
		if(linkId.equals("malePatRecievedCotrimo0To179")){
			currentList=(List<Object[]>) map.get("malePatRecievedCotrimo0To179List");
			title=" Male Patients(Age between 0 to 179 months) Received Cotrimo this quarter ";
		}
		if(linkId.equals("maleOver180ReceivedCotrimo")){
			currentList=(List<Object[]>) map.get("maleOver180ReceivedCotrimoList");
			title=" Male Patients(Aged Over 180 months) who received Cotrimo this quarter";
		}
		if(linkId.equals("femalePatRecievedCotrimo0To179")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCotrimo0To179List");
			title=" Female Patients(Age between 0 to 179 months) Received Cotrimo this quarter";
		}
		if(linkId.equals("femaleOver180ReceivedCotrimo")){
			currentList=(List<Object[]>) map.get("femaleOver180ReceivedCotrimoList");
			title=" Female Patients(Aged Over 180 months) who received Cotrimo this quarter";
		}
		if(linkId.equals("totalPatientsRecievedCotrimo")){
			currentList=(List<Object[]>) map.get("totalPatientsRecievedCotrimoList");
			title=" Total Patients who received Cotrimo this quarter";
		}
		
		//#################pediatric who received Cotrimo this quarter###########################
		// male 0-11
		if(linkId.equals("malePatRecievedCotrimo0To11")){
			currentList=(List<Object[]>) map.get("malePatRecievedCotrimo0To11List");
			title=" Male Patients(Aged Between 0 and 11 months) who received Cotrimo this quarter";
		}
		// male 12-23
		if(linkId.equals("malePatRecievedCotrimo12To23")){
			currentList=(List<Object[]>) map.get("malePatRecievedCotrimo12To23List");
			title = "male Patients(Aged between 12 and 23 months) received Cotrimo this quarter";
		}
		// male 24-59
		if(linkId.equals("malePatRecievedCotrimo24To59")){
			currentList=(List<Object[]>) map.get("malePatRecievedCotrimo24To59List");
			title = "male Patients(Aged between 24 and 59 months) received Cotrimo this quarter";
		}
		// male 60-179
		if(linkId.equals("malePatRecievedCotrimo60To179")){
			currentList=(List<Object[]>) map.get("malePatRecievedCotrimo60To179List");
			title=" Male Patients(Aged Between 60 and 179) who received Cotrimo this quarter";
		}
		//female 0-11
		if(linkId.equals("femalePatRecievedCotrimo0To11List")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCotrimo0To11");
			title=" Female Patients(Aged Between 0 and 11) who received Cotrimo this quarter";
		}
		//female 12-23
		if(linkId.equals("femalePatRecievedCotrimo12To23")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCotrimo12To23List");
			title = "male Patients(Aged between 12 and 23 months) received Cotrimo this quarter";
		}
		//female 24-59
		if(linkId.equals("femalePatRecievedCotrimo24To59")){
			currentList=(List<Object[]>) map.get("femalePatRecievedCotrimo24To59List");
			title=" Female Patients(Aged Between 24 and 59) who receivedCotrimo this quarter";
		}
		//female 60-179
		if(linkId.equals("femalePatRecievedCotrimo60To179")){ 
			currentList=(List<Object[]>) map.get("femalePatRecievedCotrimo60To179List");
			title=" Female Patients(Aged Between 60 and 179) who received Cotrimo this quarter";
		}		
		
		//=======================pediatric enrolled=================================================================
		// male 0-11
		if(linkId.equals("malePat0To11EnrolledByTheBegOfQter")){
			currentList=(List<Object[]>) map.get("malePat0To11EnrolledByTheBegOfQterList");
			title=" Male Patients(Aged Between 0 and 11 months) Enrolled Buy the Beginning of the Quarter";
		}
		if(linkId.equals("malePat0To11EnrolledDuringTheQter")){
			currentList=(List<Object[]>) map.get("malePat0To11EnrolledDuringTheQterList");
			title=" Male Patients(Aged Between 0 and 11 months) Enrolled During the Quarter";
		}
		if(linkId.equals("malePat0To11EnrolledByTheEndOfQter")){
			currentList=(List<Object[]>) map.get("malePat0To11EnrolledByTheEndOfQterList");
			title=" Female Patients(Aged Between 0 and 11 months) Buy the End of the Quarter";
		}
		// male 12-23
		if(linkId.equals("malePat12To23BegQter")){
			currentList=(List<Object[]>) map.get("malePat12To23BegQterList");
			title=" Male Patients(Aged Between 12 and 23 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("malePat12To23DuringQter")){
			currentList=(List<Object[]>) map.get("malePat12To23DuringQterList");
			title=" Male Patients(Aged Between 12 and 23 months) Enrolled During the Quarter";
		}
		if(linkId.equals("malePat12To23EndQter")){
			currentList=(List<Object[]>) map.get("malePat12To23EndQterList");
			title=" Male Patients(Aged Between 12 and 23 months) Enrolled Buy the End of the Quarter";
		}
		// male 24-59
		if(linkId.equals("malePat24To59BegQter")){
			currentList=(List<Object[]>) map.get("malePat24To59BegQterList");
			title=" Male Patients(Aged Between 24 and 59 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("malePat24To59DuringQter")){
			currentList=(List<Object[]>) map.get("malePat24To59DuringQterList");
			title=" Male Patients(Aged Between 24 and 59 months) Enrolled During the Quarter";
		}
		if(linkId.equals("malePat24To59EndQter")){
			currentList=(List<Object[]>) map.get("malePat24To59EndQterList");
			title=" Male Patients(Aged Between 24 and 59 months) Enrolled Buy the End of the Quarter";
		}
		
		// male 60-179
		if(linkId.equals("malePat60To179BegQter")){
			currentList=(List<Object[]>) map.get("malePat60To179BegQterList");
			title=" Male Patients(Aged Between 60 and 179 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("malePat60To179DuringTheQter")){
			currentList=(List<Object[]>) map.get("malePat60To179DuringTheQterList");
			title=" Male Patients(Aged Between 60 and 179) Enrolled During the Quarter";
		}
		if(linkId.equals("malePat60To179EndQter")){
			currentList=(List<Object[]>) map.get("malePat60To179EndQterList");
			title=" Male Patients(Aged Between 60 and 179) Enrolled Buy the End of the Quarter";
		}
		// female 0-11
		if(linkId.equals("femalePat0To11EnrolledBegQter")){
			currentList=(List<Object[]>) map.get("femalePat0To11EnrolledBegQterList");
			title=" Female Patients(Aged Between 0 and 11 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("femalePat0To11EnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat0To11EnrolledDuringQterList");
			title=" Female Patients(Aged Between 0 and 11 months) Enrolled During the Quarter";
		}
		if(linkId.equals("femalePat0To11EnrolledEndQter")){
			currentList=(List<Object[]>) map.get("femalePat0To11EnrolledEndQterList");
			title=" Female Patients(Aged Between 0 and 11 months) Enrolled Buy the End of the Quarter";
		}
		
		//female 12-23
		if(linkId.equals("femalePat12To23EnrolledBegQter")){
			currentList=(List<Object[]>) map.get("femalePat12To23EnrolledBegQterList");
			title=" Female Patients(Aged Between 12 and 23 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("femalePat12To23EnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat12To23EnrolledDuringQterList");
			title=" Female Patients(Aged Between 12 and 23 months) Enrolled During the Quarter";
		}
		if(linkId.equals("femalePat12To23EnrolledEndQter")){
			currentList=(List<Object[]>) map.get("femalePat12To23EnrolledEndQterList");
			title=" Female Patients(Aged Between 12 and 23 months) Enrolled Buy the End of the Quarter";
		}
		
		// female 24-59
		if(linkId.equals("femalePat24To59EnrolledBegQter")){
			currentList=(List<Object[]>) map.get("femalePat24To59EnrolledBegQterList");
			title=" Female Patients(Aged Between 24 and 59 months) Enrolled Buy the End of the Quarter";
		}
		if(linkId.equals("femalePat24To59EnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat24To59EnrolledDuringQterList");
			title=" Female Patients(Aged Between 24 and 59 months) Enrolled During the Quarter";
		}
		if(linkId.equals("femalePat24To59EnrolledEndQter")){
			currentList=(List<Object[]>) map.get("femalePat24To59EnrolledEndQterList");
			title=" Female Patients(Aged Between 24 and 59 months) Enrolled Buy the End of the Quarter";
		}
		
		// female 60-179
		if(linkId.equals("femalePat60To179EnrolledBegQter")){
			currentList=(List<Object[]>) map.get("femalePat60To17EnrolledBegQterList");
			title=" Female Patients(Aged Between 60 and 179 months) Enrolled Buy the beginning of the Quarter";
		}
		if(linkId.equals("femalePat60To179EnrolledDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat60To179EnrolledDuringQterList");
			title=" Female Patients(Aged Between 60 and 179 months) Enrolled During  the Quarter";
		}
		if(linkId.equals("femalePat60To179EnrolledEndQter")){
			currentList=(List<Object[]>) map.get("femalePat60To179EnrolledEndQterList");
			title=" Female Patients(Aged Between 60 and 179 months) Enrolled Buy the End of the Quarter";
		}
		
		//____________________________eligible_________________________________________________________________________
		if(linkId.equals("eligiblePatients")){
			currentList=(List<Object[]>) map.get("eligiblePatientsList");
			title=" Patients in HIV care during the quarter & eligible for ART, but NOT started ART by the end of the quarter ";
		}
		
		//_________________________________ table 2 ====>ART CARE_______________________________________________________	
		// male 0-179
		if(linkId.equals("cumMalePat0To179StartedARTBegOfQter")){
			currentList= (List<Object[]>) map.get("cumMalePat0To179StartedARTBegQterList");
			title=" Cumulative Male Patients(Aged Between 0 and 179 months) started on ART by the beginning of the quarter ";
		}
		if(linkId.equals("cumMalePat0To179StartedDuringQter")){
			currentList=(List<Object[]>) map.get("cumMalePat0To179StartedDuringQterList");
			title=" Cumulative Male Patients(Aged Between 0 and 179 months) started on ART During the quarter ";
		}
		if(linkId.equals("cumMalePat0To179StartedEndQter")){
			currentList=(List<Object[]>) map.get("cumMalePat0To179StartedEndQterList");
			title=" Cumulative Male Patients(Aged Between 0 and 179 months) started on ART Buy the end of the quarter ";
		}
		if(linkId.equals("male0To179NewOnArtDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("male0To179NewOnARTDuringQterNoTransfList");
			title=" Cumulative Male Patients(Aged Between 0 and 179 months) New on ART During the quarter ---->[Transfer In Excluded] ";
		}
		
		// female 0-179
		if(linkId.equals("cumfem0To179StartedARTBegQter")){
			currentList=(List<Object[]>) map.get("cumfem0To179StartedARTBegQterList");
			title=" Cumulative Female Patients(Aged Between 0 and 179 months) started on ART by the beginning of the quarter ";
		}
		if(linkId.equals("cumfem0To179StartedARTDuringQter")){
			currentList=(List<Object[]>) map.get("cumfem0To179StartedARTDuringQterList");
			title=" Cumulative Female Patients(Aged Between 0 and 179 months) started on ART during the quarter ";
		}

		if(linkId.equals("cumfemPat0To179StartedARTEndQter")){
			currentList=(List<Object[]>) map.get("cumfemPat0To179StartedARTEndQterList");
			title="Cumulative Patients (Female Aged 0 to 179 months) started ART By the End of the Quarter " ;
		}
		if(linkId.equals("fem0To179OnArtDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("fem0To179NewOnARTDuringQterNoTransfList");
			title="Cumulative Patients (Female Aged 0 to 179 months) started ART During the Quarter----> [Transferred ones excluded] " ;
		}
		
		// male >180
		if(linkId.equals("cumMalePatOver180StartedARTBegQter")){
			currentList=(List<Object[]>) map.get("cumMalePatOver180StartedARTBegQterList");
			title="Cumulative Patients (Male Aged Over 180) started ART By the beginning of the Quarter " ;
		}
		if(linkId.equals("cumMalePatOver180StartedARTDuringQter")){
			currentList=(List<Object[]>) map.get("cumMalePatOver180StartedARTDuringQterList");
			title="Cumulative Patients (Male Aged Over 180) started ART During the Quarter " ;
		}
		if(linkId.equals("cumMalePatOver80StartedARTEndQter")){
			currentList=(List<Object[]>) map.get("cumMalePatOver180StartedARTEndQterList");
			title="Cumulative Patients (Male Aged Over 180) started ART By the End of the Quarter " ;
		}
		if(linkId.equals("maleOver180NewOnArtDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("maleOver179NewOnARTDuringQterNoTransfList");
			title="Cumulative Patients (Male Aged Over 180) started ART During the Quarter -----> Transferred Patients Not Included " ;
		}
		
		// female >180
		if(linkId.equals("cumFemPatOver180StartedARTBegQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180StartedARTBegQterList");
			title=" Cumulative Female Patients(Aged over 180 months) started on ART by the beginning of the quarter ";
		}
		if(linkId.equals("cumFemPatOver180StartedARTDuringQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180StartedARTDuringQterList");
			title=" Cumulative Female Patients(Aged over 180 months) started on ART during the quarter ";
		}

		if(linkId.equals("cumFemPatOver180StartedARTEndQter")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180StartedARTEndQterList");
			title="Cumulative Patients (Female Aged over 180 months) started ART By the End of the Quarter " ;
		}
		if(linkId.equals("femOver180NewOnARTDuringQterNoTransfIn")){
			currentList=(List<Object[]>) map.get("cumFemPatOver180NewOnARTDuringQterList");
			title="Cumulative Patients (Female Aged over 180 months) started ART During the Quarter----> [Transferred ones excluded] " ;
		}
		
				
		//active on ART ===> male 0-179,male over 180,female 0-179,female over 180
		if(linkId.equals("male0To179ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("male0To179ActiveOnARTTotalEndQterList");
			title="Active Patients(Male Aged 0 to 179 months) on ART By the End of the Quarter " ;
		}
		if(linkId.equals("maleOver180ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("maleOver180ActiveOnARTTotalEndQterList");
			title="Active Patients(Male Aged over 180 months) on ART By the End of the Quarter " ;
		}
		if(linkId.equals("fem0To179ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("female0To179ActiveOnARTTotalEndQterList");
			title="Active Patients(Female Aged 0 to 179 months) on ART By the End of the Quarter " ;
		}
		if(linkId.equals("femOver180ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("femaleOver180ActiveOnARTTotalEndQterList");
			title="Active Patients(Female Aged Over 180 months) on ART By the End of the Quarter " ;
		}
		if(linkId.equals("totalAdultActiveInART")){
			currentList=(List<Object[]>) map.get("coll3");
			title="Total Adult Patient on ART By the End of the Quarter ---->[Active] " ;
		}
		
		//active pregnants females
		if(linkId.equals("activePregnants")){
			currentList=(List<Object[]>) map.get("activePregnantsList");
			title="Active Pregnant Females" ;
		}
		
		//
		if(linkId.equals("usdFunded")){ 
			currentList=(List<Object[]>) map.get("usdFundedList");
			title="Persons on ART at the end of the quarter who were treated with USG-funded ART" ;
		}
		
		// female >15
		if(linkId.equals("cumFemalePatOverFifteenStartedARTBuyTheBegOfQter")){
			currentList=(List<Object[]>) map.get("cumFemalePatOverFifteenStartedARTBuyTheBegOfQterList");
			title="Cumulative Patients(Female Aged Over 15) Started ART By the Beginning of the Quarter " ;
		}
		if(linkId.equals("cumFemalePatOverFifteenStartedARTDuringTheQter")){
			currentList=(List<Object[]>) map.get("cumFemalePatOverFifteenStartedARTDuringTheQterList");
			title="Cumulative Patients(Male Aged Over 15) Started ART During the Quarter " ;
		}
		if(linkId.equals("cumFemalePatOverFifteenStartedARTBuyTheEndOfQter")){
			currentList=(List<Object[]>) map.get("cumFemalePatOverFifteenStartedARTBuyTheEndOfQterList");
			title="Cumulative Patients(Female Aged Over 15) Started ART By the End the Quarter " ;
		}
		if(linkId.equals("femaleOver15NewOnARTDuringQterNoTransfIn")){
			currentList=(List<Object[]>) map.get("femaleOver15NewOnARTDuringQterNoTransfList");
			title="Female Patients(Male Aged Over 15) Started ART During the Quarter -----> Transferred In Not Considered " ;
		}
		
		//total new on ART +  transferred in(adult)
		if(linkId.equals("totalCumBuyBegQter")){
			currentList=(List<Object[]>) map.get("cumStartedARTBuyTheBegOfQterList");
			title="Total Cumulative Patients Started ART By the Beginning of the Quarter " ;
		}
		
		if(linkId.equals("totalCumDuringQter")){
			currentList=(List<Object[]>) map.get("cumPatStartedDuringTheQterList");
			title="Total Cumulative Patients Started ART During the Quarter " ;
		}
		
		if(linkId.equals("totalCumEndQter")){
			currentList=(List<Object[]>) map.get("cumPatStartedARTBuyTheEndOfQterList");
			title="Total Cumulative Patients Started ART By the End of the Quarter " ;
		}
		
		//new not including transferred in
		if(linkId.equals("totalActiveOnART")){
			currentList=(List<Object[]>) map.get("newOnArtDuringQterNoTransferInList");
			title="Total Patients On ART ----> Transferred In Not Considered!!!!!! " ;
		}
		
		// pediatric ART
		if(linkId.equals("male0To1ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("male0To1ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Male Aged 0 to 1) On ART by the End of the Quarter ----->[Active] " ;
		}
		if(linkId.equals("male2To4ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("male2To4ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Male Aged 2 to 4) On ART by the End of the Quarter ----->[Active] " ;
		}
		
		if(linkId.equals("male5To14ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("male5To14ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Male Aged 5 to 14) On ART by the End of the Quarter ----->[Active] " ;
		}
		
		if(linkId.equals("female0To1ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("female0To1ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Female Aged 0 to 1) On ART by the End of the Quarter ----->[Active] " ;
		}
		if(linkId.equals("female2To4ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("female2To4ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Female Aged 2 to 4) On ART by the End of the Quarter ----->[Active] " ;
		}
		if(linkId.equals("female5To14ActiveOnARTTotalEndQter")){
			currentList=(List<Object[]>) map.get("female5To14ActiveOnARTTotalEndQterList");
			title="Total Pediatric Patients(Aged 5 to 14) On ART by the End of the Quarter ----->[Active] " ;
		}
		
		//___________________________________patients transferred in during the quarter________________________________________________
		
		//===========================adults transferred in=============================================================
		//male 0-179
		if(linkId.equals("male0To179TransfInDuringQter")){ 
			currentList=(List<Object[]>) map.get("male0To179TransfInDuringQterList");
			title="Male Patients(Aged 0 to 179) Transferred in During the Quarter";
		}
		//male >180
		if(linkId.equals("maleOver180TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("maleOver180TransfInDuringQterList");
			title="Male Patient(Aged Over 180) Transferred in During the Quarter";
		}
		
		//female 0-179
		if(linkId.equals("femaleOTo179TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("femaleOTo179TransfInDuringQterList");
			title="Female Patients(Aged 0 to 179) Transferred in During the Quarter";
		}
		
		//female >180
		if(linkId.equals("femaleOver180TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("femaleOver180TransfInDuringQterList");
			title="Female Patients(Aged 0 to 180) Transferred in During the Quarter";
		}
		
		if(linkId.equals("transferredInTotal")){
			currentList=(List<Object[]>) map.get("transferredInTotalList");
			title="Total Patients Transferred In During the Quarter";
		}
		
		//===========================pediatric transferred in=============================================================
		//male 0-11
		if(linkId.equals("male0To11TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("male0To1TransfInDuringQterList");
			title="Pediatric (Male Aged 0 to 11) Transferred in During the Quarter";
		}
		//male 12-23
		if(linkId.equals("male12To23TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("male12To23TransfInDuringQterList");
			title="Pediatric(Male Aged 12 to 23) Transferred in During the Quarter";
		}
		
		//male 24-59
		if(linkId.equals("male24To59TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("male24To59TransfInDuringQterList");
			title="Pediatric(Male Aged 24 to 59) Transferred in During the Quarter";
		}
		
		//male 60-179
		if(linkId.equals("male60To179TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("male60To179TransfInDuringQterList");
			title="Pediatric(Male Aged 60 to 179) Transferred in During the Quarter";
		}
		
		//female 0-11
		if(linkId.equals("female0To11TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("female0To11TransfInDuringQterList");
			title="Pediatric(Female Aged 0 to 11) Transferred in During the Quarter";
		}
		
		//female 12-23
		if(linkId.equals("female12To23TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("female12To23TransfInDuringQterList");
			title="Pediatric(Female Aged 12 to 23) Transferred in During the Quarter";
		}
		
		//female 24-59
		if(linkId.equals("female24To59TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("female24To59TransfInDuringQterList");
			title="Pediatric(Female Aged 24 to 59) Transferred in During the Quarter";
		}
		
		//female 60-179
		if(linkId.equals("female60To179TransfInDuringQter")){
			currentList=(List<Object[]>) map.get("female60To179TransfInDuringQterList");
			title="Pediatric(Female Aged 60 to 179) Transferred in During the Quarter";
		}
		
		//_________________________________pregnant females__________________________________________________________
		if(linkId.equals("cumFemPregnantNewOnARTInTheBegOfQter")){
			currentList=(List<Object[]>) map.get("cumFemPregnantNewOnARTInTheBegOfQterList");
			title="Cumulative Pregnants Patients By the Beginning of the Quarter";
		}
		if(linkId.equals("newOnARTPregnant")){
			currentList=(List<Object[]>) map.get("newOnARTPregnantList");
			title="New Pregnant female during the quarter";
		}
		if(linkId.equals("pregnantFemTransfIn")){
			currentList=(List<Object[]>) map.get("pregnantFemTransfInList");
			title="Transferred In Pregnant female during the quarter";
			
		}
		if(linkId.equals("newOnARTPlusTransInPregnantFem")){
			currentList=(List<Object[]>) map.get("newOnARTPlusTransInPregnantFemList");
			title="New Pregnant female Plus  Transferred In during the quarter";
		}
		if(linkId.equals("cumPregnantFemEndQter")){
			currentList=(List<Object[]>) map.get("cumPregnantFemEndQterList");
			title="Cumulative Pregnant females By the End of the Quarter ";
		}
		
		//Nbre total de patients en ARV a la fin du trimestre		
		if(linkId.equals("male0To14Total")){
			currentList=(List<Object[]>) map.get("male0To14NotOnARTList");
			title="Total Active Male Patients(Aged 0 to 14) on ART By the End of the Quarter ";
		}
		if(linkId.equals("maleOver15Total")){
			currentList=(List<Object[]>) map.get("maleOver15NotOnARTList");
			title="Total Active Male Patients(Aged Over 15) on ART By the End of the Quarter ";
		}
		if(linkId.equals("female0To14Total")){
			currentList=(List<Object[]>) map.get("fem0to14NotAtARTList");
			title="Total Active Female Patients(Aged 0 to 14) on ART By the End of the Quarter ";
		}
		if(linkId.equals("femaleOver15Total")){
			currentList=(List<Object[]>) map.get("femaleOver15NotOnARTList");
			title="Total Active Female Patients(Aged Over 15) on ART By the End of the Quarter ";
		}
		if(linkId.equals("bigTotal")){
			currentList=(List<Object[]>) map.get("bigTotalList");
			title="Total Active Patients on ART By the End of the Quarter ";
		}
		// ====================2.1 (pediatric)=======================================================================
		// male 0-1
		if(linkId.equals("malePat01StartedByTheBegOfQter")){ // mariam
			currentList=(List<Object[]>) map.get("female5To14TransfInDuringQterList");
			title="Male Patients(Aged 0 to 1) on ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("malePat01StartedDuringTheQter")){
			currentList=(List<Object[]>) map.get("malePat01StartedDuringTheQterList");
			title="Male Patients(Aged 0 to 1) on ART During the Quarter ";
		}
		if(linkId.equals("malePat01StartedByTheEndOfQter")){
			currentList=(List<Object[]>) map.get("malePat01StartedByTheEndOfQterList");
			title="Male Patients(Aged 0 to 1) on ART By the End of the Quarter ";
		}
		if(linkId.equals("male0To1NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("male0To1NewOnARTDuringQterNoTransfList");
			title="Male Patients(Aged 0 to 1) on ART During the Quarter ------> Transferred In Not Considered ";
		}
		
		// male 2-4
		if(linkId.equals("malePat24StartedByTheBegOfQter")){
			currentList=(List<Object[]>) map.get("malePat24StartedByTheBegOfQterList");
			title="Male Patients(Aged 2 to 4) on ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("malePat24StartedByDuringQter")){
			currentList=(List<Object[]>) map.get("malePat24StartedByDuringQterList");
			title="Male Patients(Aged  to 1) on ART During the Quarter ";
		}
		if(linkId.equals("malePat24StartedByTheEnd")){
			currentList=(List<Object[]>) map.get("malePat24StartedByTheEndList");
			title="Male Patients(Aged 2 to 4) on ART By the End of the Quarter ";
		}
		if(linkId.equals("male2To4NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("male2To4NewOnARTDuringQterNoTransfList");
			title="Male Patients(Aged 2 to 4) on ART During the Quarter -------> Transferred In Not Considerred ";
		}
		
		//male 5-14
		if(linkId.equals("malePat5To14StartedByTheBegOfQter")){
			currentList=(List<Object[]>) map.get("malePat5To14StartedByTheBegOfQterList");
			title="Male Patients(Aged 5 to 14) Started ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("malePat5To14StartedByDuringQter")){
			currentList=(List<Object[]>) map.get("malePat5To14StartedByDuringQterList");
			title="Male Patients(Aged 5 to 14) Started ART During the Quarter ";
		}
		if(linkId.equals("malePat5To14StartedByTheEndOfQter")){
			currentList=(List<Object[]>) map.get("malePat5To14StartedByTheEndOfQterList");
			title="Male Patients(Aged 5 to 14) Started ART By the End of the Quarter ";
		}
		if(linkId.equals("male5To14NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("male5To14NewOnARTDuringQterNoTransfList");
			title="Male Patients(Aged 5 to 14) Stated ART During the Quarter -------> Transferred In Not Considerred ";
		}
		
		//female 0-1
		if(linkId.equals("femalePat0To1StartedByTheBegOfQter")){
			currentList=(List<Object[]>) map.get("femalePat01StartedByTheBegOfQterList");
			title="Female Patients(Aged 0 to 1) New on ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("femalePat0To1StartedDuringTheQter")){
			currentList=(List<Object[]>) map.get("femalePat01StartedDuringTheQterList");
			title="Female Patients(Aged 0 to 1) New on ART During the Quarter ";
		}
		if(linkId.equals("femalePat0To1StartedByTheEndOfQter")){
			currentList=(List<Object[]>) map.get("femalePat01StartedByTheEndOfQterList");
			title="Female Patients(Aged 0 to 1) New on ART By the End of the Quarter ";
		}
		if(linkId.equals("female0To1NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("female0To1NewOnARTDuringQterNoTransfList");
			title="Female Patients(Aged 0 to 1) New ART During the Quarter -------> Transferred In Not Considerred ";
		}
		
		//female 2-4
		if(linkId.equals("femalePat2To4StartedByTheBegOfQter")){
			currentList=(List<Object[]>) map.get("femalePat2To4StartedByTheBegOfQterList");
			title="Female Patients(Aged 2 to 4) Started ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("femalePat2To4StartedByDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat2To4StartedByDuringQterList");
			title="Female Patients(Aged 2 to 4) Stated ART During the Quarter ";
		}
		if(linkId.equals("femalePat2To4StartedByTheEnd")){
			currentList=(List<Object[]>) map.get("femalePat2To4StartedByTheEndList");
			title="Female Patients(Aged 2 to 4) Started ART By the End of the Quarter ";
		}
		if(linkId.equals("female2To4NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("female2To4NewOnARTDuringQterNoTransfList");
			title="Female Patients(Aged 2 to 4) New on ART During the Quarter -------> Transferred In Not Considerred ";
		}
		
		//female 5-14
		if(linkId.equals("fPat5To14StartedBegOfQter")){
			currentList=(List<Object[]>) map.get("femalePat5To14StartedByTheBegOfQterList");
			title="Female Patients(Aged 5 to 14) Started ART By the Beginning of the Quarter ";
		}
		if(linkId.equals("fPat5To14StartedDuringQter")){
			currentList=(List<Object[]>) map.get("femalePat5To14StartedByDuringQterList");
			title="Female Patients(Aged 5 to 14) Started ART During the Quarter ";
		}
		if(linkId.equals("fPat5To14StartedEndOfQter")){
			currentList=(List<Object[]>) map.get("fPat5To14StartedEndOfQterList");
			title="Female Patients(Aged 5 to 14) Started ART By the End of the Quarter ";
		}
		if(linkId.equals("female5To14NewOnARTDuringQterNoTransferIn")){
			currentList=(List<Object[]>) map.get("female5To14NewOnARTDuringQterNoTransfList");
			title="Female Patients(Aged 5 to 14) Started ART During the Quarter -------> Transferred In Not Considerred";
		}
		
		//________________________________________patients exited from care and lost to follow up_________________________________________________________________
		//adult
		// male and female aged 0-179 and >180
		//male 0-179
		if(linkId.equals("male0To179StoppedART")){
			currentList=(List<Object[]>) map.get("male0To179StoppedARTList");
			title = "Male Patients(Aged 0 to 179) stopped ART ";
		}
		if(linkId.equals("male0to179TransfOut")){
			currentList=(List<Object[]>) map.get("male0to179TransfOutList");
			title = "Male Patients(Aged 0 to 179) transferred Out ";
		}
		if(linkId.equals("male0to179Died")){
			currentList=(List<Object[]>) map.get("male0to179DiedList");
			title = "Male Patients(Aged 0 to 179) died ";
		}
		if(linkId.equals("male0To179Lost")){
			currentList=(List<Object[]>) map.get("male0To179LostList");
			title = "Male Patients(Aged 0 to 179) lost ";
		}
		
		//male >180
		if(linkId.equals("maleOver180Stopped")){
			currentList= (List<Object[]>) map.get("maleOver180StoppedList");
			title = "Male Patients(Aged Over 180) stopped ART ";
		}
		if(linkId.equals("maleOver180TransOut")){
			currentList=(List<Object[]>) map.get("maleOver180TransOutList");
			title = "Male Patients(Aged Over 180) transferred Out ";
		}
		if(linkId.equals("maleOver180Died")){
			currentList=(List<Object[]>) map.get("maleOver180DiedList");
			title = "Male Patients(Aged Over 180) died ";
		}
		if(linkId.equals("maleOver180Lost")){
			currentList=(List<Object[]>) map.get("maleOver180LostList");
			title = "Male Patients(Aged Over 180) lost ";
		}
		
		//female 0-179
		if(linkId.equals("female0To179StoppedART")){
			currentList=(List<Object[]>) map.get("female0To179StoppedARTList");
			title = "Female Patients(Aged 0 to 179) stopped ARV ";
		}
		if(linkId.equals("fema0to179TransfOut")){
			currentList=(List<Object[]>) map.get("fem0to179TransfOutList");
			title = "Female Patients(Aged 0 to 179) transferred Out ";
		}
		if(linkId.equals("fem0to179Died")){
			currentList=(List<Object[]>) map.get("fem0to179DiedList");
			title = "Female Patients(Aged 0 to 179) died ";
		}
		if(linkId.equals("fem0to179Lost")){
			currentList=(List<Object[]>) map.get("fem0to179LostList");
			title = "Female Patients(Aged 0 to 179) lost ";
		}
		
		//female >180
		if(linkId.equals("femaleOver180Stopped")){
			currentList=(List<Object[]>) map.get("femaleOver180StoppedList");
			title = "Female Patients(Aged Over 180) stopped ARVs ";
		}
		if(linkId.equals("femOver180TransOut")){
			currentList=(List<Object[]>) map.get("femOver180TransOutList");
			title = "Female Patients(Aged Over 180) transferred Out ";
		}
		if(linkId.equals("femOver180Died")){
			currentList=(List<Object[]>) map.get("femOver180DiedList");
			title = "Female Patients(Aged Over 180) Died ";
		}
		if(linkId.equals("femOver180Lost")){
			currentList=(List<Object[]>) map.get("femOver180LostList");
			title = "Female Patients(Aged Over 180) lost to follow up ";
		}
		
		//pediatric
		//male 0-11
		if(linkId.equals("male0To11StoppedARV")){
			currentList=(List<Object[]>) map.get("male0To11StoppedARVList");
			title = "Pediatric Male Patients(Aged 0 to 11) stopped ARVs ";
		}
		if(linkId.equals("male0to11TransfOut")){
			currentList=(List<Object[]>) map.get("male0to11TransfOutList");
			title = "Pediatric Male Patients(Aged 0 to 11) Transferred Out ";
		}
		if(linkId.equals("male0To11Died")){
			currentList=(List<Object[]>) map.get("male0To11DiedList");
			title = "Pediatric Male Patients(Aged 0 to 11) Died ";
		}
		if(linkId.equals("male0To11Lost")){
			currentList=(List<Object[]>) map.get("male0To11LostList");
			title = "Pediatric Male Patients(Aged 0 to 11) Lost to Follow up ";
		}
		
		//male 12-23
		if(linkId.equals("male12To23StoppedARV")){
			currentList=(List<Object[]>) map.get("male12To23StoppedARVList");
			title = "Pediatric Male Patients(Aged 12 to 23) Stopped ARVs ";
		}
		if(linkId.equals("male12to23TransfOut")){
			currentList=(List<Object[]>) map.get("male12to23TransfOutList");
			title = "Pediatric Male Patients(Aged 12 to 23) transferred Out ";
		}
		if(linkId.equals("male12To23Died")){
			currentList=(List<Object[]>) map.get("male12To23DiedList");
			title = "Pediatric Male Patients(Aged 12 to 23) Died ";
		}
		if(linkId.equals("male12To23Lost")){
			currentList=(List<Object[]>) map.get("male12To23LostList");
			title = "Pediatric Male Patients(Aged 12 to 23) Lost to Follow up ";
		}
		
		//male 24-59
		if(linkId.equals("male24To59StoppedARV")){
			currentList=(List<Object[]>) map.get("male24To59StoppedARVList");
			title = "Pediatric Male Patients(Aged 24 to 59) Stopped ARVs ";
		}
		if(linkId.equals("male24to59TransfOut")){
			currentList=(List<Object[]>) map.get("male24to59TransfOutList");
			title = "Pediatric Male Patients(Aged 24 to 59) transferred Out ";
		}
		if(linkId.equals("male24To59Died")){
			currentList=(List<Object[]>) map.get("male24To59DiedList");
			title = "Pediatric Male Patients(Aged 24 to 59) Died ";
		}
		if(linkId.equals("male24To59Lost")){
			currentList=(List<Object[]>) map.get("male24To59LostList");
			title = "Pediatric Male Patients(Aged 24 to 59) Lost to Follow up ";
		}
		
		
		// male 60-179
		if(linkId.equals("male60To179StoppedARV")){
			currentList=(List<Object[]>) map.get("male60To179StoppedARVList");
			title = "Male Patients(Aged 60 to 179) who have been transferred Out ";
		}
		if(linkId.equals("male60to179TransfOut")){
			currentList=(List<Object[]>) map.get("male60to179TransfOutList");
			title = "Male Patients(Aged 60 to 179) Transferred Out ";
		}
		if(linkId.equals("male60To179Died")){
			currentList=(List<Object[]>) map.get("male60To179DiedList");
			title = "Male Patients(Aged 60 to 179) Died ";
		}
		if(linkId.equals("male60To179Lost")){
			currentList=(List<Object[]>) map.get("male60To179LostList");
			title = "Male Patients(Aged 60 to 179) Lost To Follow Up ";
		}
		
		//female 0-11
		if(linkId.equals("female0To11StopARV")){
			currentList=(List<Object[]>) map.get("female0To11StopARVList");
			title = "Pediatric Female Patients(Aged 0 to 11) who Stopped ARVs ";
		}
		if(linkId.equals("fem0to11TransfOut")){
			currentList=(List<Object[]>) map.get("fem0to11TransfOutList");
			title = "Pediatric Female Patients(Aged 0 to 11) Transferred Out ";
		}
		if(linkId.equals("fem0To11Died")){
			currentList=(List<Object[]>) map.get("fem0To11DiedList");
			title = "Pediatric Female Patients(Aged 0 to 11) Died ";
		}
		if(linkId.equals("fem0To11Lost")){
			currentList=(List<Object[]>) map.get("fem0To11LostList");
			title = "Pediatric Female Patients(Aged 0 to 11) Lost To Follow Up ";
		}
		
		//female 12-23
		if(linkId.equals("female12To23StoppedARV")){
			currentList=(List<Object[]>) map.get("fem12To23StopARVList");
			title = "Pediatric Female Patients(Aged 12 to 23) Stopped ARVs ";
		}
		if(linkId.equals("fem12to23TransfOut")){
			currentList=(List<Object[]>) map.get("fem12to23TransfOutList");
			title = "Pediatric Femaale Patients(Aged 12 to 23) Transferred Out ";
		}
		if(linkId.equals("fem12To23Died")){
			currentList=(List<Object[]>) map.get("fem12To23DiedList");
			title = "Pediatric Female Patients(Aged 12 to 23) Died ";
		}
		if(linkId.equals("fem12To23Lost")){
			currentList=(List<Object[]>) map.get("fem12To23LostList");
			title = "Pediatric Female Patients(Aged 12 to 23) Lost To Follow Up ";
		}
		
		//female 24-59
		if(linkId.equals("fem24To59StopARV")){
			currentList=(List<Object[]>) map.get("fem24To59StopARVList");
			title = "Pediatric Female Patients(Aged 24 to 59) Stopped ARVs ";
		}
		if(linkId.equals("fem24to59TransfOut")){
			currentList=(List<Object[]>) map.get("fem24to59TransfOutList");
			title = "Pediatric Femaale Patients(Aged 24 to 59) Transferred Out ";
		}
		if(linkId.equals("fem24To59Died")){
			currentList=(List<Object[]>) map.get("fem24To59DiedList");
			title = "Pediatric Female Patients(Aged 24 to 59) Died ";
		}
		if(linkId.equals("fem24To59Lost")){
			currentList=(List<Object[]>) map.get("fem24To59LostList");
			title = "Pediatric Female Patients(Aged 24 to 59) Lost To Follow Up ";
		}
		
		// female 60-179
		if(linkId.equals("fem60To179StopARV")){
			currentList=(List<Object[]>) map.get("fem60To179StopARVList");
			title = "Pediatric Female Patients(Aged 60 to 179) Stopped ARVs ";
		}
		if(linkId.equals("fem60to179TransfOut")){
			currentList=(List<Object[]>) map.get("fem60to179TransfOutList");
			title = "Pediatric Female Patients(Aged 60 to 179) Transferred Out ";
		}
		if(linkId.equals("fem60To179Died")){
			currentList=(List<Object[]>) map.get("fem60To179DiedList");
			title = "Pediatric Female Patients(Aged 60 to 179) Died";
		}
		if(linkId.equals("fem60To179Lost")){
			currentList=(List<Object[]>) map.get("fem60To179LostList");
			title = "Pediatric Female Patients(Aged 60 to 179) Lost To Follow Up ";
		}
		
		//==============================Total of patients stopped ARV/Transferred out/Died/Lost
		
		//adult
		if(linkId.equals("male0To179NotOnARTTotal")){
			currentList=(List<Object[]>) map.get("male0To179NotOnARTList");
			title = "Total Pediatric Patients(Male Aged 0 to 179) who were NOT on ART at the end of the quarter  ";
		}
		if(linkId.equals("maleOver180NotOnARTTotal")){
			currentList=(List<Object[]>) map.get("maleOver180NotOnARTList");
			title = "Total Pediatric Patients(Male Aged over 180) who were NOT on ART at the end of the quarter  ";
		}
		
		if(linkId.equals("female0To179NotOnARTTotal")){
			currentList=(List<Object[]>) map.get("fem0to179NotAtARTList");
			title = "Total Pediatric Patients(Female Aged 0 to 179) who were NOT on ART at the end of the quarter  ";
		}
		
		if(linkId.equals("femOver180NotOnARTTotal")){
			currentList=(List<Object[]>) map.get("femOver180NotOnARTList");
			title = "Total Pediatric Patients(Female Aged over 180) who were NOT on ART at the end of the quarter  ";
		}
		//pediatric
		//male
		if(linkId.equals("male0To11NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("male0To11NotOnARVTotalList");
			title = "Total Pediatric Patients(Male Aged 0 to 11) who were NOT on ART at the end of the quarter  ";
		}
		if(linkId.equals("male12To23NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("male12to23NotOnARVTotalList");
			title = "Total Pediatric Patients(Male Aged 12 to 23) who were NOT on ART at the end of the quarter  ";
		}
		
		if(linkId.equals("male24To59NotOnARVTTotal")){
			currentList=(List<Object[]>) map.get("male24To59NotOnARVTTotalList");
			title = "Total Pediatric Patients(Male Aged 24 to 59) who were NOT on ART at the end of the quarter  ";
		}
		
		if(linkId.equals("male60To179NotOnARVTTotal")){
			currentList=(List<Object[]>) map.get("male60To179NotOnARVTTotalList");
			title = "Total Pediatric Patients(Male Aged 60 to 179) who were NOT on ART at the end of the quarter  ";
		}
		//female
		if(linkId.equals("female0To11NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("female0To11NotOnARVTotalList");
			title = "Total Pediatric Patients(Female Aged 0 to 11) who were NOT on ART at the end of the quarter  ";
		}
		if(linkId.equals("female12To23NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("female12To23NotOnARVTotalList");
			title = "Total Pediatric Patients(Female Aged 12 to 23) who were NOT on ART at the end of the quarter  ";
		}
		if(linkId.equals("female24To59NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("fem24To59NotOnARVTotalList");
			title = "Total Pediatric Patients(Female Aged 24 to 59) who were NOT on ART at the end of the quarter  ";
		}
		if(linkId.equals("female60To179NotOnARVTotal")){
			currentList=(List<Object[]>) map.get("female60To179NotOnARVTotalList");
			title = "Total Pediatric Patients(Female Aged 60 to 179) who were NOT on ART at the end of the quarter  ";
		}
		
		//==========================total of stopped ART===================================
		if(linkId.equals("totalStoppedART")){
			currentList=(List<Object[]>) map.get("totalStoppedARTList");
			title = "Total Patients who Stopped ART at the end of the quarter  ";
		}
		if(linkId.equals("totalTransfOut")){
			currentList=(List<Object[]>) map.get("totalTransfOutList");
			title = "Total Patients Transferred Out at the end of the quarter  ";
		}
		if(linkId.equals("totalDied")){
			currentList=(List<Object[]>) map.get("totalDiedList");
			title = "Total Patients who Died at the end of the quarter  ";
		}
		if(linkId.equals("totalLost")){
			currentList=(List<Object[]>) map.get("totalLostList");
			title = "Total Patients lost at the end of the quarter  ";
		}
		
		
		//_____________________________________ table 4 _____________________________________________________________
		//=======================================6 month ago=========================================================
		if(linkId.equals("patientsInCohortBaselineSix")){
			currentList=(List<Object[]>) map.get("patientsInCohortBaselineSixList");
			title = "All Patients On ART for 6 Months Ago  ";
		}
		
		if(linkId.equals("patients6MonthsAgoWithCD4BaselineValue")){
			currentList=(List<Object[]>) map.get("patients6MonthsAgoWithCD4BaselineValueList");
			title = "No. in cohort who have CD4 count [CD4 M0 between 3 months BEFORE to 2 weeks AFTER starting ARV]  ";
		}
		
		if(linkId.equals("patientsInCohortAt6thMonth")){
			currentList=(List<Object[]>) map.get("patientsInCohortAt6thMonthList");
			title = "Active Patients On ART for 6 Months Ago  ";
		}
		
		if(linkId.equals("patientsAt6thMonthWithCD4")) {
			currentList=(List<Object[]>) map.get("patientsAt6thMonthWithCD4List");
			title = "No. in cohort who have CD4 count[CD4 M6 between 4 months to 7 months AFTER starting ARV] ";
		}
		
		//=======================================12 month ago===================================================================
		if(linkId.equals("patientsInCohortBaselineTwelve")){
			currentList=(List<Object[]>) map.get("patientsInCohortBaselineTwelveList");
			title = "All Patients On ART for 12 Month Ago  ";
		}
		
		if(linkId.equals("patients12MonthsAgoWithCD4BaselineValue")){
			currentList=(List<Object[]>) map.get("patients12MonthsAgoWithCD4BaselineValueList");
			title = "No. in cohort who have CD4 count [CD4 M12 between 8 to 13 months AFTER starting ARV]  ";
		}
		
		
		if(linkId.equals("patientsInCohortAt12thMonth")){
			currentList=(List<Object[]>) map.get("patientsInCohortAt12thMonthList");
			title = "Active Patients On ART for 12 Month Ago[CD4 M12 between 8 to 13 months AFTER starting ARV]  ";
		}
		
		if(linkId.equals("patientsAt12thMonthWithCD4")){
			currentList=(List<Object[]>) map.get("patientsAt12thMonthWithCD4List");
			title = "Active Patients On ART for 12 Month Ago who did CD4 Count Test  ";
		}
		//________________________________patients who received ARVs for x out of x month___________________________________________
		if(linkId.equals("nberPatReceivedARVsFor6OutOf6Month")){
			currentList=(List<Object[]>) map.get("patWhoReceivedARVsFor6OutOf6MonthList");
			
			title = "No. in cohort who received ARVs for 6 out of 6 months  ";
		}
		
		if(linkId.equals("nberPatReceivedARVsFor12OutOf12Month")){
			currentList=(List<Object[]>) map.get("patWhoReceivedARVsFor12OutOf2MonthList");
			title = "No.of persons in cohort who received ARVs for 12 out of 12 months  ";
		}
		
		
		//_____________________________________table 5___get patients buy regimen__________________________________________________
		QuarterlyReportingService service = Context.getService(QuarterlyReportingService.class);
		String index="";
		if(request.getParameter("index")!=null){
			index =request.getParameter("index") ;
		    currentList = (List<Object[]>) map.get(index);
		    char regimenIndex = 0;
		    regimenIndex=index.toString().charAt(9);
		    String str = Character.toString(regimenIndex);
//		    title = "Patients Taking ...... "+service.getRegimenNameById(Integer.parseInt(str));
		    title = "Patients Taking ...... ";
		}
		
		if(linkId.equals("totOnReg")){
//			log.info("+++++++++++++++++++++++totOnReg== "+linkId);
			currentList=(List<Object[]>) map.get("totalPatientsOnReg");
		}
		
		//_______________________________________get patients from their ids_________________________________________________________

		List<Object[]> objects = new ArrayList<Object[]>();
		List<Patient> patients = new ArrayList<Patient>();
		for (Object[] ob : currentList) {
			//patientsFromId.add(id[0]);
			Patient patient = Context.getPatientService().getPatient((Integer) ob[0]);
			
			 if(ob.length==3){
				 if(ob[2]!=null)
				 dateTitle = ob[2].toString();
				 
				 objects.add(new Object[]{patient,ob[1]});
			}
			if(ob.length==6){
				
				if(ob[2]!=null)
				dateTitle = ob[2].toString();
				cd4Title=ob[3].toString();
				
				//add in objects patient,arv start date,cd4 value and obs date
				//see how the patientsAt6thMonthWithCD4List in QuarterlyReportFormController looks like
				objects.add(new Object[]{patient,ob[1],ob[4],ob[5]});
				
			}
	
			patients.add(patient);
		}
		
		map.put("columnTitle", dateTitle);
		map.put("cd4Title", cd4Title);
		map.put("objects", objects);
		map.put("objectsSize", objects.size());
		
		//get quarter from the main controller (in map there)
		map.put("quarterBegin", map.get("quarterBegin"));
		map.put("quarterEnd", map.get("quarterEnd"));
		map.put("title", title);
		
		
		
		//___________________________create session witch will help to get the patients list while exportation________________________
		request.getSession().setAttribute("patients",patients);
		
		return new ModelAndView(getViewName(), map);
	}

}
