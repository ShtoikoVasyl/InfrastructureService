package edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient;

import edu.shtoiko.grpc.TerminalServiceProto;
import edu.shtoiko.grpc.TerminalServiceGrpc;
import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;
import edu.shtoiko.infrastructureservice.terminalcontroller.TerminalServiceController;
import edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient.interceptor.AuthInterceptor;
import edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient.interceptor.TokenAddingServerInterceptor;
import edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient.security.AuthServiceImpl;
import edu.shtoiko.infrastructureservice.service.Encoder;
import edu.shtoiko.infrastructureservice.service.implementation.WithdrawalServiceImpl;
import edu.shtoiko.infrastructureservice.utils.JwtTokenUtil;
import io.grpc.Context;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient.interceptor.AuthInterceptor.JWT_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalServiceControllerGrpcImpl implements TerminalServiceController {

    private final WithdrawalServiceImpl withdrawalService;

    @Value("${terminalservicecontroller.port}")
    private int port;
    private Server server;
    private final AuthServiceImpl authService;
    private final Encoder encoder;
    private final AuthInterceptor authInterceptor;
    private final JwtTokenUtil tokenUtil;

    private final TokenAddingServerInterceptor tokenAddingServerInterceptor;

    @PostConstruct
    public void startServer() {
        new Thread(this::start).start();
    }

    private void start() {
        try {
            server = ServerBuilder.forPort(port)
                .addService(new TerminalGrpcServiceImpl())
                .addService(authService)
                .intercept(tokenAddingServerInterceptor)
                .intercept(authInterceptor)
                .build()
                .start();
            log.info("TerminalServer started at port:{}", port);
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TerminalGrpcServiceImpl extends TerminalServiceGrpc.TerminalServiceImplBase {

        @Override
        public void withdraw(TerminalServiceProto.WithdrawRequest request,
            StreamObserver<TerminalServiceProto.WithdrawResponse> responseObserver) {
            String token = JWT_TOKEN.get(Context.current());
            long username = tokenUtil.getUsernameFromToken(token);
            log.info("Withdraw request from {} for accountNumber {}", username, request.getAccountNumber());
            String withdrawalTextResult = null;
            long approvedAmount = request.getAmount();
            try {
                withdrawalTextResult = withdrawalService.provideWithdraw(username, request.getAccountNumber(),
                    request.getPinCode(), request.getCurrencyCode(), request.getAmount());
            } catch (WithdrawalException e) {
                log.atError()
                    .setMessage("WithdrawalTransaction is not allowed")
                    .addArgument(request.getAccountNumber())
                    .setCause(e)
                    .log();
                withdrawalTextResult = e.getMessage();
                approvedAmount = 0;
            }
            log.atInfo()
                .addArgument(request.getAccountNumber())
                .setMessage("Withdrawal request result %d".formatted(approvedAmount))
                .log();
            TerminalServiceProto.WithdrawResponse response = TerminalServiceProto.WithdrawResponse.newBuilder()
                .setMessage(withdrawalTextResult)
                .setBalance(approvedAmount)
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<TerminalServiceProto.StatusReport> reportStatus(
            final StreamObserver<TerminalServiceProto.StatusResponse> responseObserver) {
            return new StreamObserver<TerminalServiceProto.StatusReport>() {
                @Override
                public void onNext(TerminalServiceProto.StatusReport report) {
                    report.getBanknotesMap().forEach((denomination, count) -> {
                    });
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    TerminalServiceProto.StatusResponse response = TerminalServiceProto.StatusResponse.newBuilder()
                        .setMessage("Status reports received")
                        .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<TerminalServiceProto.LogMessage> sendLogs(
            final StreamObserver<TerminalServiceProto.LogResponse> responseObserver) {
            return new StreamObserver<TerminalServiceProto.LogMessage>() {
                @Override
                public void onNext(TerminalServiceProto.LogMessage log) {
                    String token = JWT_TOKEN.get(Context.current());
                    long username = tokenUtil.getUsernameFromToken(token);
                    String logMessage = log.getLevel() + " !!! " + log.getMessage() + " !!!  " + log.getTimestamp();
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    TerminalServiceProto.LogResponse response = TerminalServiceProto.LogResponse.newBuilder()
                        .setMessage("Logs received")
                        .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
