package brillbe.springboot.springbootdeveloper.service;

import brillbe.springboot.springbootdeveloper.dto.AddArticleRequest;
import brillbe.springboot.springbootdeveloper.domain.Article;
import brillbe.springboot.springbootdeveloper.dto.UpdateArticleRequest;
import brillbe.springboot.springbootdeveloper.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor //final이 붙거나 @NotNull이 붙은 필드의 생성자 추가. 즉 필수 파라미터는 받는 생성자
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(Long id){
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: "+id));
    }

    public void deleteById(Long id){
        blogRepository.deleteById(id);
    }

    @Transactional //트랜잭션 메서드, 트랜잭션 처리 -> 조회/업데이트 둘 다 성공해야 성공 처리됨.
    public Article update(Long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: "+ id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
