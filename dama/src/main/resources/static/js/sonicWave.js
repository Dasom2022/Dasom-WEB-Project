function ajax(){
    $.ajax({
        type:"post",
        url:"/api/sensor/allValueList",
        dataType:"json"
    }).done(res=>{
        console.log(res);
    }).fail(err=>{
        console.log("api안됨");
    })
}