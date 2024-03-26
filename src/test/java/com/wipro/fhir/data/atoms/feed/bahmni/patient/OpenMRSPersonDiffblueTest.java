package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class OpenMRSPersonDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPerson#setAddresses(List)}
     *   <li>{@link OpenMRSPerson#setAge(int)}
     *   <li>{@link OpenMRSPerson#setAttributes(List)}
     *   <li>{@link OpenMRSPerson#setBirthdate(Date)}
     *   <li>{@link OpenMRSPerson#setBirthdateEstimated(boolean)}
     *   <li>{@link OpenMRSPerson#setBirthtime(String)}
     *   <li>{@link OpenMRSPerson#setDead(boolean)}
     *   <li>{@link OpenMRSPerson#setDeathDate(Date)}
     *   <li>{@link OpenMRSPerson#setDeathdateEstimated(boolean)}
     *   <li>{@link OpenMRSPerson#setDisplay(String)}
     *   <li>{@link OpenMRSPerson#setGender(String)}
     *   <li>{@link OpenMRSPerson#setNames(List)}
     *   <li>{@link OpenMRSPerson#setPreferredAddress(OpenMRSPersonAddress)}
     *   <li>{@link OpenMRSPerson#setPreferredName(OpenMRSPersonName)}
     *   <li>{@link OpenMRSPerson#setUuid(String)}
     *   <li>{@link OpenMRSPerson#getAddresses()}
     *   <li>{@link OpenMRSPerson#getAge()}
     *   <li>{@link OpenMRSPerson#getAttributes()}
     *   <li>{@link OpenMRSPerson#getBirthdate()}
     *   <li>{@link OpenMRSPerson#getBirthtime()}
     *   <li>{@link OpenMRSPerson#getDeathDate()}
     *   <li>{@link OpenMRSPerson#getDisplay()}
     *   <li>{@link OpenMRSPerson#getGender()}
     *   <li>{@link OpenMRSPerson#getNames()}
     *   <li>{@link OpenMRSPerson#getPreferredAddress()}
     *   <li>{@link OpenMRSPerson#getPreferredName()}
     *   <li>{@link OpenMRSPerson#getUuid()}
     *   <li>{@link OpenMRSPerson#isBirthdateEstimated()}
     *   <li>{@link OpenMRSPerson#isDead()}
     *   <li>{@link OpenMRSPerson#isDeathdateEstimated()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPerson openMRSPerson = new OpenMRSPerson();
        ArrayList<OpenMRSPersonAddress> addresses = new ArrayList<>();

        // Act
        openMRSPerson.setAddresses(addresses);
        openMRSPerson.setAge(1);
        ArrayList<OpenMRSPersonAttribute> attributes = new ArrayList<>();
        openMRSPerson.setAttributes(attributes);
        Date birthdate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        openMRSPerson.setBirthdate(birthdate);
        openMRSPerson.setBirthdateEstimated(true);
        openMRSPerson.setBirthtime("Birthtime");
        openMRSPerson.setDead(true);
        Date deathDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        openMRSPerson.setDeathDate(deathDate);
        openMRSPerson.setDeathdateEstimated(true);
        openMRSPerson.setDisplay("Display");
        openMRSPerson.setGender("Gender");
        ArrayList<OpenMRSPersonName> names = new ArrayList<>();
        openMRSPerson.setNames(names);
        OpenMRSPersonAddress preferredAddress = new OpenMRSPersonAddress();
        openMRSPerson.setPreferredAddress(preferredAddress);
        OpenMRSPersonName preferredName = new OpenMRSPersonName();
        openMRSPerson.setPreferredName(preferredName);
        openMRSPerson.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        List<OpenMRSPersonAddress> actualAddresses = openMRSPerson.getAddresses();
        int actualAge = openMRSPerson.getAge();
        List<OpenMRSPersonAttribute> actualAttributes = openMRSPerson.getAttributes();
        Date actualBirthdate = openMRSPerson.getBirthdate();
        String actualBirthtime = openMRSPerson.getBirthtime();
        Date actualDeathDate = openMRSPerson.getDeathDate();
        String actualDisplay = openMRSPerson.getDisplay();
        String actualGender = openMRSPerson.getGender();
        List<OpenMRSPersonName> actualNames = openMRSPerson.getNames();
        OpenMRSPersonAddress actualPreferredAddress = openMRSPerson.getPreferredAddress();
        OpenMRSPersonName actualPreferredName = openMRSPerson.getPreferredName();
        String actualUuid = openMRSPerson.getUuid();
        boolean actualIsBirthdateEstimatedResult = openMRSPerson.isBirthdateEstimated();
        boolean actualIsDeadResult = openMRSPerson.isDead();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertEquals("Birthtime", actualBirthtime);
        assertEquals("Display", actualDisplay);
        assertEquals("Gender", actualGender);
        assertEquals(1, actualAge);
        assertTrue(actualIsBirthdateEstimatedResult);
        assertTrue(actualIsDeadResult);
        assertTrue(openMRSPerson.isDeathdateEstimated());
        assertSame(preferredAddress, actualPreferredAddress);
        assertSame(preferredName, actualPreferredName);
        assertSame(addresses, actualAddresses);
        assertSame(attributes, actualAttributes);
        assertSame(names, actualNames);
        assertSame(birthdate, actualBirthdate);
        assertSame(deathDate, actualDeathDate);
    }
}
