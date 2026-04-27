package brillbe.springboot.springbootdeveloper.controller;

import brillbe.springboot.springbootdeveloper.dto.AddArticleRequest;
import brillbe.springboot.springbootdeveloper.dto.ArticleResponse;
import brillbe.springboot.springbootdeveloper.dto.UpdateArticleRequest;
import brillbe.springboot.springbootdeveloper.service.BlogService;
import brillbe.springboot.springbootdeveloper.domain.Article;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new).toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id){
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse((article)));
    }

    @DeleteMapping("/api/articles/{id}")
        public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id){
            blogService.deleteById(id);

            return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody UpdateArticleRequest request){
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().build();

    }

}
