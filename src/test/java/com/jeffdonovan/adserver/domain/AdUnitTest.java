package com.jeffdonovan.adserver.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class AdUnitTest {

	@Test
	public void testValidateValidAd() {
		validateAd(new Ad("Partner Id", 10L, 300L, "Content"), 0);
	}

	@Test
	public void testValidateNullPartnerId() {
		validateAd(new Ad(null, 10L, 300L, "Content"), 1);
	}

	@Test
	public void testValidateEmptyPartnerId() {
		validateAd(new Ad("", 10L, 300L, "Content"), 1);
	}

	@Test
	public void testValidateNullAdContent() {
		validateAd(new Ad("Parter Id", 10L, 300L, null), 1);
	}

	@Test
	public void testValidateEmptyAdContent() {
		validateAd(new Ad("Parter Id", 10L, 300L, ""), 1);
	}

	@Test
	public void testValidateNullDuration() {
		validateAd(new Ad("Parter Id", null, 300L, "Content"), 1);
	}

	@Test
	public void testValidateNegativeDuration() {
		validateAd(new Ad("Parter Id", -1L, 300L, "Content"), 1);
	}

	@Test
	public void testValidateZeroDuration() {
		validateAd(new Ad("Parter Id", 0L, 300L, ""), 1);
	}

	private void validateAd(Ad ad, int expectedErrorSize) {
		List<String> errors = Ad.validate(ad);
		assertEquals(errors.size(), expectedErrorSize);
	}
}
