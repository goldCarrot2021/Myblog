

function updateArticle(aid){

    let title = $("#title").val().trim();
    let content = $("#content").val().trim();
    let data={
        'title':title,
        'content':content
    }

    $.ajax({
        type:"PUT",
        url:`/articles/${aid}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success : function (response){
            if(response =="titleNull"){
                alert('제목은 비워둘수없습니다.');
            }else{
                alert('게시글이 등록되었습니다.');
                window.location.replace('/articles/'+response);
            }

        }


    })
}