package com.example.tasky.agenda.domain.model

data class Photo(
    val id: String,
    val uri: String,
    val compressedBytes: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (!compressedBytes.contentEquals(other.compressedBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (compressedBytes?.contentHashCode() ?: 0)
        return result
    }
}
