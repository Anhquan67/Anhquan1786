package com.example.hikeapp.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.hikeapp.R;
import com.example.hikeapp.database.DatabaseHandler;
import com.example.hikeapp.model.HikeModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditHikeFragment extends Fragment {
    private EditText nameEdt;
    private EditText locationEdt;
    private EditText lengthEdt;
    private EditText levelEdt;
    private EditText descriptionEdt;
    private TextView dateEdt;
    private boolean isParkingAvailable = false;
    private DatabaseHandler databaseHandler;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_hike, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String uuid = EditHikeFragmentArgs.fromBundle(getArguments()).getHikeUUID();
        databaseHandler = new DatabaseHandler(requireContext());
        HikeModel hikeModel = databaseHandler.getHikeByUUID(uuid);
        nameEdt = view.findViewById(R.id.nameEdt); //Required
        locationEdt = view.findViewById(R.id.locationEdt); //Required
        dateEdt = view.findViewById(R.id.dateEdt); //Required
        SwitchCompat isParkingAvailableSwitch = view.findViewById(R.id.isParkingAvailable); //Required
        lengthEdt = view.findViewById(R.id.lengthEdt); //Required
        levelEdt = view.findViewById(R.id.levelEdt); //Required
        descriptionEdt = view.findViewById(R.id.descriptionEdt); //Optional
        Button btnSave = view.findViewById(R.id.btnSave);
        nameEdt.setText(hikeModel.getName());
        locationEdt.setText(hikeModel.getLocation());
        dateEdt.setText(hikeModel.getDate());
        lengthEdt.setText(hikeModel.getLength());
        levelEdt.setText(hikeModel.getLevel());
        descriptionEdt.setText(hikeModel.getDescription());
        isParkingAvailable = hikeModel.isParkingAvailable().equals("Yes");
        isParkingAvailableSwitch.setChecked(isParkingAvailable);
        OnDateSetListener date = new OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                dateEdt.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        requireContext(),
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        isParkingAvailableSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isParkingAvailable = isChecked;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getText().toString();
                String location = locationEdt.getText().toString();
                String dateString = dateEdt.getText().toString();
                String length = lengthEdt.getText().toString();
                String level = levelEdt.getText().toString();
                String desc = descriptionEdt.getText().toString();
                if (name.equals("") || location.equals("") || dateString.equals("") || length.equals("") || level.equals("")) {
                    Toast.makeText(getContext(), "Please fill to required field", Toast.LENGTH_SHORT).show();
                } else {
                    HikeModel hikeModel1 = new HikeModel(
                            uuid,
                            name,
                            location,
                            dateString,
                            isParkingAvailable ? "Yes" : "No",
                            length,
                            level,
                            desc,
                            ""
                    );
                    databaseHandler.updateHike(hikeModel1, uuid);
                    Navigation.findNavController(requireView()).navigateUp();
                }
            }
        });
    }
}


