package nirmalya.aathithya.webmodule.common.security;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;

//@Configuration
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//	static final String CLIENT_ID = "android-client";
//    static final String CLIENT_SECRET = "android-secret";
//    static final String GRANT_TYPE_PASSWORD = "password";
//    static final String AUTHORIZATION_CODE = "authorization_code";
//    static final String REFRESH_TOKEN = "refresh_token";
//    static final String IMPLICIT = "implicit";
//    static final String SCOPE_READ = "read";
//    static final String SCOPE_WRITE = "write";
//    static final String TRUST = "trust";
//    static final int ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
//    static final int REFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
//
//        configurer
//                .inMemory()
//                .withClient(CLIENT_ID)
//                .secret(CLIENT_SECRET)
//                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT )
//                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
//                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
//                refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(tokenStore)
//                .authenticationManager(authenticationManager);
//    }
//
//
//   @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
//    }
//
//	
//}
