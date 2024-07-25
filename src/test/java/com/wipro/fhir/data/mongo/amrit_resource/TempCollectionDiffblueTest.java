package com.wipro.fhir.data.mongo.amrit_resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TempCollectionDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link TempCollection#setBeneficiaryRegID(Long)}
     *   <li>{@link TempCollection#setCreateBy(String)}
     *   <li>{@link TempCollection#setCreateDate(Date)}
     *   <li>{@link TempCollection#setDataJson(Map)}
     *   <li>{@link TempCollection#setDataType(String)}
     *   <li>{@link TempCollection#setId(String)}
     *   <li>{@link TempCollection#setVisitCode(Long)}
     *   <li>{@link TempCollection#getBeneficiaryRegID()}
     *   <li>{@link TempCollection#getCreateBy()}
     *   <li>{@link TempCollection#getCreateDate()}
     *   <li>{@link TempCollection#getDataJson()}
     *   <li>{@link TempCollection#getDataType()}
     *   <li>{@link TempCollection#getId()}
     *   <li>{@link TempCollection#getVisitCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        TempCollection tempCollection = new TempCollection();

        // Act
        tempCollection.setBeneficiaryRegID(1L);
        tempCollection.setCreateBy("Create By");
        Date createDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        tempCollection.setCreateDate(createDate);
        HashMap<Integer, List<String>> dataJson = new HashMap<>();
        tempCollection.setDataJson(dataJson);
        tempCollection.setDataType("Data Type");
        tempCollection.setId("42");
        tempCollection.setVisitCode(1L);
        Long actualBeneficiaryRegID = tempCollection.getBeneficiaryRegID();
        String actualCreateBy = tempCollection.getCreateBy();
        Date actualCreateDate = tempCollection.getCreateDate();
        Map<Integer, List<String>> actualDataJson = tempCollection.getDataJson();
        String actualDataType = tempCollection.getDataType();
        String actualId = tempCollection.getId();
        Long actualVisitCode = tempCollection.getVisitCode();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Create By", actualCreateBy);
        assertEquals("Data Type", actualDataType);
        assertEquals(1L, actualBeneficiaryRegID.longValue());
        assertEquals(1L, actualVisitCode.longValue());
        assertSame(dataJson, actualDataJson);
        assertSame(createDate, actualCreateDate);
    }
}
