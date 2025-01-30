package com.advertising.advertising_exposure.util

import org.springframework.test.util.ReflectionTestUtils

class TestUtils {
    companion object {
        fun setId(target: Any, fieldName: String, value: Any) {
            ReflectionTestUtils.setField(target, fieldName, value)
        }
    }
}