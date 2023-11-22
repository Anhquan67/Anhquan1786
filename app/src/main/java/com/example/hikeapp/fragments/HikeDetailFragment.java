package com.example.hikeapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikeapp.R;
import com.example.hikeapp.adapter.ObservationAdapter;
import com.example.hikeapp.database.DatabaseHandler;
import com.example.hikeapp.model.HikeModel;
import com.example.hikeapp.model.ObservationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HikeDetailFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_WRITE_PERMISSION = 1002;
    private DatabaseHandler databaseHandler;
    private TextView nameTv;
    private TextView locationTv;
    private TextView dateTv;
    private TextView isParkingTv;
    private TextView lengthTv;
    private TextView levelTv;
    private TextView descriptionTv;
    private TextView imgLabel;
    private Button btnAddImage;
    private ImageView imageView;
    private RecyclerView rvObservation;
    private ObservationAdapter observationAdapter;
    private FloatingActionButton btnEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hike_detail, container, false);
    }

    private String uuid = "";

    @SuppressLint("WrongThread")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uuid = HikeDetailFragmentArgs.fromBundle(getArguments()).getHikeUUID();
        databaseHandler = new DatabaseHandler(requireContext());
        HikeModel hikeModel = databaseHandler.getHikeByUUID(uuid);
        ArrayList<ObservationItem> arrayList = databaseHandler.getAllObservationItemByParentId(uuid);
        nameTv = view.findViewById(R.id.nameValue);
        locationTv = view.findViewById(R.id.locationValue);
        dateTv = view.findViewById(R.id.dateValue);
        isParkingTv = view.findViewById(R.id.isParkingValue);
        lengthTv = view.findViewById(R.id.lengthValue);
        levelTv = view.findViewById(R.id.levelValue);
        descriptionTv = view.findViewById(R.id.descriptionValue);
        imgLabel = view.findViewById(R.id.imgLabel);
        btnEdit = view.findViewById(R.id.btnEdit);
        imageView = view.findViewById(R.id.imageView);
        btnAddImage = view.findViewById(R.id.btnAddImage);
        rvObservation = view.findViewById(R.id.rvObservations);
        observationAdapter = new ObservationAdapter(getContext(), arrayList);
        rvObservation.setAdapter(observationAdapter);
        nameTv.setText(hikeModel.getName());
        locationTv.setText(hikeModel.getLocation());
        dateTv.setText(hikeModel.getDate());
        isParkingTv.setText(hikeModel.isParkingAvailable());
        lengthTv.setText(hikeModel.getLength());
        levelTv.setText(hikeModel.getLevel());
        descriptionTv.setText(hikeModel.getDescription());
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HikeDetailFragmentDirections.ActionHikeDetailFragmentToEditHikeFragment action =
                        HikeDetailFragmentDirections.actionHikeDetailFragmentToEditHikeFragment();
                action.setHikeUUID(uuid);
                Navigation.findNavController(view).navigate(action);
            }
        });
        String imagePath = hikeModel.getImage();
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
            imgLabel.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageView.setImageBitmap(myBitmap);
            }
        } else {
            imgLabel.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseHandler = new DatabaseHandler(getContext());
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String readImagePermission = Manifest.permission.READ_MEDIA_IMAGES;
            if (ContextCompat.checkSelfPermission(requireContext(), readImagePermission) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                requestPermissions(new String[]{readImagePermission}, REQUEST_WRITE_PERMISSION);
            }
            return;
        }
        boolean isGrantedPermission = (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (isGrantedPermission) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String filePath = getRealPathFromURI(selectedImageUri);
            updateImage(selectedImageUri, filePath);
        }
    }

    public void updateImage(Uri uri, String filePath) {
        btnAddImage.setVisibility(View.GONE);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            databaseHandler.updateHikeImage(filePath, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}


