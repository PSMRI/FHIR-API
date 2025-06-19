package com.wipro.fhir.repo.healthID;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.wipro.fhir.data.healthID.HealthIDResponse;

@Repository
@RestResource(exported = false)
public interface HealthIDRepo extends CrudRepository<HealthIDResponse, Long> {

	@Query(" SELECT HIDR from HealthIDResponse HIDR WHERE HIDR.healthId = :healthId")
	public ArrayList<HealthIDResponse> getHealthIDDetails(@Param("healthId") String healthId);

	@Query(" SELECT HIDR from HealthIDResponse HIDR WHERE HIDR.healthIdNumber = :healthIdNumber")
	public ArrayList<HealthIDResponse> getHealthIDDetailsUsingHealthNumber(
			@Param("healthIdNumber") String healthIdNumber);

	@Query(" SELECT HIDR from HealthIDResponse HIDR WHERE HIDR.mobile = :mobile")
	public ArrayList<HealthIDResponse> getHealthIDDetailsUsingMobileNumber(@Param("mobile") String mobile);
	
	@Query("SELECT COUNT(*) FROM HealthIDResponse HIDR WHERE HIDR.healthIdNumber = :healthId")
	public Integer getCountOfHealthIdNumber(@Param("healthId") String healthId);

	boolean existsByHealthIdNumber(String healthIdNumber);

}
