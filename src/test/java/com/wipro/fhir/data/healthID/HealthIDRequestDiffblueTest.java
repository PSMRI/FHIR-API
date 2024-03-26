package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HealthIDRequestDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HealthIDRequest#setAddress(String)}
     *   <li>{@link HealthIDRequest#setCreatedBy(String)}
     *   <li>{@link HealthIDRequest#setDayOfBirth(String)}
     *   <li>{@link HealthIDRequest#setDistrictCode(Integer)}
     *   <li>{@link HealthIDRequest#setDistrictName(String)}
     *   <li>{@link HealthIDRequest#setEmail(String)}
     *   <li>{@link HealthIDRequest#setFirstName(String)}
     *   <li>{@link HealthIDRequest#setGender(String)}
     *   <li>{@link HealthIDRequest#setHealthId(String)}
     *   <li>{@link HealthIDRequest#setLastName(String)}
     *   <li>{@link HealthIDRequest#setLastname(String)}
     *   <li>{@link HealthIDRequest#setMonthOfBirth(String)}
     *   <li>{@link HealthIDRequest#setName(String)}
     *   <li>{@link HealthIDRequest#setOtp(String)}
     *   <li>{@link HealthIDRequest#setPincode(String)}
     *   <li>{@link HealthIDRequest#setProfilePhoto(String)}
     *   <li>{@link HealthIDRequest#setProviderServiceMapID(Integer)}
     *   <li>{@link HealthIDRequest#setStateCode(Integer)}
     *   <li>{@link HealthIDRequest#setStateName(String)}
     *   <li>{@link HealthIDRequest#setToken(String)}
     *   <li>{@link HealthIDRequest#setTxnId(String)}
     *   <li>{@link HealthIDRequest#setUsername(String)}
     *   <li>{@link HealthIDRequest#setYearOfBirth(String)}
     *   <li>{@link HealthIDRequest#getAddress()}
     *   <li>{@link HealthIDRequest#getCreatedBy()}
     *   <li>{@link HealthIDRequest#getDayOfBirth()}
     *   <li>{@link HealthIDRequest#getDistrictCode()}
     *   <li>{@link HealthIDRequest#getDistrictName()}
     *   <li>{@link HealthIDRequest#getEmail()}
     *   <li>{@link HealthIDRequest#getFirstName()}
     *   <li>{@link HealthIDRequest#getGender()}
     *   <li>{@link HealthIDRequest#getHealthId()}
     *   <li>{@link HealthIDRequest#getLastName()}
     *   <li>{@link HealthIDRequest#getLastname()}
     *   <li>{@link HealthIDRequest#getMonthOfBirth()}
     *   <li>{@link HealthIDRequest#getName()}
     *   <li>{@link HealthIDRequest#getOtp()}
     *   <li>{@link HealthIDRequest#getPincode()}
     *   <li>{@link HealthIDRequest#getProfilePhoto()}
     *   <li>{@link HealthIDRequest#getProviderServiceMapID()}
     *   <li>{@link HealthIDRequest#getStateCode()}
     *   <li>{@link HealthIDRequest#getStateName()}
     *   <li>{@link HealthIDRequest#getToken()}
     *   <li>{@link HealthIDRequest#getTxnId()}
     *   <li>{@link HealthIDRequest#getUsername()}
     *   <li>{@link HealthIDRequest#getYearOfBirth()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        HealthIDRequest healthIDRequest = new HealthIDRequest();

        // Act
        healthIDRequest.setAddress("42 Main St");
        healthIDRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        healthIDRequest.setDayOfBirth("Day Of Birth");
        healthIDRequest.setDistrictCode(1);
        healthIDRequest.setDistrictName("District Name");
        healthIDRequest.setEmail("jane.doe@example.org");
        healthIDRequest.setFirstName("Jane");
        healthIDRequest.setGender("Gender");
        healthIDRequest.setHealthId("42");
        healthIDRequest.setLastName("Doe");
        healthIDRequest.setLastname("Doe");
        healthIDRequest.setMonthOfBirth("Month Of Birth");
        healthIDRequest.setName("Name");
        healthIDRequest.setOtp("Otp");
        healthIDRequest.setPincode("Pincode");
        healthIDRequest.setProfilePhoto("alice.liddell@example.org");
        healthIDRequest.setProviderServiceMapID(1);
        healthIDRequest.setStateCode(1);
        healthIDRequest.setStateName("MD");
        healthIDRequest.setToken("ABC123");
        healthIDRequest.setTxnId("42");
        healthIDRequest.setUsername("janedoe");
        healthIDRequest.setYearOfBirth("Year Of Birth");
        String actualAddress = healthIDRequest.getAddress();
        String actualCreatedBy = healthIDRequest.getCreatedBy();
        String actualDayOfBirth = healthIDRequest.getDayOfBirth();
        Integer actualDistrictCode = healthIDRequest.getDistrictCode();
        String actualDistrictName = healthIDRequest.getDistrictName();
        String actualEmail = healthIDRequest.getEmail();
        String actualFirstName = healthIDRequest.getFirstName();
        String actualGender = healthIDRequest.getGender();
        String actualHealthId = healthIDRequest.getHealthId();
        String actualLastName = healthIDRequest.getLastName();
        String actualLastname = healthIDRequest.getLastname();
        String actualMonthOfBirth = healthIDRequest.getMonthOfBirth();
        String actualName = healthIDRequest.getName();
        String actualOtp = healthIDRequest.getOtp();
        String actualPincode = healthIDRequest.getPincode();
        String actualProfilePhoto = healthIDRequest.getProfilePhoto();
        Integer actualProviderServiceMapID = healthIDRequest.getProviderServiceMapID();
        Integer actualStateCode = healthIDRequest.getStateCode();
        String actualStateName = healthIDRequest.getStateName();
        String actualToken = healthIDRequest.getToken();
        String actualTxnId = healthIDRequest.getTxnId();
        String actualUsername = healthIDRequest.getUsername();

        // Assert that nothing has changed
        assertEquals("42 Main St", actualAddress);
        assertEquals("42", actualHealthId);
        assertEquals("42", actualTxnId);
        assertEquals("ABC123", actualToken);
        assertEquals("Day Of Birth", actualDayOfBirth);
        assertEquals("District Name", actualDistrictName);
        assertEquals("Doe", actualLastName);
        assertEquals("Doe", actualLastname);
        assertEquals("Gender", actualGender);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jane", actualFirstName);
        assertEquals("MD", actualStateName);
        assertEquals("Month Of Birth", actualMonthOfBirth);
        assertEquals("Name", actualName);
        assertEquals("Otp", actualOtp);
        assertEquals("Pincode", actualPincode);
        assertEquals("Year Of Birth", healthIDRequest.getYearOfBirth());
        assertEquals("alice.liddell@example.org", actualProfilePhoto);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals("janedoe", actualUsername);
        assertEquals(1, actualDistrictCode.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualStateCode.intValue());
    }
}
