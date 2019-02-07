# SOAP Security : Basic Authentication with Spring Security to secure Apache CXF SOAP Web Service

## 0.0 Adding necessary dependencies
```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-web</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>${spring.version}</version>
</dependency>
```

Also, we have to remove `spring-security-crypto` dependency because it is already included in newly added dependencies.

## 0.1 Changes to web.xml
Add the following to the web.xml file. These additions enable the Spring Security (which we'll implement in the next step)
```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 1.0 Spring Security Configuration
```java
@Configuration
@EnableWebSecurity(debug = true)
public class Config extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/ws/cs?wsdl", "/ws/").permitAll()
                .antMatchers("/*").hasAnyRole()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
```

We're basically enabling **Basic Authentication** using `.httpBasic()` method. The user we're asking for is a user with username and password as *admin*.

## 2. Changes to the `cxf-servlet.xml`
First of all, we have to disable the WS-Security stuffs we previously added. So basically, the new version of the config file is as below.
```xml
<jaxws:endpoint id="countryService"
                    address="/cs"
                    implementor="com.owary.endpoints.implementations.CountryServiceImpl" />

<!--Spring part-->
<!--Autowire enabling-->
<context:annotation-config/>
<bean id="countryRepository" class="com.owary.repository.CountryRepositoryImpl"/>
<bean id="userRepository" class="com.owary.repository.UserRepositoryImpl"/>

<bean name="config" class="com.owary.config.Config" />
<bean name="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>
```

**config** bean is the Spring Security Config class we have created previously.

**passwordEncoder** bean is the Password Encoder to encode the password.