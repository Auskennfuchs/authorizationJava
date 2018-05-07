package de.afb.authorization.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.afb.authorization.AuthSession;
import de.afb.authorization.KeyProvider;
import de.afb.authorization.RefreshTokenCreator;
import de.afb.authorization.error.ErrorDetails;
import de.afb.authorization.error.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RequestMapping(path = "/authentication")
public class AuthorizationController implements ErrorController {

    @Autowired
    private KeyProvider keyProvider;

    @Autowired
    private RefreshTokenCreator refreshTokenCreator;

    @Value("${debug:false}")
    private boolean debug;

    @RequestMapping(path = "/authenticate")
    @ResponseBody
    public AuthSession authenticate(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) {
        Algorithm algorithm = Algorithm.RSA256(null,keyProvider.getPrivateKey());
        String compactJWT = JWT.create()
                .withSubject(name)
                .withClaim("iss","sales")
                .sign(algorithm);

        String refreshToken = refreshTokenCreator.create();

        AuthSession authSession = new AuthSession(compactJWT,refreshToken,name,"hier@da.de",10101011,10101010,"BANK");
        return authSession;
    }

    @RequestMapping(path = "/validate")
    @ResponseBody
    public String validate(@RequestHeader(name="authorization")String authHeader) throws ForbiddenException {
        String[] authSplit = authHeader.split(" ");
        if(authSplit.length!=2 || !"Bearer".equals(authSplit[0])) {
            throw new ForbiddenException("missing authorization token");
        }
        String jwt = authSplit[1];

        Algorithm algorithm = Algorithm.RSA256(keyProvider.getPublicKey(),null);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            return decodedJWT.getSubject();
        }catch (SignatureVerificationException | JWTDecodeException ex) {
            throw new ForbiddenException("access denied");
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<ErrorDetails> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), debug ? ex.getStackTrace() : null);
        return new ResponseEntity<>(details, HttpStatus.FORBIDDEN);
    }

}
