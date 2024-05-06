package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SyncDispenseDetailsRequest.class})
@ExtendWith(SpringExtension.class)
class SyncDispenseDetailsRequestDiffblueTest {
    @Autowired
    private SyncDispenseDetailsRequest syncDispenseDetailsRequest;

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SyncDispenseDetailsRequest#SyncDispenseDetailsRequest()}
     *   <li>{@link SyncDispenseDetailsRequest#setDrug_id(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstdt_expiry_date(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstdt_issue_date(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstnum_issue_no(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstnum_issue_qty(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstnum_itembrand_id(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHstnum_store_id(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setHststr_batch_sl_no(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setQuantity_dispensed(String)}
     *   <li>{@link SyncDispenseDetailsRequest#setQuantity_prescribed(String)}
     *   <li>{@link SyncDispenseDetailsRequest#getDrug_id()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstdt_expiry_date()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstdt_issue_date()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstnum_issue_no()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstnum_issue_qty()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstnum_itembrand_id()}
     *   <li>{@link SyncDispenseDetailsRequest#getHstnum_store_id()}
     *   <li>{@link SyncDispenseDetailsRequest#getHststr_batch_sl_no()}
     *   <li>{@link SyncDispenseDetailsRequest#getQuantity_dispensed()}
     *   <li>{@link SyncDispenseDetailsRequest#getQuantity_prescribed()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        SyncDispenseDetailsRequest actualSyncDispenseDetailsRequest = new SyncDispenseDetailsRequest();
        actualSyncDispenseDetailsRequest.setDrug_id("Drug id");
        actualSyncDispenseDetailsRequest.setHstdt_expiry_date("2020-03-01");
        actualSyncDispenseDetailsRequest.setHstdt_issue_date("2020-03-01");
        actualSyncDispenseDetailsRequest.setHstnum_issue_no("Hstnum issue no");
        actualSyncDispenseDetailsRequest.setHstnum_issue_qty("Hstnum issue qty");
        actualSyncDispenseDetailsRequest.setHstnum_itembrand_id("Hstnum itembrand id");
        actualSyncDispenseDetailsRequest.setHstnum_store_id("Hstnum store id");
        actualSyncDispenseDetailsRequest.setHststr_batch_sl_no("Hststr batch sl no");
        actualSyncDispenseDetailsRequest.setQuantity_dispensed("Quantity dispensed");
        actualSyncDispenseDetailsRequest.setQuantity_prescribed("Quantity prescribed");
        String actualDrug_id = actualSyncDispenseDetailsRequest.getDrug_id();
        String actualHstdt_expiry_date = actualSyncDispenseDetailsRequest.getHstdt_expiry_date();
        String actualHstdt_issue_date = actualSyncDispenseDetailsRequest.getHstdt_issue_date();
        String actualHstnum_issue_no = actualSyncDispenseDetailsRequest.getHstnum_issue_no();
        String actualHstnum_issue_qty = actualSyncDispenseDetailsRequest.getHstnum_issue_qty();
        String actualHstnum_itembrand_id = actualSyncDispenseDetailsRequest.getHstnum_itembrand_id();
        String actualHstnum_store_id = actualSyncDispenseDetailsRequest.getHstnum_store_id();
        String actualHststr_batch_sl_no = actualSyncDispenseDetailsRequest.getHststr_batch_sl_no();
        String actualQuantity_dispensed = actualSyncDispenseDetailsRequest.getQuantity_dispensed();

        // Assert that nothing has changed
        assertEquals("2020-03-01", actualHstdt_expiry_date);
        assertEquals("2020-03-01", actualHstdt_issue_date);
        assertEquals("Drug id", actualDrug_id);
        assertEquals("Hstnum issue no", actualHstnum_issue_no);
        assertEquals("Hstnum issue qty", actualHstnum_issue_qty);
        assertEquals("Hstnum itembrand id", actualHstnum_itembrand_id);
        assertEquals("Hstnum store id", actualHstnum_store_id);
        assertEquals("Hststr batch sl no", actualHststr_batch_sl_no);
        assertEquals("Quantity dispensed", actualQuantity_dispensed);
        assertEquals("Quantity prescribed", actualSyncDispenseDetailsRequest.getQuantity_prescribed());
    }

    /**
     * Method under test:
     * {@link SyncDispenseDetailsRequest#SyncDispenseDetailsRequest(String, String, String, String, String, String, String, String, String)}
     */
    @Test
    void testNewSyncDispenseDetailsRequest() {
        // Arrange and Act
        SyncDispenseDetailsRequest actualSyncDispenseDetailsRequest = new SyncDispenseDetailsRequest("Drug id",
                "Quantity prescribed", "Hstnum store id", "Hstnum issue no", "Hstnum itembrand id", "Hststr batch sl no",
                "2020-03-01", "Hstnum issue qty", "2020-03-01");

        // Assert
        assertEquals("2020-03-01", actualSyncDispenseDetailsRequest.getHstdt_expiry_date());
        assertEquals("2020-03-01", actualSyncDispenseDetailsRequest.getHstdt_issue_date());
        assertEquals("Drug id", actualSyncDispenseDetailsRequest.getDrug_id());
        assertEquals("Hstnum issue no", actualSyncDispenseDetailsRequest.getHstnum_issue_no());
        assertEquals("Hstnum issue qty", actualSyncDispenseDetailsRequest.getHstnum_issue_qty());
        assertEquals("Hstnum itembrand id", actualSyncDispenseDetailsRequest.getHstnum_itembrand_id());
        assertEquals("Hstnum store id", actualSyncDispenseDetailsRequest.getHstnum_store_id());
        assertEquals("Hststr batch sl no", actualSyncDispenseDetailsRequest.getHststr_batch_sl_no());
        assertEquals("Quantity prescribed", actualSyncDispenseDetailsRequest.getQuantity_prescribed());
    }
}
