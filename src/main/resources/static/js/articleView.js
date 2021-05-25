




let username = "";

// 로그인한 유저의 이름 체크
function userCheck(){
    $.ajax({
        type: "GET",
        url: `/comments/users`,
        success: function (response) {
            username = response;
        }
    });
}


/*********************게시글 js****************************/

// 게시글 삭제
function deleteAritcleOne(aid){
    // console.log(aid);
    let result = confirm("정말 삭제하시겠습니까?");

    if(result == true){
        $.ajax({
            type: "DELETE",
            url: `/articles/${aid}`,
            success: function (response) {
                if(response =='success'){
                    alert('삭제되었습니다.');
                    window.location.href='/main';
                }else{
                }
            }
        });
    }
}

//게시글 수정 페이지 가져오기
function getModifyAritcle(aid){
    window.location.replace('/articles/update/'+aid)
}




/********************* 댓글 js****************************/


// 댓글 등록
function addComment(aid){

    let content = $("#comment-content").val().trim();

    if(content ==''){
        alert("댓글내용을 입력하세요.");
        $("#comment-content").focus()
        return false;
    }

    // console.log(aid)
    // console.log(content);

    $.ajax({
        type: "POST",
        url: `/comments/${aid}`,
        contentType:"application/json",
        data:JSON.stringify({'content':content}),
        success: function (response) {
            if(response == "success"){
                window.location.reload();
            }else{
                alert('로그인을 해야 댓글을 작성할 수 있습니다.');
                window.location.replace('/users/login');
            }

        }
    });
}

/* comment List 출력 */
function getComment(aid){
    userCheck();
    // console.log(userCheck());
    $.ajax({
        type: "GET",
        url: `/comments/${aid}`,
        success: function (response) {
            for(let i = 0; i < response.length; i++){
                if(username == response[i].username){

                    //로그인한 사용자와 댓글의 username이 같을때
                    let tempHtml = addCommentHtmlUpdate(response[i]);
                    $("#comment-list-box").append(tempHtml);

                }else{
                    let tempHtml = addCommentHtml(response[i]);
                    $("#comment-list-box").append(tempHtml);
                }

            }
        }
    });
}

/* 로그인한 사용자와 댓글의 usrname이 같은 경우 수정 삭제 버튼 보이게 */
function addCommentHtmlUpdate(comments){
    let tempHtml =`
                <div class="comment-card">
                    <div class="comment-name-box">
                        <div class="comment-username">
                            <span>${comments.username}</span>
                            <span>${comments.modifiedAt}</span>
                        </div>
                        <div class="comment-btn-box">
                            <button onclick="updateShow('${comments.cid}')" id="show-update${comments.cid}" class="btn comment-btn">수정</button>
                            <button id="comment-update${comments.cid}" onclick="updateComment('${comments.cid}')" class="btn comment-btn none">등록</button>
                            <button class="btn comment-btn" onclick="deletComment('${comments.cid}')">삭제</button>
                        </div>
                    </div>
                    <div class="comment-content">
                        <textarea readonly maxlength="250" id="update-content${comments.cid}">${comments.content}</textarea>
                    </div>
                </div>
    `;
    return tempHtml;
}

/* 로그인한 사용자와 댓글의 usrname이 다른 경우 */
function addCommentHtml(comments){
    let tempHtml =`
                <div class="comment-card">
                    <div class="comment-name-box">
                        <div class="comment-username">
                            <span>${comments.username}</span>
                            <span>${comments.modifiedAt}</span>
                        </div>
                    </div>
                    <div class="comment-content">
                        <textarea readonly id="update-content">${comments.content}</textarea>
                    </div>
                </div>
    `;
    return tempHtml;
}


// 수정 버튼 누르면 수정 버튼 없어지고 등록 버튼 나타나기
// textarea attr 변경
function updateShow(cid) {
    // console.log(cid)
    $("#show-update"+cid).hide();
    $("#comment-update"+cid).removeClass('none');
    $("#update-content"+cid).attr('readonly',false);
    $("#update-content"+cid).css('border','1px solid #000');
}


// 댓글 업데이트
function updateComment(cid) {
    // console.log(cid);
    let content = $("#update-content"+cid).val().trim();

    if (content == '') {
        alert("댓글내용을 입력하세요.");
        $("#update-content").focus()
        return false;
    }

    $.ajax({
        type: "PUT",
        url: `/comments/${cid}`,
        contentType:"application/json",
        data:JSON.stringify({'content':content}),
        success: function (response) {
            if(response != null){
                alert('수정되었습니다.');
                window.location.reload();
            }else{
            }
        }
    });
}


function deletComment(cid) {
    // console.log(cid);

    let result = confirm("정말 삭제하시겠습니까?");

    if(result == true){
        $.ajax({
            type: "DELETE",
            url: `/comments/${cid}`,
            success: function (response) {
                if(response =='success'){
                    alert('삭제되었습니다.');
                    window.location.reload();
                }else{
                }
            }
        });
    }
}