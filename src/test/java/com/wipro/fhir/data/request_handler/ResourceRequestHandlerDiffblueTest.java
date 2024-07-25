package com.wipro.fhir.data.request_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResourceRequestHandlerDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ResourceRequestHandler#setAmritId(String)}
     *   <li>{@link ResourceRequestHandler#setBenFlowID(Long)}
     *   <li>{@link ResourceRequestHandler#setBeneficiaryID(Long)}
     *   <li>{@link ResourceRequestHandler#setBeneficiaryRegID(Long)}
     *   <li>{@link ResourceRequestHandler#setDistrict(String)}
     *   <li>{@link ResourceRequestHandler#setExternalId(String)}
     *   <li>{@link ResourceRequestHandler#setHealthId(String)}
     *   <li>{@link ResourceRequestHandler#setHealthIdNumber(String)}
     *   <li>{@link ResourceRequestHandler#setPageNo(Integer)}
     *   <li>{@link ResourceRequestHandler#setPhoneNo(String)}
     *   <li>{@link ResourceRequestHandler#setState(String)}
     *   <li>{@link ResourceRequestHandler#setVillage(String)}
     *   <li>{@link ResourceRequestHandler#setVisitCode(Long)}
     *   <li>{@link ResourceRequestHandler#getAmritId()}
     *   <li>{@link ResourceRequestHandler#getBenFlowID()}
     *   <li>{@link ResourceRequestHandler#getBeneficiaryID()}
     *   <li>{@link ResourceRequestHandler#getBeneficiaryRegID()}
     *   <li>{@link ResourceRequestHandler#getDistrict()}
     *   <li>{@link ResourceRequestHandler#getExternalId()}
     *   <li>{@link ResourceRequestHandler#getHealthId()}
     *   <li>{@link ResourceRequestHandler#getHealthIdNumber()}
     *   <li>{@link ResourceRequestHandler#getPageNo()}
     *   <li>{@link ResourceRequestHandler#getPhoneNo()}
     *   <li>{@link ResourceRequestHandler#getState()}
     *   <li>{@link ResourceRequestHandler#getVillage()}
     *   <li>{@link ResourceRequestHandler#getVisitCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ResourceRequestHandler resourceRequestHandler = new ResourceRequestHandler();

        // Act
        resourceRequestHandler.setAmritId("42");
        resourceRequestHandler.setBenFlowID(1L);
        resourceRequestHandler.setBeneficiaryID(1L);
        resourceRequestHandler.setBeneficiaryRegID(1L);
        resourceRequestHandler.setDistrict("District");
        resourceRequestHandler.setExternalId("42");
        resourceRequestHandler.setHealthId("42");
        resourceRequestHandler.setHealthIdNumber("42");
        resourceRequestHandler.setPageNo(1);
        resourceRequestHandler.setPhoneNo("6625550144");
        resourceRequestHandler.setState("MD");
        resourceRequestHandler.setVillage("Village");
        resourceRequestHandler.setVisitCode(1L);
        String actualAmritId = resourceRequestHandler.getAmritId();
        Long actualBenFlowID = resourceRequestHandler.getBenFlowID();
        Long actualBeneficiaryID = resourceRequestHandler.getBeneficiaryID();
        Long actualBeneficiaryRegID = resourceRequestHandler.getBeneficiaryRegID();
        String actualDistrict = resourceRequestHandler.getDistrict();
        String actualExternalId = resourceRequestHandler.getExternalId();
        String actualHealthId = resourceRequestHandler.getHealthId();
        String actualHealthIdNumber = resourceRequestHandler.getHealthIdNumber();
        Integer actualPageNo = resourceRequestHandler.getPageNo();
        String actualPhoneNo = resourceRequestHandler.getPhoneNo();
        String actualState = resourceRequestHandler.getState();
        String actualVillage = resourceRequestHandler.getVillage();
        Long actualVisitCode = resourceRequestHandler.getVisitCode();

        // Assert that nothing has changed
        assertEquals("42", actualAmritId);
        assertEquals("42", actualExternalId);
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthIdNumber);
        assertEquals("6625550144", actualPhoneNo);
        assertEquals("District", actualDistrict);
        assertEquals("MD", actualState);
        assertEquals("Village", actualVillage);
        assertEquals(1, actualPageNo.intValue());
        assertEquals(1L, actualBenFlowID.longValue());
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualBeneficiaryRegID.longValue());
        assertEquals(1L, actualVisitCode.longValue());
    }
}
