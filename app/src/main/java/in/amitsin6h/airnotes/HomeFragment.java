package in.amitsin6h.airnotes;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.R.attr.name;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private List<Notes> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private FloatingActionButton fabAction;
    private FrameLayout frameLayoutHome;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        fabAction = (FloatingActionButton) rootView.findViewById(R.id.fabAction);
        frameLayoutHome = (FrameLayout) rootView.findViewById(R.id.framelayout_home);

        notesAdapter = new NotesAdapter(notesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notesAdapter);



        fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNotesActivity.class);
                startActivity(intent);
               // overridePendingTransition(R.anim.slide_up, R.anim.close_in);
            }
        });



        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).viewForeground;

                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).viewForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                // get the removed item name to display it in snack bar
                // String name = notesList.get(viewHolder.getAdapterPosition()).getNotes();

                //backup of removed data
                final Notes deletedItem = notesList.get(viewHolder.getAdapterPosition());
                final int position = viewHolder.getAdapterPosition();

                if(direction == ItemTouchHelper.LEFT){
                    //swipe left
                    notesList.remove(position);
                    notesAdapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(),"Swipped to left", Toast.LENGTH_SHORT).show();

                    Snackbar.make(frameLayoutHome, "Notes Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesAdapter.restoreItem(deletedItem, position);
                                }
                            }).show();


                }else if(direction == ItemTouchHelper.RIGHT){//swipe right

                    notesList.remove(position);
                    notesAdapter.notifyItemRemoved(position);

                    Toast.makeText(getContext(),"Swipped to right",Toast.LENGTH_SHORT).show();

                    Snackbar.make(frameLayoutHome, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesAdapter.restoreItem(deletedItem, position);
                                }
                            }).show();

                }

            }

        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);




        prepareNoteData();


        DatabaseHandler db = new DatabaseHandler(getContext());

        //Inserting Contacts
        Log.e("Insert: ", "Inserting.......");

       //db.addNotes(new Notes("SQLite Open Helper working ", "sqlite", "Oct"));

       // db.addNotes(new Notes("Notes", "category", "date"));

        // Reading all contacts
        Log.e("Reading: ", "Reading all contacts..");
        //List<Notes> notes = db.getAllNotes();

       // String[] queryCols = new String[]{"_id", Notes}

        //loadNotesCategory();


        return rootView;
    }






    private void prepareNoteData(){

        DatabaseHandler db = new DatabaseHandler(getContext());
        List<Notes> notes = db.getAllNotes();

        for (Notes n : notes) {
            String log = "Id: "+n.get_id()+" ,Notes: " + n.getNotes() + " ,Category: " + n.getCategory() +
                    "Created At: " + n.getCreatedAt();
            Log.e("Msg: ", log);
            notesList.add(n);
            notesAdapter.notifyDataSetChanged();
        }
    }

}
