package com.wipro.fhir.data.healthID_validate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddressDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Address#setDistrict(String)}
     *   <li>{@link Address#setLine(String)}
     *   <li>{@link Address#setPinCode(String)}
     *   <li>{@link Address#setState(String)}
     *   <li>{@link Address#getDistrict()}
     *   <li>{@link Address#getLine()}
     *   <li>{@link Address#getPinCode()}
     *   <li>{@link Address#getState()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Address address = new Address();

        // Act
        address.setDistrict("District");
        address.setLine("Line");
        address.setPinCode("Pin Code");
        address.setState("MD");
        String actualDistrict = address.getDistrict();
        String actualLine = address.getLine();
        String actualPinCode = address.getPinCode();

        // Assert that nothing has changed
        assertEquals("District", actualDistrict);
        assertEquals("Line", actualLine);
        assertEquals("MD", address.getState());
        assertEquals("Pin Code", actualPinCode);
    }
}
