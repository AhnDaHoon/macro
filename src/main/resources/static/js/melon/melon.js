function login(requestUrl){
    let melonLoginForm = $("#melonLoginForm");
    let melonLoginFormInputs = $("#melonLoginForm input");

    isValid = true;
    melonLoginFormInputs.each(function() {
        if(!ValidateCheck($(this).val(), $(this).attr('id'))){
            isValid = false;
            return isValid;
        }
    })

    if(!isValid){
        return isValid;
    }

    ajaxRequest("/melon/login", melonLoginForm.serialize());
}

function ticketing(requestUrl){
    let melonTicketingForm = $("#melonTicketingForm");
    let melonTicketingFormInputs = $("#melonTicketingForm input");

    isValid = true;
    melonTicketingFormInputs.each(function() {
        if(!ValidateCheck($(this).val(), $(this).attr('id'))){
            isValid = false;
            return isValid;
        }
    })

    if(!isValid){
        return isValid;
    }

    ajaxRequest("/melon/ticketing", melonTicketingForm.serialize());
}
