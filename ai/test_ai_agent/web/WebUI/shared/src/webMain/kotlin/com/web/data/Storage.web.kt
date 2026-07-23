package com.web.data

import kotlinx.browser.localStorage

actual class Storage actual constructor() {

    actual fun load(key: String): String? {
        return localStorage.getItem(key)
    }

    actual fun store(key: String, value: String) {
        localStorage.setItem(key, value)
    }

}