function login(){
    let id = $("#id").val()
    let pwd = $("#pwd").val();

    let data = {
      "id" : id,
      "pwd" : pwd
    }

    ajaxRequest("/melon/login", data)
}