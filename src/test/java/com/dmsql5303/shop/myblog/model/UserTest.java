package com.dmsql5303.shop.myblog.model;

import com.dmsql5303.shop.myblog.dto.SignRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    @Nested
    @DisplayName("User 객체 생성")
    class createUser{
        private String username;
        private String password;
        private String email;

        @BeforeEach
        void setup(){
            username = "test1";
            password = "12345";
            email = "test1@naver.com";
        }

        @Test
        @DisplayName("정상 케이스")
        void createUserNomal(){
            SignRequestDto requestDto = new SignRequestDto(username,password,email);

            User user = new User(requestDto);

            assertNull(user.getUid());
            assertEquals(username , user.getUsername());
            assertEquals(password, user.getPassword());
            assertEquals(email, user.getEmail());
        }

        @Nested
        @DisplayName("실패케이스")
        class FailCases{
            @Nested
            @DisplayName("회원ID")
            class userID{
                @Test
                @DisplayName("null")
                void fail1(){
                    username = null;
                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class,()->{
                        new User(requestDto);
                    });
                    assertEquals("usernameNull",exception.getMessage());
                }
                @Test
                @DisplayName("글자수")
                void fail2(){
                    username = "te";
                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class,()->{
                        new User(requestDto);
                    });
                    assertEquals("usernameLength",exception.getMessage());
                }
                @Test
                @DisplayName("회원 유효성 검사")
                void fail3(){
                    username = "한글테스트";
                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class,()->{
                       new User(requestDto);
                    });
                    assertEquals("userPatten",exception.getMessage());
                }
            }
            @Nested
            @DisplayName("회원 비밀번호")
            class password{
                @Test
                @DisplayName("null체크")
                void fail1(){
                    password = null;

                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class ,()->{
                        new User(requestDto);
                    });
                    assertEquals("passwordNull",exception.getMessage());
                }
                @Test
                @DisplayName("비밀번호 글자수")
                void fail2(){
                    password ="123";

                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class ,()->{
                       new User(requestDto);
                    });
                    assertEquals("passwordlength",exception.getMessage());
                }
                @Test
                @DisplayName("아이디 포함")
                void fail3(){
                    password ="test1";

                    SignRequestDto requestDto = new SignRequestDto(username,password,email);

                    Exception exception = assertThrows(IllegalArgumentException.class ,()->{
                        new User(requestDto);
                    });
                    assertEquals("passwordPatten",exception.getMessage());
                }
            }


        }

    }
}