package in.amitsin6h.airnotes;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategorizedFragment extends Fragment {

    private ChipCloud chipCloud;

    private String[] categoryArray;


    public CategorizedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categorized, container, false);
        getActivity().setTitle("Categorized");





        DatabaseHandler db = new DatabaseHandler(getContext());

        List<String> categoryLables = db.getNotesCategory();
        categoryArray = categoryLables.toArray(new String[0]);

       //String[] categoryAray = getResources().getStringArray(R.array.android_dropdown_arrays);

        Log.e("CHIP Cloud: ", " = " + categoryArray);


        chipCloud = (ChipCloud) rootView.findViewById(R.id.chip_cloud);

        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(Color.parseColor("#28405E"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(100)
                .deselectTransitionMS(250)
                .labels(categoryArray)
                .mode(ChipCloud.Mode.MULTI)
                .allCaps(false)
                .gravity(ChipCloud.Gravity.CENTER)
                .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        Intent intent = new Intent(getContext(), CategorizedActivity.class);

                        //passing categoryname
                        intent.putExtra("categoryName", categoryArray[index]);
                        Toast.makeText(getContext(),"Selected: " + categoryArray[index], Toast.LENGTH_LONG).show();

                        startActivity(intent);
                    }
                    @Override
                    public void chipDeselected(int index) {
                        //...
                    }
                })
                .build();

        //loadCategoryLabels();


        return rootView;
    }





}
