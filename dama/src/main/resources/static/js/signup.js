let join_submit;
let nicknamecheck=-1;
let passwordcheck=-1;
let idcheck=-1;
let existemailcheck=-1;
let emailcheck=-1;
let emailcodecheck=-1;

function checkNickname(){

    var nickname=$("#nickname").val();

    console.log(nickname)//커밋용 주석

    $.ajax({
        type:"get",
        url:'/api/signup/nickname/'+nickname+'/exist',
        dataType: "json"
    })
        .done(res=>{
            console.log(res, "중복 api 성공")
            if(res === 1){
                alert("사용하실수 있는 닉네임입니다.")
                nicknamecheck=1;
            }else{
                alert("이미 존재하는 닉네임입니다.")
            }
            console.log(nicknamecheck);
        }).fail(error=>{
        console.log(error, "중복 api 오류")
    });
}

function checkId(){

    var username=$("#username").val();

    console.log(username)//커밋용 주석

    $.ajax({
        type:"get",
        url:'/api/signup/username/'+username+'/exist',
        dataType: "json"
    })
        .done(res=>{
            console.log(res, "중복 api 성공")
            if(res === 1){
                alert("사용하실수 있는 아이디입니다.")
                idcheck=1;
            }else{
                alert("이미 존재하는 아이디입니다.")
            }
            console.log(idcheck);
        }).fail(error=>{
        console.log(error, "중복 api 오류")
    });
}

$(document).ready(function(){
    var now = new Date();
    var year = now.getFullYear();
    var mon = (now.getMonth() + 1) > 9 ? ''+(now.getMonth() + 1) : '0'+(now.getMonth() + 1);
    var day = (now.getDate()) > 9 ? ''+(now.getDate()) : '0'+(now.getDate());
    //년도 selectbox만들기
    for(var i = 1900 ; i <= year ; i++) {
        $('#year').append('<option value="' + i + '">' + i + '년</option>');
    }
     // 월별 selectbox 만들기
    for(var i=1; i <= 12; i++) {
         var mm = i > 9 ? i : "0"+i ; $('#month').append('<option value="' + mm + '">' + mm + '월</option>');
     }
    // 일별 selectbox 만들기
    for(var i=1; i <= 31; i++) {
        var dd = i > 9 ? i : "0"+i ; $('#day').append('<option value="' + dd + '">' + dd+ '일</option>');
    }
    $("#year > option[value="+year+"]").attr("selected", "true");
    $("#month > option[value="+mon+"]").attr("selected", "true");
    $("#day > option[value="+day+"]").attr("selected", "true");
})

var check = function() {

    if (document.getElementById('password').value ==

        document.getElementById('repassword').value) {

        document.getElementById('message').style.color = 'green';

        document.getElementById('message').innerHTML = '비밀번호가 일치합니다';

        passwordcheck=1;
        console.log(passwordcheck);

    } else {

        document.getElementById('message').style.color = 'red';

        document.getElementById('message').innerHTML = '비밀번호가 일치하지 않습니다';

        console.log(passwordcheck);
    }

}

function existEmail(){
    var email=$("#email").val();
    $.ajax({
        type:"get",
        url:"/api/signup/email/"+email+"/exist",
        dataType:"json"
    })
        .done(res=>{
            if(res===1){
                document.getElementById('email_message').style.color='green';
                document.getElementById('email_message').innerHTML='사용하실 수 있는 이메일입니다';
                existemailcheck=1;
            }else{
                document.getElementById('email_message').style.color='red';
                document.getElementById('email_message').innerHTML='이미 인증에 사용하신 이메일입니다';
                existemailcheck=2;
            }
            console.log(existemailcheck);
        }).fail(error=>{
            console.log("errorrrrrrrrrrrrrrrrrrrrrrrr");
    });
}


function emailSend(e){
    var email=$("#email").val();
    if(existemailcheck==2){
        e.preventDefault();
    }else {
        $.ajax({
            type:"post",
            url:"/member/mail",
            data: {
                "email":email
            },
            success:function (data){
                alert("이메일이 전송되었습니다");
                emailcheck=1;
                console.log(emailcheck);
            }
        });
    }
}

function confirmCode(){
    var confirm_email=$("#confirm_email").val();
    $.ajax({
        type:"post",
        url:"/member/verifyCode",
        data: {
            "confirm_email":confirm_email
        },
        dataType:"json"
    })
        .done(result=>{
            if(result===1){
                console.log(confirm_email);
                emailcodecheck=1;
                alert("인증코드가 정확합니다");
            }else{
                console.log(confirm_email);
                console.log("안됐어요");
            }
            console.log(emailcodecheck);
        }).fail(error=>{
            console.log(error);
    })
}

document.addEventListener('submit',function (event){
    join_submit=passwordcheck+nicknamecheck+idcheck+emailcheck+emailcodecheck+existemailcheck;
    if (join_submit!=6){
        if (nicknamecheck !==1) {
            alert("닉네임중복 체크를 해주세요");
        } else if (idcheck !==1) {
            alert("아이디 중복 체크를 해주세요");
        } else if (existemailcheck !==1) {
            alert("중복된 이메일을 사용중입니다");
        } else if (emailcheck !==1) {
            alert("이메일 인증코드를 받아주세요");
        } else if (emailcodecheck !==1) {
            alert("옳바른 인증코드를 입력해주세요");
        } else if (passwordcheck !==1) {
            alert("비밀번호 확인을 해주세요");
        } else {
            alert("회원가입 필수 사항을 진행하여주세요");
        }
        event.preventDefault();
    }
    else {
        alert("회원가입이 완료되었습니다");
    }
})
