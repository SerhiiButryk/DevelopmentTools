#include "Crypto.hpp"

#include <openssl/evp.h>
#include <openssl/rand.h>

#include <memory>
#include <print>
#include <vector>
#include <stdio.h>
#include <stdlib.h>

// A unique_ptr custom deleter for OpenSSL's EVP_CIPHER_CTX
struct EvpCipherCtxDeleter {
    void operator()(EVP_CIPHER_CTX* ctx) const {
        EVP_CIPHER_CTX_free(ctx);
    }
};

using EvpCipherCtxPtr = std::unique_ptr<EVP_CIPHER_CTX, EvpCipherCtxDeleter>;

// Function to handle OpenSSL errors
void handle_openssl_error() {
    std::println("OpenSSL error occurred.");
}

// Strategy explained here:
// https://linux.die.net/man/3/evp_encryptupdate
std::vector<unsigned char> encrypt(const std::string& plaintext,
                                   const unsigned char* key,
                                   const unsigned char* iv)
{
    EvpCipherCtxPtr ctx(EVP_CIPHER_CTX_new());
    if (!ctx) {
        handle_openssl_error();
        return {};
    }
    
    if (1 != EVP_EncryptInit_ex(ctx.get(), EVP_aes_256_cbc(), nullptr, key, iv)) {
        handle_openssl_error();
        return {};
    }
    
    // By convention the size of buffer should be input length + cipher block size
    std::vector<unsigned char> ciphertext(plaintext.size() + EVP_MAX_BLOCK_LENGTH);
    // Len of encrypted byte string
    int len = 0;
    int ciphertext_len = 0;
    
    // Encryptes the current block of data
    // This function can be called multiple times. So we can encrypt successive block data
    if (1 != EVP_EncryptUpdate(ctx.get(), ciphertext.data(), &len,
                               reinterpret_cast<const unsigned char*>(plaintext.data()), static_cast<int>(plaintext.size()))) {
        handle_openssl_error();
        return {};
    }
    
    ciphertext_len = len;
    
    // Encrypts the final data part only if padding is enabled or used.
    // This is a last step. No further EVP_EncryptUpdate() can be made.
    if (1 != EVP_EncryptFinal_ex(ctx.get(), ciphertext.data() + len, &len)) {
        handle_openssl_error();
        return {};
    }
    
    ciphertext_len += len;
    ciphertext.resize(ciphertext_len);
    
    return ciphertext;
}

std::vector<unsigned char> decrypt(const unsigned char* ciphertext,
                                   int ciphertextSize,
                                   const unsigned char* key,
                                   const unsigned char* iv)
{
    EvpCipherCtxPtr ctx(EVP_CIPHER_CTX_new());
    
    if (!ctx) {
        handle_openssl_error();
        return {};
    }
    
    if (1 != EVP_DecryptInit_ex(ctx.get(), EVP_aes_256_cbc(), nullptr, key, iv)) {
        handle_openssl_error();
        return {};
    }
    
    // By convention the size of buffer should be input length + cipher block size - 1
    std::vector<unsigned char> plaintext(ciphertextSize + EVP_MAX_BLOCK_LENGTH);
    int len = 0;
    int plaintext_len = 0;
    
    if (1 != EVP_DecryptUpdate(ctx.get(), plaintext.data(), &len, ciphertext, ciphertextSize)) {
        handle_openssl_error();
        return {};
    }
    
    plaintext_len = len;
    
    if (1 != EVP_DecryptFinal_ex(ctx.get(), plaintext.data() + len, &len)) {
        handle_openssl_error();
        return {};
    }
    
    plaintext_len += len;
    plaintext.resize(plaintext_len);
    
    return plaintext;
}

void saveInFile(const char* fileName, unsigned char* buffer, const size_t bufferSize)
{
    FILE* file = fopen(fileName, "wb");
    
    if (!file) {
        std::println("saveInFile() ERROR: failed to open file: {}", fileName);
        return;
    }
    
    size_t result = fwrite(buffer,
                           1 /* Size of each object. As operate with string data this is more likely
                              1 byte (size of 1 character) */, bufferSize, file);
    
    if (result != bufferSize) {
        std::println("saveInFile() ERROR: wrote less data than requested");
    }
    
    fclose(file);
    
    std::println("saveInFile() done, file: {}", fileName);
}

// Read data from a file
unsigned char* readFile(const char* fileName, size_t* len)
{
    FILE* file = fopen(fileName, "rb");
    
    if (!file) {
        std::println("readFile() ERROR: failed to open file: {}", fileName);
        return nullptr;
    }
    
    fseek(file, 0, SEEK_END); // Move file pointer to the end of the file
    long size = ftell(file); // The number of bytes from the beginning
    
    if (len != nullptr) *len = size;
    
    if (size == 0) {
        std::println("readFile() file is empty, stopped");
        fclose(file);
        return nullptr;
    }
    
    unsigned char* buffer = new unsigned char[size]{};
    
    rewind(file); // Move file pointer to the begging of the file
    
    long actual = fread(buffer, 1 /* 1 byte */, size, file); // Read file
    
    std::println("readFile() actual read size = {}, expected = {}", actual, size);
    
    fclose(file);
    
    return buffer;
}

bool isFileNotEmpty(const char* fileName)
{
    FILE* file = fopen(fileName, "rb");
    
    if (!file) {
        std::println("isFileNotEmpty() ERROR: failed to open file: {}", fileName);
        return false;
    }
    
    fseek(file, 0, SEEK_END); // Move file pointer to the end of the file
    long size = ftell(file); // The number of bytes from the beginning
    
    std::println("isFileNotEmpty() size = {}", size);
    
    fclose(file);
    
    return size != 0;
}

