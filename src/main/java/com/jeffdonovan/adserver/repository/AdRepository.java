package com.jeffdonovan.adserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeffdonovan.adserver.domain.Ad;

@Repository
public interface AdRepository extends CrudRepository<Ad, Long> {
	
    @Query("SELECT ad FROM Ad ad where ad.partnerId = :partnerId and ad.expiration > :expiration")
	public Ad findActiveAdByPartnerId(@Param("partnerId") String partnerId, @Param("expiration") Long expiration);

}
