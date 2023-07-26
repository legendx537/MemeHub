package com.crio.starter.data;

import com.mongodb.lang.NonNull;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "memes")
@NoArgsConstructor
@AllArgsConstructor
public class MemeEntity {
  @Id
  private String id;
  @NotNull
  @NonNull
  private String name;
  @NotNull
  @NonNull
  private String url;
  @NotNull
  @NonNull
  private String caption;
}

// Here the data folder reprsent the models that will interact with the mongoDB database