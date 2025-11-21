// Cryptography overview
//
// Types of cryptographic algorithms:
//
// 1. Secret key cryptography
//
// a) Sream cipher
// Stream ciphers operate on a single bit (byte or computer word) at a time and
// implement some form of feedback mechanism so that the key is constantly changing
//
// Self-synchronizing stream
// Key stream depends on cipher text or plain text
//
// Synchronous stream ciphers
// Key stream DOESN'T depend on cipher text or plain text
//
// b) Block cipher
// A block cipher is so-called because the scheme encrypts one fixed-size block of
// data at a time.
//
// In a block cipher, a given plaintext block will always encrypt to the same
// ciphertext when using the same key (i.e., it is deterministic) whereas the same
// plaintext will encrypt to different ciphertext in a stream cipher.
//
// Electronic Codebook (ECB) mode
// Two identical cipher blocks will alway generate the same ciphertext. An error in
// single bit of ciphertext during the transmission will result in an error in
// entire block of decrypted plaintext
//
// Cipher Block Chaining (CBC) mode
// Two identical blocks will result in different ciphertext. An error in
// single bit of ciphertext during the transmission will result in an error in
// entire plaintext (Probably entirely different message ???)
//
// Cipher Feedback (CFB) mode
//
// Output Feedback (OFB) mode
//
// Counter (CTR) mode
//
//

// 2. Public key cryptography

// 3. Hash functions
//
// A message digests or one-way encryptionone, a fixed-length hash value is computed based upon
// the plaintext that makes it impossible for either the contents or length of the plaintext
// to be recovered.

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

