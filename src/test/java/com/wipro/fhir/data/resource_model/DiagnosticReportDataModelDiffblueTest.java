package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DiagnosticReportDataModel.class})
@ExtendWith(SpringExtension.class)
class DiagnosticReportDataModelDiffblueTest {
    @Autowired
    private DiagnosticReportDataModel diagnosticReportDataModel;

    /**
     * Method under test:
     * {@link DiagnosticReportDataModel#getDiagnosticReportList(List)}
     */
    @Test
    void testGetDiagnosticReportList() {
        // Arrange, Act and Assert
        assertTrue(diagnosticReportDataModel.getDiagnosticReportList(new ArrayList<>()).isEmpty());
    }
}
