package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhamza007.iptv.api.ApiService
import com.mhamza007.iptv.api.Config
import com.mhamza007.iptv.model.Movie
import com.mhamza007.iptv.model.MovieCategory
import com.mhamza007.iptv.util.GlideApp
import com.mhamza007.iptv.util.SharedPreference
import com.mhamza007.iptv.util.Utils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
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

class MoviesActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
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
        setContentView(R.layout.activity_movies)

        sharedPreference = SharedPreference(this@MoviesActivity)

        if (sharedPreference.getActivationCode() != "") {
            userName = sharedPreference.getActivationCode()
            password = sharedPreference.getActivationCode()
        } else {
            userName = sharedPreference.getUserName()
            password = sharedPreference.getPassword()
        }

        back.setOnClickListener {
            finish()
        }

        liveTv.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, LiveTvActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@MoviesActivity,
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
                this@MoviesActivity,
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
                this@MoviesActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        // Movies Categories list
        getMoviesCategories()

        // Movies List
        getMoviesList("")
    }

    override fun onResume() {
        super.onResume()

        getMoviesList("")
    }

    private fun getMoviesCategories() {
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

        val call = request.getMoviesCategories(
            userName,
            password,
            getString(R.string.get_vod_categories)
        )

        call.enqueue(object : Callback<List<MovieCategory>> {
            override fun onResponse(
                call: Call<List<MovieCategory>>,
                response: Response<List<MovieCategory>>
            ) {
                if (response.isSuccessful) {
                    val movieCategory = response.body()

                    try {
                        if (movieCategory?.size != null && movieCategory.isNotEmpty()) {
                            Log.d(TAG, "Live Tv Category List: ${movieCategory.size}")

                            val moviesCategoryAdapter = GroupAdapter<GroupieViewHolder>()

                            movieCategory.forEach {
                                moviesCategoryAdapter.add(MoviesCategoryItem(it))
                            }
                            subCategoryList.layoutManager = LinearLayoutManager(this@MoviesActivity)
                            subCategoryList.adapter = moviesCategoryAdapter
                        } else {
                            Log.e(TAG, "onResponse Movies Category size 0 or null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse Exception $e")
                    }
                } else {
                    Log.e(TAG, "Response Failed")
                }
            }

            override fun onFailure(call: Call<List<MovieCategory>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
                Utils.toast(this@MoviesActivity, "Failure")
            }
        })
    }

    fun getMoviesList(categoryId: String) {
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
            request.getMoviesFromCategoryId(
                userName,
                password,
                getString(R.string.get_vod_streams),
                categoryId
            )
        } else {
            request.getMovies(
                userName,
                password,
                getString(R.string.get_vod_streams)
            )
        }

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()

                    try {
                        if (movies?.size != null && movies.isNotEmpty()) {
                            Log.d(TAG, "Movies List: ${movies.size}")

                            progress.visibility = View.GONE

                            val movieItemAdapter = GroupAdapter<GroupieViewHolder>()

                            movies.forEach {
                                movieItemAdapter.add(MovieItem(it, movies))
                            }

                            moviesList.layoutManager = GridLayoutManager(this@MoviesActivity, 3)
                            moviesList.adapter = movieItemAdapter
                        } else {
                            Log.e(TAG, "onResponse Movies size 0 or null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse Exception $e")
                    }
                } else {
                    Log.e(TAG, "Response Failed")
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
                Utils.toast(this@MoviesActivity, "Failure")
            }
        })
    }

    companion object {
        const val TAG = "MoviesActivity"
    }

    inner class MoviesCategoryItem(
        private val movieCategory: MovieCategory
    ) :
        Item<GroupieViewHolder>() {
        override fun getLayout() = R.layout.sub_category_item

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.apply {
                with(viewHolder.itemView) {
                    subCategoryTitle.text = movieCategory.categoryName
                    subCategoryTitle.setOnClickListener {
                        if (movieCategory.categoryId != null) {
                            progress.visibility = View.VISIBLE
                            getMoviesList(movieCategory.categoryId!!)
                        } else {
                            Utils.toast(context, "Can't load from this category")
                        }
                    }
                }
            }
        }
    }

    inner class MovieItem(private var movie: Movie, private var movies: List<Movie>) :
        Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.movie_item
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.apply {
                with(viewHolder.itemView) {
                    GlideApp.with(context).load(movie.streamIcon).placeholder(R.mipmap.ic_launcher)
                        .into(itemImage)
                    itemName.text = movie.name

                    this.setOnClickListener {
                        context.startActivity(Intent(context, PlayerActivity::class.java))
                    }

//                    searchField.setOnClickListener {
//                        val intent = Intent(this@MoviesActivity, SearchActivity::class.java)
//                        intent.putExtra("list", movies)
//                        startActivity()
//                    }

//                    searchField.setOnEditorActionListener { _, actionId, _ ->
//                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                            filterResult(searchField.text.toString().trim())
//                            return@setOnEditorActionListener true
//                        }
//                        return@setOnEditorActionListener false
//                    }

                    /*searchField.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                        override fun afterTextChanged(s: Editable?) {
                            filterResult(s.toString())
                        }
                    })*/
                }
            }
        }

//        private fun filterItems(list: List<Movie>) {
//            movies = list
//        }

//        private fun filterResult(string: String) {
//            val list = ArrayList<Movie>()
//            for (mov in list) {
//                if (mov.name!!.contains(string, ignoreCase = true)) {
////                    filterItems(mov)
//                list.add(mov)
//                }
//            }
//            movies = list
//        }
    }
}