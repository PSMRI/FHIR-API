package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.gson.Gson;
import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class HealthIDServiceImplTest {
	@Mock
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@InjectMocks
	private HealthIDServiceImpl healthIDServiceImpl;

	@Mock
	private HttpUtils httpUtils;

	@Mock
	private Gson gson;

	@Test
    public void testMapHealthIDToBeneficiary_SuccessWithBeneficiaryID() throws FHIRException {
        // Given
        BenHealthIDMapping healthIDMapping = new BenHealthIDMapping();
        healthIDMapping.setBeneficiaryID(456L);
        String request = gson.toJson(healthIDMapping);
        Long beneficiaryRegId = 123L;
        BenHealthIDMapping savedHealthIDMapping = new BenHealthIDMapping();
        savedHealthIDMapping.setBeneficiaryID(456L);
        savedHealthIDMapping.setBeneficiaryRegId(123L);

        // Mock the repository behavior
        when(benHealthIDMappingRepo.getBenRegID(456L)).thenReturn(beneficiaryRegId);
        when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenReturn(savedHealthIDMapping);

        // Call the method
        String result = healthIDServiceImpl.mapHealthIDToBeneficiary(request);

        // Verify interactions
        verify(benHealthIDMappingRepo).getBenRegID(456L);
        verify(benHealthIDMappingRepo).save(any(BenHealthIDMapping.class));

        // Then
        String expectedResponse = gson.toJson(savedHealthIDMapping);
        assertEquals(expectedResponse, result);
    }

	@Test
    public void testMapHealthIDToBeneficiary_Failure_NoBeneficiaryIDOrRegId() {
        // Given
        BenHealthIDMapping healthIDMapping = new BenHealthIDMapping();
        String request = gson.toJson(healthIDMapping);

        // When / Then
        FHIRException exception = assertThrows(FHIRException.class, () -> {
            healthIDServiceImpl.mapHealthIDToBeneficiary(request);
        });
        assertEquals("Error in mapping request", exception.getMessage());
    }

	@Test
	public void testMapHealthIDToBeneficiary_ExceptionHandling() throws FHIRException {
		// Given
		BenHealthIDMapping healthIDMapping = new BenHealthIDMapping();
		healthIDMapping.setBeneficiaryID(456L);
		String request = gson.toJson(healthIDMapping);
		Long beneficiaryRegId = 123L;

		// When
		when(benHealthIDMappingRepo.getBenRegID(456L)).thenReturn(beneficiaryRegId);
		when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenThrow(new RuntimeException("Save Error"));

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthIDServiceImpl.mapHealthIDToBeneficiary(request);
		});

		// Then
		assertEquals("Error in saving data", exception.getMessage());
	}

	@Test
	void testMapHealthIDToBeneficiary() throws FHIRException {
		// Arrange, Act and Assert
		assertThrows(FHIRException.class, () -> healthIDServiceImpl.mapHealthIDToBeneficiary(""));
	}

	@Test
	void testGetBenHealthID() {
		// Arrange
		when(benHealthIDMappingRepo.getHealthDetails(Mockito.<Long>any())).thenReturn(new ArrayList<>());

		// Act
		String actualBenHealthID = healthIDServiceImpl.getBenHealthID(1L);

		// Assert
		verify(benHealthIDMappingRepo).getHealthDetails(Mockito.<Long>any());
		assertEquals("{BenHealthDetails=[]}", actualBenHealthID);
	}
}
