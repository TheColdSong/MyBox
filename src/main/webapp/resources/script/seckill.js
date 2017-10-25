/**
 * Created by gjp on 0024 2017/10/24.
 */
//存放交互逻辑
//模块化分
    //想象成 seckill.detail.方法名（参数）
var seckill= {
        //AJAX url
        URL: {
            now: function () {
                return '/seckill/time/now';
            },
            exposer: function (seckillId) {
                return '/seckill/' + seckillId + '/exposer';
            },
            execution: function (seckillId, md5) {
                return '/seckill/' + seckillId + '/' + md5 + '/execution';
            }
        },
        //验证手机号
        valiDatePhone: function (phone) {
            if (phone && phone.length == 11 && !isNaN(phone)) {
                return true;
            } else {
                return false;
            }
        },

        handleSeckillkill: function (seckillId, node) {
            node.hide().html('秒杀已经开始'+'<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀!</button>');
            $.post(seckill.URL.exposer(seckillId), {}, function (result) {
                if (result && result['success']) {
                    var exposer = result['data'];
                    if (exposer['exposed']) {
                        //开启秒杀，获得md5的地址
                        var md5 = exposer['md5'];
                        var killUrl = seckill.URL.execution(seckillId, md5);
                        console.log("killUrl" + killUrl);
                        //绑定一次点击事件 多次点击会重复发送请求
                        $('#killBtn').one('click', function () {
                            //执行秒杀请求
                            //禁用该按钮
                            $(this).addClass('disabled');
                            //发送秒杀请求
                            $.post(killUrl, {}, function (result) {

                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示结果 是否成功
                                node.html('<span class="label label-success">' + stateInfo + '</span>');

                            });
                        });
                        node.show();
                    } else {
                        var now = exposer['now'];
                        var start = exposer['start'];
                        var end = exposer['end'];
                        //重新计时
                        seckill.countdown(seckillId, now, start, end);
                    }
                } else {
                    console.warn("data:" + result);
                }
            });
        },

        countdown: function (seckillId, nowTime, startTime, endTime) {
            var seckillBox = $('#seckill-box');
            if (nowTime >= endTime) {
                //秒杀已结束
                seckillBox.html("秒杀已结束！");
            } else if (nowTime < startTime) {
                //秒杀未开始，进行秒杀倒计时,事件绑定

                var killTime = new Date(startTime + 1000);
                seckillBox.countdown(killTime, function (event) {
                    //时间格式输出
                    var format = event.strftime('秒杀未开始！请耐心等待<br>秒杀倒计时: %D天 %H时 %M分 %S秒');
                    seckillBox.html(format);
                    /*完成后回调事件*/
                }).on('finish.countdown', function () {
                    //获取秒杀地址，控制实现逻辑 出现一个秒杀按钮
                    seckill.handleSeckillkill(seckillId, seckillBox);
                });
            } else {
                //秒杀开始
                seckill.handleSeckillkill(seckillId, seckillBox);
            }
        },
        //秒杀逻辑
        detail: {
            init: function (params) {
                //手机验证登录 计时交互
                //规划交互流程
                //cookie`中的手机号码
                var killPhone = $.cookie('killPhone');

                //验证开始
                if (!seckill.valiDatePhone(killPhone)) {
                    //如果手机号不对，开始绑定电话
                    //控制输出
                    var killPhoneModal = $('#killPhoneModal');
                    //显示弹出层
                    killPhoneModal.modal({
                        //显示弹出层界面
                        show: true,//显示出该隐藏页面
                        backdrop: 'static',//不能关闭
                        keybarod: false//关闭键盘事件
                    });
                    //jquery事件选择器 绑定点击事件
                    $('#killPhoneBtn').click(function () {
                        //得到输入手机号
                        var phone = $('#killPhoneKey').val();
                        console.log("输入的手机号" + phone);
                        if (seckill.valiDatePhone(phone)) {
                            //刷新之前把电话号写到cookie
                            //对web后端处理不要所有路径都存，有影响
                            $.cookie('killPhone', phone, {expires: 7, path: '/seckill'});
                            //刷新页面
                            window.location.reload();
                        } else {
                            $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                        }
                    });
                } else {
                    //登陆成功后的操作 计时交互 seckillResult
                    //js访问json方式
                    var startTime = params['startTime'];
                    var endTime = params['endTime'];
                    var seckillId = params['seckillId'];
                    //发送到controller result是控制器返回的对象
                    $.get(seckill.URL.now(), {}, function (result) {
                        if (result && result['success']) {
                            var nowTime = result['data']; //拿到当前时间
                            //时间的判断
                            seckill.countdown(seckillId, nowTime, startTime, endTime);
                        } else {
                            console.log('result' + result);
                        }
                    });
                }
            }
        }
    }