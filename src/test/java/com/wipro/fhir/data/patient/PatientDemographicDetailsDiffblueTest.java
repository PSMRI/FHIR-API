package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PatientDemographicDetails.class})
@ExtendWith(SpringExtension.class)
class PatientDemographicDetailsDiffblueTest {
    @Autowired
    private PatientDemographicDetails patientDemographicDetails;

    /**
     * Method under test: {@link PatientDemographicDetails#getdOB()}
     */
    @Test
    void testGetdOB() {
        // Arrange, Act and Assert
        assertNull(patientDemographicDetails.getdOB());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientDemographicDetails#setActualAge(Integer)}
     *   <li>{@link PatientDemographicDetails#setAgeUnits(String)}
     *   <li>{@link PatientDemographicDetails#setBenPhoneMaps(ArrayList)}
     *   <li>{@link PatientDemographicDetails#setBeneficiaryID(Long)}
     *   <li>{@link PatientDemographicDetails#setBeneficiaryRegID(Long)}
     *   <li>{@link PatientDemographicDetails#setEmail(String)}
     *   <li>{@link PatientDemographicDetails#setFatherName(String)}
     *   <li>{@link PatientDemographicDetails#setFirstName(String)}
     *   <li>{@link PatientDemographicDetails#setGenderID(Integer)}
     *   <li>{@link PatientDemographicDetails#setI_bendemographics(PatientAddress)}
     *   <li>{@link PatientDemographicDetails#setLastName(String)}
     *   <li>{@link PatientDemographicDetails#setM_Gender(M_Gender)}
     *   <li>{@link PatientDemographicDetails#setM_title(M_title)}
     *   <li>{@link PatientDemographicDetails#setMaritalStatus(MaritalStatus)}
     *   <li>{@link PatientDemographicDetails#setdOB(Timestamp)}
     *   <li>{@link PatientDemographicDetails#getActualAge()}
     *   <li>{@link PatientDemographicDetails#getAgeUnits()}
     *   <li>{@link PatientDemographicDetails#getBenPhoneMaps()}
     *   <li>{@link PatientDemographicDetails#getBeneficiaryID()}
     *   <li>{@link PatientDemographicDetails#getBeneficiaryRegID()}
     *   <li>{@link PatientDemographicDetails#getEmail()}
     *   <li>{@link PatientDemographicDetails#getFatherName()}
     *   <li>{@link PatientDemographicDetails#getFirstName()}
     *   <li>{@link PatientDemographicDetails#getGenderID()}
     *   <li>{@link PatientDemographicDetails#getI_bendemographics()}
     *   <li>{@link PatientDemographicDetails#getLastName()}
     *   <li>{@link PatientDemographicDetails#getM_Gender()}
     *   <li>{@link PatientDemographicDetails#getM_title()}
     *   <li>{@link PatientDemographicDetails#getMaritalStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientDemographicDetails patientDemographicDetails = new PatientDemographicDetails();

        // Act
        patientDemographicDetails.setActualAge(1);
        patientDemographicDetails.setAgeUnits("Age Units");
        ArrayList<PatientPhoneMaps> benPhoneMaps = new ArrayList<>();
        patientDemographicDetails.setBenPhoneMaps(benPhoneMaps);
        patientDemographicDetails.setBeneficiaryID(1L);
        patientDemographicDetails.setBeneficiaryRegID(1L);
        patientDemographicDetails.setEmail("jane.doe@example.org");
        patientDemographicDetails.setFatherName("Father Name");
        patientDemographicDetails.setFirstName("Jane");
        patientDemographicDetails.setGenderID(1);
        PatientAddress i_bendemographics = new PatientAddress();
        patientDemographicDetails.setI_bendemographics(i_bendemographics);
        patientDemographicDetails.setLastName("Doe");
        M_Gender m_Gender = new M_Gender();
        m_Gender.setGenderID(1);
        m_Gender.setGenderName("Gender Name");
        patientDemographicDetails.setM_Gender(m_Gender);
        M_title m_title = new M_title();
        m_title.setTitleID(1);
        m_title.setTitleName("Dr");
        patientDemographicDetails.setM_title(m_title);
        MaritalStatus maritalStatus = new MaritalStatus();
        patientDemographicDetails.setMaritalStatus(maritalStatus);
        patientDemographicDetails.setdOB(mock(Timestamp.class));
        Integer actualActualAge = patientDemographicDetails.getActualAge();
        String actualAgeUnits = patientDemographicDetails.getAgeUnits();
        ArrayList<PatientPhoneMaps> actualBenPhoneMaps = patientDemographicDetails.getBenPhoneMaps();
        Long actualBeneficiaryID = patientDemographicDetails.getBeneficiaryID();
        Long actualBeneficiaryRegID = patientDemographicDetails.getBeneficiaryRegID();
        String actualEmail = patientDemographicDetails.getEmail();
        String actualFatherName = patientDemographicDetails.getFatherName();
        String actualFirstName = patientDemographicDetails.getFirstName();
        Integer actualGenderID = patientDemographicDetails.getGenderID();
        PatientAddress actualI_bendemographics = patientDemographicDetails.getI_bendemographics();
        String actualLastName = patientDemographicDetails.getLastName();
        M_Gender actualM_Gender = patientDemographicDetails.getM_Gender();
        M_title actualM_title = patientDemographicDetails.getM_title();
        MaritalStatus actualMaritalStatus = patientDemographicDetails.getMaritalStatus();

        // Assert that nothing has changed
        assertEquals("Age Units", actualAgeUnits);
        assertEquals("Doe", actualLastName);
        assertEquals("Father Name", actualFatherName);
        assertEquals("Jane", actualFirstName);
        assertEquals("jane.doe@example.org", actualEmail);
        assertEquals(1, actualActualAge.intValue());
        assertEquals(1, actualGenderID.intValue());
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualBeneficiaryRegID.longValue());
        assertSame(m_Gender, actualM_Gender);
        assertSame(m_title, actualM_title);
        assertSame(maritalStatus, actualMaritalStatus);
        assertSame(i_bendemographics, actualI_bendemographics);
        assertSame(benPhoneMaps, actualBenPhoneMaps);
    }
}
