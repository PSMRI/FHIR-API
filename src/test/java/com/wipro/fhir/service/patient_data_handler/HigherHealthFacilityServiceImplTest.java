package com.wipro.fhir.service.patient_data_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.atoms.feed.bahmni.encounter.BahmniDiagnoses;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.DrugOrders;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.GroupMembers;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.Observations;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.Providers;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.VisitWiseEncounterData;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HigherHealthFacilityServiceImplTest {
    @Mock
    private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

    @InjectMocks
    private HigherHealthFacilityServiceImpl higherHealthFacilityServiceImpl;

    @Mock
    private PatientDemographicModel_NDHM_Patient_Profile_Repo patientDemographicModel_NDHM_Patient_Profile_Repo;

    @Autowired
    private ResourceRequestHandler resourceRequestHandler;

    @Test
    void testUpdateBengenIDToHigherHealthFacilityBeneficiary() throws FHIRException {
        // Arrange
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByExternalId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        when(patientDemographicModel_NDHM_Patient_Profile_Repo
                .saveAll(Mockito.<Iterable<PatientDemographicModel_NDHM_Patient_Profile>>any())).thenReturn(new ArrayList<>());

        // Act
        String actualUpdateBengenIDToHigherHealthFacilityBeneficiaryResult = higherHealthFacilityServiceImpl
                .updateBengenIDToHigherHealthFacilityBeneficiary(resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByExternalId(isNull());
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo)
                .saveAll(Mockito.<Iterable<PatientDemographicModel_NDHM_Patient_Profile>>any());
        assertEquals("Beneficiary ID updated successfully", actualUpdateBengenIDToHigherHealthFacilityBeneficiaryResult);
    }

    @Test
    void testGetCLinicalDataHigherhealthFacility() throws FHIRException {
        // Arrange
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        // Act
        String actualCLinicalDataHigherhealthFacility = higherHealthFacilityServiceImpl
                .getCLinicalDataHigherhealthFacility(resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(isNull());
        assertEquals("[]", actualCLinicalDataHigherhealthFacility);
    }

    @Test
    void testGetCLinicalDataHigherhealthFacility2() throws FHIRException {
        // Arrange
        when(encounterFullRepresentationRepo.findByPatientId(Mockito.<String>any())).thenReturn(new ArrayList<>());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile.setId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile = patientDemographicModel_NDHM_Patient_Profile.new Profile();
        profile.setHipCode("Hip Code");
        profile.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile2 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile2.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile2.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile2.setId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setProfile(profile);
        patientDemographicModel_NDHM_Patient_Profile2.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile2 = patientDemographicModel_NDHM_Patient_Profile2.new Profile();
        profile2.setHipCode("Hip Code");
        profile2.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile3 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile3.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile3.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile3.setId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setProfile(profile2);
        patientDemographicModel_NDHM_Patient_Profile3.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile3 = patientDemographicModel_NDHM_Patient_Profile3.new Profile();
        profile3.setHipCode("Hip Code");
        profile3.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile4 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile4.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile4.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile4.setId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setProfile(profile3);
        patientDemographicModel_NDHM_Patient_Profile4.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setTriggerTableAIId(1L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> patientDemographicModel_NDHM_Patient_ProfileList = new ArrayList<>();
        patientDemographicModel_NDHM_Patient_ProfileList.add(patientDemographicModel_NDHM_Patient_Profile4);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(patientDemographicModel_NDHM_Patient_ProfileList);

        // Act
        String actualCLinicalDataHigherhealthFacility = higherHealthFacilityServiceImpl
                .getCLinicalDataHigherhealthFacility(resourceRequestHandler);

        // Assert
        verify(encounterFullRepresentationRepo).findByPatientId(eq("42"));
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(isNull());
        assertEquals("[]", actualCLinicalDataHigherhealthFacility);
    }

    @Test
    void testGetCLinicalDataHigherhealthFacility3() throws FHIRException {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        ArrayList<BahmniDiagnoses> bahmniDiagnoses = new ArrayList<>();
        encounterFullRepresentation.setBahmniDiagnoses(bahmniDiagnoses);
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> encounterFullRepresentationList = new ArrayList<>();
        encounterFullRepresentationList.add(encounterFullRepresentation);
        when(encounterFullRepresentationRepo.findByPatientId(Mockito.<String>any()))
                .thenReturn(encounterFullRepresentationList);

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile.setId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile = patientDemographicModel_NDHM_Patient_Profile.new Profile();
        profile.setHipCode("Hip Code");
        profile.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile2 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile2.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile2.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile2.setId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setProfile(profile);
        patientDemographicModel_NDHM_Patient_Profile2.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile2 = patientDemographicModel_NDHM_Patient_Profile2.new Profile();
        profile2.setHipCode("Hip Code");
        profile2.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile3 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile3.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile3.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile3.setId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setProfile(profile2);
        patientDemographicModel_NDHM_Patient_Profile3.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile3 = patientDemographicModel_NDHM_Patient_Profile3.new Profile();
        profile3.setHipCode("Hip Code");
        profile3.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile4 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile4.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile4.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile4.setId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setProfile(profile3);
        patientDemographicModel_NDHM_Patient_Profile4.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setTriggerTableAIId(1L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> patientDemographicModel_NDHM_Patient_ProfileList = new ArrayList<>();
        patientDemographicModel_NDHM_Patient_ProfileList.add(patientDemographicModel_NDHM_Patient_Profile4);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(patientDemographicModel_NDHM_Patient_ProfileList);

        // Act
        higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(resourceRequestHandler);

        // Assert
        verify(encounterFullRepresentationRepo).findByPatientId(eq("42"));
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(isNull());
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(bahmniDiagnoses, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(bahmniDiagnoses, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetCLinicalDataHigherhealthFacility4() throws FHIRException {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        EncounterFullRepresentation encounterFullRepresentation2 = new EncounterFullRepresentation();
        encounterFullRepresentation2.setAssociatedToPatientProgram(false);
        ArrayList<BahmniDiagnoses> bahmniDiagnoses = new ArrayList<>();
        encounterFullRepresentation2.setBahmniDiagnoses(bahmniDiagnoses);
        encounterFullRepresentation2.setDisposition(Boolean.TRUE.toString());
        encounterFullRepresentation2.setDrugOrders(new ArrayList<>());
        encounterFullRepresentation2
                .setEncounterDateTime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        encounterFullRepresentation2.setEncounterType("consultation");
        encounterFullRepresentation2.setEncounterTypeUuid("1234");
        encounterFullRepresentation2.setEncounterUuid("1234");
        encounterFullRepresentation2.setId("consultation");
        encounterFullRepresentation2.setLocationName(Boolean.TRUE.toString());
        encounterFullRepresentation2.setLocationUuid("1234");
        encounterFullRepresentation2.setObservations(new ArrayList<>());
        encounterFullRepresentation2.setPatientId("consultation");
        encounterFullRepresentation2.setPatientProgramUuid("1234");
        encounterFullRepresentation2.setPatientUuid("1234");
        encounterFullRepresentation2.setProviders(new ArrayList<>());
        encounterFullRepresentation2.setReason("consultation");
        encounterFullRepresentation2.setVisitType(Boolean.TRUE.toString());
        encounterFullRepresentation2.setVisitTypeUuid("1234");
        encounterFullRepresentation2.setVisitUuid("1234");

        ArrayList<EncounterFullRepresentation> encounterFullRepresentationList = new ArrayList<>();
        encounterFullRepresentationList.add(encounterFullRepresentation2);
        encounterFullRepresentationList.add(encounterFullRepresentation);
        when(encounterFullRepresentationRepo.findByPatientId(Mockito.<String>any()))
                .thenReturn(encounterFullRepresentationList);

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile.setId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile = patientDemographicModel_NDHM_Patient_Profile.new Profile();
        profile.setHipCode("Hip Code");
        profile.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile2 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile2.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile2.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile2.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile2.setId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setProfile(profile);
        patientDemographicModel_NDHM_Patient_Profile2.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile2 = patientDemographicModel_NDHM_Patient_Profile2.new Profile();
        profile2.setHipCode("Hip Code");
        profile2.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile3 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile3.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile3.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile3.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile3.setId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setProfile(profile2);
        patientDemographicModel_NDHM_Patient_Profile3.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setTriggerTableAIId(1L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile3 = patientDemographicModel_NDHM_Patient_Profile3.new Profile();
        profile3.setHipCode("Hip Code");
        profile3.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile4 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile4.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile4.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile4.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile4.setId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setProfile(profile3);
        patientDemographicModel_NDHM_Patient_Profile4.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setTriggerTableAIId(1L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> patientDemographicModel_NDHM_Patient_ProfileList = new ArrayList<>();
        patientDemographicModel_NDHM_Patient_ProfileList.add(patientDemographicModel_NDHM_Patient_Profile4);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(patientDemographicModel_NDHM_Patient_ProfileList);

        // Act
        higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(resourceRequestHandler);

        // Assert
        verify(encounterFullRepresentationRepo).findByPatientId(eq("42"));
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(isNull());
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(bahmniDiagnoses, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(bahmniDiagnoses, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData() {
        // Arrange, Act and Assert
        assertTrue(higherHealthFacilityServiceImpl.getVisitWiseEncounterData(new ArrayList<>()).isEmpty());
    }

    @Test
    void testGetVisitWiseEncounterData2() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(1, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData3() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        EncounterFullRepresentation encounterFullRepresentation2 = new EncounterFullRepresentation();
        encounterFullRepresentation2.setAssociatedToPatientProgram(false);
        encounterFullRepresentation2.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation2.setDisposition(Boolean.TRUE.toString());
        encounterFullRepresentation2.setDrugOrders(new ArrayList<>());
        encounterFullRepresentation2
                .setEncounterDateTime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        encounterFullRepresentation2.setEncounterType("consultation");
        encounterFullRepresentation2.setEncounterTypeUuid("1234");
        encounterFullRepresentation2.setEncounterUuid("1234");
        encounterFullRepresentation2.setId("consultation");
        encounterFullRepresentation2.setLocationName(Boolean.TRUE.toString());
        encounterFullRepresentation2.setLocationUuid("1234");
        encounterFullRepresentation2.setObservations(new ArrayList<>());
        encounterFullRepresentation2.setPatientId("consultation");
        encounterFullRepresentation2.setPatientProgramUuid("1234");
        encounterFullRepresentation2.setPatientUuid("1234");
        encounterFullRepresentation2.setProviders(new ArrayList<>());
        encounterFullRepresentation2.setReason("consultation");
        encounterFullRepresentation2.setVisitType(Boolean.TRUE.toString());
        encounterFullRepresentation2.setVisitTypeUuid("1234");
        encounterFullRepresentation2.setVisitUuid("1234");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation2);
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(2, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData4() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId(null);
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(1, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData5() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType(null);
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals(1, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData6() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid(null);
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(1, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData7() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        encounterFullRepresentation
                .setEncounterDateTime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid(null);

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act and Assert
        assertEquals(1, higherHealthFacilityServiceImpl.getVisitWiseEncounterData(resultList).size());
    }

    @Test
    void testGetVisitWiseEncounterData8() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        encounterFullRepresentation
                .setEncounterDateTime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        EncounterFullRepresentation encounterFullRepresentation2 = new EncounterFullRepresentation();
        encounterFullRepresentation2.setAssociatedToPatientProgram(false);
        encounterFullRepresentation2.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation2.setDisposition(Boolean.TRUE.toString());
        encounterFullRepresentation2.setDrugOrders(new ArrayList<>());
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation2.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation2.setEncounterType("consultation");
        encounterFullRepresentation2.setEncounterTypeUuid("1234");
        encounterFullRepresentation2.setEncounterUuid("1234");
        encounterFullRepresentation2.setId("consultation");
        encounterFullRepresentation2.setLocationName(Boolean.TRUE.toString());
        encounterFullRepresentation2.setLocationUuid("1234");
        encounterFullRepresentation2.setObservations(new ArrayList<>());
        encounterFullRepresentation2.setPatientId("consultation");
        encounterFullRepresentation2.setPatientProgramUuid("1234");
        encounterFullRepresentation2.setPatientUuid("1234");
        encounterFullRepresentation2.setProviders(new ArrayList<>());
        encounterFullRepresentation2.setReason("consultation");
        encounterFullRepresentation2.setVisitType(Boolean.TRUE.toString());
        encounterFullRepresentation2.setVisitTypeUuid("1234");
        encounterFullRepresentation2.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation2);
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("1234", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("consultation", visitWiseEncounterData.getPatientId());
        assertNull(higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertNull(higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertEquals(1, actualVisitWiseEncounterData.size());
        String expectedVisitType = Boolean.TRUE.toString();
        assertEquals(expectedVisitType, visitWiseEncounterData.getVisitType());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, visitWiseEncounterData.getBahmniDiagnoses());
        assertEquals(groupMembersList, visitWiseEncounterData.getDrugOrders());
        assertSame(encounterDateTime, visitWiseEncounterData.getEncounterDateTime());
    }

    @Test
    void testGetVisitWiseEncounterData9() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = mock(EncounterFullRepresentation.class);
        when(encounterFullRepresentation.getPatientId()).thenReturn("42");
        when(encounterFullRepresentation.getVisitType()).thenReturn("Visit Type");
        when(encounterFullRepresentation.getVisitTypeUuid()).thenReturn("01234567-89AB-CDEF-FEDC-BA9876543210");
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(encounterFullRepresentation.getEncounterDateTime()).thenReturn(fromResult);
        when(encounterFullRepresentation.getObservations()).thenReturn(new ArrayList<>());
        when(encounterFullRepresentation.getEncounterType()).thenReturn("3");
        when(encounterFullRepresentation.getVisitUuid()).thenReturn("01234567-89AB-CDEF-FEDC-BA9876543210");
        doNothing().when(encounterFullRepresentation).setAssociatedToPatientProgram(Mockito.<Boolean>any());
        doNothing().when(encounterFullRepresentation).setBahmniDiagnoses(Mockito.<List<BahmniDiagnoses>>any());
        doNothing().when(encounterFullRepresentation).setDisposition(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setDrugOrders(Mockito.<List<DrugOrders>>any());
        doNothing().when(encounterFullRepresentation).setEncounterDateTime(Mockito.<Date>any());
        doNothing().when(encounterFullRepresentation).setEncounterType(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setEncounterTypeUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setEncounterUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setId(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setLocationName(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setLocationUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setObservations(Mockito.<List<Observations>>any());
        doNothing().when(encounterFullRepresentation).setPatientId(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setPatientProgramUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setPatientUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setProviders(Mockito.<List<Providers>>any());
        doNothing().when(encounterFullRepresentation).setReason(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setVisitType(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setVisitTypeUuid(Mockito.<String>any());
        doNothing().when(encounterFullRepresentation).setVisitUuid(Mockito.<String>any());
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        encounterFullRepresentation.setBahmniDiagnoses(new ArrayList<>());
        encounterFullRepresentation.setDisposition("Disposition");
        encounterFullRepresentation.setDrugOrders(new ArrayList<>());
        encounterFullRepresentation
                .setEncounterDateTime(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setObservations(new ArrayList<>());
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setProviders(new ArrayList<>());
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        ArrayList<EncounterFullRepresentation> resultList = new ArrayList<>();
        resultList.add(encounterFullRepresentation);

        // Act
        List<VisitWiseEncounterData> actualVisitWiseEncounterData = higherHealthFacilityServiceImpl
                .getVisitWiseEncounterData(resultList);

        // Assert
        verify(encounterFullRepresentation, atLeast(1)).getEncounterDateTime();
        verify(encounterFullRepresentation, atLeast(1)).getEncounterType();
        verify(encounterFullRepresentation, atLeast(1)).getObservations();
        verify(encounterFullRepresentation, atLeast(1)).getPatientId();
        verify(encounterFullRepresentation, atLeast(1)).getVisitType();
        verify(encounterFullRepresentation, atLeast(1)).getVisitTypeUuid();
        verify(encounterFullRepresentation, atLeast(1)).getVisitUuid();
        verify(encounterFullRepresentation).setAssociatedToPatientProgram(Mockito.<Boolean>any());
        verify(encounterFullRepresentation).setBahmniDiagnoses(Mockito.<List<BahmniDiagnoses>>any());
        verify(encounterFullRepresentation).setDisposition(eq("Disposition"));
        verify(encounterFullRepresentation).setDrugOrders(Mockito.<List<DrugOrders>>any());
        verify(encounterFullRepresentation).setEncounterDateTime(Mockito.<Date>any());
        verify(encounterFullRepresentation).setEncounterType(eq("3"));
        verify(encounterFullRepresentation).setEncounterTypeUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setEncounterUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setId(eq("42"));
        verify(encounterFullRepresentation).setLocationName(eq("Location Name"));
        verify(encounterFullRepresentation).setLocationUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setObservations(Mockito.<List<Observations>>any());
        verify(encounterFullRepresentation).setPatientId(eq("42"));
        verify(encounterFullRepresentation).setPatientProgramUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setPatientUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setProviders(Mockito.<List<Providers>>any());
        verify(encounterFullRepresentation).setReason(eq("Just cause"));
        verify(encounterFullRepresentation).setVisitType(eq("Visit Type"));
        verify(encounterFullRepresentation).setVisitTypeUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        verify(encounterFullRepresentation).setVisitUuid(eq("01234567-89AB-CDEF-FEDC-BA9876543210"));
        VisitWiseEncounterData visitWiseEncounterData = higherHealthFacilityServiceImpl.visitWiseEncounterData;
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitTypeUuid());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", visitWiseEncounterData.getPatientId());
        assertEquals("Visit Type", visitWiseEncounterData.getVisitType());
        assertEquals(1, actualVisitWiseEncounterData.size());
        List<GroupMembers> groupMembersList = higherHealthFacilityServiceImpl.finalfamilymember;
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalCon);
        assertEquals(groupMembersList, higherHealthFacilityServiceImpl.grpMembersFinalReg);
        assertSame(fromResult, visitWiseEncounterData.getEncounterDateTime());
    }
}
