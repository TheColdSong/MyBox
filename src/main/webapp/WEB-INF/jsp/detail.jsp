<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 0024 2017/10/24
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情</title>
    <%@ include file="../common/head.jsp"%>

</head>
<body>

    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
               <h1>${detail.name}</h1>
            </div>
        </div>

        <div class="panel-body">
            <h2 class="text-danger">
                <!--显示time图标 自带字体图标-->
                <span class="glyphicon glyphicon-time"></span>
                <!--显示倒计时-->
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
    <!--登录输入层 输入电话号码
        如果没有登录 弹出一层先是登陆
    -->
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog"><!--对话框-->
            <div class="modal-content">

                <div class="modal-header">
                    <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>电话
                    </h3>
                </div>

                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                            placeholder="“手机号别忘了填~”" class="form-control">
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <!--验证该手机号码填写是否正确-->
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button class="btn btn-success" id="killPhoneBtn" type="button">
                        <span class="glyphicon glyphicon-phone"></span>
                        提交
                    </button>
                </div>

            </div>
        </div>
    </div>

</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!--cdn获取js插件-->
<!--jquery Cookie插件-->
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<!--jquery 倒计时插件-->
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>

<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script  type="text/javascript">
    $(function () {

        seckill.detail.init(
            {
            seckillId : ${detail.seckillId},
            startTime : ${detail.startTime.time},
            endTime : ${detail.endTime.time}
            }
        )
    })

</script>
</html>
