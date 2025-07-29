package com.example.tasky.core.data.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.serialization.json.Json
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager(private val keyAlias: String = "DefaultEncryptionKey") {

    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(data: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data)
        return iv + encrypted
    }

    fun decrypt(encryptedData: ByteArray): ByteArray? {
        return try {
            val iv = encryptedData.copyOfRange(0, cipher.blockSize)
            val data = encryptedData.copyOfRange(cipher.blockSize, encryptedData.size)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
            cipher.doFinal(data)
        } catch (e: Exception) {
            null
        }
    }

    fun encryptString(plainText: String): String {
        val encryptedBytes = encrypt(plainText.toByteArray())
        return encryptedBytes.encodeToBase64()
    }

    fun decryptString(encryptedBase64: String): String? {
        return try {
            val encryptedBytes = encryptedBase64.decodeFromBase64()
            val decryptedBytes = decrypt(encryptedBytes)
            decryptedBytes?.decodeToString()
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> encryptObject(obj: T, json: Json = Json.Default): String {
        val jsonString = json.encodeToString(obj)
        return encryptString(jsonString)
    }

    inline fun <reified T> decryptObject(encryptedBase64: String, json: Json = Json.Default): T? {
        return try {
            val decryptedJson = decryptString(encryptedBase64)
            decryptedJson?.let { jsonString ->
                json.decodeFromString<T>(jsonString)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun ByteArray.encodeToBase64(): String = Base64.getEncoder().encodeToString(this)
    private fun String.decodeFromBase64(): ByteArray = Base64.getDecoder().decode(this)
}
