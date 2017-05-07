package com.jeffdonovan.adserver.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeffdonovan.adserver.domain.Ad;
import com.jeffdonovan.adserver.exception.ResourceException;
import com.jeffdonovan.adserver.service.AdService;

/**
 * Controller for storing and retrieving Ad resource. 
 * 
 * @author Jeff.Donovan
 *
 */
@RestController
public class AdController {

	@Autowired
	AdService adService;
	
	/**
	 * End point to retrieve all {@link Ad} resources in the database
	 * 
	 * @return List<Ad> List of all Ads in database
	 */
	@RequestMapping("/ad")
    public @ResponseBody List<Ad> index() {
		List<Ad> ads = adService.findAllAds();
    	return ads;
    }
	
	/**
	 * End point to retrieve a single {@link Ad} resource by partner id
	 * 
	 * @param partnerId
	 * @param response
	 * @return
	 * @throws ResourceException
	 */
	@RequestMapping("/ad/{partnerId}")
    public @ResponseBody Ad show(@PathVariable String partnerId, HttpServletResponse response) throws ResourceException {
		Ad ad = adService.findActiveAdByPartnerId(partnerId);
		if (null == ad){
			throw new ResourceException(HttpStatus.NOT_FOUND, "Could not find resource for partner id " + partnerId);
		}
		return ad;
    }
	
	/**
	 * End point used for saving {@link Ad} resource
	 * @param ad
	 * @param response
	 * @return
	 * @throws ResourceException
	 */
    @RequestMapping(value ="/ad", method = { RequestMethod.POST  })
    public @ResponseBody Ad save(@RequestBody Ad ad, HttpServletResponse response) throws ResourceException {
    	
    	List<String> validationErrors = Ad.validate(ad);
    	if (!CollectionUtils.isEmpty(validationErrors)){
    		throw new ResourceException(HttpStatus.BAD_REQUEST, 
    				               validationErrors.stream().map(Object::toString).collect(Collectors.joining(", ")));
    	}
    	
    	Ad activeAd = adService.findActiveAdByPartnerId(ad.getPartnerId());
		if (null != activeAd){
			throw new ResourceException(HttpStatus.CONFLICT, "Active resource exists for partner id " + ad.getPartnerId());
		}
		
    	Ad savedAd = adService.saveAd(ad);
    	if (null == savedAd){
    		throw new ResourceException(HttpStatus.BAD_REQUEST, "Save Ad failed");
    	}
    	else {
    		response.setStatus(Response.SC_CREATED);
    	}
    	return savedAd;
    	
    }
    
}
