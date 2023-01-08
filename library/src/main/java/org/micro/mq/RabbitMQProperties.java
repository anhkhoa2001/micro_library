package org.micro.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties {

    //User service
    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    @Value("${user.worker.queue}")
    public static String USER_WORKER_QUEUE;

    @Value("${user.direct.key}")
    public static String USER_DIRECT_KEY;

    @Value("${user.direct.exchange}")
    public static String USER_DIRECT_EXCHANGE;


    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.worker.queue}") String userWorkerQueue,
                              @Value("${user.direct.key}") String userDirectKey,
                              @Value("${user.direct.exchange}") String userDirectExchange) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_WORKER_QUEUE = userWorkerQueue;
        USER_DIRECT_KEY = userDirectKey;
        USER_DIRECT_EXCHANGE = userDirectExchange;
    }
}
