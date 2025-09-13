#include <stdio.h>
#include <string.h>
#include "aes.h"

void print_hex(uint8_t *data, size_t len) {
    for (size_t i = 0; i < len; ++i) {
        printf("%02x", data[i]);
    }
    printf("\n");
}

int main() {
    // Example key and IV (Initialization Vector)
    uint8_t key[16] = "examplekey123456";
    uint8_t iv[16] = "exampleiv1234567";

    // Example plaintext
    uint8_t plaintext[] = "This is a test message for AES CBC mode.";
    size_t plaintext_len = strlen((char *)plaintext);

    // Padding plaintext to be a multiple of AES block size (16 bytes)
    size_t padded_len = ((plaintext_len / 16) + 1) * 16;
    uint8_t padded_plaintext[padded_len];
    memcpy(padded_plaintext, plaintext, plaintext_len);
    memset(padded_plaintext + plaintext_len, 0, padded_len - plaintext_len);

    // Buffer for ciphertext
    uint8_t ciphertext[padded_len];

    // AES context
    struct AES_ctx ctx;

    // Initialize AES context with key and IV
    AES_init_ctx_iv(&ctx, key, iv);

    // Encrypt the plaintext
    AES_CBC_encrypt_buffer(&ctx, padded_plaintext, padded_len);

    // Copy ciphertext
    memcpy(ciphertext, padded_plaintext, padded_len);

    // Print ciphertext in hex
    printf("Ciphertext: ");
    print_hex(ciphertext, padded_len);

    // Decrypt the ciphertext
    AES_init_ctx_iv(&ctx, key, iv);
    AES_CBC_decrypt_buffer(&ctx, ciphertext, padded_len);

    // Print decrypted text
    printf("Decrypted text: %s\n", ciphertext);

    return 0;
}
