function login(requestUrl){
    let melonLoginForm = $("#melonLoginForm");
    let melonLoginFormInputs = $("#melonLoginForm input");
    melonLoginFormInputs.each(function() {
        !ValidateCheck($(this).val(), $(this).attr('id'))
    })

    ajaxRequest("/melon/login", melonLoginForm.serialize());
}

function ticketing(requestUrl){
    let melonLoginForm = $("#melonTicketingForm");
    let melonLoginFormInputs = $("#melonTicketingForm input");
    melonLoginFormInputs.each(function() {
        !ValidateCheck($(this).val(), $(this).attr('id'))
    })

    ajaxRequest("/melon/ticketing", melonLoginForm.serialize());
}
