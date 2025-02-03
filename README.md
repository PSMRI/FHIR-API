# AMRIT - FHIR Service
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)  ![branch parameter](https://github.com/PSMRI/FHIR-API/actions/workflows/sast.yml/badge.svg)

FHIR (Fast Healthcare Interoperability Resources) standard defines how healthcare information can be exchanged between different computer systems regardless of how it is stored in those systems. FHIR provides a means for representing and sharing information among clinicians and organizations in a standard way regardless of the ways local EHRs represent or store the data. FHIR combines the best features of previous standards into a common specification, while being flexible enough to meet needs of a wide variety of use cases within the healthcare ecosystem. Resources are the basis for all exchangeable FHIR content. Each resource includes a standard definition and human-readable descriptions about how to use the resource. Each resource also has a set of common and resource-specific metadata (attributes) to allow its use clearly and unambiguously. FHIR Resources can store and/or exchange many types of clinical and administrative data.

In AMRIT, currently we have developed 9 resources out of 27 resources. Contributors are working on developing rest of the 18 resources which will make AMRIT to be compliant with ABDM guidelines. FHIR R4 is the latest version which we are migrating from HL7 V2.0 current version of AMRIT application.

### Key APIs in FHIR service
* Care Context Services
* e-Aushadhi
* ABHA Card Services
* OP Consultation Record Sharing
* Diagnostic Report Record Sharing
* Prescription Record Sharing
* Higher Health Facility

## Environment and Setup
For setting up the development environment, please refer to the [Developer Guide](https://piramal-swasthya.gitbook.io/amrit/developer-guide/development-environment-setup) .

## API Guide
Detailed information on API endpoints can be found in the [API Guide](https://piramal-swasthya.gitbook.io/amrit/architecture/api-guide).

## Usage
All features have been exposed as REST endpoints. Refer to the SWAGGER API specification for details.

## Filing Issues

If you encounter any issues, bugs, or have feature requests, please file them in the [main AMRIT repository](https://github.com/PSMRI/AMRIT/issues). Centralizing all feedback helps us streamline improvements and address concerns efficiently.  

## Join Our Community

We’d love to have you join our community discussions and get real-time support!  
Join our [Discord server](https://discord.gg/FVQWsf5ENS) to connect with contributors, ask questions, and stay updated.  
