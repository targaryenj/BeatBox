package com.example.jdm.beatbox.fragment;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jdm.beatbox.BeatBox;
import com.example.jdm.beatbox.R;
import com.example.jdm.beatbox.Sound;

import java.io.IOException;
import java.util.List;

/**
 * Created by JDM on 2016/5/13.
 */
public class BeatBoxFragment extends Fragment {
    private BeatBox mBeatBox;


    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持BeatBox实体在设置变化时存活
        setRetainInstance(true);

        startBeatBox();

    }

    private void startBeatBox(){
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beat_box,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_beat_box_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getmSounds()));

        return view;
    }

    private class SoundHolder extends RecyclerView.ViewHolder{

        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater,ViewGroup viewGroup){
            super(inflater.inflate(R.layout.list_item_sound,viewGroup,false));
            mButton = (Button) itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mBeatBox.load(mSound);
                    } catch (IOException e) {
                        Log.e("BeatBoxFragment","Could not load sound " + mSound.getmName(),e);
                    }
                    mBeatBox.play(mSound);
                }
            });
        }

        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getmName());
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }











}
