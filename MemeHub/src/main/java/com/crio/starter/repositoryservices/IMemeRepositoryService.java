package com.crio.starter.repositoryservices;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.PostMemeRequest;
import java.util.List;

public interface IMemeRepositoryService {
  String storeMeme(PostMemeRequest postmemerequest);
  
  List<Meme> getTop100();

  Meme findById(String id);

  boolean isDuplicateMeme(PostMemeRequest postMemeRequest);
}
