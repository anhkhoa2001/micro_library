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

    //BookLoan Service
    @Value("${book_loan.rpc.exchange}")
    public static String BOOK_LOAN_RPC_EXCHANGE;

    @Value("${book_loan.rpc.queue}")
    public static String BOOK_LOAN_RPC_QUEUE;

    @Value("${book_loan.rpc.key}")
    public static String BOOK_LOAN_RPC_KEY;

    @Value("${book_loan.worker.queue}")
    public static String BOOK_LOAN_WORKER_QUEUE;

    @Value("${book_loan.direct.key}")
    public static String BOOK_LOAN_DIRECT_KEY;

    @Value("${book_loan.direct.exchange}")
    public static String BOOK_LOAN_DIRECT_EXCHANGE;
    //Library Service
    @Value("${library.rpc.exchange}")
    public static String LIBRARY_RPC_EXCHANGE;

    @Value("${library.rpc.queue}")
    public static String LIBRARY_RPC_QUEUE;

    @Value("${library.rpc.key}")
    public static String LIBRARY_RPC_KEY;

    @Value("${library.worker.queue}")
    public static String LIBRARY_WORKER_QUEUE;

    @Value("${library.direct.key}")
    public static String LIBRARY_DIRECT_KEY;

    @Value("${library.direct.exchange}")
    public static String LIBRARY_DIRECT_EXCHANGE;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.worker.queue}") String userWorkerQueue,
                              @Value("${user.direct.key}") String userDirectKey,
                              @Value("${user.direct.exchange}") String userDirectExchange,
                              @Value("${book_loan.rpc.exchange}") String bookLoanRpcExchange,
                              @Value("${book_loan.rpc.queue}") String bookLoanRpcQueue,
                              @Value("${book_loan.rpc.key}") String bookLoanRpcKey,
                              @Value("${book_loan.worker.queue}") String bookLoanWorkerQueue,
                              @Value("${book_loan.direct.key}") String bookLoanDirectKey,
                              @Value("${book_loan.direct.exchange}") String bookLoanDirectExchange,
                              @Value("${library.rpc.exchange}") String libraryRpcExchange,
                              @Value("${library.rpc.queue}") String libraryRpcQueue,
                              @Value("${library.rpc.key}") String libraryRpcKey,
                              @Value("${library.worker.queue}") String libraryWorkerQueue,
                              @Value("${library.direct.key}") String libraryDirectKey,
                              @Value("${library.direct.exchange}") String libraryDirectExchange) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_WORKER_QUEUE = userWorkerQueue;
        USER_DIRECT_KEY = userDirectKey;
        USER_DIRECT_EXCHANGE = userDirectExchange;

        //Book Loan
        BOOK_LOAN_DIRECT_EXCHANGE = bookLoanDirectExchange;
        BOOK_LOAN_DIRECT_KEY = bookLoanDirectKey;
        BOOK_LOAN_RPC_KEY = bookLoanRpcKey;
        BOOK_LOAN_RPC_EXCHANGE = bookLoanRpcExchange;
        BOOK_LOAN_WORKER_QUEUE = bookLoanWorkerQueue;
        BOOK_LOAN_RPC_QUEUE = bookLoanRpcQueue;

        //Library
        LIBRARY_DIRECT_KEY = libraryDirectKey;
        LIBRARY_RPC_EXCHANGE = libraryRpcExchange;
        LIBRARY_RPC_KEY = libraryRpcKey;
        LIBRARY_RPC_QUEUE = libraryRpcQueue;
        LIBRARY_WORKER_QUEUE = libraryWorkerQueue;
        LIBRARY_DIRECT_EXCHANGE = libraryDirectExchange;
    }
}
