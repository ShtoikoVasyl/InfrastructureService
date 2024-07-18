package edu.shtoiko.infrastructureservice.grpcclient.interceptor;

import edu.shtoiko.infrastructureservice.utils.JwtTokenUtil;
import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static edu.shtoiko.infrastructureservice.grpcclient.interceptor.AuthInterceptor.USERNAME;

@Component
@RequiredArgsConstructor
public class TokenAddingServerInterceptor implements ServerInterceptor {
    private final JwtTokenUtil tokenUtil;
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String methodName = call.getMethodDescriptor().getFullMethodName();
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata headers) {
                super.sendHeaders(headers);
            }

            @Override
            public void sendMessage(RespT message) {
                super.sendMessage(message);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                String token = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    try {
                        long username = tokenUtil.getUsernameFromToken(token);
                        String newToken = tokenUtil.createToken(username);
                        Metadata.Key<String> tokenKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
                        trailers.put(tokenKey, "Bearer " + newToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (methodName.equals("AuthService/Authenticate") && status.isOk()) {


                    long username = USERNAME.get();
                    String newToken = tokenUtil.createToken(username);
                    Metadata.Key<String> tokenKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
                    trailers.put(tokenKey, "Bearer " + newToken);
                }
                super.close(status, trailers);
            }
        }, headers)) {};
    }
}
