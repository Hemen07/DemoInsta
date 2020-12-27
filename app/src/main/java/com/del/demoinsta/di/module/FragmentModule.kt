package com.del.demoinsta.di.module


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.repository.PhotoRepository
import com.del.demoinsta.data.repository.PostRepository
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.di.FragmentContextQualifier
import com.del.demoinsta.di.FragmentScope
import com.del.demoinsta.di.TempDirectoryQualifier
import com.del.demoinsta.ui.adapter.PostsAdapter
import com.del.demoinsta.ui.base.BaseFragment
import com.del.demoinsta.ui.viewmodel.HomeViewModel
import com.del.demoinsta.ui.viewmodel.MainSharedViewModel
import com.del.demoinsta.ui.viewmodel.PhotoViewModel
import com.del.demoinsta.ui.viewmodel.ProfileViewModel
import com.del.demoinsta.utils.ViewModelProviderFactory
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import com.mindorks.paracamera.Camera
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import java.io.File


/**
 * |||||||||| Fragment ||||||||||
 */
@SuppressLint("LogNotTimber")
@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    //
    private val TAG = FragmentModule::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init: () ")
    }


    /*
    * ------------------------------------ Common ---------------------------------------
    */


    /**
     *
     */
    @FragmentContextQualifier
    @FragmentScope
    @Provides
    fun provideContext(): Context = fragment.requireContext()


    /**
     *
     */
    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)


    /*
     * ------------------------------------ HOME Fragment ---------------------------------------
     */

    /**
     *
     */
    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    )
            : HomeViewModel {
        Log.d(TAG, "provideHomeViewModel: () ")
        //
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    userRepository,
                    postRepository,
                    ArrayList<Post>(),

                    // HOT Subject,
                    // same as Subject just supports Back-pressure.
                    PublishProcessor.create()
                )
            }
        ).get(HomeViewModel::class.java)

    }


    /**
     *
     */
    @Provides
    fun providePostsAdapter(): PostsAdapter {
        //
        return PostsAdapter(fragment.lifecycle, ArrayList())
    }


    /*
     * ------------------------------------------ Photo Add Fragment -----------------------------------------
     */

    /**
     *
     */
    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        postRepository: PostRepository,

        // from application module.
        @TempDirectoryQualifier directory: File
    )
            : PhotoViewModel {
        Log.d(TAG, "provideHomeViewModel: () ")
        //
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(PhotoViewModel::class) {
                PhotoViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    userRepository,
                    photoRepository,
                    postRepository,
                    directory

                )
            }
        ).get(PhotoViewModel::class.java)

    }


    /**
     * ParaCamera provider.
     *
     * So when you get a image from Camera (small/mid/big/huge)
     * it will save the file, with below options - format, height-size, dir, name
     *
     *
     *TODO :  Original file, save that too.
     */
    @Provides
    fun provideCamera(): Camera = Camera.Builder()

        // uses original photo rotation from meta data
        // if you later rotated to (say) landscape and the original was portrait, then portrait will be used.
        .resetToCorrectOrientation(true)
        // anything
        .setTakePhotoRequestCode(1)
        // create dir
        .setDirectory("temp")
        // name
        .setName("camera_temp_img")
        // format
        .setImageFormat(Camera.IMAGE_JPEG)
        //
        .setCompression(75)
        // height 500
        .setImageHeight(500)// it will try to achieve this height as close as possible maintaining the aspect ratio;
        //
        .build(fragment)


    /*
    * ------------------------------------------ Profile Fragment -----------------------------------------
    */


    /**
     *
     */
    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    )
            : ProfileViewModel {
        Log.d(TAG, "provideProfileViewModel: () ")
        //
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(ProfileViewModel::class) {
                ProfileViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper
                )
            }
        ).get(ProfileViewModel::class.java)

    }


    // Shared VM.

    /**
     * comm b/w Photo F and HF via MA's container
     *
     */
    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel {
        Log.d(TAG, "provideMainSharedViewModel: () ")

        //
        return ViewModelProvider(
            fragment.requireActivity(),
            ViewModelProviderFactory(MainSharedViewModel::class) {
                MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
            }).get(MainSharedViewModel::class.java)

    }
}