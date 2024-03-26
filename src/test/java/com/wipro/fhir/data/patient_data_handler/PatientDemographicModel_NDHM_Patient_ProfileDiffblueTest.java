package com.wipro.fhir.data.patient_data_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PatientDemographicModel_NDHM_Patient_ProfileDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setAmritId(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setCreatedDate(Date)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setDataType(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile#setExternalId(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setHealthId(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile#setHealthIdNumber(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setHipId(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setHipName(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setId(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setLastModDate(Date)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile#setProfile(PatientDemographicModel_NDHM_Patient_Profile.Profile)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setRequestId(String)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#setTimestamp(Date)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile#setTriggerTableAIId(Long)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getAmritId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getCreatedDate()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getDataType()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getExternalId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getHealthId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getHealthIdNumber()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getHipId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getHipName()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getLastModDate()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getProfile()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getRequestId()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile#getTimestamp()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile#getTriggerTableAIId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile = new PatientDemographicModel_NDHM_Patient_Profile();

        // Act
        patientDemographicModel_NDHM_Patient_Profile.setAmritId("42");
        Date createdDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        patientDemographicModel_NDHM_Patient_Profile.setCreatedDate(createdDate);
        patientDemographicModel_NDHM_Patient_Profile.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile.setId("42");
        Date lastModDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        patientDemographicModel_NDHM_Patient_Profile.setLastModDate(lastModDate);
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
        patientDemographicModel_NDHM_Patient_Profile2
                .setProfile((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile());
        patientDemographicModel_NDHM_Patient_Profile2.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile2
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile2.setTriggerTableAIId(1L);
        PatientDemographicModel_NDHM_Patient_Profile.Profile profile = patientDemographicModel_NDHM_Patient_Profile2.new Profile();
        profile.setHipCode("Hip Code");
        profile.setPatient(mock(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.class));
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
        patientDemographicModel_NDHM_Patient_Profile3.setProfile(profile);
        patientDemographicModel_NDHM_Patient_Profile3.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile3
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile3.setTriggerTableAIId(1L);
        PatientDemographicModel_NDHM_Patient_Profile.Profile profile2 = patientDemographicModel_NDHM_Patient_Profile3.new Profile();
        profile2.setHipCode("Hip Code");
        profile2.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());
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
        patientDemographicModel_NDHM_Patient_Profile4.setProfile(profile2);
        patientDemographicModel_NDHM_Patient_Profile4.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile4
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile4.setTriggerTableAIId(1L);
        PatientDemographicModel_NDHM_Patient_Profile.Profile profile3 = patientDemographicModel_NDHM_Patient_Profile4.new Profile();
        profile3.setHipCode("Hip Code");
        profile3.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());
        PatientDemographicModel_NDHM_Patient_Profile patientDemographicModel_NDHM_Patient_Profile5 = new PatientDemographicModel_NDHM_Patient_Profile();
        patientDemographicModel_NDHM_Patient_Profile5.setAmritId("42");
        patientDemographicModel_NDHM_Patient_Profile5
                .setCreatedDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setDataType("Data Type");
        patientDemographicModel_NDHM_Patient_Profile5.setExternalId("42");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthId("42");
        patientDemographicModel_NDHM_Patient_Profile5.setHealthIdNumber("42");
        patientDemographicModel_NDHM_Patient_Profile5.setHipId("42");
        patientDemographicModel_NDHM_Patient_Profile5.setHipName("Hip Name");
        patientDemographicModel_NDHM_Patient_Profile5.setId("42");
        patientDemographicModel_NDHM_Patient_Profile5
                .setLastModDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setProfile(profile3);
        patientDemographicModel_NDHM_Patient_Profile5.setRequestId("42");
        patientDemographicModel_NDHM_Patient_Profile5
                .setTimestamp(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        patientDemographicModel_NDHM_Patient_Profile5.setTriggerTableAIId(1L);
        PatientDemographicModel_NDHM_Patient_Profile.Profile profile4 = patientDemographicModel_NDHM_Patient_Profile5.new Profile();
        profile4.setHipCode("Hip Code");
        profile4.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());
        patientDemographicModel_NDHM_Patient_Profile.setProfile(profile4);
        patientDemographicModel_NDHM_Patient_Profile.setRequestId("42");
        Date timestamp = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        patientDemographicModel_NDHM_Patient_Profile.setTimestamp(timestamp);
        patientDemographicModel_NDHM_Patient_Profile.setTriggerTableAIId(1L);
        String actualAmritId = patientDemographicModel_NDHM_Patient_Profile.getAmritId();
        Date actualCreatedDate = patientDemographicModel_NDHM_Patient_Profile.getCreatedDate();
        String actualDataType = patientDemographicModel_NDHM_Patient_Profile.getDataType();
        String actualExternalId = patientDemographicModel_NDHM_Patient_Profile.getExternalId();
        String actualHealthId = patientDemographicModel_NDHM_Patient_Profile.getHealthId();
        String actualHealthIdNumber = patientDemographicModel_NDHM_Patient_Profile.getHealthIdNumber();
        String actualHipId = patientDemographicModel_NDHM_Patient_Profile.getHipId();
        String actualHipName = patientDemographicModel_NDHM_Patient_Profile.getHipName();
        String actualId = patientDemographicModel_NDHM_Patient_Profile.getId();
        Date actualLastModDate = patientDemographicModel_NDHM_Patient_Profile.getLastModDate();
        PatientDemographicModel_NDHM_Patient_Profile.Profile actualProfile = patientDemographicModel_NDHM_Patient_Profile
                .getProfile();
        String actualRequestId = patientDemographicModel_NDHM_Patient_Profile.getRequestId();
        Date actualTimestamp = patientDemographicModel_NDHM_Patient_Profile.getTimestamp();

        // Assert that nothing has changed
        assertEquals("42", actualAmritId);
        assertEquals("42", actualExternalId);
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthIdNumber);
        assertEquals("42", actualHipId);
        assertEquals("42", actualId);
        assertEquals("42", actualRequestId);
        assertEquals("Data Type", actualDataType);
        assertEquals("Hip Name", actualHipName);
        assertEquals(1L, patientDemographicModel_NDHM_Patient_Profile.getTriggerTableAIId().longValue());
        assertSame(profile4, actualProfile);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
        assertSame(timestamp, actualTimestamp);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile#Profile(PatientDemographicModel_NDHM_Patient_Profile)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile#setHipCode(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile#setPatient(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient)}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile.Profile#getHipCode()}
     *   <li>{@link PatientDemographicModel_NDHM_Patient_Profile.Profile#getPatient()}
     * </ul>
     */
    @Test
    void testProfileGettersAndSetters() {
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

        // Act
        PatientDemographicModel_NDHM_Patient_Profile.Profile actualProfile = patientDemographicModel_NDHM_Patient_Profile4.new Profile();
        actualProfile.setHipCode("Hip Code");
        PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient patient = ((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient();
        actualProfile.setPatient(patient);
        String actualHipCode = actualProfile.getHipCode();

        // Assert that nothing has changed
        assertEquals("Hip Code", actualHipCode);
        assertSame(patient, actualProfile.getPatient());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#Patient(PatientDemographicModel_NDHM_Patient_Profile.Profile)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setAddress(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setDayOfBirth(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setFirstName(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setGender(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setHealthId(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setHealthIdNumber(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setIdentifiers(List)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setLastName(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setMonthOfBirth(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setName(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setPhoneNo(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#setYearOfBirth(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getAddress()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getDayOfBirth()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getFirstName()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getGender()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getHealthId()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getHealthIdNumber()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getIdentifiers()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getLastName()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getMonthOfBirth()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getName()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getPhoneNo()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient#getYearOfBirth()}
     * </ul>
     */
    @Test
    void testProfile_PatientGettersAndSetters() {
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

        PatientDemographicModel_NDHM_Patient_Profile.Profile profile4 = patientDemographicModel_NDHM_Patient_Profile4.new Profile();
        profile4.setHipCode("Hip Code");
        profile4.setPatient(((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient());

        // Act
        PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient actualPatient = profile4.new Patient();
        PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address address = (((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient()).new Address();
        actualPatient.setAddress(address);
        actualPatient.setDayOfBirth("Day Of Birth");
        actualPatient.setFirstName("Name");
        actualPatient.setGender("Gender");
        actualPatient.setHealthId("42");
        actualPatient.setHealthIdNumber("42");
        ArrayList<Map<String, String>> identifiers = new ArrayList<>();
        actualPatient.setIdentifiers(identifiers);
        actualPatient.setLastName("Name");
        actualPatient.setMonthOfBirth("Month Of Birth");
        actualPatient.setName("Name");
        actualPatient.setPhoneNo("6625550144");
        actualPatient.setYearOfBirth("Year Of Birth");
        PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address actualAddress = actualPatient.getAddress();
        String actualDayOfBirth = actualPatient.getDayOfBirth();
        String actualFirstName = actualPatient.getFirstName();
        String actualGender = actualPatient.getGender();
        String actualHealthId = actualPatient.getHealthId();
        String actualHealthIdNumber = actualPatient.getHealthIdNumber();
        List<Map<String, String>> actualIdentifiers = actualPatient.getIdentifiers();
        String actualLastName = actualPatient.getLastName();
        String actualMonthOfBirth = actualPatient.getMonthOfBirth();
        String actualName = actualPatient.getName();
        String actualPhoneNo = actualPatient.getPhoneNo();

        // Assert that nothing has changed
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthIdNumber);
        assertEquals("6625550144", actualPhoneNo);
        assertEquals("Day Of Birth", actualDayOfBirth);
        assertEquals("Gender", actualGender);
        assertEquals("Month Of Birth", actualMonthOfBirth);
        assertEquals("Name", actualFirstName);
        assertEquals("Name", actualLastName);
        assertEquals("Name", actualName);
        assertEquals("Year Of Birth", actualPatient.getYearOfBirth());
        assertSame(address, actualAddress);
        assertSame(identifiers, actualIdentifiers);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#Address(PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#setDistrict(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#setLine(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#setPincode(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#setState(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#setVillage(String)}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#getDistrict()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#getLine()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#getPincode()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#getState()}
     *   <li>
     * {@link PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address#getVillage()}
     * </ul>
     */
    @Test
    void testProfile_Patient_AddressGettersAndSetters() {
        // Arrange and Act
        PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address actualAddress = (((new PatientDemographicModel_NDHM_Patient_Profile()).new Profile()).new Patient()).new Address();
        actualAddress.setDistrict("District");
        actualAddress.setLine("Line");
        actualAddress.setPincode("Pincode");
        actualAddress.setState("MD");
        actualAddress.setVillage("Village");
        String actualDistrict = actualAddress.getDistrict();
        String actualLine = actualAddress.getLine();
        String actualPincode = actualAddress.getPincode();
        String actualState = actualAddress.getState();

        // Assert that nothing has changed
        assertEquals("District", actualDistrict);
        assertEquals("Line", actualLine);
        assertEquals("MD", actualState);
        assertEquals("Pincode", actualPincode);
        assertEquals("Village", actualAddress.getVillage());
    }
}
