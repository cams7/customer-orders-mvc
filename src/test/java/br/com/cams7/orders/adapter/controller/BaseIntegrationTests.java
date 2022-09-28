package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

public abstract class BaseIntegrationTests extends BaseWebClientTests {

  protected static final String REQUEST_TRACE_ID = "123";

  protected static final String TIMESTAMP_ATTRIBUTE = "$.timestamp";
  protected static final String PATH_ATTRIBUTE = "$.path";
  protected static final String STATUS_ATTRIBUTE = "$.status";
  protected static final String ERROR_ATTRIBUTE = "$.error";
  protected static final String MESSAGE_ATTRIBUTE = "$.message";
  protected static final String TRACE_ATTRIBUTE = "$.trace";
  protected static final String REQUESTID_ATTRIBUTE = "$.requestId";
  protected static final String EXCEPTION_ATTRIBUTE = "$.exception";

  protected static final String BAD_REQUEST_NAME = "Bad Request";
  protected static final int BAD_REQUEST_CODE = 400;
  protected static final String NOT_FOUND_NAME = "Not Found";
  protected static final int NOT_FOUND_CODE = 404;
  protected static final String INTERNAL_SERVER_ERROR_NAME = "Internal Server Error";
  protected static final int INTERNAL_SERVER_ERROR_CODE = 500;

  @Autowired protected MongoTemplate mongoTemplate;

  @Autowired private WebApplicationContext applicationContext;
  protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    mockMvc = webAppContextSetup(applicationContext).build();
  }

  protected void dropCollection(String country, String collectionName) {
    mongoTemplate.dropCollection(getCollectionName(country, collectionName));
  }

  protected static String getCollectionName(String country, String collectionName) {
    return getCollectionByCountry(country, collectionName);
  }

  protected <T> T getResponse(ResultActions resultActions, Class<T> type)
      throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
    var result = resultActions.andReturn();
    var data =
        objectMapper.readValue(
            result.getResponse().getContentAsString(Charset.forName("UTF-8")), type);
    return data;
  }
}
