package com.wipro.fhir.repo.v3.careContext;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;

public interface CareContextRepo extends CrudRepository<PatientEligibleForResourceCreation, BigInteger> {
	
	@Query(value="SELECT COUNT(*) FROM t_phy_vitals WHERE VisitCode = :visitCode", nativeQuery = true)
	public int hasPhyVitals(@Param("visitCode") String visitCode);
	
	@Query(value="SELECT COUNT(*) FROM t_prescribeddrug WHERE VisitCode = :visitCode", nativeQuery = true)
	public int hasPrescribedDrugs(@Param("visitCode") String visitCode);
	
	@Query(value="SELECT COUNT(*) FROM t_lab_testorder WHERE VisitCode = :visitCode", nativeQuery = true)
	public int hasLabtestsDone(@Param("visitCode") String visitCode);
	
	@Query(value="SELECT COUNT(*) FROM t_childvaccinedetail1 WHERE VisitCode = :visitCode", nativeQuery = true)
	public int hasVaccineDetails(@Param("visitCode") String visitCode);
	
}
