package com.crio.starter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.GetMemeResponse;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.exchange.PostMemeResponse;
import com.crio.starter.repositoryservices.MemeRepositoryServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class MemeServiceTest {
  @Mock
  private MemeRepositoryServiceImpl memeRepositoryServiceImpl;
  
  @InjectMocks
  private MemeServiceImpl memeServiceImpl;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }
  
  @Test
  public void saveMemeTest() {
    // Arange 
    PostMemeRequest postMemeRequest = new PostMemeRequest("Om", "http://meme.com/meme.jpg", "It is a meme!");
    when(memeRepositoryServiceImpl.storeMeme(postMemeRequest)).thenReturn("1");
   
    // Act 
    PostMemeResponse response = memeServiceImpl.saveMeme(postMemeRequest);

    // Assert 
    assertNotNull(response);
    assertEquals("1", response.getId());
  }

  @Test
  public void findTop100ByOrder() {
    // Arrange 
    List<Meme> memeList = new ArrayList<>();                                                 
    memeList.add(new Meme("1", "Om", "http://image.com/image.jpg", "Understand if u can"));
    memeList.add(new Meme("1", "Om", "http://image.com/image.jpg", "Understand if u can")); 
    memeList.add(new Meme("1", "Om", "http://image.com/image.jpg", "Understand if u can")); 
    when(memeRepositoryServiceImpl.getTop100()).thenReturn(memeList);

    // Act 
    GetMemeResponse response = memeServiceImpl.findTop100ByOrder();

    // Assert
    assertNotNull(response);
    assertEquals(memeList, response.getMemeList());
  }
  
  @Test
  public void testGetMemeById() {
    // Arrange
    Meme mockMeme = new Meme("123", "Om", "http://image.com/image.jpg", "Understand if u can");
    when(memeRepositoryServiceImpl.findById("123")).thenReturn(mockMeme);

    // Act
    Meme result = memeServiceImpl.getMemeById("123");

    // Assert
    assertEquals(mockMeme, result);
  }
  
  @Test
  public void testIsDuplicateMeme() {
    // Arrange
    PostMemeRequest postMemeRequest = new PostMemeRequest("Om", "http://meme.com/meme.jpg", "It is a meme!");
    when(memeRepositoryServiceImpl.isDuplicateMeme(postMemeRequest)).thenReturn(true);

    // Act 
    boolean result = memeServiceImpl.isDuplicateMeme(postMemeRequest);

    // Assert 
    assertTrue(result);
  }
}

/*
 * Due to time constraints
 * we have constructed only test cases
 * for service and controller layer 
 * we will construct more test cases
 * in the later period of time
 */