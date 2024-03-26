package com.wipro.fhir.controller.carecontext;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.service.care_context.CareContextService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

@ExtendWith(MockitoExtension.class)
class CareContextControllerTest {

	@InjectMocks
	CareContextController careContextController;

	@Mock
	CareContextService careContextService;

	@Test
	void generateOTPTest() throws FHIRException {

		OutputResponse response = new OutputResponse();

		String Authorization = "Authorization";

		String request = "{\"healthID\":\"H123\",\"authenticationMode\":\"Authenticated\",\"healthIdNumber\":\"H123Elth\"}";

		String s = request;

		when(careContextService.generateOTPForCareContext(request)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertNotNull(request);
		Assertions.assertEquals(response.toString(), careContextController.generateOTP(request, Authorization));
	}

	@Test
	void testGenerateOTP_CareContextServiceThrowsFHIRException() throws Exception {

		String Authorization = "Authorization";

		String request = null;

		String response = careContextController.generateOTP(request, Authorization);

		Assertions.assertNull(request);
		Assertions.assertEquals(response, careContextController.generateOTP(request, Authorization));

	}

	@Test
	void testGenerateOTP_Exception() throws Exception {

		String Authorization = "Authorization";

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(careContextService.generateOTPForCareContext(request)).thenThrow(FHIRException.class);

		String response = careContextController.generateOTP(request, Authorization);

		Assertions.assertEquals(response, careContextController.generateOTP(request, Authorization));
	}

	@Test
	void validateOTPAndCreateCareContextTest() throws FHIRException {

		OutputResponse response = new OutputResponse();

		String request = "{\"healthID\":\"H123\",\"visitCode\":\"H123\",\"beneficiaryID\":\"H123\",\"beneficiaryRegID\":\"H123\",\"otp\":\"H123\",\"txnId\":\"H123\",\"visitcategory\":\"H123\",\"healthIdNumber\":\"Hy23654Eesrdr\"}";

		String Authorization = "Authorization";

		String s = request;

		when(careContextService.validateOTPAndCreateCareContext(request)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertNotNull(request);
		Assertions.assertEquals(response.toString(),
				careContextController.validateOTPAndCreateCareContext(request, Authorization));

	}

	@Test
	void testValidateOTPAndCreateCareContextThrowsFHIRException() throws Exception {

		String Authorization = "Authorization";

		String request = null;

		String response = careContextController.validateOTPAndCreateCareContext(request, Authorization);

		Assertions.assertNull(request);
		Assertions.assertEquals(response,
				careContextController.validateOTPAndCreateCareContext(request, Authorization));

	}

	@Test
	void testValidateOTPAndCreateCareContext_Exception() throws Exception {

		String Authorization = "Authorization";

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(careContextService.validateOTPAndCreateCareContext(request)).thenThrow(FHIRException.class);

		String response = careContextController.validateOTPAndCreateCareContext(request, Authorization);

		Assertions.assertEquals(response,
				careContextController.validateOTPAndCreateCareContext(request, Authorization));
	}

	@Test
	void saveCareContextToMongoTest() throws FHIRException {

		String Authorization = "Authorization";

		String request = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		String response = careContextController.saveCareContextToMongo(request, Authorization);

		Assertions.assertEquals(response,
				careContextController.saveCareContextToMongo(request, Authorization));

	}

}
