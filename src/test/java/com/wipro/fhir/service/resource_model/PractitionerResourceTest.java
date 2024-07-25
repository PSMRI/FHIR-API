package com.wipro.fhir.service.resource_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PractitionerResourceTest {
    @InjectMocks
    private PractitionerResource practitionerResource;

    @Test
    void testGetPractitioner() {
        // Arrange and Act
        Practitioner actualPractitioner = practitionerResource.getPractitioner();

        // Assert
        HumanName nameFirstRep = actualPractitioner.getNameFirstRep();
        assertEquals("Harsh Dhave", nameFirstRep.getText());
        IdType idElement = actualPractitioner.getIdElement();
        assertEquals("MAX1456", idElement.getIdPart());
        Identifier identifierFirstRep = actualPractitioner.getIdentifierFirstRep();
        assertEquals("MAX1456", identifierFirstRep.getValue());
        assertEquals("MBBS", nameFirstRep.getPrefixAsSingleString());
        assertEquals("Practitioner", idElement.getResourceType());
        assertEquals("Practitioner/MAX1456", idElement.getValue());
        assertEquals("Practitioner/MAX1456", actualPractitioner.getId());
        assertEquals("https://www.mciindia.in/doctor", identifierFirstRep.getSystem());
        UriType systemElement = identifierFirstRep.getSystemElement();
        assertEquals("https://www.mciindia.in/doctor", systemElement.getValue());
        assertEquals("https://www.mciindia.in/doctor", systemElement.getValueAsString());
        assertNull(idElement.getBaseUrl());
        assertNull(idElement.getVersionIdPart());
        assertEquals(1, actualPractitioner.getIdentifier().size());
        assertEquals(1, actualPractitioner.getName().size());
        assertFalse(nameFirstRep.hasSuffix());
        assertTrue(nameFirstRep.hasPrefix());
        assertTrue(nameFirstRep.hasText());
        assertTrue(identifierFirstRep.hasValue());
    }
}
