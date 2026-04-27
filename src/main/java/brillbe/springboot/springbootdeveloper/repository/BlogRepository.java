package brillbe.springboot.springbootdeveloper.repository;

import brillbe.springboot.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
