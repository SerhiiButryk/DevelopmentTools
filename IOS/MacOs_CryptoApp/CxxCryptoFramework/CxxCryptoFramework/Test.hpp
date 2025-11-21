#pragma once

static const char* KEY_DATA_FILE = "Key_32_byte_Data.txt";
static const char* IV_DATA_FILE = "IV_16_byte_Data.txt";
static const char* INPUT_DATA_FILE = "Input_Data.txt";
static const char* ENCRYPTED_DATA_FILE = "Encrypted_Data.txt";
static const char* DECRYPTED_DATA_FILE = "Decrypted_Data.txt";

bool encryptTest();
bool decryptTest();
