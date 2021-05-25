
$(document).ready(function(){
   showArticle();
});

function showArticle(){

    let sorting = $("#sorting option:selected").val();
    let isAsc = $(":radio[name='isAsc']:checked").val();
    // console.log(sorting, isAcc);

    let dataSource = `/articles?sortBy=${sorting}&isAsc=${isAsc}`;

    $('#articl-list-box').empty();

    $("#pagination").pagination({
       dataSource,
        locator : 'content',
        alias:{
            pageNumber: 'page',
            pageSize: 'size'
        },
        totalNumberLocator: (response) => {
            return response.totalElements;
        },
        pageSize: 10,
        showPrevious: true,
        showNext: true,
        ajax:{
        },
        callback : function (data,pagination){
            $('#articl-list-box').empty();
            for (let i = 0; i < data.length; i++) {
                let article = data[i];
                let aritlceHtml = addArticleHtml(article);
                $("#articl-list-box").append(aritlceHtml);
            }
        }
    });
}

function  addArticleHtml(article){
    let tempHtml =`
                    <div class="article-list-card">
                    <div class="article-list-card-title">
                        <a href="/articles/${article.aid}">
                            <h2>${article.title}</h2>
                        </a>
                    </div>
                    <div class="article-list-card-txt">
                        <span">${article.username}</span>
                        <span>${article.createdAt}</span>
                    </div>
                </div>
    `;
    return tempHtml;
}