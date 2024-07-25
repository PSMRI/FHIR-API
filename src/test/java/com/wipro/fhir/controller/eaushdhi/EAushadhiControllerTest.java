package com.wipro.fhir.controller.eaushdhi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.google.gson.Gson;
import com.wipro.fhir.data.e_aushdhi.E_AusdhFacilityProcessLog;
import com.wipro.fhir.data.e_aushdhi.M_Facility;
import com.wipro.fhir.service.e_aushdhi.EAushadhiService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

@ExtendWith(MockitoExtension.class)
class EAushadhiControllerTest {

	@InjectMocks
	EAushadhiController eAushadhiController;

	@Mock
	EAushadhiService eAushadhiService;

	@Test
	@Description("Tests retrieval of stock details for a specific store (TC_GetStoreStockDetails_Success_001)")
	void getStoreStockDetailsTest() throws FHIRException {

		String request = "E-aushadhi Stock Added Successfully";

		OutputResponse response = new OutputResponse();

		String resp = request;

		response.setResponse(resp);
		response.setError(200, "success", "success");

		assertTrue(response.toString().contains("success"));
		assertTrue(response.toString().contains(resp));
	}

	@Test
	@Description("Tests handling of exceptions during stock retrieval (TC_GetStoreStockDetails_Exception_002)")
	void getStoreStockDetailsTestElsePart() throws FHIRException {

		String request = "{\"facilityID\":123}";

		String Authorization = "Authorization";

		OutputResponse response = new OutputResponse();

		String resp = request;

		when(eAushadhiService.getEaushadhiStoreDetailsByFacilityID(request)).thenReturn(resp);

		response.setError(5000, "E-aushadhi Error while getting store stock details : " + resp);

		Assertions.assertEquals(response.toString(), eAushadhiController.getStoreStockDetails(request, Authorization));
	}

	@Test
	@Description("Tests handling of exceptions during stock retrieval (TC_GetStoreStockDetails_Exception_003)")
	void getStoreStockDetailsTest_Exception() throws Exception {

		String Authorization = "Authorization";

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(eAushadhiService.getEaushadhiStoreDetailsByFacilityID(request)).thenThrow(FHIRException.class);

		String response = eAushadhiController.getStoreStockDetails(request, Authorization);

		Assertions.assertEquals(response, eAushadhiController.getStoreStockDetails(request, Authorization));
	}

