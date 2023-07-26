package com.crio.starter.exchange;

import com.mongodb.lang.NonNull;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMemeRequest {
  @NonNull
  @NotNull
  private String name;
  @NonNull
  @NotNull
  private String url;
  @NonNull
  @NotNull
  private String caption;
}
