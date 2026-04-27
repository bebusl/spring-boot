package brillbe.springboot.springbootdeveloper.dto;

import brillbe.springboot.springbootdeveloper.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor// 모든 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    // DTO
    private String title;
    private String content;

    // DTO -> Entity로 바꾸어줌
    public Article toEntity(){
        return Article.builder().title(title).content(content).build();
    }
}
