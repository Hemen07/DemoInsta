package com.del.demoinsta.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.data.local.db.DatabaseService
import com.del.demoinsta.data.local.prefs.UserPreferences
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.remote.NetworkService
import com.del.demoinsta.data.remote.request.LoginRequest
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 *
 * Repository for storing user info.
 *
 */
@SuppressLint("LogNotTimber")
@Singleton
class UserRepository @Inject constructor(
    //
    private val networkService: NetworkService,
    //
    private val databaseService: DatabaseService,
    //
    private val userPreferences: UserPreferences


) {

    //
    private val TAG = UserRepository::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init()")
    }

    // delegate to that specific class
    fun saveCurrentUser(user: User) {
        Log.d(TAG, "saveCurrentUser: () ")
        userPreferences.setUserId(user.id)
        userPreferences.setUserName(user.name)
        userPreferences.setUserEmail(user.email)
        userPreferences.setAccessToken(user.accessToken)
        //
        // TODO : avatar is not being stored
    }

    fun removeCurrentUser() {
        Log.d(TAG, "removeCurrentUser: () ")
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
    }

    fun getCurrentUser(): User? {
      //  Log.d(TAG, "getCurrentUser: () ")

        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val accessToken = userPreferences.getAccessToken()

        return if (userId !== null && userName != null && userEmail != null && accessToken != null)
            User(userId, userName, userEmail, accessToken)
        else
            null
    }

    /**
     * Single
     *
     * @return [User] of  Rx [Single]
     */
    fun doUserLogin(email: String, password: String): Single<User> {
        Log.d(TAG, "doUserLogin: () ")
        //
        return networkService.doLoginCall(LoginRequest(email, password))

            // mapping LoginResponse to User
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }
    }
}