package com.ruslaniusupov.android.bakingapp.ui.fragments;


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
import android.widget.TextView;

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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ruslaniusupov.android.bakingapp.R;
import com.ruslaniusupov.android.bakingapp.models.Step;


import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements Player.EventListener {

    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();
    private static final String BUNDLE_STEP = "step";
    private static final String APP_NAME = "BakingApp";

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    @BindView(R.id.step_description)TextView mStepDescription;
    @BindView(R.id.video_view)PlayerView mPlayerView;

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

        if (savedInstanceState == null) {

            if (getArguments().containsKey(BUNDLE_STEP)) {

                mStep = getArguments().getParcelable(BUNDLE_STEP);

                showContent(mStep);

            }

        } else {

            mStep = savedInstanceState.getParcelable(BUNDLE_STEP);

            showContent(mStep);

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
            if (mMediaSession != null) {
                mMediaSession.setActive(false);
            }
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

            String videoUrl = step.getVideoUrl();

            if (!TextUtils.isEmpty(videoUrl)) {

                mPlayerView.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(videoUrl);

                initializeMediaSession();
                initializePlayer(uri);

            } else {

                mPlayerView.setVisibility(View.GONE);

            }

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

        if (mExoPlayer == null) {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory trackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            mExoPlayer.addListener(this);

            mPlayerView.setPlayer(mExoPlayer);

            MediaSource videoSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), APP_NAME)))
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(videoUri);

            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);

        }

    }

    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
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

    }

}
