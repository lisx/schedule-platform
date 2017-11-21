/**
 * Created by chen yun  on 2016/7/8.
 */
$(function () {
    load();
    $(window).resize(function(){
        load();
    })
    //刷新功能
    $(".btn-refresh").click(function () {
        window.location.reload();//刷新当前页面
    })
    //左侧菜单点击时间
    $(".panel a").click(function(){
        $(this).addClass("active").siblings().removeClass("active");
    })
    //表格删除操作
    $(".table td .delete").click(function(){
        Alert("danger");
        var target = $(this).closest("tr");
        $(document).on("click",".Alert.danger .btn-confirm",function(){
            target.remove();
            $(".Alert").remove();
            $("#mask",parent.document).hide();
        })
    })
    //删除站区
    $(".station-head .delete").click(function(){
        $(this).closest(".list").remove();
    })
    //人员分组--删除站点
    $(".stations .delete").click(function(){
        $(this).closest("li").remove();
    })
    //模态框显示，遮罩层显示
    $("[data-toggle='modal']").click(function(){
        $("#mask",parent.document).show();
    })
    $("[data-dismiss='modal']").click(function(){
        $("#mask",parent.document).hide();
    })
})
function load(){
    var h = $("body").height();
    if($("body>.wrapper").siblings().length == 0){
        $("body>.wrapper").height(h - 40);
    }
}