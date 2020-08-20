package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mhamza007.iptv.model.Movie
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.sub_category_item.view.*

class MoviesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_movies)

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

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(this, "Searching ", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        val subCatAdapter = GroupAdapter<GroupieViewHolder>()
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCatAdapter.add(SubCategoryItem(this, "Favourites"))
        subCategoryList.layoutManager = LinearLayoutManager(this)
        subCategoryList.adapter = subCatAdapter

        val movieItemAdapter = GroupAdapter<GroupieViewHolder>()
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        movieItemAdapter.add(
            MovieItem(
                this,
                Movie(getString(R.string.test_link), "Movie Name", "2.6")
            )
        )
        moviesList.layoutManager = GridLayoutManager(this, 3)
        moviesList.adapter = movieItemAdapter
    }
}

class SubCategoryItem(private val context: Context, private val name: String) :
    Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.sub_category_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.subCategoryTitle.text = name
    }
}

class MovieItem(private val context: Context, private val movie: Movie) :
    Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.movie_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(context).load(movie.icon).into(viewHolder.itemView.itemImage)
        viewHolder.itemView.itemName.text = movie.name
//        viewHolder.itemView.itemRatings.rating = movie.rating.toFloat()

        viewHolder.itemView.setOnClickListener {
            context.startActivity(Intent(context, PlayerActivity::class.java))
        }
    }
}