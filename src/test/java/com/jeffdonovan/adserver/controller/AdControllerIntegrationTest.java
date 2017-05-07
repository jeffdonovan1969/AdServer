package com.jeffdonovan.adserver.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffdonovan.adserver.AdserverApplication;
import com.jeffdonovan.adserver.domain.Ad;
import com.jeffdonovan.adserver.repository.AdRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdserverApplication.class)
@WebAppConfiguration
public class AdControllerIntegrationTest {

	private static String APPLICATION_JSON = "application/json";

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	AdRepository adRepository;

	private ObjectMapper mapper = new ObjectMapper();

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		assertEquals(adRepository.count(), 0);
	}

	@After
	public void tearDown() {
		adRepository.deleteAll();
	}

	@Test
	public void testPost() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("partnerId1", 1000L, "The Ad Content"), status().isCreated());
	}

	@Test
	public void testPostActiveAdExists() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("partnerId1", 1000L, "The Ad Content"), status().isCreated());
		postAdAndExpectStatus("/ad", new Ad("partnerId1", 1000L, "The Ad Content2"), status().isConflict());
	}

	@Test
	public void testPostBadRequestNullPartnerId() throws Exception {
		postAdAndExpectStatus("/ad", new Ad(null, 1000L, "The Ad Content"), status().isBadRequest());
	}

	@Test
	public void testPostBadRequestEmptyPartnerId() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", 1000L, "The Ad Content"), status().isBadRequest());
	}

	@Test
	public void testPostBadRequestDurationNull() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", null, "The Ad Content"), status().isBadRequest());
	}

	@Test
	public void testPostBadRequestDurationLessThanZero() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", -1000L, "The Ad Content"), status().isBadRequest());
	}

	@Test
	public void testPostBadRequestDurationZero() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", 0L, "The Ad Content"), status().isBadRequest());
	}

	@Test
	public void testPostAdContentEmpty() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", 1000L, ""), status().isBadRequest());
	}

	@Test
	public void testPostAdContentNull() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("", 1000L, null), status().isBadRequest());
	}

	@Test
	public void testShow() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("partnerId1", 1000L, "The Ad Content"), status().isCreated());
		getAdAndExpectStatus("/ad/partnerId1", status().isOk());
	}

	@Test
	public void testShowNotFound() throws Exception {
		getAdAndExpectStatus("/ad/partnerId", status().isNotFound());
	}

	@Test
	public void testShowAdExistsButNotActive() throws Exception {
		postAdAndExpectStatus("/ad", new Ad("partnerId", 1L, "The Ad Content"), status().isCreated());
		// Simulate Ad Expiring
		Thread.sleep(2000);
		getAdAndExpectStatus("/ad/partnerId", status().isNotFound());
	}

	@Test
	public void testGet() throws Exception {
		getAdAndExpectStatus("/ad", status().isOk());
	}

	private void postAdAndExpectStatus(String url, Ad ad, ResultMatcher status) throws Exception {
		MockHttpServletRequestBuilder postRequest = post(url);
		postRequest.contentType(APPLICATION_JSON).content(mapper.writeValueAsString(ad));
		ResultActions result = mockMvc.perform(postRequest);
		result.andExpect(status);
		// If desired POST REQUEST is successful ie status == CREATED verify
		// fields in body
		if (status == status().isCreated()) {
			MvcResult mvc = result.andReturn();
			Ad adResponse = mapper.readValue(mvc.getResponse().getContentAsString(), Ad.class);
			assertEquals(ad.getPartnerId(), adResponse.getPartnerId());
			assertEquals(ad.getDuration(), adResponse.getDuration());
			assertEquals(ad.getAdContent(), adResponse.getAdContent());
			assertNotNull(adResponse.getExpiration());
		}
	}

	private void getAdAndExpectStatus(String url, ResultMatcher status) throws Exception {
		MockHttpServletRequestBuilder getRequest = get(url);
		ResultActions getResults = mockMvc.perform(getRequest);
		getResults.andExpect(status);
		// If desired GET REQUEST is successful ie status == OK verify fields in
		// body
		if (status == status().isOk()) {
			MvcResult mvc = getResults.andReturn();
			Ad adResponse = mapper.readValue(mvc.getResponse().getContentAsString(), Ad.class);
			assertNotNull(adResponse.getPartnerId());
			assertNotNull(adResponse.getDuration());
			assertNotNull(adResponse.getAdContent());
			assertNotNull(adResponse.getExpiration());
		}
	}

}
