package com.example.hikeapp.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikeapp.R;
import com.example.hikeapp.database.DatabaseHandler;
import com.example.hikeapp.model.ObservationItem;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationDialog extends Dialog {
    private Context context;
    private String hikeUuid;
    private EditText observation;
    private EditText comment;
    private TextView time;
    private TextView dateTv;
    private MaterialButton btnConfirm;
    private OnAddObservationListener onAddObservationListener;
    private Calendar myCalendar;

    public AddObservationDialog(Context context, String hikeUuid) {
        super(context);
        this.context = context;
        this.hikeUuid = hikeUuid;
        myCalendar = Calendar.getInstance();
    }

    public void setOnAddObservationListener(OnAddObservationListener onAddObservationListener) {
        this.onAddObservationListener = onAddObservationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_observation_dialog);
        observation = findViewById(R.id.observation);
        time = findViewById(R.id.time);
        dateTv = findViewById(R.id.date);
        comment = findViewById(R.id.comment);
        btnConfirm = findViewById(R.id.btnAddExpense);
        btnConfirm.setOnClickListener(view -> storeToDB());
        time.setOnClickListener(view -> {
            Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    (view1, hourOfDay, minute) -> time.setText(hourOfDay + ":" + minute),
                    mHour,
                    mMinute,
                    false
            );
            timePickerDialog.show();
        });
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            dateTv.setText(dateFormat.format(myCalendar.getTime()));
        };
        dateTv.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void storeToDB() {
        String observationString = observation.getText().toString();
        String timeString = time.getText().toString();
        String dateString = dateTv.getText().toString();
        String commentString = comment.getText().toString();
        if (observationString.equals("") || timeString.equals("") || dateString.equals("")) {
            Toast.makeText(getContext(), "Please fill to all field", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            ObservationItem observationItem = new ObservationItem(
                    hikeUuid,
                    observationString,
                    dateString,
                    timeString,
                    commentString
            );
            databaseHandler.addObservationItem(observationItem);
            dismiss();
            onAddObservationListener.onSuccess();
        }
    }

    public interface OnAddObservationListener {
        void onSuccess();
    }
}