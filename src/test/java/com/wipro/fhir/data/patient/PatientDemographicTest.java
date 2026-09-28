/*
* AMRIT - Accessible Medical Records via Integrated Technologies
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.data.patient;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("PatientDemographic row mapper Test Suite")
class PatientDemographicTest {

    private static final Timestamp DOB = new Timestamp(650_000_000_000L);

    private Object[] row() {
        return new Object[] { "abc@sbx", "11-1111-1111-1111", 4321L, 9999L, "Asha Devi", 2, "Female", 1,
                "Married", DOB, "9999999999", "India", "Jharkhand", "Ranchi" };
    }

    @Test
    @DisplayName("getPatientDemographic should map every demographic column in order")
    void getPatientDemographic_shouldMapEveryColumn() {
        PatientDemographic mapped = new PatientDemographic().getPatientDemographic(List.<Object[]>of(row()));

        assertEquals("abc@sbx", mapped.getHealthID());
        assertEquals("11-1111-1111-1111", mapped.getHealthIdNo());
        assertEquals(BigInteger.valueOf(4321L), mapped.getBeneficiaryRegID());
        assertEquals(BigInteger.valueOf(9999L), mapped.getBeneficiaryID());
        assertEquals("Asha Devi", mapped.getName());
        assertEquals(2, mapped.getGenderID());
        assertEquals("Female", mapped.getGender());
        assertEquals(1, mapped.getMaritalStatusID());
        assertEquals("Married", mapped.getMaritalStatus());
        assertEquals(DOB, mapped.getDOB());
        assertEquals("9999999999", mapped.getPreferredPhoneNo());
        assertEquals("India", mapped.getCountry());
        assertEquals("Jharkhand", mapped.getState());
        assertEquals("Ranchi", mapped.getDistrict());
    }

    @Test
    @DisplayName("getPatientDemographic should map the last row when the query returns several")
    void getPatientDemographic_shouldMapLastRow() {
        Object[] earlier = row();
        earlier[4] = "Older Record";

        PatientDemographic mapped = new PatientDemographic()
                .getPatientDemographic(Arrays.asList(earlier, row()));

        assertEquals("Asha Devi", mapped.getName());
    }

    @Test
    @DisplayName("getPatientDemographic should answer with nothing when the beneficiary has no row")
    void getPatientDemographic_shouldAnswerNullWithoutRows() {
        assertNull(new PatientDemographic().getPatientDemographic(List.<Object[]>of()));
        assertNull(new PatientDemographic().getPatientDemographic(null));
    }
}
