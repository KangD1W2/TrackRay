package com.trackray.web.api;

import com.trackray.web.service.PluginService;
import com.trackray.base.bean.ResultCode;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/plugin")
@RestController
public class PluginApi {

    @Autowired
    private PluginService pluginService;

    @RequestMapping(value = "list" )
    public ResultCode list(){
        JSONArray plugins = pluginService.findPlugins();
        return ResultCode.getInstance(200,"正常",plugins);
    }

    @RequestMapping(value = "use")
    public void use(@RequestParam Map<String,String> map , HttpServletRequest request ,HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        if (map==null || map.isEmpty() || !map.containsKey("key")){
            ResultCode code = ResultCode.ERROR("参数不可为空");
            response.getWriter().print(code.toString());
        }else if (map.containsKey("key")){
            String key =  map.get("key");
            pluginService.usePlugin(map , response);
        }
    }


    /* @RequestMapping(value = "use")
    public ResultCode use(@RequestParam Map<String,String> map){

        if (map==null || map.isEmpty()){
            return ResultCode.ERROR("参数不可为空");
        }else if (map.containsKey("key")){
            String key =  map.get("key");
            return pluginService.usePlugin(map);
        }
        return ResultCode.ERROR("参数不可为空");
    }
*/
    @RequestMapping(value = "result")
    public void result(String task , HttpServletRequest request , HttpServletResponse response) throws IOException {
        switch (request.getParameter("type")) {
            case "html":response.setContentType("text/html");break;
            case "text":response.setContentType("text/plain");break;
            default:response.setContentType("application/json");break;
        }

        if(StringUtils.isNotBlank(task)){
            this.pluginService.getPlugin(task , response);
        }else {
            response.getWriter().print(ResultCode.WARN("请输入task参数"));
        }
    }

    @RequestMapping(value = "stop")
    public ResultCode stop(String task){

        if (StringUtils.isNotBlank(task)){
            return this.pluginService.killPlugin(task);
        }

        return ResultCode.ERROR;
    }

}
