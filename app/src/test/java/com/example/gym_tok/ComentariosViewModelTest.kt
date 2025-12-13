package com.example.gym_tok

import com.example.gym_tok.api.ApiService
import com.example.gym_tok.controller.ComentariosViewModel
import com.example.gym_tok.model.Comment
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ComentariosViewModelTest {

    // --- Dependencias y configuración ---
    private lateinit var viewModel: ComentariosViewModel
    private val mockApiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Se establece el dispatcher de prueba para controlar las corutinas.
        Dispatchers.setMain(testDispatcher)
        // Se instancia el ViewModel pasándole el ApiService falso.
        viewModel = ComentariosViewModel(mockApiService)
    }

    @After
    fun tearDown() {
        // Se limpia el dispatcher después de cada test.
        Dispatchers.resetMain()
    }

    // --- TESTS ---

    @Test
    fun `cuando loadComments es exitoso, la lista de comentarios se actualiza`() = runTest {
        // Arrange (Preparar)
        val postId = 1L
        val fakeComments = listOf(
            Comment(1, "¡Qué buena rutina!", "user1", "2023-10-27T10:15:30Z"),
            Comment(2, "¡A darle con todo!", "user2", "2023-10-27T10:16:00Z")
        )
        val successResponse = Response.success(fakeComments)

        // Se le dice al mock cómo debe responder.
        coEvery { mockApiService.getComments(postId) } returns successResponse

        // Act (Actuar)
        viewModel.loadComments(postId)
        testDispatcher.scheduler.advanceUntilIdle() // Se ejecutan las corutinas pendientes.

        // Assert (Verificar)
        assertEquals(fakeComments, viewModel.comments.value) // ¿La lista de comentarios es la esperada?
        assertEquals(false, viewModel.isLoading.value)      // ¿El indicador de carga está desactivado?
        assertNull(viewModel.error.value)                    // ¿No hay ningún mensaje de error?
    }

    @Test
    fun `cuando loadComments falla por un error de red, el estado de error se actualiza`() = runTest {
        // Arrange (Preparar)
        val postId = 1L
        val networkException = RuntimeException("No se pudo conectar al servidor")

        // Se le dice al mock que debe lanzar una excepción.
        coEvery { mockApiService.getComments(postId) } throws networkException

        // Act (Actuar)
        viewModel.loadComments(postId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert (Verificar)
        assertEquals(true, viewModel.comments.value.isEmpty()) // ¿La lista de comentarios está vacía?
        assertEquals(false, viewModel.isLoading.value)         // ¿El indicador de carga está desactivado?
        assertEquals("Excepción al cargar comentarios: ${networkException.message}", viewModel.error.value) // ¿Se ha guardado el mensaje de error correcto?
    }
}
