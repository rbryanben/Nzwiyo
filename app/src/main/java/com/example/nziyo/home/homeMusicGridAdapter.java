package com.example.nziyo.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nziyo.R;

import java.io.File;
import java.util.ArrayList;

public class homeMusicGridAdapter extends ArrayAdapter<String> implements homeInterface{

    public homeMusicGridAdapter(@NonNull Context context, ArrayList<String> paths) {
        super(context, R.layout.item_track, paths);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView != null){
            return  convertView ;
        }
        LayoutInflater view = LayoutInflater.from(getContext());
        View customview = view.inflate(R.layout.item_track, parent,false);


        //current track
        final String currentTrack = getItem(position);

        //set items
        File file = new File(currentTrack);

        //references
        TextView trackName = customview.findViewById(R.id.trackNameAndArtistz);
        ImageView trackImage = customview.findViewById(R.id.trackImage);

        //set data
        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(getItem(position));

        try {
             //artist
             String artistAndName = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
             artistAndName += "\n" + metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
             trackName.setText(artistAndName);

             //image
            byte[] art = metaRetriver.getEmbeddedPicture();
            if (art != null) {
                Bitmap songImage = BitmapFactory
                        .decodeByteArray(art, 0, art.length);
                trackImage.setImageBitmap(songImage);

            }

             if (metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) == null) {
                 trackName.setText(file.getName() + "\nunkown artist");
             }

        }
        catch (Exception ex){

        }

        return customview ;
    }

    @Override

}
