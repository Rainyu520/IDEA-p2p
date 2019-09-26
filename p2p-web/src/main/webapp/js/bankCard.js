
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
	var realNameErrFlag = true;
	var phoneErrFlag= true;
	var idCardErrFlag = true;
	var bankCardNoErrFlag = true;
	var messageErrflag = true;
	//验证真实姓名
    $("#realName").on("blur",function () {
        var realName = $.trim($("#realName").val())
        if (!realName){
            showError("realName","真实姓名不能为空")
            realNameErrFlag = false;
        }else {
            showSuccess("realName")
            realNameErrFlag = true;
        }
    })

	//验证手机号
    $("#phone").on("blur",function () {
        var phone = $.trim($("#phone").val())
        if(!phone){
           showError("phone","手机号不能为空")
            phoneErrFlag = false;
        }else {
            showSuccess("phone")
            phoneErrFlag =true;
        }
    })



	//短信验证码
    $("#dateBtn1").on("click", function () {

        var phone = $.trim($("#phone").val())

        $("#phone").blur()

        if (!phoneErrFlag) {
            return;
        }
        $("#realName").blur()
        if (!realNameErrFlag) {
            return;
        }
        $.ajax({
            "url": "loan/msgCheck",
            "data": "phone=" + phone,
            "type": "post",
            success: function (data) {
                if (data.statuscode == "10000") {
                    alert(data.messageCode)
                    //60秒倒计时
                    var _this = $(this);
                    if (!$("#dateBtn1").hasClass("on")) {
                        $.leftTime(60, function (d) {
                            if (d.status) {
                                $("#dateBtn1").addClass("on");
                                $("#dateBtn1").html((d.s == "00" ? "60" : d.s) + "秒后重新获取");
                            } else {
                                $("#dateBtn1").removeClass("on");
                                $("#dateBtn1").html("获取验证码");
                            }
                        });
                    }


                } else {
                    $("#showId").html(data.msg)
                }
            },
            error: function () {
                $("#showId").html("系统异常，请稍候重试")
            }
        })


    })
    $("#messageCode").on("blur",function () {
        var messageCode = $.trim($("#messageCode").val())
        if(!messageCode){
            showError("messageCode","短信验证码不能为空")
            messageErrflag = false;
        }else {
            showSuccess("messageCode")
            messageErrflag = true;
        }
    })

    //验证身份证号
    $("#idCard").on("blur",function () {
        var idCard = $.trim($("#idCard").val())
        if(!idCard){
            showError("idCard","身份证号不能为空")
            idCardErrFlag = false;
        }else {
            showSuccess("idCard")
            idCardErrFlag = true;
        }
    })
    //验证银行卡号
    $("#bankCardNo").on("blur",function () {
        var bankCardNo = $.trim($("#bankCardNo").val())
        if(!bankCardNo){
            showError("bankCardNo","银行卡号不能为空")
            bankCardNoErrFlag =false;
        }else {
            showSuccess("bankCardNo")
            bankCardNoErrFlag = true;
        }
    })


    //认证身份证信息
    $("#btnRegist").on("click", function () {

        var realName = $.trim($("#realName").val())
        var phone = $.trim($("#phone").val())
        var bankCardNo = $.trim($("#bankCardNo").val())
        var idCard = $.trim($("#idCard").val())
        var messageCode = $.trim($("#messageCode").val())

        $("#realName").blur()
        if (!realName) {
            return;
        }
        $("#phone").blur()
        if (!phoneErrFlag) {
            return;
        }$("#messageCode").blur()
        if (!messageCode) {
            return;
        }


        $("#idCard").blur()
        if (!idCardErrFlag) {
            return;
        }
        $("#bankCardNo").blur()
        if (!bankCardNoErrFlag) {
            return;
        }


        $.ajax({
            "url": "loan/bankCard",
            "data": {
                "phone": phone,
                "realName":realName,
                "bankCardNo":bankCardNo,
                "idCard":idCard,
                "messageCode":messageCode
            },
            "type": "post",
            success: function (data) {
                if (data.statuscode == 10000) {
                    alert("验证成功")
                } else {
                   alert(data.msg)
                }
            }
        })


    })

});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


















