package brillbe.springboot.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Auditing == 데이터를 언제, 누가 생성하고 변경했는지 추적하는 것. 그걸 가능하게 해주는 애너테이션.

 * 스위치 켜기: @EnableJpaAuditing을 설정 파일(Configuration)에 붙여서 "이제부터 Auditing 기능을 쓸게!"라고 알립니다.

 * Entity에 적용: 추적하고 싶은 엔티티에 @EntityListeners(AuditingEntityListener.class)를 붙입니다.

 * 상속/조합: createdAt, updatedAt 필드를 가진 BaseTimeEntity 클래스를 만들고, 이를 엔티티들이 상속받게 하면 끝입니다.
 */
@EnableJpaAuditing
// 이 애너테이션으로 인해 스프링 부트 사용에 필요한 기본 설정을 할 수 있게 됨.
@SpringBootApplication
public class SpringBootDeveloperApplication    {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}
