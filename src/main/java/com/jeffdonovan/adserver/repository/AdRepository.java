package com.jeffdonovan.adserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeffdonovan.adserver.domain.Ad;

/**
 * Respository for saving {@link Ad} domain
 * @author Jeff.Donovan
 *
 */
@Repository
public interface AdRepository extends CrudRepository<Ad, Long> {
	
	// Method used to find Ad based on partner id and expiration that is greater than the current time passed as argument
    @Query("SELECT ad FROM Ad ad where ad.partnerId = :partnerId and ad.expiration > :currentTime")
	public Ad findActiveAdByPartnerId(@Param("partnerId") String partnerId, @Param("currentTime") Long currentTime);

}
