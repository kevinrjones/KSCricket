# ACS Stats Web README

## Certificates

Sometimes the app doesn't start because there's a certificate error. There are a few things to try.

1. Delete (or rename) ~/.aspnet. This is the directory that contains the dotnet core certificates, this may be out of date.

If that doesn't work then

2. Look at ~/Dropbox/projects/certificates and try and rebuild the certificates from there. Read this file to the end as you 
   may also need to update the Kestrel server certificates (it's all in the readme file). 






# Running the app in the staging environment

``` bash
cd /Users/kevinjones/Dropbox/projects/cricket/CricketArchive/acsstats/AcsStatsWeb
dotnet publish 
cd bin/Release/net9.0/publish
dotnet AcsStatsWeb.dll  --http_ports 5002 --environment staging
```
