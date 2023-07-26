package com.crio.starter.controller;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.GetMemeResponse;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.exchange.PostMemeResponse;
import com.crio.starter.service.IMemeService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequestMapping(MemeController.MEME_API_ENDPOINT)
public class MemeController {
  public static final String MEME_API_ENDPOINT = "/memes";
  public static final String MEME_BASE = "/";
  public static final String MEME_ID = "/{id}";
  @Autowired
  private transient IMemeService memeService;
    
  @PostMapping(MEME_BASE)
  public ResponseEntity<PostMemeResponse> createMeme(@RequestBody 
        @Valid PostMemeRequest postMemeRequest) {
    log.info("create Meme called with {}", postMemeRequest);
    if (memeService.isDuplicateMeme(postMemeRequest)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    PostMemeResponse postMemeResponse = memeService.saveMeme(postMemeRequest);
    log.info("returned Response with {}", postMemeResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(postMemeResponse);
  }
  
  @GetMapping(MEME_BASE)
  public ResponseEntity<List<Meme>> getMemes() {
    log.info("getMemes is called to retrieve top 100");
    GetMemeResponse getMemeResponse = memeService.findTop100ByOrder();
    List<Meme> top100Memes = getMemeResponse.getMemeList();
    log.info("getMemes is called with {}", getMemeResponse);
    return ResponseEntity.ok().body(top100Memes);
  }
  
  @GetMapping(MEME_ID)
  public ResponseEntity<Meme> getMemeById(@PathVariable @Valid String id) {
    log.info("getMemeById is called with {}", id);
    Meme meme = memeService.getMemeById(id);
    if (meme == null) {
      // return ResponseEntity.notFound().build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
    log.info("getMemeById returned with {}", meme);
    return ResponseEntity.ok().body(meme);
  }
}