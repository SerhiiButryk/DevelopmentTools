package com.web.data

expect class Storage() {
    fun load(key: String): String?
    fun store(key: String, value: String)
}