package Modelo


import com.example.marveltptaller.Keys
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class CharactersService(
    private val charactersRepository: CharactersRepository
) {

    suspend fun getCharacters(): List<Character> {
        val keys = Keys()
        val timestamp = System.currentTimeMillis()
        return charactersRepository.getCharacters(
            timestamp,
            //md5(timestamp.toString() + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY)
            md5(timestamp.toString() + keys.kPrivate + keys.kPublic)
        )
    }

    private fun md5(string: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(MD5)
            digest.update(string.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}