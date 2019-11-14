package handlers;

import cibaparameters.CIBAParameters;
import com.nimbusds.jose.*;

import configuration.ConfigurationFile;
import dao.DaoFactory;
import exceptions.ForbiddenException;
import com.nimbusds.jwt.JWTClaimsSet;
import tempErrorCache.TempErrorCache;
import transactionartifacts.TokenResponse;

import java.util.logging.Logger;


/**
 * This class provides the token reponses once validated.
 * */
public class TokenResponseHandler implements Handlers {
    private static final Logger LOGGER = Logger.getLogger(TokenResponseHandler.class.getName());
    private TokenResponseHandler() {

    }

    private static TokenResponseHandler tokenResponseHandlerInstance = new TokenResponseHandler();

    public static TokenResponseHandler getInstance() {
        if (tokenResponseHandlerInstance == null) {

            synchronized (TokenResponseHandler.class) {

                if (tokenResponseHandlerInstance == null) {

                    /* instance will be created at request time */
                    tokenResponseHandlerInstance = new TokenResponseHandler();
                }
            }
        }
        return tokenResponseHandlerInstance;


    }


    public Payload createTokenResponse(String auth_req_id) {
        CIBAParameters cibaparameters = CIBAParameters.getInstance();

    TokenResponse tokenResponse = DaoFactory.getInstance().getArtifactStoreConnector(ConfigurationFile.getInstance().
            getSTORE_CONNECTOR_TYPE()).getTokenResponse(auth_req_id);


    //Only checking the presence of refresh token and creating payload accordingly
    if (tokenResponse.getRefreshToken() != null) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("access_token", tokenResponse.getAccessToken())
                .claim("token_type", tokenResponse.getTokenType())
                .claim("refresh_token", tokenResponse.getRefreshToken())
                .claim("token_expires_in", tokenResponse.getTokenExpirein())
                .claim("id_token", tokenResponse.getIdToken())
                .build();


        Payload payload = new Payload(claims.toJSONObject());
        //JWSObject response = new JWSObject(header, payload);
        return payload;
    } else {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("access_token", tokenResponse.getAccessToken())
                .claim("token_type", tokenResponse.getTokenType())
                .claim("token_expires_in", tokenResponse.getTokenExpirein())
                .claim("id_token", tokenResponse.getIdToken())
                .build();


        Payload payload = new Payload(claims.toJSONObject());
        //JWSObject response = new JWSObject(header, payload);
        return payload;
    }
}



       public Payload createTokenErrorResponse (String auth_req_id) {
           if (TempErrorCache.getInstance().getAuthenticationResponse(auth_req_id).equals("Failed")) {
               System.out.println("Failed Authentication error response.");
              return new Payload("Authentication Denied.");

           } else {
               ErrorCodeHandlers errorCodeHandlers = ErrorCodeHandlers.getInstance();
               try {
                   throw new ForbiddenException("Invalid Token Request");
               } catch (ForbiddenException forbiddenException) {
                   forbiddenException.printStackTrace();
               }

               return new Payload("Invalid");
           }
       }

        public boolean checkTokenReceived (String auth_req_id) {

// TODO: 9/12/19 added now

                if (DaoFactory.getInstance().getArtifactStoreConnector(ConfigurationFile.getInstance().
                                getSTORE_CONNECTOR_TYPE()).getTokenResponse(auth_req_id) != null) {
                    return true;

                } else {
                    LOGGER.info("Token Response still not received");
                    return false;
                }
            }



}