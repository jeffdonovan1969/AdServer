package com.jeffdonovan.adserver.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

@Entity
@Table(name = "Ad")
public class Ad {
	
	@Id
	@Column(name = "partner_id")
	private String partnerId;
	
	@Column(name = "duration")
	private Long duration;
	
	@Column(name = "expiration")
	private Long expiration;
	
	@Column(name = "ad_content", length = 500)
    private String adContent;

    public Ad(){}
    
    public Ad(String partnerId, Long duration, String adContent) {
        this.partnerId = partnerId;
        this.duration = duration;
        this.adContent = adContent;
    }
    
    public Ad(String partnerId, Long duration, Long expiration, String adContent) {
        this.partnerId = partnerId;
        this.duration = duration;
        this.expiration = expiration;
        this.adContent = adContent;
    }

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getAdContent() {
		return adContent;
	}

	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
	
	public static List<String> validate(Ad ad){
		List<String> validationErrors = new ArrayList<String>();
		if (null == ad){
			validationErrors.add("Ad must have a value");
		}
		if (StringUtils.isEmpty(ad.partnerId)){
			validationErrors.add("Partner Id must have a value");
		}
		if (StringUtils.isEmpty(ad.adContent)){
			validationErrors.add("Ad Content must have a value");
		}
		if (null == ad.duration || (Long.compare(0, ad.duration) > 0)){
			validationErrors.add("Ad Content must have a value and be greater than 0");
		}
		return validationErrors;
	}

}
