package com.mhamza007.iptv

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.player_nav_list.*
import kotlinx.android.synthetic.main.player_nav_sub_list.*
import kotlinx.android.synthetic.main.sub_category_item.view.*


class PlayerActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    var playWhenReady = true

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var playbackStateListener: PlaybackStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_player)

        playbackStateListener = PlaybackStateListener()

        if (Util.SDK_INT >= 24) {
            initPlayer()
        }

        back.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }

        val subCatList = findViewById<View>(R.id.player_nav_sub_list)

        val catAdapter = GroupAdapter<GroupieViewHolder>()

        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))
        catAdapter.add(NavCatItem(this, "Favourites"))

        nav_cat_list.layoutManager = LinearLayoutManager(this)
        nav_cat_list.adapter = catAdapter

        val subCatAdapter = GroupAdapter<GroupieViewHolder>()

        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))
        subCatAdapter.add(NavCatItem(this, "Ary News"))

        nav_sub_cat_list.layoutManager = LinearLayoutManager(this)
        nav_sub_cat_list.adapter = subCatAdapter
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player

        val uri = Uri.parse(getString(R.string.demo_video))
        Log.i(TAG, uri.toString())
        val mediaSource = buildMediaSource(uri)

        player?.addListener(playbackStateListener)
        player?.playWhenReady = playWhenReady
        player?.prepare(mediaSource!!, false, false)
    }

    inner class PlaybackStateListener : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            val stateString: String
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {
                    stateString = "ExoPlayer.STATE_IDLE      -"
                    Log.i(TAG, "onPlayerStateChanged: $stateString")
                }
                ExoPlayer.STATE_BUFFERING -> {
                    findViewById<View>(R.id.progress).visibility = View.VISIBLE
                    stateString = "ExoPlayer.STATE_BUFFERING -"
                    Log.i(TAG, "onPlayerStateChanged: $stateString")
                }
                ExoPlayer.STATE_READY -> {
                    findViewById<View>(R.id.progress).visibility = View.GONE
                    stateString = "ExoPlayer.STATE_READY     -"
                    Log.i(TAG, "onPlayerStateChanged: $stateString")
                }
                ExoPlayer.STATE_ENDED -> {
                    stateString = "ExoPlayer.STATE_ENDED     -"
                    Log.i(TAG, "onPlayerStateChanged: $stateString")
                }
                else -> {
                    stateString = "UNKNOWN_STATE             -"
                    Log.i(TAG, "onPlayerStateChanged: $stateString")
                }
            }
            Log.d(
                TAG,
                "changed state to $stateString playWhenReady: $playWhenReady"
            )
        }
    }

    companion object {
        const val TAG = "PlayerActivity"
    }

    override fun onPause() {
        if (player != null) {
            if (player!!.isPlaying) {
                player?.release()
            }
        }

        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 && player == null) {
            initPlayer()
        }
    }

    override fun onStart() {
        super.onStart()

        if (player != null) {
            if (player!!.isPlaying) {
                releasePlayer()
            }
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player?.removeListener(playbackStateListener)
            player?.release()
            player = null
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}

class NavCatItem(
    private val context: Context,
    private val name: String
) :
    Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.sub_category_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.subCategoryTitle.text = name
    }
}