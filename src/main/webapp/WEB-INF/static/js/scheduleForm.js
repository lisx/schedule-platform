/**
 * Created by jsychen on 2017/4/28.
 */
$(function(){
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
	var scheduleInfoId='';
	//绿色、黄色区域鼠标悬浮显示备注
    $(document).on("mouseover",".scheduleForm .bgGreen,.scheduleForm .bgYellow",function(){
    	scheduleInfoId = $(this).attr("data-scheduleinfoid");
    	var left = $(this).offset().left;
    	var top = $(this).offset().top+40;
        jQuery.ajax({
            type: "post",
            url: "getScheduleLogByInfo?scheduleInfoId="+scheduleInfoId,
            data: null,
            dataType: "json",
            error: function (xmlHttpReq, status) {
                Alert("fail", "网络错误");
            },
            success: function (data) {
            	if(data.length>0){
            		var str = '';
                    for(var i=0;i<data.length;i++){
                        var date = new Date(data[i].createdAt);
                        var day = date.Format("yyyy-MM-dd hh:mm:ss");
                        if(i==0){
                            str += '<hr><p class="head">'+ data[i].logType +':</p>';
                            if(data[i].startAt!=null)
                            str += '<p class="con">'+ data[i].startAt + "至"+ data[i].endAt +'</p>';
                            str += '<p class="con">备注:'+data[i].remark+'</p>';
                            str += '<p class="con">创建时间:'+day+'</p>';
                            str += '<p class="con">创建人:'+data[i].creatorName+'</p>';
                        }else{
                            str += '<hr><p class="head">'+ data[i].logType +':</p>';
                            if(data[i].startAt!=null)
                            str += '<p class="con">'+ data[i].startAt + "至"+ data[i].endAt +'</p>';
                            str += '<p class="con">备注:'+data[i].remark+'</p>';
                            str += '<p class="con">更新时间:'+day+'</p>';
                            str += '<p class="con">更新人:'+data[i].creatorName+'</p>';
                        }
                    }
                    $("#rightMenu").addClass("hidden");
                    $("#remarks").html(str).css({"left":left+'px',"top":top+"px"}).removeClass("hidden");
            	}
            }
        })
    }).on("mouseout",".scheduleForm .bgGreen,.scheduleForm .bgYellow",function(){
    	$("#remarks").addClass("hidden");
    })
    //右击展示菜单
    document.oncontextmenu = function(e){
        e.preventDefault();
    };
    $(document).on("mousedown",".scheduleForm td.rightMenu",function(e){
    	$("#minDate").val($(this).attr("data-date"));
    	console.log($("#minDate").val()+"|||"+$(this).attr("data-date"));
    	$("#editHoliday .datetimepicker").eq(0).data("DateTimePicker").date($(this).attr("data-date"));
        $("#editHoliday .datetimepicker").eq(1).data("DateTimePicker").date($(this).attr("data-date"));
        var index = $(this).index();
        var L = $(this).siblings().length;
        var userId = $(this).attr("data-userid");
        scheduleInfoId = $(this).attr("data-scheduleinfoid");
        var logId =  $(this).attr("data-logid");
        var psStationArea = $(this).attr("data-stationArea");
        var psStation = $(this).attr("data-station");
        var left = $(this).offset().left;
    	var top = $(this).offset().top+40;
        if(e.button == 2){
            if(index != 0 && index != 1 && index != L && index != L-1 && index != L-2){
                $(this).find("#remarks").addClass("hidden");
                $("#rightMenu").css({"left":left+'px',"top":top+"px"}).removeClass("hidden");
            }
            $("input[name='userId']").val(userId);
            $("input[name='scheduleInfoId']").val(scheduleInfoId);
            //$("input[name='stationArea']").val(psStationArea);
            //$("input[name='station']").val(psStation);
            //alert(psStationArea);
            $("#rsStationArea").val(psStationArea);
            $("#rsStation").val(psStation);
            $("#logId").val(logId);
        }
    })
    //编辑实际工时
    $(".scheduleForm td.edit").dblclick(function(){
        $(this).html("<input type='text' value='"+ $(this).text() +"'>");
        $(this).find("input").focus();
    })
    $(document).on("blur",".scheduleForm td.edit input",function(){
        $(this).parent().html($(this).val());
    })
    $("*").not('.rightMenu').click(function(){
        $("#rightMenu").addClass("hidden");
    })
    //点击班次变更，加载班次选项
    $(document).on("click","a[href='#shiftChange']",function(){
    	jQuery.ajax({
    	    type: "get",
    	    url: "getShiftSettingByInfo?scheduleInfoId="+scheduleInfoId,
    	    data: null,
    	    dataType: "json",
    	    error: function (xmlHttpReq, status) {
    	        Alert("fail", "网络错误");
    	    },
    	    success: function (data) {
    	        console.log(data)
    	        var select='';
    	        for(var i=0;i<data.length;i++){
    	            select += '<option value="'+data[i].shiftName+'">'+ data[i].shiftName + '班</option>';
    	        }
    	        //赋值给#shiftChange框里的select
    	        $("#shiftChange form select").html(select);
    	    }
    	})
    })
    $("#shiftChange .btn-confirm").click(function(){
    	
    })
})
//点击<<班次变更>>调此ajax
