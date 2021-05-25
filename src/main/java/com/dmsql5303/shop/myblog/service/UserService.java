package com.dmsql5303.shop.myblog.service;

import com.dmsql5303.shop.myblog.dto.SignRequestDto;
import com.dmsql5303.shop.myblog.model.User;
import com.dmsql5303.shop.myblog.repository.UserRepository;
import com.dmsql5303.shop.myblog.security.UserDetailsImpl;
import com.dmsql5303.shop.myblog.security.kakao.KakaoOAuth2;
import com.dmsql5303.shop.myblog.security.kakao.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOAuth2 kakaoOAuth2;
    private static final String GARBAGE_NUMBER  = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";


    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,KakaoOAuth2 kakaoOAuth2){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.kakaoOAuth2 = kakaoOAuth2;
    }

    // 회원가입
    public User createUser(SignRequestDto requestDto){


        String username = requestDto.getUsername();
        Optional<User> foundUser = userRepository.findByUsername(username);

        // 회원 중복체크
        if(foundUser.isPresent()){
            throw new IllegalArgumentException("userIdExist");
        }

        // 비밀번호 암호화
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        User user = new User(requestDto);
        userRepository.save(user);

        return user;
    }


    //카카오 로그인
    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // 우리 DB 에서 회원 Id 와 패스워드
        // 회원 Id = 카카오 nickname
        String username = nickname;
        // 패스워드 = 카카오 Id + ADMIN TOKEN
        String password = kakaoId + GARBAGE_NUMBER;

        // DB 에 중복된 Kakao Id 가 있는지 확인

        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            // 패스워드 인코딩
            String encodedPassword = passwordEncoder.encode(password);
            // ROLE = 사용자

            kakaoUser = new User(nickname, encodedPassword, email, kakaoId);
            userRepository.save(kakaoUser);

        }

        // 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}