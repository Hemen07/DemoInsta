package com.del.demoinsta.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.del.demoinsta.R
import com.del.demoinsta.di.component.FragmentComponent
import com.del.demoinsta.ui.base.BaseFragment
import com.del.demoinsta.ui.viewmodel.MainSharedViewModel
import com.del.demoinsta.ui.viewmodel.PhotoViewModel
import com.del.demoinsta.utils.common.Event
import com.mindorks.paracamera.Camera
import java.io.FileNotFoundException
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class PhotoFragment : BaseFragment<PhotoViewModel>() {

    // setting up BaseFragment TAG
    init {

        TAG = PhotoFragment::class.simpleName + " : ";
    }

    //
    private lateinit var mCameraView: View

    //
    private lateinit var mGalleryView: View

    //
    private lateinit var mProgressBar: ProgressBar

    // para camera janishar.
    @Inject
    lateinit var camera: Camera

    // this VM for shared comm b/w fragment and parents.
    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    //
    companion object {
        //
        internal val TAG = PhotoFragment::class.java.simpleName + " : "


        //
        const val RESULT_GALLERY_IMG = 1001


        //
        fun getInstance(fragmentManager: FragmentManager): PhotoFragment {
            Log.e(TAG, "getInstance () : ")
            var photoFragment = fragmentManager.findFragmentByTag(TAG) as PhotoFragment?
            if (photoFragment == null) {
                photoFragment = PhotoFragment()
            }
            return photoFragment
        }

        //
        fun newInstance(): PhotoFragment {
            Log.e(TAG, "newInstance: () ")
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int {
        Log.d(TAG, "provideLayoutId: () ")
        return R.layout.fragment_photo
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        Log.d(TAG, "injectDependencies: () ")
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        Log.d(TAG, "setupView: () ")

        //
        mCameraView = view.findViewById(R.id.view_camera)
        mGalleryView = view.findViewById(R.id.view_gallery)
        mProgressBar = view.findViewById(R.id.photo_fragment_pb_loading)


        //
        mCameraView.setOnClickListener {
            Log.i(TAG, "setupView: onclick : Camera View ")
            //
            try {
                camera.takePicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        // open gallery, can be any app, result to onActivityResult.
        mGalleryView.setOnClickListener {
            Log.i(TAG, "setupView: onclick : Gallery View ")

            //
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RESULT_GALLERY_IMG)
                }
        }

    }


    // You know why I override this, don't you.
    // see the documentation.
    override fun setupObservers() {
        super.setupObservers()
        Log.d(TAG, "setupObservers: () ")

        viewModel.loading.observe(this, Observer {
            mProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(viewLifecycleOwner, Observer {
            it.getIfNotHandled()?.run {
                Log.i(TAG, "setupObservers: |||||||||||  ||||||||||||||  POST  ")

                // telling new post added
                // HF is observing this, and will act on this when received.
                // take the new post to the top of List.
                mainSharedViewModel.newPost.postValue(Event(this))

                // telling MA to load HF.
                mainSharedViewModel.onHomeRedirect()
            }
        })

    }

    //
    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        Log.i(TAG, "onActivityResult: (), resultCode = $resultCode")
        //
        if (resultCode == RESULT_OK) {
            //
            when (reqCode) {

                // with respect to Gallery
                RESULT_GALLERY_IMG -> {
                    try {
                        // get the input stream from uri and send it to VM.
                        intent?.data?.let {
                            //
                            activity?.contentResolver?.openInputStream(it)?.run {
                                //
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.try_again)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.try_again)

                    }
                }
                // with respect to camera.
                Camera.REQUEST_TAKE_PHOTO -> {

                    // camera.cameraBitmapPath() heavy operation.

                    // I could have easily passed camera instance to VM
                    // and call cameraBitmapPath method from there
                    // but that will be unnecessary.

                    // Why not pass the function as lambda

                    // supplying a lambda. (which is camera.cameraBitmapPath() )
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
            }
        }

    }

}
