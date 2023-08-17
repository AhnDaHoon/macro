function login(){
    let id = $("#id").val()
    let pwd = $("#pwd").val();

    if(!ValidateCheck(id, "id")){
        return false;
    }
    if(!ValidateCheck(pwd, "pwd")){
        return false;
    }

    let data = {
      "id" : id,
      "pwd" : pwd
    }

    ajaxRequest("/melon/login", data)
}