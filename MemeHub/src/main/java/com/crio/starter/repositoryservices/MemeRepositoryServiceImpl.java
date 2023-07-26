package com.crio.starter.repositoryservices;


import com.crio.starter.data.MemeEntity;
import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.PostMemeRequest;
import com.crio.starter.repository.MemeRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Provider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemeRepositoryServiceImpl implements IMemeRepositoryService {
  @Autowired
  private transient MemeRepository memeRepository;
  @Autowired
  private transient Provider<ModelMapper> modelMapperProvider;
  private int currentId = 1;
  

  @Override
  public String storeMeme(PostMemeRequest postMemeRequest) {
    MemeEntity memeEntity = modelMapperProvider.get().map(postMemeRequest, MemeEntity.class);
    memeEntity.setId(String.valueOf(currentId++));
    memeRepository.save(memeEntity); 
    return memeEntity.getId();
  }

  @Override
  public List<Meme> getTop100() {
    List<MemeEntity> memeEntityList = memeRepository.findAll();
    Collections.reverse(memeEntityList);
    List<Meme> memeList = new ArrayList<>();
    for (MemeEntity memeEntity: memeEntityList) {
      if (memeList.size() < 100) {
        memeList.add(modelMapperProvider.get().map(memeEntity,Meme.class));
      } else {
        break;
      }
    }
    return memeList;
  }

  @Override
  public Meme findById(String id) {
    MemeEntity memeEntity = memeRepository.findById(id).orElse(null);
    if (memeEntity == null) {
      return null;
    }
    return modelMapperProvider.get().map(memeEntity,Meme.class);
  }

  @Override
  public boolean isDuplicateMeme(PostMemeRequest postMemeRequest) {
    List<MemeEntity> memeEntityList = memeRepository.findAll();
    for (MemeEntity meme: memeEntityList) {
      if (meme.getName().equals(postMemeRequest.getName()) 
          && meme.getUrl().equals(postMemeRequest.getUrl()) 
          && meme.getCaption().equals(postMemeRequest.getCaption())) {
        return true;
      }
    }
    return false;
  } 
    
}
