function ajaxRequest(requestUrl, requestData){
    let jsonData = JSON.stringify(requestData);
    console.log(jsonData)
    console.log(requestUrl)

    $.ajax({
        type : 'post',
        url : requestUrl,
        dataType : 'json',
        contentType: 'application/json',
        data : jsonData,
        success : function(result) {
            console.log(result);
        },
        error : function(request, status, error) {
            console.log(error)
        }
    })
}

function ValidateCheck(tag, name){
    if(tag.length == 0){
        alert(`${name}을 입력해주세요.`)
        return false;
    }
    return true;
}