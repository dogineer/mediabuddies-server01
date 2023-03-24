package com.develop.web.domain.auth.service;

import com.develop.web.domain.auth.mapper.AuthMapper;
import com.develop.web.domain.auth.vo.AuthVo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/*
* AuthServiceSessionImpl
* */
@Service
public class AuthServiceSessionImpl implements AuthService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceSessionImpl(AuthMapper authMapper, PasswordEncoder passwordEncoder) {
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    * 회원가입 서비스
    * */
    @Override
    public boolean SignUpService(AuthVo authVo) {
        System.out.println("\nAuthService - SignUp");

        Optional<AuthVo> vo = Optional.ofNullable(authMapper.selectByUser(authVo));

        if (vo.isPresent()){
            System.out.println("아이디가 중복입니다.");
            return false;
        } else {
            String encodePassword = passwordEncoder.encode(authVo.getUserPassword());
            authVo.setUserPassword(encodePassword);
            authMapper.insertUser(authVo);
            System.out.println("회원가입이 완료되었습니다");
            return true;
        }
    }

    /*
    * 로그인 서비스
    * */
    @Override
    public boolean loginService(AuthVo formUserData, HttpSession session) throws Exception{
        System.out.println("\nAuthService - login\n");

        AuthVo dbUserData = authMapper.selectByUser(formUserData); // db 조회하고 객체 담기
        System.out.println("db 조회하고 객체 담기 = " + dbUserData);

        boolean isSame = passwordEncoder.matches(
                formUserData.getUserPassword(), dbUserData.getUserPassword());

        if (isSame) {
            session.setAttribute("userInfo", dbUserData);

            String role = String.valueOf(dbUserData.getRole());
        } else System.out.println("비밀번호가 일치하지 않습니다.");

        return isSame;
    }

    @Override
    public String redirectPage(String url, HttpSession session){
        if (session.getAttribute("userInfo") == null){
            return "redirect:/";
        }

        return url;
    }
}