package in.amitsin6h.airnotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jkcarino.rtexteditorview.RTextEditorButton;
import com.jkcarino.rtexteditorview.RTextEditorToolbar;
import com.jkcarino.rtexteditorview.RTextEditorView;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateNotesActivity extends AppCompatActivity implements ColorPickerDialogListener {

    private static final String TAG = "AirNotes";

    private static final int DIALOG_TEXT_FORE_COLOR_ID = 0;
    private static final int DIALOG_TEXT_BACK_COLOR_ID = 1;

    private RTextEditorView editorView;
    private RTextEditorToolbar editorToolbar;
    private RTextEditorButton textForeColorButton, textBackColorButton, insertTableButton, insertLinkButton;
    private Spinner addCategory;
    private TextView dateTime;

    private int unCatPos;
    private String catItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        editorView = (RTextEditorView) findViewById(R.id.editor_view);
        editorToolbar = (RTextEditorToolbar) findViewById(R.id.editor_toolbar);
        // Set the RTextEditorView to our toolbar
        editorToolbar.setEditorView(editorView);

        //category spinner
        addCategory = (Spinner) findViewById(R.id.addCategory);
        loadNotesCategory();

        //date time
        dateTime = (TextView) findViewById(R.id.dateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d yyyy, hh:mm a");
        String getDateTime = "";
        Calendar calendar = Calendar.getInstance();
        getDateTime = simpleDateFormat.format(calendar.getTime());
        //Log.e("DAte: ", getDateTime);
        dateTime.setText(getDateTime);


        // Listen to the editor's text changes
        editorView.setOnTextChangeListener(new RTextEditorView.OnTextChangeListener() {
           @Override
           public void onTextChanged(String content) {
               Log.e(TAG, "onTextChanged: " + content);
           }
        });


        //editorView.getSettings().setJavaScriptEnabled(false);

        // Set initial content
        String welcomeNote = "Keep saving paper";

        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        String safeWelcomeNote = policy.sanitize(welcomeNote);

        editorView.setHtml(safeWelcomeNote);

        // Text foreground color
        textForeColorButton = (RTextEditorButton) findViewById(R.id.text_fore_color);
        textForeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialog.newBuilder()
                        .setDialogId(DIALOG_TEXT_FORE_COLOR_ID)
                        .setDialogTitle(R.string.dialog_title_text_color)
                        .setShowAlphaSlider(false)
                        .setAllowCustom(true)
                        .show(CreateNotesActivity.this);
            }
        });


        // Text background color
        textBackColorButton = (RTextEditorButton) findViewById(R.id.text_back_color);
        textBackColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialog.newBuilder()
                        .setDialogId(DIALOG_TEXT_BACK_COLOR_ID)
                        .setDialogTitle(R.string.dialog_title_text_back_color)
                        .setShowAlphaSlider(false)
                        .setAllowCustom(true)
                        .show(CreateNotesActivity.this);
            }
        });



        // Insert table
        insertTableButton = (RTextEditorButton) findViewById(R.id.insert_table);
        insertTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableDialogFragment dialog = TableDialogFragment.newInstance();
                dialog.setOnInsertClickListener(onInsertTableClickListener);
                dialog.show(getSupportFragmentManager(), "insert-table-dialog");
            }
        });


        // Insert Link
        insertLinkButton = (RTextEditorButton) findViewById(R.id.insert_link);
        insertLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinkDialogFragment dialog = LinkDialogFragment.newInstance();
                dialog.setOnInsertClickListener(onInsertLinkClickListener);
                dialog.show(getSupportFragmentManager(), "insert-link-dialog");
            }
        });





    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down, R.anim.close_in);

        String data = editorView.getHtml();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.addNotes(new Notes(data, catItem , dateTime.getText().toString()));


        Toast.makeText(this, data + catItem + dateTime.getText().toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(item.getItemId() == android.R.id.home){
            finish();
            CreateNotesActivity.this.overridePendingTransition(R.anim.slide_down, R.anim.close_in);
        }else if (id == R.id.action_undo){
            editorView.undo();
            return true;
        } else if (id == R.id.action_redo) {
            editorView.redo();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editorView.setOnTextChangeListener(null);
        editorView.removeAllViews();
        editorView.destroy();
        editorView = null;
    }

    private final TableDialogFragment.OnInsertClickListener onInsertTableClickListener =
            new TableDialogFragment.OnInsertClickListener() {
                @Override
                public void onInsertClick(int colCount, int rowCount) {
                    editorView.insertTable(colCount, rowCount);
                }
            };

    private final LinkDialogFragment.OnInsertClickListener onInsertLinkClickListener =
            new LinkDialogFragment.OnInsertClickListener() {
                @Override
                public void onInsertClick(@NonNull String title, @NonNull String url) {
                    editorView.insertLink(title, url);
                }
            };

    @Override
    public void onColorSelected(int dialogId, int color) {
        if (dialogId == DIALOG_TEXT_FORE_COLOR_ID) {
            editorView.setTextColor(color);
        } else if (dialogId == DIALOG_TEXT_BACK_COLOR_ID) {
            editorView.setTextBackgroundColor(color);
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadNotesCategory() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> labels = db.getNotesCategory();
        final String addCategoryItem = "Create Category";
        labels.add(addCategoryItem);


        final String unCategorized = "Un Categorized";
        labels.add(unCategorized);

        //Log.e("CAT:", " = " + labels);



        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CreateNotesActivity.this,
                android.R.layout.simple_spinner_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCategory.setAdapter(spinnerAdapter);

        unCatPos = spinnerAdapter.getPosition(unCategorized);


        addCategory.setSelection(unCatPos);

        addCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catItem = addCategory.getSelectedItem().toString();

                if(catItem.equals(addCategoryItem)){
                    showChangeLangDialog();
                }

                Toast.makeText(CreateNotesActivity.this, "Spinner Data " + catItem +"", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                addCategory.setSelection(unCatPos);

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }





}
