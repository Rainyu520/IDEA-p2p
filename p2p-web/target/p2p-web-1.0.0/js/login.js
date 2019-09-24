var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
    try {
        if (window.opener) {
            // IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性
            referrer = window.opener.location.href;
        }
    } catch (e) {
    }
}

//按键盘Enter键即可登录
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        login();
    }
});


$(function () {
    var phoneErrFlag = true;
    var passwordErrFlag = true;
    var checkcodeErrFlag = true;
    var messageCodeErrFlag = true;

    $.ajax({
        "url": "loan/loginResources",
        "type": "get",
        success: function (data) {
            $(".historyAverageRate").html(data.historyAverageRate)
            $("#UserCount").html(data.UserCount)
            $("#countMoney").html(data.countMoney)

        }

    })
    //验证手机号
    $("#phone").on("blur", function () {
        var phone = $.trim($("#phone").val())

        if ("" == phone) {
            $("#showId").html("用户名不能为空")
            phoneErrFlag = false
        } else if (!/^1[1-9]\d{9}$/.test(phone)) {
            $("#showId").html("手机号格式不正确")
            phoneErrFlag = false

        } else {
            $("#showId").html("")
            phoneErrFlag = true;
        }
    })
    //验证密码
    $("#loginPassword").on("blur", function () {
        var password = $.trim($("#loginPassword").val())

        if ("" == password) {
            $("#showId").html("密码不能为空")
            passwordErrFlag = false;
        } else {
            $("#showId").html("")
            passwordErrFlag = true;
        }
    })
    //短信验证码
    $("#dateBtn1").on("click", function () {

        var phone = $.trim($("#phone").val())

        $("#phone").blur()

        if (!phoneErrFlag) {
            return;
        }
        $("#loginPassword").blur();
        if (!passwordErrFlag) {
            return;
        }
        $.ajax({
            "url": "loan/messageCheck",
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
    $("#messageCode").on("blur", function () {
        var messageCode = $.trim($("#messageCode").val())
        if (!messageCode) {
            $("#showId").html("短信验证码不能为空")
            messageCodeErrFlag = false;
        } else {
            $("#showId").html("")
            messageCodeErrFlag = true;

        }
    })
    //图形验证码
    /*$("#captcha").on("blur", function () {
        var checkcode = $.trim($("#captcha").val())
        if ("" == checkcode) {
            $("#showId").html("验证码不能为空")
            checkcodeErrFlag = false;

        } else {
            $.ajax({
                "url": "loan/checkCode",
                "data": {
                    "checkCode": checkcode
                },
                "type": "post",
                success: function (data) {
                    if (data.statuscode == 10000) {
                        checkcodeErrFlag = true;

                    } else {
                        $("#showId").html(data.msg)
                        checkcodeErrFlag = false;

                    }
                },
                error: function () {
                    $("#showId").html("系统异常，请稍候重试")
                    checkcodeErrFlag = false;

                }

            })
        }
    })*/
    //登录
    $("#login").on("click", function () {
        var phone = $.trim($("#phone").val())
        var password = $.trim($("#loginPassword").val())
        var messageCode = $.trim($("#messageCode").val())


        $("#phone").blur()
        if (!phoneErrFlag) {
            return;
        }

        $("#loginPassword").blur()
        if (!passwordErrFlag) {
            return;
        }
        $("#messageCode").blur()
        if (!messageCodeErrFlag) {
            return;
        }
        $("#loginPassword").val($.md5(password))

        $.ajax({
            "url": "loan/login",
            "data": {
                "phone": phone,
                "password": $.md5(password),
                "messageCode":messageCode
            },
            "type": "get",
            success: function (data) {
                if (data.statuscode == 10000) {
                    //跳转到前一个页面
                    window.location.href = referrer;
                } else {
                    $("#showId").html(data.msg)
                }
            }
        })


    })

})

