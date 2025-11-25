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

* View certificates (PEM or CRT file)

$ openssl x509 -in aaa_cert.pem -noout -text
$ openssl x509 -in ca.crt -noout -text

* Create CA certificate from private key

$ openssl req -new -x509 -sha256 -key pkey.pem -out ca.crt -days 3650

* Create CSR to the Certificate Authority (CA)

$ openssl req -new -key client_private.key -out client_request.csr

* Issue new certificate based on CSR

$ openssl x509 -req -in client_request.csr -CA ca.crt -CAkey ca_private.key -out client.crt -days 365 -sha256

 Guides: 
 https://www.digitalocean.com/community/tutorials/openssl-essentials-working-with-ssl-certificates-private-keys-and-csrs
