/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micro.validate;

import org.micro.config.GatewayConfig;
import org.micro.dto.RabbitMapper;
import org.micro.util.StringUtil;

import java.util.List;

/**
 *
 * @author Admin
 */
public class GatewayValidation {

    public String validate(String requestPath, String service) {
        //Request uri
        if (StringUtil.isNullOrEmpty(requestPath)) {
            return "Path request không được trống";
        }
        //Service
        if(StringUtil.isNullOrEmpty(service) || !GatewayConfig.SERVICE_LIST.contains(service)){
            return "Service request không tồn tại";
        }
        requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH)
                ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;
        //Check has path param
        int index = requestPath.lastIndexOf("/");
        if(index != -1){
            String lastStr = requestPath.substring(index + 1);
            if(StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)){
                requestPath = requestPath.substring(0, index);
            }
        }
        //Check exist path
        RabbitMapper rabbitMapper = GatewayConfig.SERVICE_PATH_MAP.getOrDefault(requestPath, null);
        if(rabbitMapper == null){
            return "Request path không tồn tại";
        }
        //Check path private => reject
        if(GatewayConfig.SERVICE_PATH_MAP.containsKey(requestPath)){
            boolean isPrivate = GatewayConfig.SERVICE_PATH_MAP.get(requestPath).getAccess();
            if(isPrivate){
                return "Request path không mở public";
            }
        }

        return null;
    }

}
