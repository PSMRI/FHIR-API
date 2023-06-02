# FHIR-API
FHIR (Fast Healthcare Interoperability Resources) standard defines how healthcare information can be exchanged between different computer systems reagrdless of how it is stored in those system

## Features
* FHIR is a standardized framework for information sharing in healthcare
* It enables clinicians and organisations to exchange data regardless of local EHR variations.
* FHIR combines the best features of previous standards into a flexible specification.
* Resources are the foundation of FHIR and contain standardized definitions.
* Each resource has human-readable descriptions for effective usage
* Resources have common and resource-specific metadata for clear and unambiguous utilization.
* FHIR resources can store and exchange various clinical and administrative data.

### Key APIs of FHIR services
* healthIdwithUid
* generateOTP
* verifyOTP
* genHealthCard
* searchProfile

## Building From Source
This microservice is built on Java, Spring boot framework and MySQL DB.

### Prerequisites 
* JDK 1.8
* Maven 

$ ./mvn clean install

## Installation
This service has been tested on Wildfly as the application server.

### Prerequisites 
* Wildfly (or any compatible app server)
* Redis
* MySQL Database

## Integrations
* Video Consultation

## Usage
All features have been exposed as REST endpoints. Refer to the SWAGGER API specification for details.
