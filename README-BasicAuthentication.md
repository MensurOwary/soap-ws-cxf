# SOAP Security : Basic Authentication to secure Apache CXF SOAP Web Service

The only change we have made is to add an Interceptor to intercept the request to extract the username and password.

```java
public class BasicAuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    @Autowired
    private UserRepository userRepository;

    public BasicAuthenticationInterceptor(){
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        // get the Authorization Header
        String basicAuth = getBasicAuthenticationPayload(message);
        // Extract username and password from the payload
        String[] credentials = getCredentials(basicAuth);
        // Extract individual username and password from the decoded credentials
        String username = credentials[0];
        String password = credentials[1];
        // authorize
        // if no exception thrown then successful
        authorize(username, password);
    }

    // returns the username:password pair, where
    // 0th entry is username
    // 1th entry is password
    private String[] getCredentials(String basicAuth){
        // get the base64 encoded part
        basicAuth = basicAuth.split("\\s+")[1];
        // decode the base64 encoded part
        byte[] decode = Base64.getDecoder().decode(basicAuth);
        // split the username and password
        String[] split = new String(decode).split(":");
        // if something is wrong
        if (split.length == 0 || split[0].isEmpty() || split.length != 2) throw new AuthenticationException("Username or Password has not been provided");
        // return the pair
        return new String[]{split[0], split[1]};
    }

    // Authorize the provided credentials against database
    private void authorize(String username, String password){
        User user = userRepository.getUserByUsername(username);
        if ( user == null || !DBHelper.passwordMatches(password, user.getPassword()) ){
            throw new AuthenticationException("Provided Credentials are Incorrect");
        }
    }

    // Gets the Authorization Header from the message
    private String getBasicAuthenticationPayload(Message message){
        Map<String, List> props = (Map<String, List>) message.getContextualProperty(Message.PROTOCOL_HEADERS);
        List<String> authorization = props.get("Authorization");
        // in case the client has not made a Basic Authentication type of Authentication
        if (authorization == null) throw new AuthenticationException("Basic Authentication  is Required");
        return authorization.get(0);
    }
}
```

The Apache CXF Interceptors are classified by `Phase`s which indicates in which part of the flow the interceptor should be invoked. `PRE_INVOKE` means to invoke before the subsequent service method is called.

Also we have to register the newly added interceptor in the `cxf-servlet.xml`

```xml
<cxf:bus>
    <cxf:inInterceptors>
        <bean id="basicAuthInterceptor" class="com.owary.interceptor.BasicAuthenticationInterceptor"/>
    </cxf:inInterceptors>
</cxf:bus>
```

That's all. So when a request is made it will be checked if it is authenticated.