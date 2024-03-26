package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VerifyBioDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link VerifyBio#setAadhaar(String)}
     *   <li>{@link VerifyBio#setBioType(String)}
     *   <li>{@link VerifyBio#setPid(String)}
     *   <li>{@link VerifyBio#getAadhaar()}
     *   <li>{@link VerifyBio#getBioType()}
     *   <li>{@link VerifyBio#getPid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        VerifyBio verifyBio = new VerifyBio();

        // Act
        verifyBio.setAadhaar("Aadhaar");
        verifyBio.setBioType("Bio Type");
        verifyBio.setPid("Pid");
        String actualAadhaar = verifyBio.getAadhaar();
        String actualBioType = verifyBio.getBioType();

        // Assert that nothing has changed
        assertEquals("Aadhaar", actualAadhaar);
        assertEquals("Bio Type", actualBioType);
        assertEquals("Pid", verifyBio.getPid());
    }
}
