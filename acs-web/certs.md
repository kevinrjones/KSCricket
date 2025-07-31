## Certificates


1. See this answer on [SO](https://stackoverflow.com/questions/21297139/how-do-you-sign-a-certificate-signing-request-with-your-certification-authority/21340898#21340898)
    1. These may also be useful
        1. [https://www.humankode.com/asp-net-core/develop-locally-with-https-self-signed-certificates-and-asp-net-core](https://www.humankode.com/asp-net-core/develop-locally-with-https-self-signed-certificates-and-asp-net-core)
        1. [https://deliciousbrains.com/ssl-certificate-authority-for-local-https-development/](https://deliciousbrains.com/ssl-certificate-authority-for-local-https-development/)

1. For the latest changes to certificates on Mac Catalina and iOS see these
    1. [https://support.apple.com/en-us/HT210176](https://support.apple.com/en-us/HT210176)
    1. [http://blog.nashcom.de/nashcomblog.nsf/dx/more-strict-server-certificate-handling-in-ios-13-macos-10.15.htm](http://blog.nashcom.de/nashcomblog.nsf/dx/more-strict-server-certificate-handling-in-ios-13-macos-10.15.htm)
    1. [https://eengstrom.github.io/musings/self-signed-tls-certs-v.-chrome-on-macos-catalina](https://eengstrom.github.io/musings/self-signed-tls-certs-v.-chrome-on-macos-catalina)

1. For the root cert
    1. Generate a key and a certificate
        1. `openssl req -x509 -config openssl-ca.conf -newkey rsa:4096 -sha256 -days 825 -nodes -out cacert.pem -outform PEM`
        1. Add the data needed for the root cert
        ``` shell
        touch index.txt
        echo '01' > serial.txt
        ```
    1. Install the root cert
        1. Import into KeyChain
        1. Double click and check 'Always Trust'
    1. Can now create a certificate for the site

1. For the development certificate
    1. Create a CSR
        1. `openssl req -config openssl.localhost.conf -newkey rsa:2048 -sha256 -nodes -days 825 -out dev.knowledgespike.local.csr -keyout dev.knowledgespike.local.key -outform PEM`
    1. Create the cert from the csr
        1. `openssl ca -config openssl-ca.conf -policy signing_policy -extensions signing_req -days 825 -out dev.knowledgespike.local.pem -infiles dev.knowledgespike.local.csr`
    1. Import this cert into the KeyChain (not sure that this is necessary)
    1. Make sure that Nginx (or whichever server you are using) references this certificate (see config below)
    1. To convert the cert to 'pfx' use `openssl pkcs12 -inkey dev.knowledgespike.local.key -in dev.knowledgespike.local.pem -export -out dev.knowledgespike.local.pfx -certfile cacert.pem`
        1. Use 'p4ssw0rd' as the password
        1. Copy this file to location where pfx is read from by Kestrel
        1. This is used by 'Kestrel' when running the app locally (without Nginx)
    1. Add it to the keychain and make sure that its CA is trusted

1. Java
    1. The default keystore password is `changeit`
    1. Import the ca certificate into the ca keystore `sudo keytool -importcert -alias knowledgespikeCA -cacerts -file cacert.pem`
    1. You can list the key store to see if the cert is in there `keytool -list -v -cacerts` or `keytool -list  -cacerts`
    1. If the entry already exists you can delete it with `sudo keytool -delete -alias knowledgespikeCA -cacerts`
    1. *** MAKE SURE YOU ARE USING THE CORRECT SDK *** 



1. In .dotnet you can now configure this in `appsettings.json` with
``` json
  "Kestrel": {
    "Certificates": {
      "Default": {
        "Path": "../certs/dev.knowledgespike.local.pfx",
        "Password": "p4ssw0rd"
      }
    }
  }

```

1. Android
    1. Create a CRT from the pem

        `openssl x509 -in cacert.pem -inform PEM -outform DER -out ca-cert.crt`
    1. Push the crt to the android device using adb, e.g.

        `/Users/kevinjones/Library/Android/sdk/platform-tools/adb push ca-cert.crt /sdcard/Download/ca_cert_file.crt`

