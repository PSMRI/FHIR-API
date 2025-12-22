package com.wipro.fhir.service.bundle_creation;

import java.io.IOException;

import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

// This is a one time validator to validate fhir bundles
public class BundleValidator {
		
	static FhirContext ctx = FhirContext.forR4();
	
	public static void main(String[] args) throws FHIRException {
		try {
		NpmPackageValidationSupport npmPackageValidationSupport = new NpmPackageValidationSupport(ctx);
			npmPackageValidationSupport.loadPackageFromClasspath(" "); // download the package from ABDM and add in resources
			
			//create a validation support chain 
			ValidationSupportChain validationSupportChain = new ValidationSupportChain(
					npmPackageValidationSupport,
					new DefaultProfileValidationSupport(ctx),
					new CommonCodeSystemsTerminologyService(ctx),
					new InMemoryTerminologyServerValidationSupport(ctx),
					new SnapshotGeneratingValidationSupport(ctx)
					
					);
			
			FhirValidator validator = ctx.newValidator();
			FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupportChain);
			validator.registerValidatorModule(instanceValidator);
			
			String bundleJson = ""; // add bundle json here
			
			ValidationResult outCome = validator.validateWithResult(bundleJson);
			
			for(SingleValidationMessage next : outCome.getMessages()) {
				System.out.print("Error - " +  next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage());
			}
			
		} catch (IOException e) {
			throw new FHIRException("Issue in validating the bundle - " + e.getMessage());
		}
		
	}
	

}
