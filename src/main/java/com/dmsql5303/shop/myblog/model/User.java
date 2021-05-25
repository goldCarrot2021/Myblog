package com.dmsql5303.shop.myblog.model;

import com.dmsql5303.shop.myblog.dto.SignRequestDto;
import com.dmsql5303.shop.myblog.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
@Setter
@NoArgsConstructor //기본자 생성
@Entity
public class User extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long uid;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;




    public User(SignRequestDto requestDto) {
        /*
        * 닉네임은 `최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`
        * 비밀번호는 `최소 4자 이상이며, 닉네임과 같은 값이 포함된 경우 회원가입에 실패
        * */

        //아이디 체크
        if(requestDto.getUsername()== null || requestDto.getUsername().isEmpty()){
            throw new IllegalArgumentException("usernameNull");

        } else if(requestDto.getUsername().length() < 3){
            throw new IllegalArgumentException("usernameLength");

        }

        String userIdPatten = "^[a-zA-Z0-9]*$";
        Matcher matcher = Pattern.compile(userIdPatten).matcher(requestDto.getUsername());

        if( !matcher.matches()){

            throw new IllegalArgumentException("userPatten");

            //비밀번호 체크
        }else if(requestDto.getPassword()== null || requestDto.getPassword().isEmpty()){
            throw new IllegalArgumentException("passwordNull");

        }else if(requestDto.getPassword().length() < 4){
            throw new IllegalArgumentException("passwordlength");

        }else if(requestDto.getPassword().contains(requestDto.getUsername())){
            throw new IllegalArgumentException("passwordPatten");
        }

        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.email = requestDto.getEmail();
        this.kakaoId = null;
        this.role = UserRole.USER;
    }

    public User(String username,String password,String email, Long kakaoId){
        this.username = username;
        this.password = password;
        this.email = email;
        this.kakaoId = kakaoId;
        this.role = UserRole.USER;
    }


}
