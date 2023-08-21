function request(requestUrl){
    let melonLoginForm = $("#melonLoginForm");
    let melonLoginFormInputs = $("#melonLoginForm input");
    melonLoginFormInputs.each(function() {
        !ValidateCheck($(this).val(), $(this).attr('id'))
    })

    ajaxRequest(requestUrl, melonLoginForm.serialize());
}

function ticketing(){
}