package com.crio.starter.service;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.GetMemeResponse;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.exchange.PostMemeResponse;

public interface IMemeService {
  PostMemeResponse saveMeme(PostMemeRequest getMemeRequest);
  
  GetMemeResponse findTop100ByOrder();

  Meme getMemeById(String id);

  boolean isDuplicateMeme(PostMemeRequest postMemeRequest);
}
