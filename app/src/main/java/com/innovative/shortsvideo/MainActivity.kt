package com.innovative.shortsvideo

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.innovative.shortsvideo.databinding.ActivityMainBinding
import com.innovative.shortsvideo.databinding.ItemReelsBinding

class MainActivity : AppCompatActivity(), Player.EventListener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var player: SimpleExoPlayer? = null
    var playerBinding: ItemReelsBinding? = null
    var lastPosition = 0
    var reelsAdapter: ReelsAdapter = ReelsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.rvReels.adapter = reelsAdapter
        reelsAdapter.addData(Democontents.getReels())
        PagerSnapHelper().attachToRecyclerView(binding.rvReels)
        binding.rvReels.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var position: Int = 0
                var view: View? = null
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0 && (binding.rvReels.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        .also {
                            position = it
                        } > -1 && lastPosition != position && binding.rvReels.layoutManager != null && binding.rvReels.layoutManager!!
                        .findViewByPosition(position).also {
                            view = it
                        } != null
                ) {
                    lastPosition = position
                    val unused: Int = lastPosition
                    val binding1 =
                        DataBindingUtil.bind<ViewDataBinding>(view!!) as ItemReelsBinding?
                    if (binding1 != null) {
                        playVideo(reelsAdapter.list[position].video_url, binding1)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        reelsAdapter.setOnReelsVideoAdapterListner(object :
            ReelsAdapter.OnReelsVideoAdapterListner {
            override fun onItemClick(reelsBinding: ItemReelsBinding, pos: Int, type: Int) {
                if (type == 1) {
                    lastPosition = pos
                    playVideo(reelsAdapter.getList()[pos].video_url, reelsBinding)
                } else if (player == null) {
                } else {
                    player!!.playWhenReady = !player!!.isPlaying
                }
            }

            override fun onDoubleClick(
                model: Video,
                event: MotionEvent,
                binding: ItemReelsBinding
            ) {
            }

            override fun onClickLike(reelsBinding: ItemReelsBinding, pos: Int) {}
            override fun onClickUser(reel: Video) {}
            override fun onClickComments(reels: Video) {}
            override fun onClickShare(reel: Video) {}
            override fun onClickGift() {}
        })
    }

    /*public void setThumbnail(String video, ItemReelsBinding reelsBinding) {
        Glide.with(reelsBinding.getRoot()).load(video).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new BlurTransformation(25, 5))).into(reelsBinding.thumbnailVideo);
    }*/
    fun playVideo(videoUrl: String, binding2: ItemReelsBinding) {
        val simpleExoPlayer = player
        if (simpleExoPlayer != null) {
            simpleExoPlayer.removeListener(this@MainActivity)
            player!!.playWhenReady = false
            player!!.release()
        }
        Log.d("TAG", "playVideo:URL  $videoUrl")
        playerBinding = binding2
        player = SimpleExoPlayer.Builder(this@MainActivity).build()
        val progressiveMediaSource = ProgressiveMediaSource.Factory(
            CacheDataSourceFactory(
                MainApplication.simpleCache, DefaultHttpDataSourceFactory(
                    Util.getUserAgent(this@MainActivity, "TejTok")
                ), CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
        ).createMediaSource(
            Uri.parse(videoUrl)
        )
        binding2.playerView.player = player
        player!!.playWhenReady = true
        player!!.seekTo(0, 0)
        player!!.repeatMode = Player.REPEAT_MODE_ALL
        player!!.addListener(this)
        binding2.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        player!!.prepare(progressiveMediaSource, true, false)
    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        //new Implemention by reverese
        if (playbackState == 2) {
            if (playerBinding != null) {
                binding.buffering.visibility = View.VISIBLE
            }
        } else if (playbackState == 3 && playerBinding != null) {
            binding.buffering.visibility = View.GONE
        }
    }

    override fun onResume() {
        if (player != null) {
            player!!.playWhenReady = true
        }
        super.onResume()
    }

    override fun onStop() {
        if (player != null) {
            player!!.playWhenReady = false
        }
        super.onStop()
    }

    override fun onPause() {
        if (player != null) {
            player!!.playWhenReady = false
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.stop()
            player!!.release()
        }
        super.onDestroy()
    }

}