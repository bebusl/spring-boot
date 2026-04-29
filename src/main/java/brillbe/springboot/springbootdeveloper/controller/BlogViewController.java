package brillbe.springboot.springbootdeveloper.controller;

import brillbe.springboot.springbootdeveloper.domain.Article;
import brillbe.springboot.springbootdeveloper.dto.ArticleListViewResponse;
import brillbe.springboot.springbootdeveloper.dto.ArticleViewResponse;
import brillbe.springboot.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> article = blogService.findAll().stream().map(ArticleListViewResponse::new).toList();
        // map(ArticleListViewResponse::new) 와 map(row -> new ArticleListViewResponse(row))는 같다. 축약형임`
        // List<ArticleListViewResponse> article = blogService.findAll().stream().map(row -> new ArticleListViewResponse(row) ).toList();

        model.addAttribute("articles", article);

        System.out.println(article);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article",new ArticleViewResponse(article));

        return "article";
    }


    // @todo 재학습! parameter로 뭔가 올 수 있음 => @RequestParam
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required=false) Long id, Model model){
        if(id == null){
            // 생성
            model.addAttribute("article",new ArticleViewResponse());
        }else{
            // 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
