package com.wipro.fhir.service.patient_data_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.patient_data_handler.TRG_PatientResourceData;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.repo.patient_data_handler.TRG_PatientResourceData_Repo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PatientDataGatewayServiceImpl.class, ResourceRequestHandler.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PatientDataGatewayServiceImplDiffblueTest {
    @Autowired
    private ResourceRequestHandler resourceRequestHandler;

    @MockBean
    private APIChannel aPIChannel;

    @MockBean
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @MockBean
    private CommonService commonService;

    @Autowired
    private PatientDataGatewayServiceImpl patientDataGatewayServiceImpl;

    @MockBean
    private PatientDemographicModel_NDHM_Patient_Profile_Repo patientDemographicModel_NDHM_Patient_Profile_Repo;

    @MockBean
    private TRG_PatientResourceData_Repo tRG_PatientResourceData_Repo;

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#feedPatientProfileToMongoDB(List)}
     */
    @Test
    void testFeedPatientProfileToMongoDB() throws FHIRException {
        // Arrange, Act and Assert
        assertNull(patientDataGatewayServiceImpl.feedPatientProfileToMongoDB(new ArrayList<>()));
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#feedPatientProfileToMongoDB(List)}
     */
    @Test
    void testFeedPatientProfileToMongoDB2() throws FHIRException {
        // Arrange
        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> patientDemographicModel_NDHM_Patient_ProfileList = new ArrayList<>();
        when(commonService.savePatientProfileDataToMongo(Mockito.<List<PatientDemographicModel_NDHM_Patient_Profile>>any()))
                .thenReturn(patientDemographicModel_NDHM_Patient_ProfileList);

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

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> ndhm_Patient_Profile_List = new ArrayList<>();
        ndhm_Patient_Profile_List.add(patientDemographicModel_NDHM_Patient_Profile4);

        // Act
        List<PatientDemographicModel_NDHM_Patient_Profile> actualFeedPatientProfileToMongoDBResult = patientDataGatewayServiceImpl
                .feedPatientProfileToMongoDB(ndhm_Patient_Profile_List);

        // Assert
        verify(commonService)
                .savePatientProfileDataToMongo(Mockito.<List<PatientDemographicModel_NDHM_Patient_Profile>>any());
        assertTrue(actualFeedPatientProfileToMongoDBResult.isEmpty());
        assertSame(patientDemographicModel_NDHM_Patient_ProfileList, actualFeedPatientProfileToMongoDBResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#feedPatientProfileToMongoDB(List)}
     */
    @Test
    void testFeedPatientProfileToMongoDB3() throws FHIRException {
        // Arrange
        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> patientDemographicModel_NDHM_Patient_ProfileList = new ArrayList<>();
        when(commonService.savePatientProfileDataToMongo(Mockito.<List<PatientDemographicModel_NDHM_Patient_Profile>>any()))
                .thenReturn(patientDemographicModel_NDHM_Patient_ProfileList);

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

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile5 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile5.setAmritId("Amrit Id");
        patientDemographicModel_NDHM_Patient_Profile5
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setDataType("Demographic");
        patientDemographicModel_NDHM_Patient_Profile5.setExternalId("External Id");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthId("Health Id");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthIdNumber("Health Id Number");
        patientDemographicModel_NDHM_Patient_Profile5.setHipId("Hip Id");
        patientDemographicModel_NDHM_Patient_Profile5.setHipName("Demographic");
        patientDemographicModel_NDHM_Patient_Profile5.setId("Id");
        patientDemographicModel_NDHM_Patient_Profile5
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile5.setRequestId("Request Id");
        patientDemographicModel_NDHM_Patient_Profile5
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile4 = patientDemographicModel_NDHM_Patient_Profile5.new Profile();
        profile4.setHipCode("42");
        profile4.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile6 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile6.setAmritId("Amrit Id");
        patientDemographicModel_NDHM_Patient_Profile6
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setDataType("Demographic");
        patientDemographicModel_NDHM_Patient_Profile6.setExternalId("External Id");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthId("Health Id");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthIdNumber("Health Id Number");
        patientDemographicModel_NDHM_Patient_Profile6.setHipId("Hip Id");
        patientDemographicModel_NDHM_Patient_Profile6.setHipName("Demographic");
        patientDemographicModel_NDHM_Patient_Profile6.setId("Id");
        patientDemographicModel_NDHM_Patient_Profile6
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setProfile(profile4);
        patientDemographicModel_NDHM_Patient_Profile6.setRequestId("Request Id");
        patientDemographicModel_NDHM_Patient_Profile6
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile5 = patientDemographicModel_NDHM_Patient_Profile6.new Profile();
        profile5.setHipCode("42");
        profile5.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile7 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile7.setAmritId("Amrit Id");
        patientDemographicModel_NDHM_Patient_Profile7
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setDataType("Demographic");
        patientDemographicModel_NDHM_Patient_Profile7.setExternalId("External Id");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthId("Health Id");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthIdNumber("Health Id Number");
        patientDemographicModel_NDHM_Patient_Profile7.setHipId("Hip Id");
        patientDemographicModel_NDHM_Patient_Profile7.setHipName("Demographic");
        patientDemographicModel_NDHM_Patient_Profile7.setId("Id");
        patientDemographicModel_NDHM_Patient_Profile7
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setProfile(profile5);
        patientDemographicModel_NDHM_Patient_Profile7.setRequestId("Request Id");
        patientDemographicModel_NDHM_Patient_Profile7
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile6 = patientDemographicModel_NDHM_Patient_Profile7.new Profile();
        profile6.setHipCode("42");
        profile6.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile8 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile8.setAmritId("Amrit Id");
        patientDemographicModel_NDHM_Patient_Profile8
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setDataType("Demographic");
        patientDemographicModel_NDHM_Patient_Profile8.setExternalId("External Id");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthId("Health Id");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthIdNumber("Health Id Number");
        patientDemographicModel_NDHM_Patient_Profile8.setHipId("Hip Id");
        patientDemographicModel_NDHM_Patient_Profile8.setHipName("Demographic");
        patientDemographicModel_NDHM_Patient_Profile8.setId("Id");
        patientDemographicModel_NDHM_Patient_Profile8
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setProfile(profile6);
        patientDemographicModel_NDHM_Patient_Profile8.setRequestId("Request Id");
        patientDemographicModel_NDHM_Patient_Profile8
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setTriggerTableAIId(2L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> ndhm_Patient_Profile_List = new ArrayList<>();
        ndhm_Patient_Profile_List.add(patientDemographicModel_NDHM_Patient_Profile8);
        ndhm_Patient_Profile_List.add(patientDemographicModel_NDHM_Patient_Profile4);

        // Act
        List<PatientDemographicModel_NDHM_Patient_Profile> actualFeedPatientProfileToMongoDBResult = patientDataGatewayServiceImpl
                .feedPatientProfileToMongoDB(ndhm_Patient_Profile_List);

        // Assert
        verify(commonService)
                .savePatientProfileDataToMongo(Mockito.<List<PatientDemographicModel_NDHM_Patient_Profile>>any());
        assertTrue(actualFeedPatientProfileToMongoDBResult.isEmpty());
        assertSame(patientDemographicModel_NDHM_Patient_ProfileList, actualFeedPatientProfileToMongoDBResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo() throws FHIRException {
        // Arrange
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20()).thenReturn(new ArrayList<>());

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo2() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo3() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("foo");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo4() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn(null);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo5() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("42");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo6() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo7() throws FHIRException {
        // Arrange
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

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo8() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(null);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByProcessedOrderByCreatedDateLimit20())
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe");

        // Assert
        verify(tRG_PatientResourceData_Repo).getByProcessedOrderByCreatedDateLimit20();
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo9() throws FHIRException {
        // Arrange
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo10() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo11() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("foo");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo12() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn(null);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo13() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("42");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo14() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any())).thenReturn("");
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findByAmritId(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo15() throws FHIRException {
        // Arrange
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

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findByAmritId(eq("1"));
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#generatePatientProfileAMRIT_SaveTo_Mongo(String, ResourceRequestHandler)}
     */
    @Test
    void testGeneratePatientProfileAMRIT_SaveTo_Mongo16() throws FHIRException {
        // Arrange
        when(aPIChannel.benSearchByBenID(Mockito.<String>any(), Mockito.<ResourceRequestHandler>any()))
                .thenReturn("Ben Search By Ben ID");

        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(null);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        trg_PatientResourceData.setCreatedDate(mock(Timestamp.class));
        trg_PatientResourceData.setId(1L);
        trg_PatientResourceData.setLastModDate(mock(Timestamp.class));
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");

        ArrayList<TRG_PatientResourceData> trg_PatientResourceDataList = new ArrayList<>();
        trg_PatientResourceDataList.add(trg_PatientResourceData);
        when(tRG_PatientResourceData_Repo.getByBenIdLatestRecord(Mockito.<Long>any()))
                .thenReturn(trg_PatientResourceDataList);

        // Act
        String actualGeneratePatientProfileAMRIT_SaveTo_MongoResult = patientDataGatewayServiceImpl
                .generatePatientProfileAMRIT_SaveTo_Mongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(tRG_PatientResourceData_Repo).getByBenIdLatestRecord(Mockito.<Long>any());
        verify(aPIChannel).benSearchByBenID(eq("JaneDoe"), Mockito.<ResourceRequestHandler>any());
        assertEquals("null", actualGeneratePatientProfileAMRIT_SaveTo_MongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongo(String, ResourceRequestHandler)}
     */
    @Test
    void testSearchPatientProfileMongo() throws FHIRException {
        // Arrange
        when(commonService.searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        String actualSearchPatientProfileMongoResult = patientDataGatewayServiceImpl.searchPatientProfileMongo("JaneDoe",
                resourceRequestHandler);

        // Assert
        verify(commonService).searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any());
        assertNull(actualSearchPatientProfileMongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongo(String, ResourceRequestHandler)}
     */
    @Test
    void testSearchPatientProfileMongo2() throws FHIRException {
        // Arrange
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

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> content = new ArrayList<>();
        content.add(patientDemographicModel_NDHM_Patient_Profile4);
        PageImpl<PatientDemographicModel_NDHM_Patient_Profile> pageImpl = new PageImpl<>(content);
        when(commonService.searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any())).thenReturn(pageImpl);

        // Act
        patientDataGatewayServiceImpl.searchPatientProfileMongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(commonService).searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongo(String, ResourceRequestHandler)}
     */
    @Test
    void testSearchPatientProfileMongo3() throws FHIRException {
        // Arrange
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

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile5 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile5.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile5.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile5.setId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile5.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile4 = patientDemographicModel_NDHM_Patient_Profile5.new Profile();
        profile4.setHipCode("pageSize");
        profile4.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile6 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile6.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile6.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile6.setId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setProfile(profile4);
        patientDemographicModel_NDHM_Patient_Profile6.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile5 = patientDemographicModel_NDHM_Patient_Profile6.new Profile();
        profile5.setHipCode("pageSize");
        profile5.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile7 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile7.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile7.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile7.setId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setProfile(profile5);
        patientDemographicModel_NDHM_Patient_Profile7.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile6 = patientDemographicModel_NDHM_Patient_Profile7.new Profile();
        profile6.setHipCode("pageSize");
        profile6.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile8 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile8.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile8.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile8.setId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setProfile(profile6);
        patientDemographicModel_NDHM_Patient_Profile8.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setTriggerTableAIId(2L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> content = new ArrayList<>();
        content.add(patientDemographicModel_NDHM_Patient_Profile8);
        content.add(patientDemographicModel_NDHM_Patient_Profile4);
        PageImpl<PatientDemographicModel_NDHM_Patient_Profile> pageImpl = new PageImpl<>(content);
        when(commonService.searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any())).thenReturn(pageImpl);

        // Act
        patientDataGatewayServiceImpl.searchPatientProfileMongo("JaneDoe", resourceRequestHandler);

        // Assert
        verify(commonService).searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongo(String, ResourceRequestHandler)}
     */
    @Test
    void testSearchPatientProfileMongo4() throws FHIRException {
        // Arrange
        when(commonService.searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any())).thenReturn(null);

        // Act
        String actualSearchPatientProfileMongoResult = patientDataGatewayServiceImpl.searchPatientProfileMongo("JaneDoe",
                resourceRequestHandler);

        // Assert
        verify(commonService).searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any());
        assertNull(actualSearchPatientProfileMongoResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongo(String, ResourceRequestHandler)}
     */
    @Test
    void testSearchPatientProfileMongo5() throws FHIRException {
        // Arrange
        when(commonService.searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> patientDataGatewayServiceImpl.searchPatientProfileMongo("JaneDoe", resourceRequestHandler));
        verify(commonService).searchPatientProfileFromMongo(Mockito.<ResourceRequestHandler>any());
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongoPagination(Integer)}
     */
    @Test
    void testSearchPatientProfileMongoPagination() throws FHIRException {
        // Arrange
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findAll(Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        String actualSearchPatientProfileMongoPaginationResult = patientDataGatewayServiceImpl
                .searchPatientProfileMongoPagination(1);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findAll(Mockito.<Pageable>any());
        assertNull(actualSearchPatientProfileMongoPaginationResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongoPagination(Integer)}
     */
    @Test
    void testSearchPatientProfileMongoPagination2() throws FHIRException {
        // Arrange
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

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> content = new ArrayList<>();
        content.add(patientDemographicModel_NDHM_Patient_Profile4);
        PageImpl<PatientDemographicModel_NDHM_Patient_Profile> pageImpl = new PageImpl<>(content);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        patientDataGatewayServiceImpl.searchPatientProfileMongoPagination(1);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findAll(Mockito.<Pageable>any());
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongoPagination(Integer)}
     */
    @Test
    void testSearchPatientProfileMongoPagination3() throws FHIRException {
        // Arrange
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

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile5 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile5.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile5.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile5.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile5.setId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile5.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile5
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile4 = patientDemographicModel_NDHM_Patient_Profile5.new Profile();
        profile4.setHipCode("pageSize");
        profile4.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile6 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile6.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile6.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile6.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile6.setId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setProfile(profile4);
        patientDemographicModel_NDHM_Patient_Profile6.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile6
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile6.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile5 = patientDemographicModel_NDHM_Patient_Profile6.new Profile();
        profile5.setHipCode("pageSize");
        profile5.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile7 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile7.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile7.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile7.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile7.setId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setProfile(profile5);
        patientDemographicModel_NDHM_Patient_Profile7.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile7
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile7.setTriggerTableAIId(2L);

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile6 = patientDemographicModel_NDHM_Patient_Profile7.new Profile();
        profile6.setHipCode("pageSize");
        profile6.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile8 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile8.setAmritId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setDataType("pageSize");
        patientDemographicModel_NDHM_Patient_Profile8.setExternalId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHealthIdNumber("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHipId("data");
        patientDemographicModel_NDHM_Patient_Profile8.setHipName("pageSize");
        patientDemographicModel_NDHM_Patient_Profile8.setId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setProfile(profile6);
        patientDemographicModel_NDHM_Patient_Profile8.setRequestId("data");
        patientDemographicModel_NDHM_Patient_Profile8
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile8.setTriggerTableAIId(2L);

        ArrayList<PatientDemographicModel_NDHM_Patient_Profile> content = new ArrayList<>();
        content.add(patientDemographicModel_NDHM_Patient_Profile8);
        content.add(patientDemographicModel_NDHM_Patient_Profile4);
        PageImpl<PatientDemographicModel_NDHM_Patient_Profile> pageImpl = new PageImpl<>(content);
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        // Act
        patientDataGatewayServiceImpl.searchPatientProfileMongoPagination(1);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findAll(Mockito.<Pageable>any());
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongoPagination(Integer)}
     */
    @Test
    void testSearchPatientProfileMongoPagination4() throws FHIRException {
        // Arrange
        when(patientDemographicModel_NDHM_Patient_Profile_Repo.findAll(Mockito.<Pageable>any())).thenReturn(null);

        // Act
        String actualSearchPatientProfileMongoPaginationResult = patientDataGatewayServiceImpl
                .searchPatientProfileMongoPagination(1);

        // Assert
        verify(patientDemographicModel_NDHM_Patient_Profile_Repo).findAll(Mockito.<Pageable>any());
        assertNull(actualSearchPatientProfileMongoPaginationResult);
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayServiceImpl#searchPatientProfileMongoPagination(Integer)}
     */
    @Test
    void testSearchPatientProfileMongoPagination5() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> patientDataGatewayServiceImpl.searchPatientProfileMongoPagination(null));
    }
}
