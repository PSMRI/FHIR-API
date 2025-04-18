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
 
## Setting Up Commit Hooks

This project uses Git hooks to enforce consistent code quality and commit message standards. Even though this is a Java project, the hooks are powered by Node.js. Follow these steps to set up the hooks locally:

### Prerequisites
- Node.js (v14 or later)
- npm (comes with Node.js)

### Setup Steps

1. **Install Node.js and npm**
   - Download and install from [nodejs.org](https://nodejs.org/)
   - Verify installation with:
     ```
     node --version
     npm --version
     ```

2. **Install dependencies**
   - From the project root directory, run:
     ```
     npm ci
     ```
   - This will install all required dependencies including Husky and commitlint

3. **Verify hooks installation**
   - The hooks should be automatically installed by Husky
   - You can verify by checking if the `.husky` directory contains executable hooks

### Commit Message Convention

This project follows a specific commit message format:
- Format: `type(scope): subject`
- Example: `feat(login): add remember me functionality`

Types include:
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code changes that neither fix bugs nor add features
- `perf`: Performance improvements
- `test`: Adding or fixing tests
- `build`: Changes to build process or tools
- `ci`: Changes to CI configuration

Your commit messages will be automatically validated when you commit, ensuring project consistency.

## Filing Issues

If you encounter any issues, bugs, or have feature requests, please file them in the [main AMRIT repository](https://github.com/PSMRI/AMRIT/issues). Centralizing all feedback helps us streamline improvements and address concerns efficiently. 

## Join Our Community

We’d love to have you join our community discussions and get real-time support!  
Join our [Discord server](https://discord.gg/FVQWsf5ENS) to connect with contributors, ask questions, and stay updated.  
