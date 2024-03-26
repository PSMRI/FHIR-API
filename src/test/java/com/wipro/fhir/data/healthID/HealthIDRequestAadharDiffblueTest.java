package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HealthIDRequestAadharDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HealthIDRequestAadhar#setCreatedBy(String)}
     *   <li>{@link HealthIDRequestAadhar#setEmail(String)}
     *   <li>{@link HealthIDRequestAadhar#setFirstName(String)}
     *   <li>{@link HealthIDRequestAadhar#setHealthId(String)}
     *   <li>{@link HealthIDRequestAadhar#setLastName(String)}
     *   <li>{@link HealthIDRequestAadhar#setMiddleName(String)}
     *   <li>{@link HealthIDRequestAadhar#setOtp(String)}
     *   <li>{@link HealthIDRequestAadhar#setPassword(String)}
     *   <li>{@link HealthIDRequestAadhar#setProfilePhoto(String)}
     *   <li>{@link HealthIDRequestAadhar#setProviderServiceMapID(Integer)}
     *   <li>{@link HealthIDRequestAadhar#setTxnId(String)}
     *   <li>{@link HealthIDRequestAadhar#setUsername(String)}
     *   <li>{@link HealthIDRequestAadhar#getCreatedBy()}
     *   <li>{@link HealthIDRequestAadhar#getEmail()}
     *   <li>{@link HealthIDRequestAadhar#getFirstName()}
     *   <li>{@link HealthIDRequestAadhar#getHealthId()}
     *   <li>{@link HealthIDRequestAadhar#getLastName()}
     *   <li>{@link HealthIDRequestAadhar#getMiddleName()}
     *   <li>{@link HealthIDRequestAadhar#getOtp()}
     *   <li>{@link HealthIDRequestAadhar#getPassword()}
     *   <li>{@link HealthIDRequestAadhar#getProfilePhoto()}
     *   <li>{@link HealthIDRequestAadhar#getProviderServiceMapID()}
     *   <li>{@link HealthIDRequestAadhar#getTxnId()}
     *   <li>{@link HealthIDRequestAadhar#getUsername()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        HealthIDRequestAadhar healthIDRequestAadhar = new HealthIDRequestAadhar();

        // Act
        healthIDRequestAadhar.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        healthIDRequestAadhar.setEmail("jane.doe@example.org");
        healthIDRequestAadhar.setFirstName("Jane");
        healthIDRequestAadhar.setHealthId("42");
        healthIDRequestAadhar.setLastName("Doe");
        healthIDRequestAadhar.setMiddleName("Middle Name");
        healthIDRequestAadhar.setOtp("Otp");
        healthIDRequestAadhar.setPassword("iloveyou");
        healthIDRequestAadhar.setProfilePhoto("alice.liddell@example.org");
        healthIDRequestAadhar.setProviderServiceMapID(1);
        healthIDRequestAadhar.setTxnId("42");
        healthIDRequestAadhar.setUsername("janedoe");
        String actualCreatedBy = healthIDRequestAadhar.getCreatedBy();
        String actualEmail = healthIDRequestAadhar.getEmail();
        String actualFirstName = healthIDRequestAadhar.getFirstName();
        String actualHealthId = healthIDRequestAadhar.getHealthId();
        String actualLastName = healthIDRequestAadhar.getLastName();
        String actualMiddleName = healthIDRequestAadhar.getMiddleName();
        String actualOtp = healthIDRequestAadhar.getOtp();
        String actualPassword = healthIDRequestAadhar.getPassword();
        String actualProfilePhoto = healthIDRequestAadhar.getProfilePhoto();
        Integer actualProviderServiceMapID = healthIDRequestAadhar.getProviderServiceMapID();
        String actualTxnId = healthIDRequestAadhar.getTxnId();

        // Assert that nothing has changed
        assertEquals("42", actualHealthId);
        assertEquals("42", actualTxnId);
        assertEquals("Doe", actualLastName);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jane", actualFirstName);
        assertEquals("Middle Name", actualMiddleName);
        assertEquals("Otp", actualOtp);
        assertEquals("alice.liddell@example.org", actualProfilePhoto);
        assertEquals("iloveyou", actualPassword);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals("janedoe", healthIDRequestAadhar.getUsername());
        assertEquals(1, actualProviderServiceMapID.intValue());
    }
}
