function Alert(status, message) {
	//fail:失败，success:成功，danger:警告
    if(status == undefined){
        status = 'success';
    }
    var str ='<div class="Alert">';
    str += '<div class="alert-dialog">';
    str += '<div class="alert-header">操作提示</div>';
    str += '<div class="alert-mes">';
    str += '<span class="icon-'+ status +'"></span>';
    str += '<b></b></div></div></div>';

    var btn =  '<div class="alert-footer">';
    btn += '<button class="btn  btn-cancel">取消</button>';
    btn += '<button class="btn btn-confirm">确定</button></div>';
    $("body .Alert").remove();
    $("body").append(str);
    var mask = $("#mask",parent.document);
    mask.show();
    var obj = $(".Alert");
    var mes = $(".alert-mes b");
    
    obj.addClass(status);
    if(status == "fail"){
        mes.text("操作失败，请重试！");
    }else if(status == "success"){
        mes.text("操作成功！");
    }else if(status == "danger"){
        obj.find(".alert-dialog").append(btn);
        mes.text("确定删除这条数据？");
    }
    if(message != undefined){
        mes.text(message);
    }
    //点击取消
    $(document).on("click",".Alert .btn-cancel",function(){
        close();
        return false;
    })
    if(!($(".Alert").is($(".danger")))){
    	setTimeout(function(){
        	close();
        },1200)
    }
    function close(){
        $(".Alert").fadeOut(600);
        mask.fadeOut(600);
    }
}