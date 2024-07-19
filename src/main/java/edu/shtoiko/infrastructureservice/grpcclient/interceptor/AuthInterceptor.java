package edu.shtoiko.infrastructureservice.grpcclient.interceptor;

import edu.shtoiko.infrastructureservice.utils.JwtTokenUtil;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements ServerInterceptor {
    public static final Context.Key<String> JWT_TOKEN = Context.key("jwt");
    public static final Context.Key<Long> USERNAME = Context.key("username");


    private final JwtTokenUtil tokenUtil;
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String token = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
        String methodName = call.getMethodDescriptor().getFullMethodName();
        System.out.println(methodName);
        System.out.println("AuthInterceptor: received call with token " + token);

        if (methodName.equals("AuthService/Authenticate")) {
            return next.startCall(call, headers);
        }
        if (token == null || !tokenUtil.validateToken(token)) {
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid or expired token"), headers);
            return new ServerCall.Listener<ReqT>() {};
        }
        token = token.substring(7); // Видаляємо префікс "Bearer"
        Context ctx = Context.current().withValue(JWT_TOKEN, token);
        return Contexts.interceptCall(ctx, call, headers, next);
    }
}
