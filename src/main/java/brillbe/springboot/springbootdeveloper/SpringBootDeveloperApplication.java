package brillbe.springboot.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 이 애너테이션으로 인해 스프링 부트 사용에 필요한 기본 설정을 할 수 있게 됨.
@SpringBootApplication
public class SpringBootDeveloperApplication    {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}
