package com.wipro.fhir.data.mongo.amrit_resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class AMRIT_ResourceMongoDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AMRIT_ResourceMongo#setBeneficiaryID(Long)}
     *   <li>{@link AMRIT_ResourceMongo#setBeneficiaryRegID(Long)}
     *   <li>{@link AMRIT_ResourceMongo#setCreateDate(Timestamp)}
     *   <li>{@link AMRIT_ResourceMongo#setId(String)}
     *   <li>{@link AMRIT_ResourceMongo#setNationalHealthID(String)}
     *   <li>{@link AMRIT_ResourceMongo#setResourceJson(String)}
     *   <li>{@link AMRIT_ResourceMongo#setResourceType(String)}
     *   <li>{@link AMRIT_ResourceMongo#setVisitCode(Long)}
     *   <li>{@link AMRIT_ResourceMongo#getBeneficiaryID()}
     *   <li>{@link AMRIT_ResourceMongo#getBeneficiaryRegID()}
     *   <li>{@link AMRIT_ResourceMongo#getCreateDate()}
     *   <li>{@link AMRIT_ResourceMongo#getId()}
     *   <li>{@link AMRIT_ResourceMongo#getNationalHealthID()}
     *   <li>{@link AMRIT_ResourceMongo#getResourceJson()}
     *   <li>{@link AMRIT_ResourceMongo#getResourceType()}
     *   <li>{@link AMRIT_ResourceMongo#getVisitCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        AMRIT_ResourceMongo amrit_ResourceMongo = new AMRIT_ResourceMongo();

        // Act
        amrit_ResourceMongo.setBeneficiaryID(1L);
        amrit_ResourceMongo.setBeneficiaryRegID(1L);
        Timestamp createDate = mock(Timestamp.class);
        amrit_ResourceMongo.setCreateDate(createDate);
        amrit_ResourceMongo.setId("42");
        amrit_ResourceMongo.setNationalHealthID("National Health ID");
        amrit_ResourceMongo.setResourceJson("Resource Json");
        amrit_ResourceMongo.setResourceType("Resource Type");
        amrit_ResourceMongo.setVisitCode(1L);
        Long actualBeneficiaryID = amrit_ResourceMongo.getBeneficiaryID();
        Long actualBeneficiaryRegID = amrit_ResourceMongo.getBeneficiaryRegID();
        Timestamp actualCreateDate = amrit_ResourceMongo.getCreateDate();
        String actualId = amrit_ResourceMongo.getId();
        String actualNationalHealthID = amrit_ResourceMongo.getNationalHealthID();
        String actualResourceJson = amrit_ResourceMongo.getResourceJson();
        String actualResourceType = amrit_ResourceMongo.getResourceType();
        Long actualVisitCode = amrit_ResourceMongo.getVisitCode();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("National Health ID", actualNationalHealthID);
        assertEquals("Resource Json", actualResourceJson);
        assertEquals("Resource Type", actualResourceType);
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualBeneficiaryRegID.longValue());
        assertEquals(1L, actualVisitCode.longValue());
        assertSame(createDate, actualCreateDate);
    }
}
