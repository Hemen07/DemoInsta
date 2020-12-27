package com.del.demoinsta.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.del.demoinsta.R
import com.del.demoinsta.di.component.FragmentComponent
import com.del.demoinsta.ui.adapter.PostsAdapter
import com.del.demoinsta.ui.base.BaseFragment
import com.del.demoinsta.ui.viewmodel.HomeViewModel
import com.del.demoinsta.ui.viewmodel.MainSharedViewModel
import javax.inject.Inject

//
@SuppressLint("LogNotTimber")
class HomeFragment : BaseFragment<HomeViewModel>() {


    // setting up BaseFragment TAG
    init {
        //
        TAG = HomeFragment::class.simpleName + " : ";
    }


    // views

    // rv
    private lateinit var mRecyclerView: RecyclerView

    // progressbar
    private lateinit var mProgressBar: ProgressBar


    //
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    //
    @Inject
    lateinit var postsAdapter: PostsAdapter


    // this VM for shared comm b/w fragment and parents.
    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    //
    companion object {

        //
        internal val TAG = HomeFragment::class.java.simpleName + " : "

        //
        fun getInstance(fragmentManager: FragmentManager): HomeFragment {
            Log.e(TAG, "getInstance () : ")
            var homeFragment = fragmentManager.findFragmentByTag(TAG) as HomeFragment?
            if (homeFragment == null) {
                homeFragment = HomeFragment()
            }
            return homeFragment
        }

        //
        fun newInstance(): HomeFragment {
            Log.e(TAG, "newInstance: () ")
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    //  What's happening in BF
    /*
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: () ")
        return inflater.inflate(provideLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: () ")
        super.onViewCreated(view, savedInstanceState)
        injectDependencies(buildFragmentComponent())
        setupView(view)
        setupObservers()
        viewModel.notifyOnCreateFinished()
    }
     */

    override fun provideLayoutId(): Int {
        Log.d(TAG, "provideLayoutId: () ")
        return R.layout.fragment_home
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        Log.d(TAG, "injectDependencies: () ")
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        Log.d(TAG, "setupView: () ")


        //
        mRecyclerView = view.findViewById(R.id.rvPosts)
        mProgressBar = view.findViewById(R.id.progressBar)

        // DI
        // val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        //
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        // DI
        // val postAdapter = PostAdapter(lifecycle, ArrayList())

        // empty list
        mRecyclerView.adapter = postsAdapter


        // scroll listener
        // will trigger onLoadMore()
        mRecyclerView.apply {
            //
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                //
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // if the last visible item is shown, then ask vm to load more.
                    layoutManager?.run {
                        //
                        if (this is LinearLayoutManager && itemCount > 0 && itemCount == findLastVisibleItemPosition() + 1) {
                            Log.d(
                                TAG,
                                "onScrolled: () " + postsAdapter.itemCount + ", " + findFirstVisibleItemPosition() + ", " + findLastVisibleItemPosition()
                            )
                            viewModel.onLoadMore()
                        }
                    }
                }
            })
        }


    }

    // You know why I override this, don't you.
    // see the documentation.
    override fun setupObservers() {
        super.setupObservers()
        Log.d(TAG, "setupObservers: () ")

        //
        viewModel.loading.observe(this, Observer {
            mProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it.data?.run {
                Log.i(TAG, "setupObservers: |||||||||||||||||||||() POSTS :  size = $it")
                //
                postsAdapter.appendData(this)
            }
        })


        // observe when new post is created in Photo F and comes to here via MA's MainSharedViewModel
        mainSharedViewModel.newPost.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "setupObservers: |||||||||||||||||||||() NEW POST ")
            //
            it.getIfNotHandled()?.run {
                // trigger another call.
                viewModel.onNewPost(this)
            }
        })

        // when new list added, this ld observes the new list
        viewModel.refreshPosts.observe(this, Observer {
            Log.i(TAG, "setupObservers: |||||||||||||||||||||() REFRESH POST ")
            //
            it.data?.run {
                //
                postsAdapter.update(this)
                //
                mRecyclerView.scrollToPosition(0)
            }
        })

        // TODO : Retrofit_meh2 is better substitution than this shit.


    }
}
