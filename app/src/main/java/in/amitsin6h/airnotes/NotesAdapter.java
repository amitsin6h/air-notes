package in.amitsin6h.airnotes;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by amitsin6h on 10/26/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<Notes> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView notes, category, createdAt;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view){
            super(view);
            notes = (TextView) view.findViewById(R.id.notes);
            category = (TextView) view.findViewById(R.id.category);
            createdAt = (TextView) view.findViewById(R.id.createdAt);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

        }
    }



    public NotesAdapter(List<Notes> notesList){
        this.notesList = notesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        final Notes notes = notesList.get(position);

//        Notes notes = notesList.get(position);
        holder.notes.setText(Html.fromHtml(notes.getNotes()));
        holder.category.setText(notes.getCategory());
        holder.createdAt.setText(notes.getCreatedAt());

    }


    @Override
    public int getItemCount(){
        return notesList.size();
    }



    public void removeItem(int position) {
        notesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Notes notes, int position) {
        notesList.add(position, notes);
        // notify item added by position
        notifyItemInserted(position);
    }



}
