package com.fandoco.wallet

fun getDatabaseUrl(): String {
    var fullDatabaseUrl = System.getenv("DATABASE_URL")

    if (fullDatabaseUrl.isNullOrBlank()) {
        fullDatabaseUrl = "postgres://wallet:password@localhost:5432/wallet"
    }
    return fullDatabaseUrl
}

fun getSecret(): String {
    var secret = System.getenv("SECRET_KEY")

    if (secret.isNullOrBlank()) {
        secret = "V26UJPH7LE3jgdTFYeg6VU0KWkZguo4nOwFPQyJy6HQCnxD6ZQDqxMnRHxaPQN9"
    }
    return secret
}