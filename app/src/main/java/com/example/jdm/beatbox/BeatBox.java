package com.example.jdm.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JDM on 2016/5/13.
 */
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();

    private static final int MAX_SOUNDS = 5;
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssets = context.getAssets();
        // is deprecated,but we need it for compatibility
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC,0);
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG,"Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG,"Could not list assets" , e);
            return;
        }

        //
        for (String filename : soundNames){

            String assetPath = SOUNDS_FOLDER + "/" + filename;
            Sound sound = new Sound(assetPath);
            mSounds.add(sound);

            // 初始化就加载太耗费时间，改为点击加载
//            try {
//
//                load(sound);
//
//            } catch (IOException e) {
//                Log.e(TAG,"Could not load sound " + filename,e);
//            }

        }
    }

    public void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getmAssetPath());
        int soundId = mSoundPool.load(afd,1);
        sound.setmSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getmSoundId();
        if (soundId == null){
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release();
    }

    public List<Sound> getmSounds() {
        return mSounds;
    }
}
