package com.example.hikeapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikeapp.R;
import com.example.hikeapp.adapter.ObservationAdapter;
import com.example.hikeapp.database.DatabaseHandler;
import com.example.hikeapp.model.HikeModel;
import com.example.hikeapp.model.ObservationItem;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ConfirmDialog extends Dialog {
    private Context context;
    private Fragment fragment;
    private String name;
    private String location;
    private String date;
    private String isParkingAvailable;
    private String length;
    private String level;
    private String description;
    private String id;
    private String image;
    private ArrayList<ObservationItem> arrayList;

    private boolean isShow = false;
    private TextView titleConfirm;
    private TextView nameTv;
    private TextView locationTv;
    private TextView dateTv;
    private TextView isParkingTv;
    private TextView lengthTv;
    private TextView levelTv;
    private TextView descriptionTv;
    private TextView expenseListLabelTv;
    private MaterialButton btnCancel;
    private MaterialButton btnConfirm;
    private Button btnAddImage;
    private ImageView imageView;
    private RecyclerView rvExpenseItem;
    private ObservationAdapter observationAdapter;
    private OnAddExpenseListener onAddExpenseListener;

    public ConfirmDialog(Context context, Fragment fragment, String name, String location, String date, String isParkingAvailable, String length, String level, String description, String id, String image, ArrayList<ObservationItem> arrayList) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        this.name = name;
        this.location = location;
        this.date = date;
        this.isParkingAvailable = isParkingAvailable;
        this.length = length;
        this.level = level;
        this.description = description;
        this.id = id;
        this.image = image;
        this.arrayList = arrayList;
    }

    public void setOnAddExpenseListener(OnAddExpenseListener onAddExpenseListener) {
        this.onAddExpenseListener = onAddExpenseListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_dialog);

        titleConfirm = findViewById(R.id.titleConfirm);
        nameTv = findViewById(R.id.nameValue);
        locationTv = findViewById(R.id.destinationValue);
        dateTv = findViewById(R.id.dateValue);
        isParkingTv = findViewById(R.id.isParkingValue);
        lengthTv = findViewById(R.id.lengthValue);
        levelTv = findViewById(R.id.levelValue);
        descriptionTv = findViewById(R.id.descriptionValue);
        expenseListLabelTv = findViewById(R.id.expenseListLabel);
        imageView = findViewById(R.id.imageView);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnAddImage = findViewById(R.id.btnAddImage);
        rvExpenseItem = findViewById(R.id.rv_expense_item);

        observationAdapter = new ObservationAdapter(getContext(), arrayList);
        rvExpenseItem.setAdapter(observationAdapter);

        if (arrayList.isEmpty()) {
            expenseListLabelTv.setVisibility(View.GONE);
            rvExpenseItem.setVisibility(View.GONE);
        } else {
            expenseListLabelTv.setVisibility(View.VISIBLE);
            rvExpenseItem.setVisibility(View.VISIBLE);
        }

        if (isShow) {
            titleConfirm.setText("Expense Detail");
            btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            btnAddImage.setVisibility(View.VISIBLE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToDB();
            }
        });

        nameTv.setText(name);
        locationTv.setText(location);
        dateTv.setText(date);
        isParkingTv.setText(isParkingAvailable);
        lengthTv.setText(length + " km");
        levelTv.setText(level);
        descriptionTv.setText(description);

        if (!image.equals("")) {
            btnAddImage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(image);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE);
        }

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void storeToDB() {
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            HikeModel hikeModel = new HikeModel(UUID.randomUUID().toString(), name, location, date, isParkingAvailable, length, level, description, "null");
            databaseHandler.addHikeModel(hikeModel);
            dismiss();
            onAddExpenseListener.onSuccess();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void updateImage(Uri uri) {
        btnAddImage.setVisibility(View.GONE);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            Uri tempUri = getImageUri(getContext(), bitmap);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            Log.d("TAG", "onActivityResult: ");
            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            databaseHandler.updateHikeImage(finalFile.getPath(), id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
    }

    public interface OnAddExpenseListener {
        void onSuccess();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }
}


