package com.example.hikeapp.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.hikeapp.R;
import com.example.hikeapp.dialog.ConfirmDialog;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddHikeFragment extends Fragment {
    private EditText nameEdt;
    private EditText locationEdt;
    private EditText lengthEdt;
    private EditText levelEdt;
    private EditText descriptionEdt;
    private TextView dateEdt;
    private boolean isParkingAvailable = false;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_hike, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        nameEdt = view.findViewById(R.id.nameEdt); //Required
        locationEdt = view.findViewById(R.id.locationEdt); //Required
        dateEdt = view.findViewById(R.id.dateEdt); //Required
        SwitchCompat isParkingAvailableSwitch = view.findViewById(R.id.isParkingAvailable); //Required
        lengthEdt = view.findViewById(R.id.lengthEdt); //Required
        levelEdt = view.findViewById(R.id.levelEdt); //Required
        descriptionEdt = view.findViewById(R.id.descriptionEdt); //Optional
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getText().toString();
                String location = locationEdt.getText().toString();
                String date = dateEdt.getText().toString();
                String length = lengthEdt.getText().toString();
                String level = levelEdt.getText().toString();
                if (name.equals("") || location.equals("") || date.equals("") || length.equals("") || level.equals("")) {
                    Toast.makeText(getContext(), "Please fill to required field", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmDialog();
                }
            }
        });

        OnDateSetListener date = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yyyy";
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
    }

    private void showConfirmDialog() {
        String hikeName = nameEdt.getText().toString();
        String location = locationEdt.getText().toString();
        String date = dateEdt.getText().toString();
        String length = lengthEdt.getText().toString();
        String lvl = levelEdt.getText().toString();
        String description = descriptionEdt.getText().toString();
        ConfirmDialog dialog = new ConfirmDialog(
                requireContext(),
                AddHikeFragment.this,
                hikeName,
                location,
                date,
                isParkingAvailable ? "Yes" : "No",
                length,
                lvl,
                description,
                "",
                "",
                new ArrayList<>()
        );
        dialog.setCancelable(false);
        dialog.setOnAddExpenseListener(new ConfirmDialog.OnAddExpenseListener() {
            @Override
            public void onSuccess() {
                Navigation.findNavController(requireView()).navigate(R.id.action_addHikeFragment_to_mainFragment);
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(null);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}


