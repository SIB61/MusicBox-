package com.sib4u.musicbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    Context ctx;
    List<Audio> audioList;
   Listener listener;
    public CustomAdapter(Context ctx,List<Audio> audioList,Listener listener) {
        this.ctx = ctx;
      this.audioList = audioList;
      this.listener=listener;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder ( LayoutInflater.from ( ctx ).inflate ( R.layout.row,parent,false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        holder.name.setText (audioList.get ( position ).getTitle ());
       // if(audioList.get ( position ).getArtist ()!=null )
        holder.title.setText ( audioList.get ( position ).getArtist ());
    }

    @Override
    public int getItemCount() {
        return audioList.size ();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView name , title ;
        public CustomViewHolder(@NonNull final View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.name );
            title=itemView.findViewById ( R.id.title )  ;
            itemView.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    try {
                        listener.clickListener ( getAdapterPosition () );
                    } catch (IOException e) {
                        e.printStackTrace ( );
                    }
                }
            } );

        }
    }
    public   interface Listener{
        void clickListener(int position) throws IOException;
    }
}
