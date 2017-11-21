/**
 * Created by chen yun  on 2016/7/8.
 */
$(function(){
    $(".datetimepicker").not(".occurrence-time").datetimepicker({
        format: "YYYY-MM-DD",
        useCurrent:false
    });
    $(".occurrence-time").datetimepicker({
        format: "YYYY-MM-DD HH:mm",// 当初始化为“:”或“/”时可以
        useCurrent:false
    })
    //组时间插件触发 dp.change事件
    $(".start_time,.end_time").on("dp.change", function (event) {
        var obj = $(this).siblings(".datetimepicker");
        $(event.target).is(".end_time") ? obj.data("DateTimePicker").maxDate($(this).find("input").val()) : obj.data("DateTimePicker").minDate($(this).find("input").val());
    })
})
