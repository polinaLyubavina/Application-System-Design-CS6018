package com.example.lifestyleapp

import org.junit.Test
import org.junit.Before
import org.junit.Assert.*

class LoginActivityTest {

    private var userEmail
    private var userPassword
    private var userEmptyEmail
    private var userEmptyPassword

    @Before
    fun setUp() {
        userEmail = "1234@abc.com"
        userPassword = 5678
        userEmptyEmail
        userEmptyPassword
    }

    @Test
    fun providedEmail() {
        assertNotNull(userEmail)
        assertNull(userEmptyEmail)
    }

    @Test
    fun providedPassword() {
        assertNotNull(userPassword)
        assertNull(userEmptyPassword)
    }
}
