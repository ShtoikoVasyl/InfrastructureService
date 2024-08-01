package edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient;

import edu.shtoiko.grpc.TerminalServiceProto;
import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WithdrawResponseHandler {
    private final ConcurrentHashMap<String, StreamObserver<TerminalServiceProto.WithdrawResponse>> responseMap =
        new ConcurrentHashMap<>();

    public StreamObserver<TerminalServiceProto.WithdrawResponse> findWithdrawResponse(String requestIdentifier) {
        return responseMap.get(requestIdentifier);
    }

    public void putResponse(String requestIdentifier,
        StreamObserver<TerminalServiceProto.WithdrawResponse> terminalResponse) {
        responseMap.put(requestIdentifier, terminalResponse);
    }

    public void sendWithdrawResponse(WithdrawResult withdrawResult) {
        String requestIdentifier = withdrawResult.getRequestIdentifier();
        StreamObserver<TerminalServiceProto.WithdrawResponse> responseObserver =
            findWithdrawResponse(requestIdentifier);

        TerminalServiceProto.WithdrawResponse response = TerminalServiceProto.WithdrawResponse.newBuilder()
            .setMessage(withdrawResult.getSystemComment())
            .setValue(withdrawResult.getAllowedAmount().longValue())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
