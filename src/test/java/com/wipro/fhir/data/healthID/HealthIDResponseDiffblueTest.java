package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {HealthIDResponse.class})
@ExtendWith(SpringExtension.class)
class HealthIDResponseDiffblueTest {
    @Autowired
    private HealthIDResponse healthIDResponse;

    /**
     * Method under test: {@link HealthIDResponse#gethID()}
     */
    @Test
    void testGethID() {
        // Arrange, Act and Assert
        assertNull((new HealthIDResponse()).gethID());
    }

    /**
     * Method under test: {@link HealthIDResponse#gethID()}
     */
    @Test
    void testGethID2() {
        // Arrange
        HealthIDResponse healthIDResponse2 = new HealthIDResponse();
        healthIDResponse2.setCreatedDate(mock(Timestamp.class));

        // Act and Assert
        assertNull(healthIDResponse2.gethID());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HealthIDResponse#setAuthMethod(String)}
     *   <li>{@link HealthIDResponse#setAuthMethods(List)}
     *   <li>{@link HealthIDResponse#setAuthenticationMode(String)}
     *   <li>{@link HealthIDResponse#setBeneficiaryRegId(Long)}
     *   <li>{@link HealthIDResponse#setCreatedBy(String)}
     *   <li>{@link HealthIDResponse#setCreatedDate(Timestamp)}
     *   <li>{@link HealthIDResponse#setDayOfBirth(String)}
     *   <li>{@link HealthIDResponse#setDeleted(Boolean)}
     *   <li>{@link HealthIDResponse#setDistrictCode(String)}
     *   <li>{@link HealthIDResponse#setDistrictName(String)}
     *   <li>{@link HealthIDResponse#setEmail(String)}
     *   <li>{@link HealthIDResponse#setFirstName(String)}
     *   <li>{@link HealthIDResponse#setGender(String)}
     *   <li>{@link HealthIDResponse#setHealthId(String)}
     *   <li>{@link HealthIDResponse#setHealthIdNumber(String)}
     *   <li>{@link HealthIDResponse#setKycPhoto(String)}
     *   <li>{@link HealthIDResponse#setLastModDate(Timestamp)}
     *   <li>{@link HealthIDResponse#setLastName(String)}
     *   <li>{@link HealthIDResponse#setMiddleName(String)}
     *   <li>{@link HealthIDResponse#setMobile(String)}
     *   <li>{@link HealthIDResponse#setModifiedBy(String)}
     *   <li>{@link HealthIDResponse#setMonthOfBirth(String)}
     *   <li>{@link HealthIDResponse#setName(String)}
     *   <li>{@link HealthIDResponse#setProcessed(String)}
     *   <li>{@link HealthIDResponse#setProviderServiceMapID(Integer)}
     *   <li>{@link HealthIDResponse#setStateCode(String)}
     *   <li>{@link HealthIDResponse#setStateName(String)}
     *   <li>{@link HealthIDResponse#setTxnId(String)}
     *   <li>{@link HealthIDResponse#setYearOfBirth(String)}
     *   <li>{@link HealthIDResponse#sethID(Integer)}
     *   <li>{@link HealthIDResponse#getAuthMethod()}
     *   <li>{@link HealthIDResponse#getAuthMethods()}
     *   <li>{@link HealthIDResponse#getAuthenticationMode()}
     *   <li>{@link HealthIDResponse#getBeneficiaryRegId()}
     *   <li>{@link HealthIDResponse#getCreatedBy()}
     *   <li>{@link HealthIDResponse#getCreatedDate()}
     *   <li>{@link HealthIDResponse#getDayOfBirth()}
     *   <li>{@link HealthIDResponse#getDeleted()}
     *   <li>{@link HealthIDResponse#getDistrictCode()}
     *   <li>{@link HealthIDResponse#getDistrictName()}
     *   <li>{@link HealthIDResponse#getEmail()}
     *   <li>{@link HealthIDResponse#getFirstName()}
     *   <li>{@link HealthIDResponse#getGender()}
     *   <li>{@link HealthIDResponse#getHealthId()}
     *   <li>{@link HealthIDResponse#getHealthIdNumber()}
     *   <li>{@link HealthIDResponse#getKycPhoto()}
     *   <li>{@link HealthIDResponse#getLastModDate()}
     *   <li>{@link HealthIDResponse#getLastName()}
     *   <li>{@link HealthIDResponse#getMiddleName()}
     *   <li>{@link HealthIDResponse#getMobile()}
     *   <li>{@link HealthIDResponse#getModifiedBy()}
     *   <li>{@link HealthIDResponse#getMonthOfBirth()}
     *   <li>{@link HealthIDResponse#getName()}
     *   <li>{@link HealthIDResponse#getProcessed()}
     *   <li>{@link HealthIDResponse#getProviderServiceMapID()}
     *   <li>{@link HealthIDResponse#getStateCode()}
     *   <li>{@link HealthIDResponse#getStateName()}
     *   <li>{@link HealthIDResponse#getTxnId()}
     *   <li>{@link HealthIDResponse#getYearOfBirth()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        HealthIDResponse healthIDResponse = new HealthIDResponse();

        // Act
        healthIDResponse.setAuthMethod("Auth Method");
        ArrayList<String> authMethods = new ArrayList<>();
        healthIDResponse.setAuthMethods(authMethods);
        healthIDResponse.setAuthenticationMode("Authentication Mode");
        healthIDResponse.setBeneficiaryRegId(1L);
        healthIDResponse.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Timestamp createdDate = mock(Timestamp.class);
        healthIDResponse.setCreatedDate(createdDate);
        healthIDResponse.setDayOfBirth("Day Of Birth");
        healthIDResponse.setDeleted(true);
        healthIDResponse.setDistrictCode("District Code");
        healthIDResponse.setDistrictName("District Name");
        healthIDResponse.setEmail("jane.doe@example.org");
        healthIDResponse.setFirstName("Jane");
        healthIDResponse.setGender("Gender");
        healthIDResponse.setHealthId("42");
        healthIDResponse.setHealthIdNumber("42");
        healthIDResponse.setKycPhoto("alice.liddell@example.org");
        Timestamp lastModDate = mock(Timestamp.class);
        healthIDResponse.setLastModDate(lastModDate);
        healthIDResponse.setLastName("Doe");
        healthIDResponse.setMiddleName("Middle Name");
        healthIDResponse.setMobile("Mobile");
        healthIDResponse.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        healthIDResponse.setMonthOfBirth("Month Of Birth");
        healthIDResponse.setName("Name");
        healthIDResponse.setProcessed("Processed");
        healthIDResponse.setProviderServiceMapID(1);
        healthIDResponse.setStateCode("MD");
        healthIDResponse.setStateName("MD");
        healthIDResponse.setTxnId("42");
        healthIDResponse.setYearOfBirth("Year Of Birth");
        healthIDResponse.sethID(1);
        String actualAuthMethod = healthIDResponse.getAuthMethod();
        List<String> actualAuthMethods = healthIDResponse.getAuthMethods();
        String actualAuthenticationMode = healthIDResponse.getAuthenticationMode();
        Long actualBeneficiaryRegId = healthIDResponse.getBeneficiaryRegId();
        String actualCreatedBy = healthIDResponse.getCreatedBy();
        Timestamp actualCreatedDate = healthIDResponse.getCreatedDate();
        String actualDayOfBirth = healthIDResponse.getDayOfBirth();
        Boolean actualDeleted = healthIDResponse.getDeleted();
        String actualDistrictCode = healthIDResponse.getDistrictCode();
        String actualDistrictName = healthIDResponse.getDistrictName();
        String actualEmail = healthIDResponse.getEmail();
        String actualFirstName = healthIDResponse.getFirstName();
        String actualGender = healthIDResponse.getGender();
        String actualHealthId = healthIDResponse.getHealthId();
        String actualHealthIdNumber = healthIDResponse.getHealthIdNumber();
        String actualKycPhoto = healthIDResponse.getKycPhoto();
        Timestamp actualLastModDate = healthIDResponse.getLastModDate();
        String actualLastName = healthIDResponse.getLastName();
        String actualMiddleName = healthIDResponse.getMiddleName();
        String actualMobile = healthIDResponse.getMobile();
        String actualModifiedBy = healthIDResponse.getModifiedBy();
        String actualMonthOfBirth = healthIDResponse.getMonthOfBirth();
        String actualName = healthIDResponse.getName();
        String actualProcessed = healthIDResponse.getProcessed();
        Integer actualProviderServiceMapID = healthIDResponse.getProviderServiceMapID();
        String actualStateCode = healthIDResponse.getStateCode();
        String actualStateName = healthIDResponse.getStateName();
        String actualTxnId = healthIDResponse.getTxnId();

        // Assert that nothing has changed
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthIdNumber);
        assertEquals("42", actualTxnId);
        assertEquals("Auth Method", actualAuthMethod);
        assertEquals("Authentication Mode", actualAuthenticationMode);
        assertEquals("Day Of Birth", actualDayOfBirth);
        assertEquals("District Code", actualDistrictCode);
        assertEquals("District Name", actualDistrictName);
        assertEquals("Doe", actualLastName);
        assertEquals("Gender", actualGender);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Jane", actualFirstName);
        assertEquals("MD", actualStateCode);
        assertEquals("MD", actualStateName);
        assertEquals("Middle Name", actualMiddleName);
        assertEquals("Mobile", actualMobile);
        assertEquals("Month Of Birth", actualMonthOfBirth);
        assertEquals("Name", actualName);
        assertEquals("Processed", actualProcessed);
        assertEquals("Year Of Birth", healthIDResponse.getYearOfBirth());
        assertEquals("alice.liddell@example.org", actualKycPhoto);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1L, actualBeneficiaryRegId.longValue());
        assertTrue(actualDeleted);
        assertSame(authMethods, actualAuthMethods);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
