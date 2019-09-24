//错误提示
function showError(id, msg) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
    $("#" + id + "Err").show();
    $("#" + id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id).removeClass("input-red");
}

//显示成功
function showSuccess(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id + "Ok").show();
    $("#" + id).removeClass("input-red");
}

//注册协议确认
$(function () {
    $("#agree").click(function () {
        var ischeck = document.getElementById("agree").checked;
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled", "disabled");
            $("#btnRegist").addClass("fail");
        }
    });
    //验证手机号
    $("#phone").on("blur", function () {
        var phone = $.trim($("#phone").val())
        if ("" == phone) {
            showError("phone", "手机号不能为空")
        } else if (!/^1[1-9]\d{9}$/.test(phone)) {
            showError("phone", "手机号格式不正确")
        } else {

            $.ajax({
                "url": "loan/checkPhone",
                "data": {
                    "phone": phone
                },
                "type": "get",
                success: function (data) {
                    if (data.statuscode == 10000) {
                        showSuccess("phone")
                    } else {
                        showError("phone", data.msg)
                    }

                },
                error: function () {
                    showError("phone", "系统异常")

                }
            })


        }
    })
    //验证登录密码
    $("#loginPassword").on("blur", function () {
        var password = $.trim($("#loginPassword").val())
        if (!password) {
            showError("loginPassword", "密码不能为空")
        } else if (!/^[0-9a-zA-Z]+$/.test(password)) {
            showError("loginPassword", "密码格式不正确")
        } else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(password)) {
            showError("loginPassword", "密码必须同时包含数字和字母")
        } else if (password.length < 6 || password.length > 20) {
            showError("loginPassword", "密码长度必须大于6位小于20位")
        } else {
            showSuccess("loginPassword")
        }

    })
    //图形验证码
    $("#captcha").on("blur", function () {
        var checkCode = $.trim($("#captcha").val())
        if (!checkCode) {
            showError("captcha", "验证码不能为空")
        } else {
            $.ajax({
                "url": "loan/checkCode",
                "data": {
                    "checkCode": checkCode
                },
                "type": "post",
                success: function (data) {
                    if (data.statuscode == 10000) {
                        showSuccess("captcha")
                    } else {
                        showError("captcha", data.msg)
                    }
                },
                error: function (error) {
                    showError("captcha", "异常异常，请稍后重试")
                }
            })


        }
    })
    $("#btnRegist").on("click", function () {

        var flag  = true;
        var password = $.trim($("#loginPassword").val())
        var phone = $.trim($("#phone").val())

        /*触发blur事件*/
        $("#phone").blur()
        $("#loginPassword").blur()
        $("#captcha").blur()
        $("div[id$='Err']").each(function () {

        var Errmsg =  $(this).html()

              if ("" != Errmsg){
                flag = false;
                //错误提示信息不为空，中止

                return;
            }

        })
       if (flag){


            //将密码设置为MD5
              $("#loginPassword").val($.md5(password));
            $.ajax({
                "url":"loan/register",
                "data":{
                    "phone":phone,
                    "password":$.md5(password)
                },
                "type":"post",
                success:function (data) {
                    if (data.statuscode == 10000){
                     //成功跳转到实名认证界面
                        window.location.href="realName.jsp";

                    }else {
                        showError("captcha",data.msg)
                    }
                },
                error:function (error) {
                    showError("captcha",error.msg)
                }

            })
        }

    })


});

//打开注册协议弹层
function alertBox(maskid, bosid) {
    $("#" + maskid).show();
    $("#" + bosid).show();
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
    $("#" + maskid).hide();
    $("#" + bosid).hide();
}