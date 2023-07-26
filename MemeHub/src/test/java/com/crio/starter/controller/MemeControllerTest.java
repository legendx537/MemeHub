package com.crio.starter.controller;

import static com.crio.starter.controller.MemeController.MEME_API_ENDPOINT;
import static com.crio.starter.controller.MemeController.MEME_BASE;
import static com.crio.starter.controller.MemeController.MEME_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.GetMemeResponse;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.exchange.PostMemeResponse;
import com.crio.starter.service.IMemeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

@AutoConfigureMockMvc
@SpringBootTest
public class MemeControllerTest {
  private static final String MEME_API_URI = MEME_API_ENDPOINT + MEME_BASE;
  private static final String MEME_API_ID = MEME_API_ENDPOINT + MEME_ID;
  
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private IMemeService memeService;
  
  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
  }

  /**
 * Test case for the "getMemes" method in the MemeController class.
 * The test case verifies the behavior of the "getMemes" method by performing the following steps:
 * Arrange:
 * - Build the URI for the MEME_API_URI endpoint using UriComponentsBuilder.
 * - Verify that the constructed URI matches the expected MEME_API_URI.
 * - Mock the behavior of the memeService by creating a GetMemeResponse
 *   with an empty list and returning it when the findTop100ByOrder method is called.
 * Act:
 * - Perform a GET request to the constructed URI, accepting the response in 
 *   APPLICATION_JSON_VALUE format.
 * - Retrieve the response from the performed request.
 * Assert:
 * - Verify that the HTTP status code of the response is HttpStatus.OK (200).
 * - Verify that the findTop100ByOrder method in the memeService is called exactly once.
 * @throws Exception if an exception occurs during the execution of the test case.
 */
  @Test
  public void testGetMemes() throws Exception {
    // Arrange
    URI uri = UriComponentsBuilder
        .fromPath(MEME_API_URI)
        .build().toUri();
    
    assertEquals(MEME_API_URI, uri.toString());
    
    // Mock the behaviour of memeService 
    GetMemeResponse getMemeResponse = new GetMemeResponse(new ArrayList<>());
    when(memeService.findTop100ByOrder()).thenReturn(getMemeResponse);
    
    // Act
    MockHttpServletResponse response = mvc.perform(
        get(uri.toString()).accept(APPLICATION_JSON_VALUE)
        ).andReturn().getResponse();
    
    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    verify(memeService, times(1)).findTop100ByOrder();
  }
  
  /**
   *<p> Test case for creating a meme.</p>
   *
   *<p>The test case verifies the behavior of the "createMeme" method by performing the 
    following steps:</p>
   *
   *<p>Arrange:
   * - Create a PostMemeRequest object with test data.
   * - Mock the behavior of the memeService by returning false for isDuplicateMeme 
   *   and a PostMemeResponse for saveMeme.
   * - Build the URI for the MEME_API_URI endpoint using UriComponentsBuilder 
   *   and verify it matches the expected MEME_API_URI.</p>
   *
   *<p>Act:
   * - Perform a POST request to the constructed URI with the 
   *   PostMemeRequest object as the request body.
   * - Retrieve the response from the performed request.</p>
   *
   *<p>Assert:
   * - Verify that the HTTP status code of the response is HttpStatus.CREATED (201).
   * - Verify that the isDuplicateMeme method in the memeService is called exactly once 
   *   with the PostMemeRequest object.
   * - Verify that the saveMeme method in the memeService is called exactly once 
   *   with the PostMemeRequest object.</p>
   *
   *<p>@throws Exception if an exception occurs during the execution of the test case.</p>
   */
  @Test
  public void createMemeTest() throws Exception {
    // Arrange 
    PostMemeRequest postMemeRequest = new PostMemeRequest("Om", "http://meme.com/meme.jpg", "It is a meme!");
    when(memeService.isDuplicateMeme(postMemeRequest)).thenReturn(false);

    PostMemeResponse postMemeResponse = new PostMemeResponse("1");
    when(memeService.saveMeme(postMemeRequest)).thenReturn(postMemeResponse);

    URI uri = UriComponentsBuilder
        .fromPath(MEME_API_URI)
        .build().toUri();
    
    assertEquals(MEME_API_URI, uri.toString());    
    
    // Act
    MockHttpServletResponse response = mvc.perform(
        post(uri.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postMemeRequest))
      ).andReturn().getResponse();
    
    // Assert 
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    verify(memeService,times(1)).isDuplicateMeme(postMemeRequest);
    verify(memeService,times(1)).saveMeme(postMemeRequest);    
  }
  
  /**
   * Test case for creating a meme when a duplicate meme already exists.
   * The test case verifies the behavior of the "createMeme" method by performing
   * the following steps:
   * Arrange:
   * - Create a PostMemeRequest object with test data.
   * - Mock the behavior of the memeService by returning true for isDuplicateMeme.
   * - Build the URI for the MEME_API_URI endpoint using UriComponentsBuilder.
   * Act:
   * - Perform a POST request to the constructed URI 
   *   with the PostMemeRequest object as the request body.
   * - Retrieve the response from the performed request.
   * Assert:
   * - Verify that the HTTP status code of the response is HttpStatus.CONFLICT (409).
   * - Verify that the isDuplicateMeme method in the memeService is called exactly once 
   *   with the PostMemeRequest object.
   * @throws Exception if an exception occurs during the execution of the test case.
   */
  @Test
  public void createMemeTestReturnHttpStatusConflict() throws Exception {
    // Arrange 
    PostMemeRequest postMemeRequest = new PostMemeRequest("Om", "http://meme.com/meme.jpg", "It is a meme!");
    when(memeService.isDuplicateMeme(postMemeRequest)).thenReturn(true);
    
    URI uri = UriComponentsBuilder
        .fromPath(MEME_API_URI)
        .build().toUri();
        
    // Act
    MockHttpServletResponse response = mvc.perform(
        post(uri.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postMemeRequest))
      ).andReturn().getResponse();
    
    // Assert 
    assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    verify(memeService,times(1)).isDuplicateMeme(postMemeRequest);  
  }
  
  /**
   * Test case for the "getMemeById" method in the MemeController class.
   * The test case verifies the behavior of the "getMemeById" 
   * method by performing the following steps:
   * Arrange:
   * - Define a memeId for the test.
   * - Create a testMeme object with the specified memeId and sample meme details.
   * - Mock the behavior of the memeService by returning the testMeme object 
   *   when the getMemeById method is called with the memeId.
   * Act:
   * - Perform a GET request to the MEME_API_ID endpoint, including the memeId in the URL.
   * - Accept the response in APPLICATION_JSON_VALUE format.
   * - Retrieve the response from the performed request.
   * Assert:
   * - Verify that the HTTP status code of the response is HttpStatus.OK (200), 
   *   indicating a successful request.
   * - Verify that the getMemeById method in the memeService is called exactly once
   *   with the specified memeId.
   * @throws Exception if an exception occurs during the execution of the test case.
   */
  @Test
  public void getMemeByIdTest() throws Exception {
    // Arrange 
    String memeId = "1";
    Meme testMeme = new Meme(memeId, "Test-Meme", "http://meme.com/meme.jpg","This is meme");
    when(memeService.getMemeById(memeId)).thenReturn(testMeme);

    // Act 
    MockHttpServletResponse response = mvc.perform(
        get(MEME_API_ID,memeId).accept(APPLICATION_JSON_VALUE)
    ).andReturn().getResponse();

    // Assert 
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    verify(memeService,times(1)).getMemeById(memeId);
  }
  
  /**
   * Test case for the "getMemeById" method in the MemeController class when the meme is not found.
   * The test case verifies the behavior of the "getMemeById" method by performing 
   * the following steps:
   * Arrange:
   * - Define a memeId for the test.
   * - Set the testMeme object to null to simulate the scenario where the meme is not found.
   * - Mock the behavior of the memeService by returning the testMeme object 
   *   when the getMemeById method is called with the memeId.
   * Act:
   * - Perform a GET request to the MEME_API_ID endpoint, including the memeId in the URL.
   * - Accept the response in APPLICATION_JSON_VALUE format.
   * - Retrieve the response from the performed request.
   * Assert:
   * - Verify that the HTTP status code of the response is HttpStatus.NOT_FOUND (404),
   *   indicating that the meme was not found.
   * - Verify that the getMemeById method in the memeService is called exactly once 
   *   with the specified memeId.
   * @throws Exception if an exception occurs during the execution of the test case.
   */
  @Test
  public void getMemeById() throws Exception {
    // Arrange 
    String memeId = "133";
    Meme testMeme = null;
    when(memeService.getMemeById(memeId)).thenReturn(testMeme);

    // Act 
    MockHttpServletResponse response = mvc.perform(
         get(MEME_API_ID,memeId).accept(APPLICATION_JSON_VALUE)
    ).andReturn().getResponse();

    // Assert 
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    verify(memeService,times(1)).getMemeById(memeId);
  }

}
