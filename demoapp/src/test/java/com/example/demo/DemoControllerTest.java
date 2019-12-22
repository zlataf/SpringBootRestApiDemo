package com.example.demo;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.DemoController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTest {
	 @Mock
	    DemoController demoController;	
	  
		@Autowired
	    private MockMvc mock;
		private static final Logger LOGGER = LoggerFactory.getLogger(DemoControllerTest.class);
		final String BASE_URL = "/api/demo";
		@Test
		public void contextLoads() {
			assertThat(demoController).isNotNull();
		}
		@Test
		public void getFilesMetaDataListTest() throws Exception {
			String uri =BASE_URL+ "/getFilesMetaDataList";
			this.mock.perform(get(uri)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());		
		}
		@Test
		public void getFileMetaDataTest() throws Exception {
			String uri =BASE_URL+ "/getFileMetaData/1";
			this.mock.perform(get(uri)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());		
		}
}
