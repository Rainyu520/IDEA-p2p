$(function () {
    //验证姓名
    $("#realName").on("blur", function () {
        var realName = $.trim($("#realName").val())
        if (!realName) {
            showError("realName", "真实姓名不能为空")
        } else if (!/^[\u4e00-\u9fa5]{0,}$/.test(realName)) {
            showError("realName", "必须为中文字符")

        } else {
            showSuccess("realName")
        }
    })
    //验证身份证号
    $("#idCard").on("blur", function () {
        var idCard = $.trim($("#idCard").val())
        if (!idCard) {
            showError("idCard", "身份证号码不能为空")
        } else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
            showError("idCard", "身份证信息格式错误")
        } else {
            showSuccess("idCard")
        }
    })
    //图形验证码
    $("#captcha").on("blur", function () {
        var checkcode = $.trim($("#captcha").val())
        if (!checkcode) {
            showError("captcha", "验证码不能为空")
        } else {
            $.ajax({
                "url": "loan/checkCode",
                "data": {
                    "checkCode": checkcode
                },
                "type": "get",
                success: function (data) {
                    if (data.statuscode == 10000) {
                        showSuccess("captcha")
                    } else {
                        showError("captcha", data.msg)
                    }
                },
                error: function () {
                    showError("captcha", "系统繁忙，请稍候重试")
                }
            })
        }

    })
    $("#btnRegist").on("click", function () {
        var realName = $.trim($("#realName").val())
        var idCard = $.trim($("#idCard").val())
        //触发验证事件
        $("#realName").blur()
        $("#idCard").blur()
        $("#captcha").blur()

        if ("" == $("div[id$='Err']").text()) {
            $.ajax({
                "url": "loan/verfiyRealName",
                "data": {
                    "realName":realName,
                    "idCard":idCard
                },
                "type":"post",
                success:function (data) {
                    if (data.statuscode == 10000){
                        window.location.href="index"
                    } else {
                        showError("captcha",data.msg)
                    }
                },
                error:function () {
                    showError("captcha","系统繁忙，请稍候重试")
                }
            })
        }


    })
})


//同意实名认证协议
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