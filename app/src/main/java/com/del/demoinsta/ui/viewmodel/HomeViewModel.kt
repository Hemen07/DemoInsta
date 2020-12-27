package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.repository.PostRepository
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor


/**
 *
 *
 * when you hit paginator -> onNext, then it will trigger a n/w call.
 *
 */
@SuppressLint("LogNotTimber")
class HomeViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,
    //
    private val userRepository: UserRepository,
    //
    private val postRepository: PostRepository,
    //
    private val allPostList: ArrayList<Post>,
    //
    private val paginator: PublishProcessor<Pair<String?, String?>>

    //
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = HomeViewModel::class.simpleName + " : ";

    // LD


    // for pb
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    // for getting Posts List
    val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    // this ld, for when new post arrived, that post to be added to existing list.
    // cool.
    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    // LoggedIn User
    // should not be used without logged in user
    private val user: User = userRepository.getCurrentUser()!!


    // keeps track of below
    var firstId: String? = null

    //
    var lastId: String? = null


    //
    init {
        Log.i(TAG, "init()");

        //
        configurePaginator()
    }

    //
    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")

        //
        loadMorePosts()

    }


    /**
     * If you are making an api call, and user scrolls up and down many times
     * so,  I am loading a new data, then
     * I don't have to call for new data (when scrolling up and down making n/w calls)
     * (usually scrolling up and down triggers call)
     */
    fun onLoadMore() {
        Log.d(TAG, "onLoadMore: () ")
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }


    /**
     * When new post added from Photo F -> MainSharedViewModel -> HF to  here
     */
    fun onNewPost(post: Post) {
        // damn resize, tc
        // let's discuss with Anand.

        // adding at first pos.
        allPostList.add(0, post)
        // creating a new list from allPostList
        //
        refreshPosts.postValue(
            Resource.success(mutableListOf<Post>()
                .apply {

                    //
                    addAll(allPostList)
                })
        )
    }


    /**
     *  If user scrolls up, then load more posts.
     */
    private fun loadMorePosts() {
        Log.d(TAG, "loadMorePosts: () ")

        //
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstId, lastId))
    }


    /**
     * Responsible for api call.
     * configured with init
     *
     * gets triggered when   paginator.onNext      -->       loadMorePosts()
     *
     * read this , cool example of subject
     * https://proandroiddev.com/rxjava-different-types-of-subjects-ef9183b5e87e
     *
     * subject.subscribe(Consumer)
     *
     * You know why we use concatMap (or concatMapSingle)
     *
     *
     */
    private fun configurePaginator() {
        Log.d(TAG, "configurePaginator: () ")
        //
        compositeDisposable.add(

            //
            paginator
                //
                .onBackpressureDrop()
                //
                .doOnNext {
                    loading.postValue(true)
                }
                //
                .concatMapSingle { pageIds ->
                    //
                    return@concatMapSingle postRepository
                        .fetchHomePostList(pageIds.first, pageIds.second, user)
//                        .subscribeOn(Schedulers.io())
                        // don't use directly
                        .subscribeOn(schedulerProvider.io())

                        // if any error, don't shut down paginator
                        // so manually handling
                        .doOnError {
                            handleNetworkError(it)
                        }
                }
                .subscribe(
                    //
                    {
                        //
                        allPostList.addAll(it)


                        // refactored.
                        // now below will store id of first post and last post.


                        // stores the id of most recent post
                        firstId = allPostList.maxByOrNull { post -> post.createdAt.time }?.id

                        // stores the id of oldest post
                        lastId = allPostList.minByOrNull { post -> post.createdAt.time }?.id

                        // got it, but when new post added, they seem to not storing firstId

                        loading.postValue(false)
                        posts.postValue(Resource.success(it))
                    },
                    //
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }


    // don't worry not calling onClear, BVM handling that
    // this is just specific to this class.
    fun clear() {
        Log.d(TAG, "clear: () ")
        // not needed
        //  compositeDisposable.clear()
    }
}