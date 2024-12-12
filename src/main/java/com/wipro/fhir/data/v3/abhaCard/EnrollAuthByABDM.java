package com.wipro.fhir.data.v3.abhaCard;

import java.util.Map;

import lombok.Data;

@Data
public class EnrollAuthByABDM {
    public Map<String, Object> authData;
    public String[] scope;

}
