package com.wipro.fhir.service.resource_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import com.wipro.fhir.data.patient.PatientDemographic;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.common.CommonServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PatientResource.class, ResourceRequestHandler.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PatientResourceDiffblueTest {
    @MockBean
    private APIChannel aPIChannel;

    @MockBean
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @MockBean
    private CommonServiceImpl commonServiceImpl;

    @MockBean
    private PatientDemographic patientDemographic;

    @MockBean
    private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;

    @Autowired
    private PatientResource patientResource;

    @Autowired
    private ResourceRequestHandler resourceRequestHandler;

    /**
     * Method under test:
     * {@link PatientResource#getPatientResource(ResourceRequestHandler)}
     */
    @Test
    void testGetPatientResource() throws FHIRException {
        // Arrange
        when(commonServiceImpl.getUUID()).thenReturn("01234567-89AB-CDEF-FEDC-BA9876543210");
        when(patientDemographic.getGenderID()).thenReturn(1);
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.getTime()).thenReturn(10L);
        when(patientDemographic.getGenderID()).thenReturn(1);
        when(patientDemographic.getBeneficiaryID()).thenReturn(1L);
        when(patientDemographic.getBeneficiaryRegID()).thenReturn(1L);
        when(patientDemographic.getGender()).thenReturn("Gender");
        when(patientDemographic.getHealthIdNo()).thenReturn("Health Id No");
        when(patientDemographic.getName()).thenReturn("Name");
        when(patientDemographic.getDOB()).thenReturn(timestamp);
        when(patientDemographic.getPatientDemographic(Mockito.<List<Object[]>>any())).thenReturn(patientDemographic);
        when(patientEligibleForResourceCreationRepo.callPatientDemographicSP(Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        // Act
        Patient actualPatientResource = patientResource.getPatientResource(resourceRequestHandler);

        // Assert
        verify(patientDemographic).getBeneficiaryID();
        verify(patientDemographic).getBeneficiaryRegID();
        verify(patientDemographic, atLeast(1)).getDOB();
        verify(patientDemographic, atLeast(1)).getGender();
        verify(patientDemographic, atLeast(1)).getHealthIdNo();
        verify(patientDemographic, atLeast(1)).getName();
        verify(patientDemographic).getPatientDemographic(Mockito.<List<Object[]>>any());
        verify(patientEligibleForResourceCreationRepo).callPatientDemographicSP(Mockito.<Long>any());
        verify(commonServiceImpl).getUUID();
        verify(timestamp, atLeast(1)).getTime();
        IdType idElement = actualPatientResource.getIdElement();
        assertEquals("1", idElement.getIdPart());
        HumanName nameFirstRep = actualPatientResource.getNameFirstRep();
        assertEquals("Name", nameFirstRep.getText());
        StringType textElement = nameFirstRep.getTextElement();
        assertEquals("Name", textElement.getValue());
        assertEquals("Name", textElement.getValueAsString());
        assertEquals("Patient", idElement.getResourceType());
        assertEquals("Patient/1", idElement.getValue());
        assertEquals("Patient/1", actualPatientResource.getId());
        Enumeration<Enumerations.AdministrativeGender> genderElement = actualPatientResource.getGenderElement();
        assertEquals("http://hl7.org/fhir/administrative-gender", genderElement.toSystem());
        assertEquals("unknown", genderElement.getCode());
        assertNull(idElement.getBaseUrl());
        assertNull(idElement.getVersionIdPart());
        assertEquals(1, actualPatientResource.getIdentifier().size());
        assertEquals(1, actualPatientResource.getName().size());
        DateType birthDateElement = actualPatientResource.getBirthDateElement();
        assertEquals(10, birthDateElement.getMillis().intValue());
        assertEquals(10000000L, birthDateElement.getNanos().longValue());
        assertEquals(TemporalPrecisionEnum.DAY, birthDateElement.getPrecision());
        assertEquals(Enumerations.AdministrativeGender.UNKNOWN, actualPatientResource.getGender());
        assertEquals(Enumerations.AdministrativeGender.UNKNOWN, genderElement.getValue());
        assertFalse(birthDateElement.isTimeZoneZulu());
        assertTrue(actualPatientResource.getIdentifierFirstRep().hasType());
        assertTrue(actualPatientResource.hasBirthDate());
        assertTrue(birthDateElement.hasPrimitiveValue());
    }

    /**
     * Method under test:
     * {@link PatientResource#getPatientResource(ResourceRequestHandler)}
     */
    @Test
    void testGetPatientResource2() throws FHIRException {
        // Arrange
        when(patientDemographic.getBeneficiaryID()).thenReturn(1L);
        when(patientDemographic.getGender()).thenReturn("Gender");
        when(patientDemographic.getHealthIdNo()).thenReturn("Health Id No");
        when(patientDemographic.getName()).thenReturn("Name");
        when(patientDemographic.getDOB()).thenReturn(mock(Timestamp.class));
        when(patientDemographic.getGenderID()).thenReturn(1);
        when(patientDemographic.getGenderID()).thenReturn(1);
        when(patientDemographic.getBeneficiaryID()).thenReturn(1L);
        when(patientDemographic.getBeneficiaryRegID()).thenReturn(null);
        when(patientDemographic.getGender()).thenReturn("Gender");
        when(patientDemographic.getHealthIdNo()).thenReturn("Health Id No");
        when(patientDemographic.getName()).thenReturn("Name");
        when(patientDemographic.getDOB()).thenReturn(mock(Timestamp.class));
        when(patientDemographic.getPatientDemographic(Mockito.<List<Object[]>>any())).thenReturn(patientDemographic);
        when(patientEligibleForResourceCreationRepo.callPatientDemographicSP(Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(FHIRException.class, () -> patientResource.getPatientResource(resourceRequestHandler));
    }
}
