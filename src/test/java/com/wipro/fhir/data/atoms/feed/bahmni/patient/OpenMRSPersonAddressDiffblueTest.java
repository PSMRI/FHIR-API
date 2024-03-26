package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OpenMRSPersonAddress.class})
@ExtendWith(SpringExtension.class)
class OpenMRSPersonAddressDiffblueTest {
    @Autowired
    private OpenMRSPersonAddress openMRSPersonAddress;

    /**
     * Method under test: {@link OpenMRSPersonAddress#canEqual(Object)}
     */
    @Test
    void testCanEqual() {
        // Arrange, Act and Assert
        assertFalse(openMRSPersonAddress.canEqual("Other"));
        assertTrue(openMRSPersonAddress.canEqual(openMRSPersonAddress));
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals() {
        // Arrange, Act and Assert
        assertNotEquals(new OpenMRSPersonAddress(), null);
        assertNotEquals(new OpenMRSPersonAddress(), "Different type to OpenMRSPersonAddress");
        assertNotEquals(new OpenMRSPersonAddress(), 1);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals2() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals3() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setPreferred(true);

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals4() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setVoided(true);

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals5() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress1("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals6() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress2("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals7() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress3("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals8() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress4("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals9() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress5("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals10() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress6("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals11() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress7("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals12() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setAddress8("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals13() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setCityVillage("Oxford");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals14() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setCountyDistrict("3");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals15() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setStateProvince("MD");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals16() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setCountry("GB");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals17() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        openMRSPersonAddress.setPostalCode("Postal Code");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, new OpenMRSPersonAddress());
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals18() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals19() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress1("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals20() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress2("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals21() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress3("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals22() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress4("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals23() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress5("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals24() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress6("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals25() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress7("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals26() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setAddress8("42 Main St");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals27() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setCityVillage("Oxford");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals28() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setCountyDistrict("3");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals29() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setStateProvince("MD");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals30() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setCountry("GB");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAddress#equals(Object)}
     */
    @Test
    void testEquals31() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();
        openMRSPersonAddress2.setPostalCode("Postal Code");

        // Act and Assert
        assertNotEquals(openMRSPersonAddress, openMRSPersonAddress2);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAddress#equals(Object)}
     *   <li>{@link OpenMRSPersonAddress#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        // Act and Assert
        assertEquals(openMRSPersonAddress, openMRSPersonAddress);
        int expectedHashCodeResult = openMRSPersonAddress.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAddress.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAddress#equals(Object)}
     *   <li>{@link OpenMRSPersonAddress#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode2() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();
        OpenMRSPersonAddress openMRSPersonAddress2 = new OpenMRSPersonAddress();

        // Act and Assert
        assertEquals(openMRSPersonAddress, openMRSPersonAddress2);
        int expectedHashCodeResult = openMRSPersonAddress.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAddress2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAddress#setAddress1(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress2(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress3(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress4(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress5(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress6(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress7(String)}
     *   <li>{@link OpenMRSPersonAddress#setAddress8(String)}
     *   <li>{@link OpenMRSPersonAddress#setCityVillage(String)}
     *   <li>{@link OpenMRSPersonAddress#setCountry(String)}
     *   <li>{@link OpenMRSPersonAddress#setCountyDistrict(String)}
     *   <li>{@link OpenMRSPersonAddress#setPostalCode(String)}
     *   <li>{@link OpenMRSPersonAddress#setPreferred(boolean)}
     *   <li>{@link OpenMRSPersonAddress#setStateProvince(String)}
     *   <li>{@link OpenMRSPersonAddress#setUuid(String)}
     *   <li>{@link OpenMRSPersonAddress#setVoided(boolean)}
     *   <li>{@link OpenMRSPersonAddress#toString()}
     *   <li>{@link OpenMRSPersonAddress#getAddress1()}
     *   <li>{@link OpenMRSPersonAddress#getAddress2()}
     *   <li>{@link OpenMRSPersonAddress#getAddress3()}
     *   <li>{@link OpenMRSPersonAddress#getAddress4()}
     *   <li>{@link OpenMRSPersonAddress#getAddress5()}
     *   <li>{@link OpenMRSPersonAddress#getAddress6()}
     *   <li>{@link OpenMRSPersonAddress#getAddress7()}
     *   <li>{@link OpenMRSPersonAddress#getAddress8()}
     *   <li>{@link OpenMRSPersonAddress#getCityVillage()}
     *   <li>{@link OpenMRSPersonAddress#getCountry()}
     *   <li>{@link OpenMRSPersonAddress#getCountyDistrict()}
     *   <li>{@link OpenMRSPersonAddress#getPostalCode()}
     *   <li>{@link OpenMRSPersonAddress#getStateProvince()}
     *   <li>{@link OpenMRSPersonAddress#getUuid()}
     *   <li>{@link OpenMRSPersonAddress#isPreferred()}
     *   <li>{@link OpenMRSPersonAddress#isVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPersonAddress openMRSPersonAddress = new OpenMRSPersonAddress();

        // Act
        openMRSPersonAddress.setAddress1("42 Main St");
        openMRSPersonAddress.setAddress2("42 Main St");
        openMRSPersonAddress.setAddress3("42 Main St");
        openMRSPersonAddress.setAddress4("42 Main St");
        openMRSPersonAddress.setAddress5("42 Main St");
        openMRSPersonAddress.setAddress6("42 Main St");
        openMRSPersonAddress.setAddress7("42 Main St");
        openMRSPersonAddress.setAddress8("42 Main St");
        openMRSPersonAddress.setCityVillage("Oxford");
        openMRSPersonAddress.setCountry("GB");
        openMRSPersonAddress.setCountyDistrict("3");
        openMRSPersonAddress.setPostalCode("Postal Code");
        openMRSPersonAddress.setPreferred(true);
        openMRSPersonAddress.setStateProvince("MD");
        openMRSPersonAddress.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        openMRSPersonAddress.setVoided(true);
        String actualToStringResult = openMRSPersonAddress.toString();
        String actualAddress1 = openMRSPersonAddress.getAddress1();
        String actualAddress2 = openMRSPersonAddress.getAddress2();
        String actualAddress3 = openMRSPersonAddress.getAddress3();
        String actualAddress4 = openMRSPersonAddress.getAddress4();
        String actualAddress5 = openMRSPersonAddress.getAddress5();
        String actualAddress6 = openMRSPersonAddress.getAddress6();
        String actualAddress7 = openMRSPersonAddress.getAddress7();
        String actualAddress8 = openMRSPersonAddress.getAddress8();
        String actualCityVillage = openMRSPersonAddress.getCityVillage();
        String actualCountry = openMRSPersonAddress.getCountry();
        String actualCountyDistrict = openMRSPersonAddress.getCountyDistrict();
        String actualPostalCode = openMRSPersonAddress.getPostalCode();
        String actualStateProvince = openMRSPersonAddress.getStateProvince();
        String actualUuid = openMRSPersonAddress.getUuid();
        boolean actualIsPreferredResult = openMRSPersonAddress.isPreferred();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertEquals("3", actualCountyDistrict);
        assertEquals("42 Main St", actualAddress1);
        assertEquals("42 Main St", actualAddress2);
        assertEquals("42 Main St", actualAddress3);
        assertEquals("42 Main St", actualAddress4);
        assertEquals("42 Main St", actualAddress5);
        assertEquals("42 Main St", actualAddress6);
        assertEquals("42 Main St", actualAddress7);
        assertEquals("42 Main St", actualAddress8);
        assertEquals("GB", actualCountry);
        assertEquals("MD", actualStateProvince);
        assertEquals(
                "OpenMRSPersonAddress(uuid=01234567-89AB-CDEF-FEDC-BA9876543210, preferred=true, voided=true, address1=42"
                        + " Main St, address2=42 Main St, address3=42 Main St, address4=42 Main St, address5=42 Main St, address6=42"
                        + " Main St, address7=42 Main St, address8=42 Main St, cityVillage=Oxford, countyDistrict=3, stateProvince=MD,"
                        + " country=GB, postalCode=Postal Code)",
                actualToStringResult);
        assertEquals("Oxford", actualCityVillage);
        assertEquals("Postal Code", actualPostalCode);
        assertTrue(actualIsPreferredResult);
        assertTrue(openMRSPersonAddress.isVoided());
    }
}
