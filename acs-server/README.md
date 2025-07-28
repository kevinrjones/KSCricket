# ACS REST Service

## Configuration

The configuration is held in the `application.conf` file but can be overridden on the command line. See [this]
(https://ktor.io/docs/server-configuration-file.html) document. For our application the command line looks like this:

`run --args="-P:jwt.jwksUrl=http://localhost:5001/.well-known/openid-configuration/jwks -P:jwt.
issuer=https://localhost:5443 -P:jdbc.username=cricketarchive -P:jdbc.password=p4ssw0rd 
-P:jdbcurl=jdbc:mariadb://localhost:3306/cricketarchive -P:ktor.deployment.port=8081"`

The `run` part is there as I'm running it from Gradle, from the command line you need to full java command

From the command line for general dev
``` shell
./acs-server -P:jwt.jwksUrl=http://localhost:5443/.well-known/openid-configuration/jwks -P:jwt.
issuer=https://localhost:5443 -P:jdbc.username=cricketarchive -P:jdbc.password=p4ssw0rd -P:jdbcurl=jdbc:mariadb://localhost:3306/cricketarchive -P:ktor.deployment.port=8080
```

From the command line for 'local' dev
``` shell
./acs-server -P:jwt.jwksUrl=https://ids.knowledgespike.local/.well-known/openid-configuration/jwks -P:jwt.
issuer=https://ids.knowledgespike.local -P:jdbc.username=cricketarchive -P:jdbc.password=p4ssw0rd 
-P:jdbcurl=jdbc:mariadb://localhost:3306/cricketarchive -P:ktor.deployment.port=8080
```

From the command line for 'staging' dev
``` shell
./acs-server -P:jwt.jwksUrl=https://ids-staging.knowledgespike.cricket/.well-known/openid-configuration/jwks -P:jwt.
issuer=https://ids-staging.knowledgespike.cricket -P:jdbc.username=cricketarchive -P:jdbc.password=p4ssw0rd 
-P:jdbcurl=jdbc:mariadb://localhost:3306/cricketarchive -P:ktor.deployment.port=8080
```


From the command line for 'prod' dev
``` shell
./acs-server -P:jwt.jwksUrl=https://ids.knowledgespike.cricket/.well-known/openid-configuration/jwks -P:jwt.
issuer=https://ids.knowledgespike.cricket -P:jdbc.username=cricketarchive -P:jdbc.password=p4ssw0rd 
-P:jdbcurl=jdbc:mariadb://localhost:3306/cricketarchive -P:ktor.deployment.port=8080
```

## Certificates in Development Mode
In development mode IdS will be listening on HTTPS on localhost. For this app to connect to IdS the certificate 
authority certificate that I generate for IdS (and the other apps using https localhost) has to be known to Java

To do this:

1. Install root certificate in Java KeyStore which depends on the version of Java installed. If you are using IntellijIdea then go to the project structure and look at the SDK that is in use (/Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home/jre/lib/security/cacerts or /Library/Java/JavaVirtualMachines/openjdk-11.0.1.jdk/Contents/Home/lib/security/cacerts or /Users/kevinjones/.sdkman/candidates/java/current/lib/security/cacerts)
1. Download 'Keystore Explorer' application
1. `sudo /Applications/KeyStore\ Explorer.app/Contents/MacOS/KeyStore\ Explorer`
1. Default password is 'changeit'
1. Open the keystore
1. Import trusted certificate (cacert.pem)

If you can't find or don't want to update the keystore then there is another option in the code `setupDevSSL()`



