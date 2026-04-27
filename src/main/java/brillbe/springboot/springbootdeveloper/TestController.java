package brillbe.springboot.springbootdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    // get에 이 함수가 매핑되어 버리는 거겠지.
//    @GetMapping("/test")
//    public String test(){
//        return "Hello, world!";
//    }

    // TestService 빈(객체) 주입
    @Autowired
    TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMemebers(){
        List<Member> members = testService.getAllMembers();
        return members;
    }
}
