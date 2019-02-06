# WS-Security : PasswordText Authentication

*Note: This example builds upon the simple SOAP WS that is built [here](./README-PrimitiveSOAPWebService.md)*

To implement basic security functionality in Apache CXF powered SOAP Web Service, the following steps should be executed

## 0. Import the required dependency
```xml
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-ws-security</artifactId>
    <version>${apache.cxf.version}</version>
</dependency>
```

## 1. Implement CallbackHandler interface
It is an interface with only one method - `handle(Callback[] callbacks)`.

Here's basic implementation. The credentials are hardcoded in this example.

```java
public class PasswordCallbackHandler implements CallbackHandler {

    private Map<String, String> credentials;

    public PasswordCallbackHandler() {
        this.credentials = new HashMap<>();
        this.credentials.put("admin", "admin");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback cb : callbacks){
            WSPasswordCallback wspc = (WSPasswordCallback) cb;

            String pass = credentials.get(wspc.getIdentifier());
            if (pass != null) {
                wspc.setPassword(pass);
                return;
            }
        }
    }
}
```

## 2. Update the cxf-servlet.xml
This is the complete xml file (`<beans>` tag has been removed for brevity)

```xml
<bean id="myPasswordCallback" class="com.owary.handler.PasswordCallbackHandler"/>

<jaxws:endpoint id="countryService"
                address="/cs"
                implementor="com.owary.endpoints.implementations.CountryServiceImpl">

    <jaxws:inInterceptors>

        <bean class="org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor">
            <constructor-arg>
                <map>
                    <entry key="action" value="UsernameToken"/>
                    <entry key="passwordType" value="PasswordText"/>
                    <entry key="passwordCallbackRef">
                        <ref bean="myPasswordCallback"/>
                    </entry>
                </map>
            </constructor-arg>
        </bean>
    </jaxws:inInterceptors>

</jaxws:endpoint>
```

 - `UsernameToken` is one of three WS-Security Authentication methods. In SOAP Envelope payload it is wrapped with `<Security>` tag. An example for `UsernameToken` payload
    ```xml
    <UsernameToken>
        <Username>usr</Username>
        <Password>pwd</Password>
    </UsernameToken>
    ```
 - Password type (taken from [here](https://www.oasis-open.org/committees/download.php/13392/wss-v1.1-spec-pr-UsernameTokenProfile-01.htm)):
 
     URI | Description
     --- | ---
     PasswordText (default) | PasswordDigest
     The actual password for the username, the password hash, or derived password or S/KEY. This type should be used when hashed password equivalents that do not rely on a nonce or creation time are used, or when a digest algorithm other than SHA1 is used. |  The digest of the password (and optionally nonce and/or creation timestame) for the username  using the algorithm described above.
      
 - The final parameter is the password handler we created previously. Done!
 
 
The actual request must be as follows

```xml
<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
    <Header>
        <!--The security tag-->
        <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" mustUnderstand="1">
            <!-- UsernameToken payload we mentioned previously -->
            <wsse:UsernameToken xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
                <wsse:Username xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">admin</wsse:Username>
                 <!--Type is PasswordText as you can see-->
                <wsse:Password xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">admin</wsse:Password>
            </wsse:UsernameToken>
        </wsse:Security>
    </Header>
    <Body>
        <getCountry xmlns="http://endpoints.owary.com/">
            <arg0 xmlns="">any</arg0>
        </getCountry>
    </Body>
</Envelope>
```

`mustUnderstand` - The other attribute that must be added only to a SOAPHeaderElement object is mustUnderstand. This attribute says whether or not the recipient (indicated by the actor attribute) is required to process a header entry. When the value of the mustUnderstand attribute is true, the actor must understand the semantics of the header entry and must process it correctly to those semantics. If the value is false, processing the header entry is optional. A SOAPHeaderElement object with no mustUnderstand attribute is equivalent to one with a mustUnderstand attribute whose value is false.