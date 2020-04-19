package com.zd.user.config;

import com.zd.core.config.redis.template.RedisTemplateToken;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;

    private final JwtAccessTokenConverter accessTokenConverter;

    /**
     * redis连接工厂
     */
    private final RedisTemplateToken redisTemplateToken;

    public AuthorizationServerConfig(AuthenticationManager authenticationManager, JwtAccessTokenConverter accessTokenConverter, RedisTemplateToken redisTemplateToken) {
        this.authenticationManager = authenticationManager;
        this.accessTokenConverter = accessTokenConverter;
        this.redisTemplateToken = redisTemplateToken;
    }

    /**
     * 令牌存储
     *
     * @return redis令牌存储对象
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisTemplateToken.getConnectionFactory());
    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 允许表单认证
        // 允许check_token访问
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
     * 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     * <p>
     * Authorization code（授权码模式）
     * Implicit Grant（隐式模式）
     * Resource Owner Password Credentials（密码模式）
     * Client Credentials（客户端模式）
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //授权码授权模式(Authorization code)
        clients.inMemory()
                .withClient("admin")
                .secret("123456")
                .authorizedGrantTypes("client_credentials")
                .scopes("all");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
