/*

     Cryptography overview

     Types of cryptographic algorithms:

     1. Secret key cryptography

     a) Sream cipher
     Stream ciphers operate on a single bit (byte or computer word) at a time and
     implement some form of feedback mechanism so that the key is constantly changing

     Self-synchronizing stream
     Key stream depends on cipher text or plain text

     Synchronous stream ciphers
     Key stream DOESN'T depend on cipher text or plain text

     b) Block cipher
     A block cipher is so-called because the scheme encrypts one fixed-size block of
     data at a time.

     In a block cipher, a given plaintext block will always encrypt to the same
     ciphertext when using the same key (i.e., it is deterministic) whereas the same
     plaintext will encrypt to different ciphertext in a stream cipher.

     Electronic Codebook (ECB) mode
     Two identical cipher blocks will alway generate the same ciphertext. An error in
     single bit of ciphertext during the transmission will result in an error in
     entire block of decrypted plaintext

     Cipher Block Chaining (CBC) mode
     Two identical blocks will result in different ciphertext. An error in
     single bit of ciphertext during the transmission will result in an error in
     entire plaintext (Probably entirely different message ???)

     Cipher Feedback (CFB) mode
     Stream cipher implementation using block cipher as the underlying primitive and can encrypt
     data in units smaller than the block size.

     Output Feedback (OFB) mode
     Similar to CFB mode, but the same plaintext block from generating the same ciphertext block by 
     using an internal feedback mechanism.

     Counter (CTR) mode
     A new block cipher which allows encryption of data blocks in parallel.
     Two identical plaintext blocks will result in different ciphertext.

     2. Public key cryptography

     a) RSA 
     It's the first and the most common algorithm for public key cryptography.
     It could be used for key generation of private/public key, key exchange,
     digital signatures and encryption of small data blocks (as it is very slow).

     b) Diffie-Hellman
     It's the second most common algorithm for public key cryptography.
     However it is used for key exchange only.

     c) Elliptic Curve Cryptography (ECC)
     It's the third most common algorithm for public key cryptography.
     It allows to generate smaller private/public key and in some cases could be faster than RAS keys.

     3. Hash functions

     A message digests or one-way encryptionone, a fixed-length hash value is computed based upon
     the plaintext that makes it impossible for either the contents or length of the plaintext
     to be recovered.
 
     4. HMAC
 
     This type of encryption allows to confirm that a message was not altered. It is fast.
 
     Example:
 
     Message digest = HASH_FUNCTION(Private key + message)
 
     Eve cannot tamper the message and regenerate the hash because it doesn't know private key

*/

#pragma once

#include <string>

/*
    Some APIs to perfome basic crypto operations
 */

// Encrypts plaintext using AES-256-CBC
std::vector<unsigned char> encrypt(const std::string& plaintext,
                                   const unsigned char* key,
                                   const unsigned char* iv);

// Decrypts plaintext using AES-256-CBC
std::vector<unsigned char> decrypt(const unsigned char* ciphertext,
                                   int ciphertextSize,
                                   const unsigned char* key,
                                   const unsigned char* iv);

// Save data to a file
void saveInFile(const char* fileName, unsigned char* buffer, const size_t size);

// Read data from a file
unsigned char* readFile(const char* fileName, size_t* len = nullptr);

bool isFileNotEmpty(const char* fileName);

