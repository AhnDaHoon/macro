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