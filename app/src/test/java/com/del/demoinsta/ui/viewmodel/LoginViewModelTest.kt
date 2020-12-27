package com.del.demoinsta.ui.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.del.demoinsta.R
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.uitls.rx.TestSchedulerProvider
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.network.NetworkHelper
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

/**
 *
 * You could have simple run with mockito
 * But I used Robo just for logs..
 *
 */
@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowLog::class])
class LoginViewModelTest {

    /**
     * initialized for @Mock annotated things.
     */
    @Rule
    @JvmField // you need this
    var mockitoRule: MockitoRule = MockitoJUnit.rule()


    /**
     * Provided by android arch
     * This helps run LD in synchronous manner.
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // ctor dependencies mock

    // TODO : Note, Din't mock this, as we are using TestSchedulerProvider, check below.
    //@Mock
    //  private lateinit var schedulerProvider: SchedulerProvider

    // TODO : Note, Din't mock because TestScheduler needs valid instance of CD
//    @Mock
//    private lateinit var compositeDisposable: CompositeDisposable

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository


    /*
     * Observers which subscribed to their respective LD
     *
     *
     * Observers -> LDS
     *
     * emailField
     * passwordField
     *
     * loginIn,
     * launchMain
     * messageStringId
     *
     */
    // This is basically an observer which  actually subscribes to loginIn LD of LVM
    // oh, the anonymous class.
    @Mock
    private lateinit var passwordFieldObserver: Observer<String>

    @Mock
    private lateinit var emailFieldObserver: Observer<String>


    @Mock
    private lateinit var loggingInObserver: Observer<Boolean>

    // this subscribes to launchMain LD
    @Mock
    private lateinit var launchMainObserver: Observer<Event<Map<String, String>>>


    // this subscribes to messageStringId LD
    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>


    // Rx utility scheduler for test for mimic concurrency into a  single thread synchronously.
    private lateinit var testScheduler: TestScheduler


    // target
    private lateinit var loginViewModel: LoginViewModel


    companion object {

        private val TAG = LoginViewModelTest::class.java.simpleName + " : "

        @BeforeClass
        @kotlin.jvm.JvmStatic
        fun beforeCLass() {
            println("|||||| beforeCLass ||||||||")
            //
            ShadowLog.setupLogging()

        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            println("------- afterClass ---------- ")
        }
    }


    @Before
    fun setup() {

        // as I said, not mocked, since it will be required for TestScheduler
        val compositeDisposable = CompositeDisposable()

        // as we said above, Rx testing Helper
        testScheduler = TestScheduler()

        /**
         *
         * TSP with TP helps to get only one kind of thread from io()/comp()/ui()
         */
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)

        // loginViewModel
        loginViewModel = LoginViewModel(
            testSchedulerProvider,
            compositeDisposable,
            networkHelper,
            userRepository
        )


        /**
         *
         * we will use observeForever which is manual
         * You must dispose it manually for that reason.
         *
         * We will use tear down.
         */


        // emailField observer -> emailField LD
        loginViewModel.emailField.observeForever(emailFieldObserver)

        // passwordField observer -> emailField LD
        loginViewModel.passwordField.observeForever(passwordFieldObserver)

        // login observer -> logginIn LD
        loginViewModel.loggingIn.observeForever(loggingInObserver)

        // launchMainObserver observer -> launchMain LD
        loginViewModel.launchMain.observeForever(launchMainObserver)

        // messageStringIdObserver observer -> messageStringId LD
        loginViewModel.messageStringId.observeForever(messageStringIdObserver)

    }


    @After
    fun tearDown() {
        // since we used observeForever, so gotta manually remove all.

        loginViewModel.emailField.removeObserver(emailFieldObserver)
        loginViewModel.passwordField.removeObserver(passwordFieldObserver)
        loginViewModel.loggingIn.removeObserver(loggingInObserver)
        loginViewModel.launchMain.removeObserver(launchMainObserver)
        loginViewModel.messageStringId.removeObserver(messageStringIdObserver)
    }


    @Test
    fun notifyOnCreateFinished() {
        loginViewModel.notifyOnCreateFinished()
    }

    @Test
    fun onEmailChange() {

        val email = "test@gmail.com"
        //
        loginViewModel.onEmailChange(email)
        assert(loginViewModel.emailField.value == email)

        Mockito.verify(emailFieldObserver).onChanged(email)
    }

    @Test
    fun onPasswordChange() {

        val password = "password"
        //
        loginViewModel.onPasswordChange(password)
        assert(loginViewModel.passwordField.value == password)

        Mockito.verify(passwordFieldObserver).onChanged(password)
    }


    /**
     *login DummyActivity is successful response from Server
     * TODO : need a test method to execute throwable part
     */
    @Test
    fun givenServerResponse200_whenLogin_shouldLaunchDummyActivity() {
        Log.d(TAG, "givenServerResponse200_whenLogin_shouldLaunchDummyActivity: () ")


        // Telling the Mockito that whenever I call isNetworkConnected of NetworkHelper
        // then do return true.
        // else, you may get different values.

        Mockito.doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()


        // valid
        val email = "test@gmai.com"
        val password = "password"


        // configured with dummy/default values.
        // Why need......check below.
        val user = User("id", "test", email, "accessToken")


        // now set this LVM ld. using setValue.
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password


        // so whenever call goes to repo for login in with user and pass
        // do return the a user. (A dummy user is defined above)
        Mockito.doReturn(Single.just(user))
            .`when`(userRepository)
            .doUserLogin(email, password)


        // now hit the actual method
        loginViewModel.onLogin()

        // but above will not run, as we used TestSchedulerProvider and TestScheduler
        // so we need to tell the TestScheduler to run
        testScheduler.triggerActions()

        /*
         * let's verify  when we get success,
         *
         * store user via userRepository.saveCurrentUser()
         * loginViewModel.loggingIn.value == false // for Progressbar stop
         * launchDummy post emptyMap
         */

        // we store user in pref via userRepository.saveCurrentUser()
        Mockito.verify(userRepository).saveCurrentUser(user)

        // as in actual code
        assert(loginViewModel.loggingIn.value == false)

        // launchDummy empty map
        // lol can't set empty map so used hashMap
        assert(loginViewModel.launchMain.value == Event(hashMapOf<String, String>()))
//        assert(loginViewModel.launchDummy.value == Event(hashMapOf<String, String>()))


        /*
         *
         * Now just check our Observers in this Test class,
         * loggingInObserver
         * launchDummyObserver
         *
         *
         * Got their onChange triggered or not.
         */

        // wjy true and false, because if you see actual code
        // before call, set to true -> to tell the progressbar to load
        Mockito.verify(loggingInObserver).onChanged(true)
        // after success, set to false -> to tell the progressbar to stop load
        Mockito.verify(loggingInObserver).onChanged(false)

        // launchDummy
        Mockito.verify(launchMainObserver).onChanged(Event(hashMapOf()))

    }

    /**
     *This is for the throwable part
     * But when net is not there..
     */
    @Test
    fun givenNoInternet_whenLogin_shouldShowNetworkError() {

        val email = "test@gmail.com"
        val password = "password"

        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password

        Mockito.doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        loginViewModel.onLogin()

        //
        assert(loginViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        Mockito.verify(messageStringIdObserver)
            .onChanged(Resource.error(R.string.network_connection_error))
    }


    @Test
    fun clear() {
        loginViewModel.clear()
    }
}