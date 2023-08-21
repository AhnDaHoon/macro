function ajaxRequest(requestUrl, requestData){
    console.log(requestUrl)
    console.log(requestData)
    console.log(JSON.stringify(requestData))

    $.ajax({
        type : "post",
        url : requestUrl,
        data : requestData,
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