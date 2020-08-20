package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Context
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
import com.mhamza007.iptv.util.GlideApp
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_live_tv.*
import kotlinx.android.synthetic.main.movie_item.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LiveTvActivity : AppCompatActivity() {

    private lateinit var gridLayoutManager: GridLayoutManager

    private val timeout = 10L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_live_tv)

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

        val subCatAdapter = GroupAdapter<GroupieViewHolder>()
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCategoryList.layoutManager = LinearLayoutManager(this)
        subCategoryList.adapter = subCatAdapter

        // Load Live TV Items List
        getLiveStreamsList(request)
    }

    private fun getLiveStreamsList(request: ApiService) {

        val call = request.getLiveStreams(
            getString(R.string.test_username),
            getString(R.string.test_password),
            "get_live_streams"
        )

        call.enqueue(object : Callback<List<LiveTv>> {
            override fun onFailure(call: Call<List<LiveTv>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
                Toast.makeText(this@LiveTvActivity, "Failure", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<List<LiveTv>>, response: Response<List<LiveTv>>) {
                if (response.isSuccessful) {
                    val liveTv = response.body()

                    try {
                        if (liveTv?.size != null || liveTv?.size != 0) {
                            Log.d(TAG, "Live Tv List: ${liveTv?.size}")

                            progress.visibility = View.GONE

                            val liveTvItemAdapter = GroupAdapter<GroupieViewHolder>()

                            liveTv?.forEach {
                                liveTvItemAdapter.add(
                                    LiveTvItem(
                                        this@LiveTvActivity,
                                        liveTv
                                    )
                                )
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
}

class LiveTvItem(private val context: Context, private val liveTv: List<LiveTv>?) :
    Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.movie_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        GlideApp.with(context).load(liveTv?.get(position)?.streamIcon)
            .into(viewHolder.itemView.itemImage)
        viewHolder.itemView.itemName.text = liveTv?.get(position)?.name
    }
}


/*        liveTvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    firstVisibleItem - gridLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false
                            previousTotal = totalItemCount
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        loading = true
                        itemsToLoad += 10
                        progress.visibility = View.VISIBLE
                        getLiveStreamsList()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })*/