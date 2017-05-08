package com.jeffdonovan.adserver.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.jeffdonovan.adserver.domain.Ad;
import com.jeffdonovan.adserver.repository.AdRepository;

/**
 * Implementation of {@link AdService} interface
 * 
 * @author Jeff.Donovan
 *
 */
@Service("adService")
@Scope("singleton")
public class AdServiceImpl implements AdService {

	@Autowired
	AdRepository adRepository;

	@Override
	public List<Ad> findAllAds() {
		List<Ad> ads = new ArrayList<Ad>();
		adRepository.findAll().forEach(ads::add);
		return ads;
	}

	@Override
	public Ad findActiveAdByPartnerId(String partnerId) {
		return adRepository.findActiveAdByPartnerId(partnerId, Instant.now().getEpochSecond());
	}

	@Override
	public Ad saveAd(Ad ad) {
		if (null == ad.getExpiration()) {
			ad.setExpiration(Instant.now().getEpochSecond() + ad.getDuration());
		}
		return adRepository.save(ad);
	}

	public AdRepository getAdRepository() {
		return adRepository;
	}

	public void setAdRepository(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

}
