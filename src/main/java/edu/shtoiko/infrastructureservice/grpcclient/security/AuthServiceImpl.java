package edu.shtoiko.infrastructureservice.grpcclient.security;

import edu.shtoiko.grpc.auth.AuthServiceGrpc;
import edu.shtoiko.grpc.auth.AuthServiceProto;
import edu.shtoiko.infrastructureservice.grpcclient.interceptor.AuthInterceptor;
import edu.shtoiko.infrastructureservice.utils.JwtTokenUtil;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final JwtTokenUtil tokenUtil;
    private final TerminalService terminalService;

    @Override
    public void authenticate(AuthServiceProto.AuthRequest request, StreamObserver<AuthServiceProto.AuthResponse> responseObserver) {
        long username = request.getUsername();
        String password = request.getPassword();
        if (isValidUser(username, password)) {
            String token = tokenUtil.createToken(username);
            Context currentContext = Context.current();
            Context newContext = currentContext.withValue(AuthInterceptor.USERNAME, username);
            Context previous = newContext.attach();
            try {
                AuthServiceProto.AuthResponse response = AuthServiceProto.AuthResponse.newBuilder().setToken(token).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } finally {
                newContext.detach(previous);
            }
        } else {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
        }
    }
    private boolean isValidUser(long username, String password) {
        return terminalService.checkin(username, password);
    }
}
