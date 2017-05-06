package com.jeffdonovan.adserver.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeffdonovan.adserver.domain.Ad;
import com.jeffdonovan.adserver.service.AdService;

@RestController
public class AdController {

	@Autowired
	AdService adService;
	
	@RequestMapping("/ad")
    public @ResponseBody List<Ad> index() {
		List<Ad> ads = adService.findAllAds();
    	return ads;
    }
	
	@RequestMapping("/ad/{partnerId}")
    public @ResponseBody Ad show(@PathVariable String partnerId, HttpServletResponse response) {
		Ad ad = adService.findActiveAdByPartnerId(partnerId);
		if (null == ad){
			response.setStatus(Response.SC_NOT_FOUND);
		}
		return ad;
    }
	
    @RequestMapping(value ="/ad", method = { RequestMethod.POST  })
    public @ResponseBody Ad save(@RequestBody Ad ad, HttpServletResponse response) {
    	
    	List<String> validationErrors = Ad.validate(ad);
    	if (!CollectionUtils.isEmpty(validationErrors)){
    		response.setStatus(Response.SC_BAD_REQUEST);
    		return null;
    	}
    	
    	Ad activeAd = adService.findActiveAdByPartnerId(ad.getPartnerId());
		if (null != activeAd){
			response.setStatus(Response.SC_CONFLICT);
			return activeAd;
		}
		
    	Ad savedAd = adService.saveAd(ad);
    	if (null == savedAd){
    		response.setStatus(Response.SC_BAD_REQUEST);
    	}
    	else {
    		response.setStatus(Response.SC_CREATED);
    	}
    	return savedAd;
    	
    }
    
}
