package com.manager.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.manager.myapplication.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class XemYoutubeActivity extends AppCompatActivity {
    YouTubePlayerView playerView;
    String idvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_youtube);
        idvideo = getIntent().getStringExtra("linkvideo");
        initView();
    }

    private void initView() {
        playerView = findViewById(R.id.youtube_player_view);

        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull  YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(idvideo, 0);
            }
        });
    }
}