package com.wipro.fhir.data.v3.abhaCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class EnrollByAadhaar{
    public Map<String, Object> authData;
    public ConsentRequest consent;
}
 
