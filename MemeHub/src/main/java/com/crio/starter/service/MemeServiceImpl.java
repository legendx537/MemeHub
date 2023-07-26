package com.crio.starter.service;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.GetMemeResponse;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.exchange.PostMemeResponse;
import com.crio.starter.repositoryservices.IMemeRepositoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemeServiceImpl implements IMemeService {
  @Autowired
  public transient IMemeRepositoryService memeRepositoryService;
    
  @Override
  public PostMemeResponse saveMeme(PostMemeRequest postMemeRequest) {
    String id = memeRepositoryService.storeMeme(postMemeRequest);
    return new PostMemeResponse(id);
  }

  @Override
  public GetMemeResponse findTop100ByOrder() {
    List<Meme> memeList = memeRepositoryService.getTop100();
    return new GetMemeResponse(memeList);
  }

  @Override
  public Meme getMemeById(String id) {
    return memeRepositoryService.findById(id);
  }

  @Override
  public boolean isDuplicateMeme(PostMemeRequest postMemeRequest) {
    return memeRepositoryService.isDuplicateMeme(postMemeRequest);
  }
}
