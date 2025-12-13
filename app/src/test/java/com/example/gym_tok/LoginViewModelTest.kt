package com.example.gym_tok

import android.app.Application
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.controller.LoginViewModel
import com.example.gym_tok.network.UserDTO
import com.example.gym_tok.repository.UserPreferencesRepository
import com.example.gym_tok.repository.UsuarioLocalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Dependencias reales del ViewModel, ahora mockeadas correctamente.
    private lateinit var viewModel: LoginViewModel
    private val mockApiService: ApiService = mockk(relaxed = true)
    private val mockUserPrefsRepo: UserPreferencesRepository = mockk(relaxed = true)
    private val mockUsuarioLocalRepo: UsuarioLocalRepository = mockk(relaxed = true)
    private val mockApplication: Application = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    // Configuración que se ejecuta antes de CADA test.
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Se establece el dispatcher de prueba.
        // Se instancia el ViewModel con las dependencias mockeadas.
        viewModel = LoginViewModel(mockApplication, mockApiService, mockUserPrefsRepo, mockUsuarioLocalRepo)
    }

    // Limpieza que se ejecuta después de CADA test.
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Se resetea el dispatcher principal.
    }

    @Test
    fun `dado un login exitoso, el estado debe ser loginSuccess y se deben guardar los datos`() = runTest {
        // Arrange (Preparar)
        val email = "test@test.com"
        val password = "password123"
        val mockUserDto = UserDTO(id = 1, name = "Test", lastName = "User", userName = "tester", email = email, sex = 'M', birthDate = "1990-01-01")
        val successResponse = Response.success(mockUserDto)

        // Se le dice a MockK cómo debe responder el ApiService.
        coEvery { mockApiService.login(any()) } returns successResponse

        // Se simula la entrada del usuario.
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act (Actuar)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle() // Se asegura que todas las corutinas en el dispatcher terminen.

        // Assert (Verificar)
        val finalState = viewModel.uiState.value
        assertTrue("Se obtiene un login exitoso", finalState.loginSuccess)
        assertEquals(null, finalState.errorMessage)

        // Se verifica que los métodos de guardado fueron llamados con los parámetros correctos.
        coVerify { mockUserPrefsRepo.saveUserSession(mockUserDto) }
        coVerify { mockUsuarioLocalRepo.insert(any()) } // Verificamos que se llame a insert.
    }

    @Test
    fun `credenciales incorrectas, el estado debe ser de error`() = runTest {
        // Arrange
        val email = "wrong@test.com"
        val password = "wrongpassword"
        // Se simula una respuesta de error 401 (No autorizado) de Retrofit.
        val errorBody = "{\"message\":\"Credenciales incorrectas\"}".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<UserDTO>(401, errorBody)
        val expectedErrorMessage = "Error (401): Credenciales incorrectas o problema del servidor."

        coEvery { mockApiService.login(any()) } returns errorResponse

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertEquals(false, finalState.loginSuccess)
        assertEquals(expectedErrorMessage, finalState.errorMessage) // Se comprueba el mensaje de error EXACTO.
    }

    @Test
    fun `un error de red, el estado debe ser de error`() = runTest {
        // Arrange
        val exception = RuntimeException("No se pudo conectar al servidor")
        val expectedErrorMessage = "Error de red: ${exception.message}"

        // Se simula que la llamada a la API lanza una excepción.
        coEvery { mockApiService.login(any()) } throws exception

        viewModel.onEmailChange("any@email.com")
        viewModel.onPasswordChange("anypassword")

        // Act
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertEquals(false, finalState.loginSuccess)
        assertEquals(expectedErrorMessage, finalState.errorMessage)
    }
}