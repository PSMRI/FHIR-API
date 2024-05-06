package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ConfirmAadhaarBioDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ConfirmAadhaarBio#setAuthType(String)}
     *   <li>{@link ConfirmAadhaarBio#setBioType(String)}
     *   <li>{@link ConfirmAadhaarBio#setPid(String)}
     *   <li>{@link ConfirmAadhaarBio#setTxnId(String)}
     *   <li>{@link ConfirmAadhaarBio#getAuthType()}
     *   <li>{@link ConfirmAadhaarBio#getBioType()}
     *   <li>{@link ConfirmAadhaarBio#getPid()}
     *   <li>{@link ConfirmAadhaarBio#getTxnId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ConfirmAadhaarBio confirmAadhaarBio = new ConfirmAadhaarBio();

        // Act
        confirmAadhaarBio.setAuthType("Auth Type");
        confirmAadhaarBio.setBioType("Bio Type");
        confirmAadhaarBio.setPid("Pid");
        confirmAadhaarBio.setTxnId("42");
        String actualAuthType = confirmAadhaarBio.getAuthType();
        String actualBioType = confirmAadhaarBio.getBioType();
        String actualPid = confirmAadhaarBio.getPid();

        // Assert that nothing has changed
        assertEquals("42", confirmAadhaarBio.getTxnId());
        assertEquals("Auth Type", actualAuthType);
        assertEquals("Bio Type", actualBioType);
        assertEquals("Pid", actualPid);
    }
}
