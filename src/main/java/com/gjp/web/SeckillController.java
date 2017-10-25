package com.gjp.web;

import com.gjp.dto.Exposer;
import com.gjp.dto.SeckillExecution;
import com.gjp.dto.SeckillResult;
import com.gjp.entity.SecKill;
import com.gjp.enums.SeckillStatEnum;
import com.gjp.exception.RepeatKillException;
import com.gjp.exception.SeckillCloseException;
import com.gjp.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by gjp on 0023 2017/10/23.
 */
@Controller // 配置的自动扫描可以扫描到
@RequestMapping("/seckill")//url:/模块/资源/｛id｝/细分；例：/seckill/list
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Resource
    private SeckillService seckillService;

    /**
     * 子请求地址 ，只接受get请求，其他请求驳回
     * @param model
     * @return 一个字符串的jsp名称
     */
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public String list(Model model){
        //从服务层得到列表页信息
        List<SecKill> secKillList = seckillService.querySeckillList();
        //model存放数据
        model.addAttribute("list",secKillList);
        return "list"; //web-inf/jsp/list.jsp
    }

    /**
     *
     * @param seckillId  路径变量 从路径里把值得到 @PathVariable("seckillId")Long seckillId 传入参数识别url占位符
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail" , method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId")Long seckillId , Model model){
        //判断是否得到秒杀id
        if (seckillId==null) {
            return "redirect:/seckill/list";
        }
            SecKill seckill = seckillService.getById(seckillId);
        //判断是否得到秒杀信息
        if (seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("detail",seckill);


        return "detail"; //web-inf/jsp/detail.jsp
    }

    /**
     * 得到暴露的地址 相关信息 dto是web层和service层之间的数据传递
     * @param seckillId
     * @return
     */
    //ajax 返回json
    @RequestMapping(value = "/{seckillId}/exposer" , method = RequestMethod.POST,
            produces ={"application/json;charset=utf-8"} )
    @ResponseBody//返回json格式
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId ){
        SeckillResult<Exposer> result ;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult(true,exposer);
        } catch (Exception e){
            logger.error(e.getMessage());
            result = new SeckillResult(false,e.getMessage());
        }
        return result;
    }
    /**
     *  //@CookieValue是指从cookie中得到值 required = false 如果没有得到值 不必须的则不报错
     * 执行秒杀 相关信息 dto是web层和service层之间的数据传递
     * @param
     * @return
     */

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody//返回json格式
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone) {
        SeckillResult<SeckillExecution> result;//灰色因为里面的秒杀成功属性没用到
        if(userPhone==null){
            return result = new SeckillResult(false,"用户未注册");
        }try{
            SeckillExecution seckillExecution = seckillService.excuteSeckillByProcedure(seckillId, userPhone, md5);
            return result = new SeckillResult(true,seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return result = new SeckillResult(false,seckillExecution);
        } catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return result = new SeckillResult(false,seckillExecution);
        } catch (Exception e) {
            logger.error(e.getMessage());
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return result = new SeckillResult(false,seckillExecution);
        }
    }


    @RequestMapping(value = "/time/now" , method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date = new Date();
        long time = date.getTime();
        return new SeckillResult(true,time);
    }

}
