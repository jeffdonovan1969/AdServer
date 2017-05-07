package com.jeffdonovan.adserver.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jeffdonovan.adserver.domain.Ad;
import com.jeffdonovan.adserver.exception.ResourceException;
import com.jeffdonovan.adserver.service.AdService;

@RunWith(MockitoJUnitRunner.class)
public class AdControllerUnitTest {

	@InjectMocks
	private AdController adController;

	@Mock
	private AdService adService;

	@Mock
	private HttpServletResponse response;

	@Test
	public void testIndex() {
		Ad ad = new Ad("partnerId1", 1000L, 2000L, "The Ad Content");
		List<Ad> ads = new ArrayList<Ad>();
		ads.add(ad);

		when(adService.findAllAds()).thenReturn(ads);

		ads = adController.index();

		verify(adService).findAllAds();

		assertEquals(ads.size(), 1);
	}

	@Test
	public void testShow() {
		Ad ad = new Ad("Partner Id 1", 1000L, 2000L, "The Ad Content");

		when(adService.findActiveAdByPartnerId(ad.getPartnerId())).thenReturn(ad);

		ad = adController.show(ad.getPartnerId(), response);

		verify(adService).findActiveAdByPartnerId(ad.getPartnerId());

		assertNotNull(ad);
	}

	@Test(expected = ResourceException.class)
	public void testShowNotFound() {
		String partnerId = "Partner Id 1";

		when(adService.findActiveAdByPartnerId(partnerId)).thenReturn(null);

		Ad ad = adController.show(partnerId, response);

		verify(adService).findActiveAdByPartnerId(partnerId);
		verify(response).setStatus(Response.SC_NOT_FOUND);

		assertEquals(ad, null);
	}

	@Test
	public void testSave() {
		Ad ad = new Ad("Partner Id 1", 1000L, 2000L, "The Ad Content");

		when(adService.findActiveAdByPartnerId(ad.getPartnerId())).thenReturn(null);
		when(adService.saveAd(ad)).thenReturn(ad);

		ad = adController.save(ad, response);

		verify(adService).findActiveAdByPartnerId(ad.getPartnerId());
		verify(adService).saveAd(ad);
		verify(response).setStatus(Response.SC_CREATED);

		assertNotNull(ad);
	}

	@Test(expected = ResourceException.class)
	public void testSaveActiveAdExists() {
		Ad ad = new Ad("Partner Id 1", 1000L, 2000L, "The Ad Content");

		when(adService.findActiveAdByPartnerId(ad.getPartnerId())).thenReturn(ad);

		ad = adController.save(ad, response);

		verify(adService).findActiveAdByPartnerId(ad.getPartnerId());
		verify(response).setStatus(Response.SC_CONFLICT);

		assertNotNull(ad);
	}

	@Test(expected = ResourceException.class)
	public void testSaveBadRequest() {
		Ad ad = new Ad("Partner Id 1", 1000L, 2000L, "The Ad Content");

		when(adService.findActiveAdByPartnerId(ad.getPartnerId())).thenReturn(null);
		when(adService.saveAd(ad)).thenReturn(null);

		Ad savedAd = adController.save(ad, response);

		verify(adService).findActiveAdByPartnerId(ad.getPartnerId());
		verify(adService).saveAd(ad);
		verify(response).setStatus(Response.SC_BAD_REQUEST);

		assertNull(savedAd);
	}

}
