package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhamza007.iptv.api.ApiService
import com.mhamza007.iptv.api.Config
import com.mhamza007.iptv.model.LiveTv
import com.mhamza007.iptv.model.LiveTvCategory
import com.mhamza007.iptv.util.GlideApp
import com.mhamza007.iptv.util.SharedPreference
import com.mhamza007.iptv.util.Utils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_live_tv.*
import kotlinx.android.synthetic.main.activity_live_tv.back
import kotlinx.android.synthetic.main.activity_live_tv.movies
import kotlinx.android.synthetic.main.activity_live_tv.progress
import kotlinx.android.synthetic.main.activity_live_tv.searchField
import kotlinx.android.synthetic.main.activity_live_tv.series
import kotlinx.android.synthetic.main.activity_live_tv.settings
import kotlinx.android.synthetic.main.activity_live_tv.subCategoryList
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.sub_category_item.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LiveTvActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    private lateinit var gridLayoutManager: GridLayoutManager

    private val timeout = 10L

    private lateinit var userName: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_live_tv)

        sharedPreference = SharedPreference(this@LiveTvActivity)
        if (sharedPreference.getActivationCode() != "") {
            userName = sharedPreference.getActivationCode()
            password = sharedPreference.getActivationCode()
        } else {
            userName = sharedPreference.getUserName()
            password = sharedPreference.getPassword()
        }

        gridLayoutManager = GridLayoutManager(this@LiveTvActivity, 3)

        back.setOnClickListener {
            finish()
        }

        movies.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, MoviesActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@LiveTvActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        series.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, SeriesActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@LiveTvActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        settings.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, SettingsActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@LiveTvActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(this, "Searching ", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // Load Live TV Categories
        getLiveTvCategories()

        // Load Live TV Items List
        getLiveStreamsList("")
    }

    private fun getLiveTvCategories() {
        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(ApiService::class.java)

        val call = request.getLiveStreamsCategories(
            userName,
            password,
            getString(R.string.get_live_categories)
        )


        call.enqueue(object : Callback<List<LiveTvCategory>> {
            override fun onResponse(
                call: Call<List<LiveTvCategory>>,
                response: Response<List<LiveTvCategory>>
            ) {
                if (response.isSuccessful) {
                    val liveTvCategory = response.body()

                    try {
                        if (liveTvCategory?.size != null && liveTvCategory.isNotEmpty()) {
                            Log.d(TAG, "Live Tv Category List: ${liveTvCategory.size}")

                            val liveTvCategoryAdapter = GroupAdapter<GroupieViewHolder>()

                            liveTvCategory.forEach {
                                liveTvCategoryAdapter.add(
                                    LiveTvCategoryItem(
                                        it
                                    )
                                )
                            }
                            subCategoryList.layoutManager = LinearLayoutManager(this@LiveTvActivity)
                            subCategoryList.adapter = liveTvCategoryAdapter
                        } else {
                            Log.e(TAG, "onResponse Live Tv Category size 0 or null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse Exception $e")
                    }
                } else {
                    Log.e(TAG, "Response Failed")
                }
            }

            override fun onFailure(call: Call<List<LiveTvCategory>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
                Utils.toast(this@LiveTvActivity, "Failure")
            }
        })
    }

    fun getLiveStreamsList(categoryId: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(ApiService::class.java)

        val call = if (categoryId != "") {
            request.getLiveStreamsFromCategoryId(
                userName,
                password,
                getString(R.string.get_live_streams),
                categoryId
            )
        } else {
            request.getLiveStreams(
                userName,
                password,
                getString(R.string.get_live_streams)
            )
        }

        call.enqueue(object : Callback<List<LiveTv>> {
            override fun onFailure(call: Call<List<LiveTv>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
                Utils.toast(this@LiveTvActivity, "Failure")
            }

            override fun onResponse(call: Call<List<LiveTv>>, response: Response<List<LiveTv>>) {
                if (response.isSuccessful) {
                    val liveTv = response.body()

                    try {
                        if (liveTv?.size != null && liveTv.isNotEmpty()) {
                            Log.d(TAG, "Live Tv List: ${liveTv.size}")

                            progress.visibility = View.GONE

                            val liveTvItemAdapter = GroupAdapter<GroupieViewHolder>()

                            liveTv.forEach {
                                liveTvItemAdapter.add(LiveTvItem(it))
                            }

                            liveTvList.layoutManager = gridLayoutManager
                            liveTvList.adapter = liveTvItemAdapter
                        } else {
                            Log.e(TAG, "onResponse Live Tv size 0 or null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse Exception $e")
                    }
                } else {
                    Log.e(TAG, "Response Failed")
                }
            }
        })
    }


    companion object {
        const val TAG = "LiveTvActivity"
    }

    inner class LiveTvCategoryItem(
        private val liveTvCategory: LiveTvCategory
    ) :
        Item<GroupieViewHolder>() {
        override fun getLayout() = R.layout.sub_category_item

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.apply {
                with(viewHolder.itemView) {
                    subCategoryTitle.text = liveTvCategory.categoryName
                    subCategoryTitle.setOnClickListener {
                        if (liveTvCategory.categoryId != null) {
                            progress.visibility = View.VISIBLE
                            getLiveStreamsList(liveTvCategory.categoryId!!)
                        } else {
                            Utils.toast(context, "Can't load from this category")
                        }
                    }
                }
            }
        }
    }

    class LiveTvItem(private val liveTv: LiveTv) :
        Item<GroupieViewHolder>() {
        override fun getLayout() = R.layout.movie_item

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.apply {
                with(viewHolder.itemView) {
                    GlideApp.with(context).load(liveTv.streamIcon)
                        .placeholder(R.mipmap.ic_launcher).into(itemImage)
                    itemName.text = liveTv.name
                }
            }
        }
    }
}