package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.Test;

class T_PatientIssueDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link T_PatientIssue#setAge(Integer)}
     *   <li>{@link T_PatientIssue#setBenRegID(Long)}
     *   <li>{@link T_PatientIssue#setBeneficiaryID(Long)}
     *   <li>{@link T_PatientIssue#setCreatedBy(String)}
     *   <li>{@link T_PatientIssue#setCreatedDate(Date)}
     *   <li>{@link T_PatientIssue#setDeleted(Boolean)}
     *   <li>{@link T_PatientIssue#setDoctorName(String)}
     *   <li>{@link T_PatientIssue#setFacilityID(Integer)}
     *   <li>{@link T_PatientIssue#setGender(String)}
     *   <li>{@link T_PatientIssue#setIssueType(String)}
     *   <li>{@link T_PatientIssue#setIssuedBy(String)}
     *   <li>{@link T_PatientIssue#setLastModDate(Date)}
     *   <li>{@link T_PatientIssue#setModifiedBy(String)}
     *   <li>{@link T_PatientIssue#setParkingPlaceID(Long)}
     *   <li>{@link T_PatientIssue#setPatientIssueID(Long)}
     *   <li>{@link T_PatientIssue#setPatientName(String)}
     *   <li>{@link T_PatientIssue#setPrescriptionID(Integer)}
     *   <li>{@link T_PatientIssue#setProcessed(Character)}
     *   <li>{@link T_PatientIssue#setProviderServiceMapID(Integer)}
     *   <li>{@link T_PatientIssue#setReference(String)}
     *   <li>{@link T_PatientIssue#setSyncFacilityID(Integer)}
     *   <li>{@link T_PatientIssue#setVanID(Long)}
     *   <li>{@link T_PatientIssue#setVanSerialNo(Long)}
     *   <li>{@link T_PatientIssue#setVisitCode(Long)}
     *   <li>{@link T_PatientIssue#setVisitID(Integer)}
     *   <li>{@link T_PatientIssue#getAge()}
     *   <li>{@link T_PatientIssue#getBenRegID()}
     *   <li>{@link T_PatientIssue#getBeneficiaryID()}
     *   <li>{@link T_PatientIssue#getCreatedBy()}
     *   <li>{@link T_PatientIssue#getCreatedDate()}
     *   <li>{@link T_PatientIssue#getDeleted()}
     *   <li>{@link T_PatientIssue#getDoctorName()}
     *   <li>{@link T_PatientIssue#getFacilityID()}
     *   <li>{@link T_PatientIssue#getGender()}
     *   <li>{@link T_PatientIssue#getIssueType()}
     *   <li>{@link T_PatientIssue#getIssuedBy()}
     *   <li>{@link T_PatientIssue#getLastModDate()}
     *   <li>{@link T_PatientIssue#getModifiedBy()}
     *   <li>{@link T_PatientIssue#getParkingPlaceID()}
     *   <li>{@link T_PatientIssue#getPatientIssueID()}
     *   <li>{@link T_PatientIssue#getPatientName()}
     *   <li>{@link T_PatientIssue#getPrescriptionID()}
     *   <li>{@link T_PatientIssue#getProcessed()}
     *   <li>{@link T_PatientIssue#getProviderServiceMapID()}
     *   <li>{@link T_PatientIssue#getReference()}
     *   <li>{@link T_PatientIssue#getSyncFacilityID()}
     *   <li>{@link T_PatientIssue#getVanID()}
     *   <li>{@link T_PatientIssue#getVanSerialNo()}
     *   <li>{@link T_PatientIssue#getVisitCode()}
     *   <li>{@link T_PatientIssue#getVisitID()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        T_PatientIssue t_PatientIssue = new T_PatientIssue();

        // Act
        t_PatientIssue.setAge(1);
        t_PatientIssue.setBenRegID(1L);
        t_PatientIssue.setBeneficiaryID(1L);
        t_PatientIssue.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        t_PatientIssue.setCreatedDate(createdDate);
        t_PatientIssue.setDeleted(true);
        t_PatientIssue.setDoctorName("Doctor Name");
        t_PatientIssue.setFacilityID(1);
        t_PatientIssue.setGender("Gender");
        t_PatientIssue.setIssueType("Issue Type");
        t_PatientIssue.setIssuedBy("Issued By");
        Date lastModDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        t_PatientIssue.setLastModDate(lastModDate);
        t_PatientIssue.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        t_PatientIssue.setParkingPlaceID(1L);
        t_PatientIssue.setPatientIssueID(1L);
        t_PatientIssue.setPatientName("Patient Name");
        t_PatientIssue.setPrescriptionID(1);
        t_PatientIssue.setProcessed('A');
        t_PatientIssue.setProviderServiceMapID(1);
        t_PatientIssue.setReference("Reference");
        t_PatientIssue.setSyncFacilityID(1);
        t_PatientIssue.setVanID(1L);
        t_PatientIssue.setVanSerialNo(1L);
        t_PatientIssue.setVisitCode(1L);
        t_PatientIssue.setVisitID(1);
        Integer actualAge = t_PatientIssue.getAge();
        Long actualBenRegID = t_PatientIssue.getBenRegID();
        Long actualBeneficiaryID = t_PatientIssue.getBeneficiaryID();
        String actualCreatedBy = t_PatientIssue.getCreatedBy();
        Date actualCreatedDate = t_PatientIssue.getCreatedDate();
        Boolean actualDeleted = t_PatientIssue.getDeleted();
        String actualDoctorName = t_PatientIssue.getDoctorName();
        Integer actualFacilityID = t_PatientIssue.getFacilityID();
        String actualGender = t_PatientIssue.getGender();
        String actualIssueType = t_PatientIssue.getIssueType();
        String actualIssuedBy = t_PatientIssue.getIssuedBy();
        Date actualLastModDate = t_PatientIssue.getLastModDate();
        String actualModifiedBy = t_PatientIssue.getModifiedBy();
        Long actualParkingPlaceID = t_PatientIssue.getParkingPlaceID();
        Long actualPatientIssueID = t_PatientIssue.getPatientIssueID();
        String actualPatientName = t_PatientIssue.getPatientName();
        Integer actualPrescriptionID = t_PatientIssue.getPrescriptionID();
        Character actualProcessed = t_PatientIssue.getProcessed();
        Integer actualProviderServiceMapID = t_PatientIssue.getProviderServiceMapID();
        String actualReference = t_PatientIssue.getReference();
        Integer actualSyncFacilityID = t_PatientIssue.getSyncFacilityID();
        Long actualVanID = t_PatientIssue.getVanID();
        Long actualVanSerialNo = t_PatientIssue.getVanSerialNo();
        Long actualVisitCode = t_PatientIssue.getVisitCode();
        Integer actualVisitID = t_PatientIssue.getVisitID();

        // Assert that nothing has changed
        assertEquals("Doctor Name", actualDoctorName);
        assertEquals("Gender", actualGender);
        assertEquals("Issue Type", actualIssueType);
        assertEquals("Issued By", actualIssuedBy);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Patient Name", actualPatientName);
        assertEquals("Reference", actualReference);
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualAge.intValue());
        assertEquals(1, actualFacilityID.intValue());
        assertEquals(1, actualPrescriptionID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualSyncFacilityID.intValue());
        assertEquals(1, actualVisitID.intValue());
        assertEquals(1L, actualBenRegID.longValue());
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualParkingPlaceID.longValue());
        assertEquals(1L, actualPatientIssueID.longValue());
        assertEquals(1L, actualVanID.longValue());
        assertEquals(1L, actualVanSerialNo.longValue());
        assertEquals(1L, actualVisitCode.longValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
