// JavaScript Document
$(function(){
//弹出登录框
	$(".modal_login").click(function(){
		var str='';
		str+='<form>';
		str+='<input type="text" class="input-lg input-focus" placeholder="请输入邮箱"/>';
		str+='<input type="password" class="input-lg" placeholder="密码"/>';
		str+='<p>';
		str+='<input type="checkbox" class="checkbox">记住我';
		str+='<a class="orange modal_forget_psw" href="javascript:;">忘记密码？</a>';
        str+='</p>';
		str+='<a class="btn-block login" href="javascript:;">登录</a>';
        str+='<a class="modal_a btn-block regist modal_regist" href="javascript:;">注册</a>';
        str+='</form>';
		$(".modal .modal-body").append(str);
		$(".modal").modal(); 

	})	
	//弹出注册框
	$(document).on("click",".modal_regist",function(){
		var str='';
		str+='<form>';
		str+='<input type="text" class="input-lg input-focus" placeholder="邮箱"/>';
		str+='<input type="password" class="input-lg" placeholder="密码"/>';
		str+='<input type="text" class="input-lg" placeholder="推荐人ID"/>';
		str+='<a class="btn-block regist" href="javascript:;">注册</a>';
        str+='</form>';
		$(".modal .modal-body").html('');	
		$(".modal .modal-body").append(str);
		$(".modal").modal(); 	
	})
	//忘记密码
	$(document).on("click",".modal_forget_psw",function(){
		var str='';
		str+='<h3 class="orange">忘了你的密码？</h3>';
		str+='<p>请输入您的有限地址，我们为您重置密码</p>';
		str+='<form>';
		str+='<input type="text" class="input-lg input-focus" placeholder="请输入邮箱地址"/>';
		str+='<a class="btn-block" href="javascript:;">确认重置密码</a>';
        str+='</form>';
		$(".modal .modal-body").html('');	
		$(".modal .modal-body").append(str);
		$(".modal").modal();
	})
})