

$(document).ready(function(){
   console.log("d");
});

function userdataCheck(){
    console.log("dd");
    let username = $("#username").val().trim();
    let password = $("#password").val().trim();
    let email = $("#email").val().trim();
    let re_password = $("#re_password").val().trim();

    let userPatten =/^.*(?=.{3,})(?=.*[0-9])(?=.*[a-zA-Z]).*$/;
    let passwordPatten = /^[0-9]{4,}$/;

    if(username =='' || username == null){
        alert("아이디를 입력하세요");
        $("#username").focus();
        return false;

    } else if(!userPatten.test(username)){
        alert("아이디는 최소 3자 이상, 영어와 숫자를 둘다 사용하여 입력하세요.");
        $("#username").focus();
        return false;


    } else if(password == '' || password == null){
        alert("비밀번호를 입력하세요");
        $("#password").focus();
        return false;

    }else if(password.includes(username)){
        alert("비밀번호에 아이디를 포함할수없습니다..");
        $("#password").focus();
        return false;

    }else if(!passwordPatten.test(password)) {
        alert("비밀번호는 숫자로 4자리 이상 입력해주세요.");
        $("#password").focus();
        return false;

    }else if(password != re_password){
        alert("비밀번호를 다시 확인해주세요.");
        $("#re_password").focus();
        return false;

    } else if(email == '' || email == null) {
        alert("이메일을 입력하세요");
        $("#email").focus();
        return false;
    }
}