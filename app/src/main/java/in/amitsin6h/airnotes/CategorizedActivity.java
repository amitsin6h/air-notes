package in.amitsin6h.airnotes;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategorizedActivity extends AppCompatActivity {

    private List<Notes> notesList = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private RecyclerView categorizedRecyclerView;
    private FrameLayout categorizedFramelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        categorizedRecyclerView = (RecyclerView) findViewById(R.id.categorized_recycler_view);
        categorizedFramelayout = (FrameLayout) findViewById(R.id.categorized_framelayout);

        notesAdapter = new NotesAdapter(notesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        categorizedRecyclerView.setLayoutManager(layoutManager);
        categorizedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categorizedRecyclerView.setAdapter(notesAdapter);








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
                    Toast.makeText(CategorizedActivity.this,"Swipped to left", Toast.LENGTH_SHORT).show();

                    Snackbar.make(categorizedFramelayout, "Notes Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesAdapter.restoreItem(deletedItem, position);
                                }
                            }).show();


                }else if(direction == ItemTouchHelper.RIGHT){//swipe right

                    notesList.remove(position);
                    notesAdapter.notifyItemRemoved(position);

                    Toast.makeText(CategorizedActivity.this,"Swipped to right",Toast.LENGTH_SHORT).show();

                    Snackbar.make(categorizedFramelayout, "Replace with your own action", Snackbar.LENGTH_LONG)
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
        itemTouchHelper.attachToRecyclerView(categorizedRecyclerView);


        prepareNoteData();






    }



    private void prepareNoteData(){

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
