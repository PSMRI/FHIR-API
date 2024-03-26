package com.wipro.fhir.service.resource_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrganizationResource.class})
@ExtendWith(SpringExtension.class)
class OrganizationResourceDiffblueTest {
    @Autowired
    private OrganizationResource organizationResource;

    /**
     * Method under test: {@link OrganizationResource#getOrganization()}
     */
    @Test
    void testGetOrganization() {
        // Arrange and Act
        Organization actualOrganization = organizationResource.getOrganization();

        // Assert
        ContactPoint telecomFirstRep = actualOrganization.getTelecomFirstRep();
        assertEquals("(+91) 011-2651-5050", telecomFirstRep.getValue());
        Identifier identifierFirstRep = actualOrganization.getIdentifierFirstRep();
        assertEquals("IN0410000183", identifierFirstRep.getValue());
        StringType valueElement = identifierFirstRep.getValueElement();
        assertEquals("IN0410000183", valueElement.getValue());
        assertEquals("IN0410000183", valueElement.getValueAsString());
        Address addressFirstRep = actualOrganization.getAddressFirstRep();
        assertEquals("India", addressFirstRep.getCountry());
        assertEquals("Max Super Speciality Hospital, Saket", actualOrganization.getName());
        StringType nameElement = actualOrganization.getNameElement();
        assertEquals("Max Super Speciality Hospital, Saket", nameElement.getValue());
        assertEquals("Max Super Speciality Hospital, Saket", nameElement.getValueAsString());
        IdType idElement = actualOrganization.getIdElement();
        assertEquals("MaxSaket01", idElement.getIdPart());
        assertEquals("New Delhi", addressFirstRep.getCity());
        assertEquals("New Delhi", addressFirstRep.getPostalCode());
        assertEquals("New Delhi", addressFirstRep.getState());
        assertEquals("Organization", idElement.getResourceType());
        assertEquals("Organization/MaxSaket01", idElement.getValue());
        assertEquals("Organization/MaxSaket01", actualOrganization.getId());
        Reference endpointFirstRep = actualOrganization.getEndpointFirstRep();
        assertEquals("Website", endpointFirstRep.getDisplay());
        assertEquals("https://facilitysbx.ndhm.gov.in", identifierFirstRep.getSystem());
        UriType systemElement = identifierFirstRep.getSystemElement();
        assertEquals("https://facilitysbx.ndhm.gov.in", systemElement.getValue());
        assertEquals("https://facilitysbx.ndhm.gov.in", systemElement.getValueAsString());
        assertEquals("https://www.max.in/hospital-network/max-super-speciality-hospital-saket",
                endpointFirstRep.getReference());
        assertNull(idElement.getBaseUrl());
        assertNull(idElement.getVersionIdPart());
        assertEquals(1, actualOrganization.getAddress().size());
        assertEquals(1, actualOrganization.getAlias().size());
        assertEquals(1, actualOrganization.getEndpoint().size());
        assertEquals(1, actualOrganization.getIdentifier().size());
        assertEquals(1, actualOrganization.getTelecom().size());
        assertEquals(ContactPoint.ContactPointSystem.PHONE, telecomFirstRep.getSystem());
        assertTrue(addressFirstRep.hasCity());
        assertTrue(addressFirstRep.hasCountry());
        assertTrue(addressFirstRep.hasLine());
        assertTrue(addressFirstRep.hasPostalCode());
        assertTrue(addressFirstRep.hasState());
        assertTrue(telecomFirstRep.hasSystem());
        assertTrue(telecomFirstRep.hasValue());
        assertTrue(actualOrganization.hasAlias());
        assertTrue(endpointFirstRep.hasDisplay());
        assertTrue(endpointFirstRep.hasReference());
    }
}
