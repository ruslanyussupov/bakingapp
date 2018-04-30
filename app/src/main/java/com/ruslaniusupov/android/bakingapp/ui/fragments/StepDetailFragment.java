package com.ruslaniusupov.android.bakingapp.ui.fragments;


import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.models.Step;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements Player.EventListener {

    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();
    private static final String BUNDLE_STEP = "step";
    private static final String BUNDLE_RESUME_POSITION = "resume_position";
    private static final String BUNDLE_RESUME_WINDOW = "resume_window";
    private static final String BUNDLE_IS_READY = "is_ready";
    private static final String APP_NAME = "BakingApp";

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private String mVideoUrl;
    private boolean mIsLandLayout;
    private Dialog mFullScreenDialog;
    private boolean mIsReady;
    private int mResumeWindow = C.INDEX_UNSET;
    private long mResumePosition = C.POSITION_UNSET;

    @BindView(R.id.step_description)TextView mStepDescription;
    @BindView(R.id.video_view)PlayerView mPlayerView;
    @BindView(R.id.video_frame)FrameLayout mVideoFrame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View landView = getActivity().findViewById(R.id.land_layout);
        mIsLandLayout = landView != null && landView.getVisibility() == View.VISIBLE;

        if (savedInstanceState == null) {

            if (getArguments().containsKey(BUNDLE_STEP)) {

                mStep = getArguments().getParcelable(BUNDLE_STEP);

                showContent(mStep);

            }

        } else {

            mStep = savedInstanceState.getParcelable(BUNDLE_STEP);
            mResumePosition = savedInstanceState.getLong(BUNDLE_RESUME_POSITION);
            mResumeWindow = savedInstanceState.getInt(BUNDLE_RESUME_WINDOW);
            mIsReady = savedInstanceState.getBoolean(BUNDLE_IS_READY);

            showContent(mStep);

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            playVideo();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.SDK_INT <= 23) {
            playVideo();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mExoPlayer != null) {
            mResumePosition = Math.max(0, mExoPlayer.getCurrentPosition());
            mResumeWindow = mExoPlayer.getCurrentWindowIndex();
            mIsReady = mExoPlayer.getPlayWhenReady();
        }

        if (Util.SDK_INT <= 23) {
            releasePlayer();
            if (mMediaSession != null) {
                mMediaSession.setActive(false);
            }
        }

        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
            if (mMediaSession != null) {
                mMediaSession.setActive(false);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_STEP, mStep);
        outState.putLong(BUNDLE_RESUME_POSITION, mResumePosition);
        outState.putInt(BUNDLE_RESUME_WINDOW, mResumeWindow);
        outState.putBoolean(BUNDLE_IS_READY, mIsReady);
        super.onSaveInstanceState(outState);
    }

    public static StepDetailFragment create(Step step) {

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_STEP, step);
        stepDetailFragment.setArguments(args);

        return stepDetailFragment;

    }

    private void showContent(Step step) {

        if (step != null) {

            mVideoUrl = step.getVideoUrl();

            String description = step.getDescription();
            mStepDescription.setText(description);

        }

    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getActivity(), LOG_TAG);

        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri videoUri) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        mExoPlayer.addListener(this);

        mPlayerView.setPlayer(mExoPlayer);

        MediaSource videoSource = new ExtractorMediaSource.Factory(new CacheDataSourceFactory(getActivity()))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(videoUri);

        mExoPlayer.prepare(videoSource);

        if (mResumeWindow != C.INDEX_UNSET) {
            mExoPlayer.seekTo(mResumePosition);
            mExoPlayer.setPlayWhenReady(mIsReady);
        } else {
            mExoPlayer.setPlayWhenReady(true);
        }

    }

    private void playVideo() {

        if (!TextUtils.isEmpty(mVideoUrl)) {

            if (mIsLandLayout) {
                initFullScreenDialog();
                openFullScreenDialog();
            } else {
                closeFullScreenDialog();
            }

            mVideoFrame.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(mVideoUrl);

            initializeMediaSession();
            initializePlayer(uri);

        } else {

            mVideoFrame.setVisibility(View.GONE);

        }

    }

    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    // Solution from https://geoffledak.com/blog/tag/fullscreen/
    private void initFullScreenDialog() {

        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

            @Override
            public void onBackPressed() {
                closeFullScreenDialog();
                super.onBackPressed();
            }
        };

    }

    private void openFullScreenDialog() {

        ViewGroup videoFrame = (ViewGroup) mPlayerView.getParent();
        videoFrame.removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();

    }

    private void closeFullScreenDialog() {

        if (mFullScreenDialog != null) {
            ViewGroup videoFrame = (ViewGroup) mPlayerView.getParent();
            videoFrame.removeView(mPlayerView);
            mVideoFrame.addView(mPlayerView);
            mFullScreenDialog.dismiss();
        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == Player.STATE_READY) && playWhenReady) {

            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);

        } else if (playbackState == Player.STATE_READY) {

            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);

        }

        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

        @Override
        public void onSeekTo(long pos) {
            mExoPlayer.seekTo(pos);
        }
    }

    // Solution from https://stackoverflow.com/questions/28700391/using-cache-in-exoplayer
    private class CacheDataSourceFactory implements DataSource.Factory {

        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
        private static final long MAX_CACHE_SIZE = 100 * 1024 * 1024;

        private final Context mContext;
        private final DefaultDataSourceFactory mDefaultDataSourceFactory;

        CacheDataSourceFactory(Context context) {
            super();
            mContext = context;
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            String userAgent = Util.getUserAgent(getActivity(), APP_NAME);
            mDefaultDataSourceFactory = new DefaultDataSourceFactory(mContext, bandwidthMeter,
                    new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
        }

        @Override
        public DataSource createDataSource() {
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE);
            SimpleCache simpleCache = new SimpleCache(
                    new File(mContext.getCacheDir(), "media"), evictor);
            return new CacheDataSource(simpleCache, mDefaultDataSourceFactory.createDataSource(),
                    new FileDataSource(), new CacheDataSink(simpleCache, MAX_FILE_SIZE),
                    CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                    null);
        }
    }

}
