/**
 * Created by chen yun  on 2016/12/28.
 */
$(function(){
    //iframe的高度
    calendar_h();
    $(window).resize(function () {
        calendar_h();
    })
    //顶部下拉菜单点击事件
    $(".header .dropdown").click(function(){
        if($(this).hasClass("open")){
            $(this).parent().css("z-index",2);
        }else{
            $(this).parent().css("z-index",12);
        }
    })
})
function calendar_h() {
    var h = $("body").height() - 60;
    var w = $("body").width() - $(".tab-content").width();
    $("#myiframe").width(w-2).height(h);
}

//表单验证
function formValidation(form){
    var obj = form.find(".require").next();
    var n = 0;
    obj.each(function(){
        //文本框验证
        if(($(this).is("input") && $.trim($(this).val()) == '') ||
            ($(this).is(".dropDown") && $(this).find(".select-drop li").length == 0) ||
            ($(this).is("select") && $(this).val() == 0) ||
            ($(this).is("textarea") && $.trim($(this).val()) == '') ||
            ($(this).is(".datetimepicker") && $.trim($(this).find("input").val()) == '')
        ){
            var text = $(this).prev().text();
            var error = text.substring(0,text.length-1);
            var str = ($(this).is("select") ? '<b>请选择'+ error +'</b>' : '<b>'+ error +'不能为空！</b>');
            if($(this).next().is("b")){
                str = ($(this).is("select") ? '请选择'+ error : error +'不能为空！');
                $(this).next().text(str);
            }else{
                $(str).insertAfter($(this));
            }
            n++;
        }else{
            if($(this).next().is("b")){
                $(this).next().text('');
            }
        }
    })
    return (n == 0 ? true : false);
}