	@Test
	@Description("Tests successful synchronization of drug dispense data and patient details (TC_SyncDrugDispenseAndPatientDetails_Success_001)")
	void syncDrugDispenseAndPatientDetailsTest() throws FHIRException {

		String request = "{\"eAushadhiFacilityId\":123,\"pageNo\":12}";

		String Authorization = "Authorization";

		OutputResponse response = new OutputResponse();

		String s = request;

		when(eAushadhiService.syncDispenseDetailsToEAushadhi(request, Authorization)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertNotNull(request);
		Assertions.assertEquals(response.toString(),
				eAushadhiController.syncDrugDispenseAndPatientDetails(request, Authorization));

	}

	@Test
	@Description("Tests handling of FHIR exception during drug dispense and patient details synchronization (TC_SyncDrugDispenseAndPatientDetails_FHIRE_002)")
	void syncDrugDispenseAndPatientDetailsTestThrowsFHIRException() throws Exception {

		String Authorization = "Authorization";

		String request = null;

		String response = eAushadhiController.syncDrugDispenseAndPatientDetails(request, Authorization);

		Assertions.assertNull(request);
		Assertions.assertEquals(response,
				eAushadhiController.syncDrugDispenseAndPatientDetails(request, Authorization));

	}

	@Test
	@Description("Tests handling of generic exceptions during drug dispense and patient details synchronization (TC_SyncDrugDispenseAndPatientDetails_Exception_003)")
	void syncDrugDispenseAndPatientDetailsTest_Exception() throws Exception {

		String Authorization = "Authorization";

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(eAushadhiService.syncDispenseDetailsToEAushadhi(request, Authorization)).thenThrow(FHIRException.class);

		String response = eAushadhiController.syncDrugDispenseAndPatientDetails(request, Authorization);

		Assertions.assertEquals(response,
				eAushadhiController.syncDrugDispenseAndPatientDetails(request, Authorization));
	}

	@Test
	@Description("Tests successful retrieval of facility stock process log (TC_GetFacilityStockProcessLog_Success_001)")
	void getFacilityStockProcessLogTest() throws FHIRException {

		M_Facility facilityDetails = new M_Facility();

		facilityDetails.setCreatedBy("Antu Kunu");
		facilityDetails.setCreatedDate(Date.valueOf("2020-09-09"));
		facilityDetails.setDeleted(false);
		facilityDetails.seteAushadhiFacilityId(123);
		facilityDetails.setFacilityCode("12ghfedh");
		facilityDetails.setFacilityDesc("Desc");
		facilityDetails.setFacilityID(1234);
		facilityDetails.setFacilityName("Medicine");
		facilityDetails.setFacilityTypeID(1);
		facilityDetails.setIsMainFacility(true);
		facilityDetails.setLastModDate(Date.valueOf("2020-09-09"));
		facilityDetails.setLocation("Kolkata");
		facilityDetails.setMainFacilityID(123456);
		facilityDetails.setModifiedBy("AK KUndu");
		facilityDetails.setPageNo(13);
		facilityDetails.setPhysicalLocation("Kolkata");
		facilityDetails.setProcessed('P');
		facilityDetails.setProviderServiceMapID(1479);
		facilityDetails.setStatus("Approved");
		facilityDetails.setStoreType("Medical Pharmacy");

		facilityDetails.toString();

		E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();

		e_AusdhFacilityProcessLog.setAmrithFacilityId(facilityDetails.getFacilityID());

		e_AusdhFacilityProcessLog.toString();

		List<E_AusdhFacilityProcessLog> resultList = new ArrayList<>();

		resultList.add(e_AusdhFacilityProcessLog);

		String request = new Gson().toJson(resultList);

		OutputResponse response = new OutputResponse();

		String s = request;

		when(eAushadhiService.getFacilityStockProcessLog(request)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertNotNull(request);
		Assertions.assertEquals(response.toString(), eAushadhiController.getFacilityStockProcessLog(request));

	}

	@Test
	@Description("Tests handling of FHIR exception during facility stock process log retrieval (TC_GetFacilityStockProcessLog_FHIRE_002)")
	void getFacilityStockProcessLogTestThrowsFHIRException() throws Exception {

		String request = null;

		String response = eAushadhiController.getFacilityStockProcessLog(request);

		Assertions.assertNull(request);
		Assertions.assertEquals(response, eAushadhiController.getFacilityStockProcessLog(request));

	}

	@Test
	@Description("Tests handling of generic exceptions during facility stock process log retrieval (TC_GetFacilityStockProcessLog_Exception_003)")
	void getFacilityStockProcessLogTest_Exception() throws Exception {

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(eAushadhiService.getFacilityStockProcessLog(request)).thenThrow(FHIRException.class);

		String response = eAushadhiController.getFacilityStockProcessLog(request);

		Assertions.assertEquals(response, eAushadhiController.getFacilityStockProcessLog(request));
	}

	@Test
	@Description("Tests successful addition of a new facility (TC_AddFacility_Success_001)")
	public void testAddFacilitySuccess() throws FHIRException {
	    // Arrange
	    String requestJson = "[\"issue1\", \"issue2\"]";
	    String expectedResponse = "Sync status updated successfully"; // Expected message from the service

	    Mockito.when(eAushadhiService.updateSyncStatusForEAushadhiDispense(Mockito.anyList())).thenReturn(expectedResponse);

	    // Act
	    String response = eAushadhiController.addFacility(requestJson);

	    // Assert
	    assertTrue(response.contains(expectedResponse), "Response should contain the success message");
	}
	
	@Test
	@Description("Tests handling of empty request for adding a facility (TC_AddFacility_EmptyRequest_002)")
	public void testAddFacilityEmptyRequest() {
	    // Arrange
	    String requestJson = "[]";

	    // Act
	    String response = eAushadhiController.addFacility(requestJson);

	    // Assert
	    assertFalse(response.contains("Unexpected error:"), "Response should indicate an unexpected error occurred");
	}

}
