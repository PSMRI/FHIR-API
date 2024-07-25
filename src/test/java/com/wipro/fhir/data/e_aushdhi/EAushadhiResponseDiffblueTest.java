package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EAushadhiResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link EAushadhiResponse#setBatchno(String)}
     *   <li>{@link EAushadhiResponse#setBrandid(String)}
     *   <li>{@link EAushadhiResponse#setCategory(String)}
     *   <li>{@link EAushadhiResponse#setCpacode(String)}
     *   <li>{@link EAushadhiResponse#setDrugname(String)}
     *   <li>{@link EAushadhiResponse#setEdl(String)}
     *   <li>{@link EAushadhiResponse#setExpdate(String)}
     *   <li>{@link EAushadhiResponse#setInhandqty(String)}
     *   <li>{@link EAushadhiResponse#setItemtypename(String)}
     *   <li>{@link EAushadhiResponse#setMfgdate(String)}
     *   <li>{@link EAushadhiResponse#setPono(String)}
     *   <li>{@link EAushadhiResponse#setPrgid(String)}
     *   <li>{@link EAushadhiResponse#setSpecification(String)}
     *   <li>{@link EAushadhiResponse#setStockstatus(String)}
     *   <li>{@link EAushadhiResponse#getBatchno()}
     *   <li>{@link EAushadhiResponse#getBrandid()}
     *   <li>{@link EAushadhiResponse#getCategory()}
     *   <li>{@link EAushadhiResponse#getCpacode()}
     *   <li>{@link EAushadhiResponse#getDrugname()}
     *   <li>{@link EAushadhiResponse#getEdl()}
     *   <li>{@link EAushadhiResponse#getExpdate()}
     *   <li>{@link EAushadhiResponse#getInhandqty()}
     *   <li>{@link EAushadhiResponse#getItemtypename()}
     *   <li>{@link EAushadhiResponse#getMfgdate()}
     *   <li>{@link EAushadhiResponse#getPono()}
     *   <li>{@link EAushadhiResponse#getPrgid()}
     *   <li>{@link EAushadhiResponse#getSpecification()}
     *   <li>{@link EAushadhiResponse#getStockstatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        EAushadhiResponse eAushadhiResponse = new EAushadhiResponse();

        // Act
        eAushadhiResponse.setBatchno("Batchno");
        eAushadhiResponse.setBrandid("Brandid");
        eAushadhiResponse.setCategory("Category");
        eAushadhiResponse.setCpacode("Cpacode");
        eAushadhiResponse.setDrugname("Drugname");
        eAushadhiResponse.setEdl("Edl");
        eAushadhiResponse.setExpdate("2020-03-01");
        eAushadhiResponse.setInhandqty("Inhandqty");
        eAushadhiResponse.setItemtypename("Itemtypename");
        eAushadhiResponse.setMfgdate("2020-03-01");
        eAushadhiResponse.setPono("Pono");
        eAushadhiResponse.setPrgid("Prgid");
        eAushadhiResponse.setSpecification("Specification");
        eAushadhiResponse.setStockstatus("Stockstatus");
        String actualBatchno = eAushadhiResponse.getBatchno();
        String actualBrandid = eAushadhiResponse.getBrandid();
        String actualCategory = eAushadhiResponse.getCategory();
        String actualCpacode = eAushadhiResponse.getCpacode();
        String actualDrugname = eAushadhiResponse.getDrugname();
        String actualEdl = eAushadhiResponse.getEdl();
        String actualExpdate = eAushadhiResponse.getExpdate();
        String actualInhandqty = eAushadhiResponse.getInhandqty();
        String actualItemtypename = eAushadhiResponse.getItemtypename();
        String actualMfgdate = eAushadhiResponse.getMfgdate();
        String actualPono = eAushadhiResponse.getPono();
        String actualPrgid = eAushadhiResponse.getPrgid();
        String actualSpecification = eAushadhiResponse.getSpecification();

        // Assert that nothing has changed
        assertEquals("2020-03-01", actualExpdate);
        assertEquals("2020-03-01", actualMfgdate);
        assertEquals("Batchno", actualBatchno);
        assertEquals("Brandid", actualBrandid);
        assertEquals("Category", actualCategory);
        assertEquals("Cpacode", actualCpacode);
        assertEquals("Drugname", actualDrugname);
        assertEquals("Edl", actualEdl);
        assertEquals("Inhandqty", actualInhandqty);
        assertEquals("Itemtypename", actualItemtypename);
        assertEquals("Pono", actualPono);
        assertEquals("Prgid", actualPrgid);
        assertEquals("Specification", actualSpecification);
        assertEquals("Stockstatus", eAushadhiResponse.getStockstatus());
    }
}
