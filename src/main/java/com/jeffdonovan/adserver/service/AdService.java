package com.jeffdonovan.adserver.service;

import java.util.List;

import com.jeffdonovan.adserver.domain.Ad;

/**
 * Ad service interface
 * 
 * @author Jeff.Donovan
 *
 */
public interface AdService {

	public List<Ad> findAllAds();
	
	public Ad findActiveAdByPartnerId(String partnerId);
	
	public Ad saveAd(Ad ad);
	
}
 