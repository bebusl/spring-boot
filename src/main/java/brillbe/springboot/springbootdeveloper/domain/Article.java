package brillbe.springboot.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // 엔티티 생성 및 수정 시간을 자동으로 감시하고 기록하기 위함 @todo 해당 에너테이션에 대해 학습
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @CreatedDate // 데이터 생성 시 생성 시각을 알아서 저장해줌
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터 수정 시 수정 시각을 알아서 저장해줌
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Article(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

//    protected Article(){}; 이렇게 기본 생성자 일 때 아무것도 안 받으면 Entity에다가 NoArgsConstructor(access = accesslevel)이런식으로 하면 자동 생성해줌.

}
