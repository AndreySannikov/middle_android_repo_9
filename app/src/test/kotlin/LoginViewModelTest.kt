import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.yandex.loginapp.LoginScreenState
import ru.yandex.loginapp.LoginViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        val testEmail = ""
        val testPassword = ""
        viewModel.login(testEmail, testPassword)
        val expectedState = LoginScreenState.EmptyFieldsError
        val actualState = viewModel.state.value
        assertEquals(expectedState, actualState)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        val testEmail = "invalidemail"
        val testPassword = "123"
        viewModel.login(testEmail, testPassword)
        val expectedState = LoginScreenState.EmailValidationError
        val actualState = viewModel.state.value
        assertEquals(expectedState, actualState)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        val testEmail = "practicum@yandex.ru"
        val testPassword = "password123"
        viewModel.login(testEmail, testPassword)
        testDispatcher.scheduler.runCurrent()
        val expectedState = LoginScreenState.Loading
        val actualState = viewModel.state.value
        assertEquals(expectedState, actualState)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        val testEmail = "practicum@yandex.ru"
        val testPassword = "password123"
        viewModel.login(testEmail, testPassword)
        testDispatcher.scheduler.advanceTimeBy(3001)
        val expectedState = LoginScreenState.Success
        val actualState = viewModel.state.value
        assertEquals(expectedState, actualState)
    }
}