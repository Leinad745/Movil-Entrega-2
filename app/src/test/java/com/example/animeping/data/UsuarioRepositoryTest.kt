package com.example.animeping.data

import androidx.
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndoridJUnit4::class)
class UsuarioRepositoryTest {
    private lateinit var userRepository : UserRepository

    @Before
    fun setUp() {
        userRepository = UserRepository(mockContext)
    }
}