package hello.hellospring.controller;

import ch.qos.logback.classic.Logger;
import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    private Logger logger = (Logger) LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        logger.trace(members.size()+"");
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @PostMapping("/members/new")
    public String create(Member member) {
        logger.trace(member.getName());
//        Member member = new Member();
//        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }
}
