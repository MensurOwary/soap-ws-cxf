# WS-Security : PasswordDigest Authentication
This example shows a basic implementation of **PasswordDigest** authentication. Also note that, this example builds upon the previous authentication - [PasswordText](./README-PasswordTextAuthEncryptedPassword.md)

# 0. Change to cxf-servlet.xml
The only thing we change is the following.
```xml
<entry key="passwordType" value="PasswordDigest"/>
```

# 1. Password Digest workflow
Used elements
 - Username
 - Password
 - *Nonce* is a one time token that is used to prevent replay attack. It is sent in an encoded format.
 - *Created* is a timestamp string
 
### The workflow is as follows:
 - Nonce is decoded
 - Nonce, Created and Password is merged and encrypted on client side and supplied in the security header
 - On the service layer, the exact same thing is done with the actual password of the user with supplied username
 - If matched then authorized
 
*Note that, PasswordDigest can't be used with BCrypt or any other non-repeating password encoder, because it requires the plaintext password.*

I have used hardcoded password throughout this example, because since it requires plaintext, there's no point in using database where the passwords from previous example are stored in an encrypted format.

The rest is upon Apache CXF.

