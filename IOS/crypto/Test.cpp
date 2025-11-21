#include "Test.hpp"

#include <vector>
#include <print>

#include <openssl/evp.h>
#include <openssl/rand.h>

#include "Types.hpp"
#include "Crypto.hpp"

ByteArray getKeyData(size_t keySize, const char* fileName);

ByteArray getIVData(size_t ivSize, const char* fileName);

std::string getMessage(const char* fileName);

// Key and IV sizes for AES-256-CBC
const size_t keySize = 32; // 256 bits
const size_t ivSize = 16;  // 128 bits

bool encryptTest()
{
    auto key = getKeyData(keySize, KEY_DATA_FILE);

    if (key == nullptr) return false;
    
    auto iv = getIVData(ivSize, IV_DATA_FILE);

    if (iv == nullptr) return false;

    auto input = getMessage(INPUT_DATA_FILE);
    
    if (input.empty()) return false;
    
    std::vector<Byte> output = encrypt(input, key, iv);
    
    if (output.empty()) {
        std::println("encryptTest() Failed to encrypt");
        return false;
    }
    
    saveInFile(ENCRYPTED_DATA_FILE, output.data(), output.size());
    
    std::println("encryptTest() Done");
    
    return true;
}

bool decryptTest()
{
    auto key = getKeyData(keySize, KEY_DATA_FILE);

    if (key == nullptr) return false;
    
    auto iv = getIVData(ivSize, IV_DATA_FILE);

    if (iv == nullptr) return false;
    
    size_t len = 0;
    ByteArray input = readFile(ENCRYPTED_DATA_FILE, &len);
    
    if (input == nullptr) return false;
    
    std::vector<Byte> output = decrypt(input, (int) len, key, iv);
    
    if (output.empty()) {
        std::println("decryptTest() Decryption failed.");
        return false;
    }
    
    saveInFile(DECRYPTED_DATA_FILE, output.data(), output.size());
    
    std::println("decryptTest() Done");
    
    return true;
    
}

ByteArray getKeyData(size_t keySize, const char* fileName)
{
    ByteArray content = nullptr;

    if (isFileNotEmpty(fileName)) {
        content = readFile(fileName);
    }
    
    if (content != nullptr) {
        std::println("getKeyData() got from file");
        return content;
    }
    
    ByteArray key = new Byte[keySize]{};
    
    if (RAND_bytes(key, (int) keySize) != 1) {
        std::println("getKeyData() Failed to gen random key");
        return nullptr;
    }
    
    saveInFile(fileName, key, keySize);
    
    return key;
}

ByteArray getIVData(size_t ivSize, const char* fileName)
{
    ByteArray content = nullptr;

    if (isFileNotEmpty(fileName)) {
        content = readFile(fileName);
    }
    
    if (content != nullptr) {
        std::println("getIVData() got from file");
        return content;
    }
    
    ByteArray iv = new Byte[ivSize]{};
    
    if (RAND_bytes(iv, (int) ivSize) != 1) {
        std::println("getIVData() Failed to gen random iv");
        return nullptr;
    }
    
    saveInFile(fileName, iv, ivSize);
    
    return iv;
}

std::string getMessage(const char* fileName)
{
    ByteArray content = nullptr;

    if (isFileNotEmpty(fileName)) {
        content = readFile(fileName);
    }
    
    if (content == nullptr) {
        std::println("getMessage() Failed to get from file");
        return {};
    }
    
    std::println("getMessage() Got from file");
    
    return std::string((char*) content);
}
