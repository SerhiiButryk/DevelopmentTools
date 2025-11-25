# Full featured command line crypto tool

Docs: https://wiki.openssl.org/index.php/Command_Line_Utilities

* Gen RSA private key 

$ openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out pkey.pem

* View private key

$ openssl pkey -in pkey.pem -text

* Gen public key from private key

$ openssl rsa -pubout -in pkey.pem -outform PEM -out pubkey.pem

* View public key

$ openssl rsa -pubin -in pubkey.pem -text

* View SSL certificate (PEM file)

$ openssl x509 -in aaa_cert.pem -noout -text

 -----------------------

 Guides: 
 https://www.digitalocean.com/community/tutorials/openssl-essentials-working-with-ssl-certificates-private-keys-and-csrs
