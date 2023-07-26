package com.crio.starter.exchange;

import com.crio.starter.dto.Meme;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMemeResponse {
  private List<Meme> memeList;
}
