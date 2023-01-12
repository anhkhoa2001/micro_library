
package org.micro.validate;

import org.micro.config.GatewayConfig;
import org.micro.dto.RabbitMapper;
import org.micro.util.StringUtil;

public class GatewayValidation {

    public String validate(String requestPath, String service, String method) {
        if (StringUtil.isNullOrEmpty(requestPath)) {
            return "Path request không được trống";
        }
        if(StringUtil.isNullOrEmpty(service) || !GatewayConfig.SERVICE_LIST.contains(service)){
            return "Service request không tồn tại";
        }
        requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH)
                ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;
        int index = requestPath.lastIndexOf("/");
        if(index != -1){
            String lastStr = requestPath.substring(index + 1);
            if(StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)){
                requestPath = requestPath.substring(0, index);
            }
        }
        RabbitMapper rabbitMapper = GatewayConfig.SERVICE_PATH_MAP.getOrDefault(requestPath + "  " + method, null);
        if(rabbitMapper == null){
            return "Request path không tồn tại";
        }
        if(GatewayConfig.SERVICE_PATH_SET_PRIVATE.contains(requestPath)){
            return "Request path không mở public";
        }

        return null;
    }

}
