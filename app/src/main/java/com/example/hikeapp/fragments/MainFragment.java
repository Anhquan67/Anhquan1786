package com.example.hikeapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikeapp.R;
import com.example.hikeapp.adapter.HikeAdapter;
import com.example.hikeapp.database.DatabaseHandler;
import com.example.hikeapp.dialog.AddObservationDialog;
import com.example.hikeapp.model.HikeModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private MaterialButton btnAdd;
    private ArrayList<HikeModel> listHikeModel = new ArrayList<>();
    private RecyclerView listExpenseRv;
    private HikeAdapter hikeAdapter;
    private EditText searchEdt;
    private MaterialButton btnSearch;
    private MaterialButton btnReset;
    private MaterialButton btnRefreshSearch;
    private DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<HikeModel> list = databaseHandler.getAllHikeModel();
        hikeAdapter.onDataChange(list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listExpenseRv = view.findViewById(R.id.listExpenseRv);
        searchEdt = view.findViewById(R.id.searchEdt);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnReset = view.findViewById(R.id.btnResetDatabase);
        btnRefreshSearch = view.findViewById(R.id.btnRefreshSearch);
        hikeAdapter = new HikeAdapter(getContext(), listHikeModel, new HikeAdapter.OnClickListener() {
            @Override
            public void onItemClick(String id) {
                MainFragmentDirections.ActionMainFragmentToHikeDetailFragment action = MainFragmentDirections.actionMainFragmentToHikeDetailFragment();
                action.setHikeUUID(id);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onEditClick(String id) {
                AddObservationDialog addObservationDialog = new AddObservationDialog(requireContext(), id);
                addObservationDialog.setOnAddObservationListener(new AddObservationDialog.OnAddObservationListener() {
                    @Override
                    public void onSuccess() {
                        addObservationDialog.dismiss();
                    }
                });
                addObservationDialog.show();
                Window window = addObservationDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(null);
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }

            @Override
            public void onDeleteClick(String id) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete this hike?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseHandler.deleteHike(id);
                                hikeAdapter.onDataChange(databaseHandler.getAllHikeModel());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        listExpenseRv.setAdapter(hikeAdapter);
        btnAdd = view.findViewById(R.id.btnAddNewExpense);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addHikeFragment);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (!searchEdt.getText().toString().equals("")) {
                    String searchContent = searchEdt.getText().toString().trim().toLowerCase();
                    HikeModel hikeModel = databaseHandler.getHikeByName(searchContent);
                    if (hikeModel != null) {
                        ArrayList<HikeModel> list = new ArrayList<>();
                        list.add(hikeModel);
                        hikeAdapter.onDataChange(list);
                    } else {
                        Toast.makeText(requireContext(), "No HIKE found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ArrayList<HikeModel> list = databaseHandler.getAllHikeModel();
                    hikeAdapter.onDataChange(list);
                }
            }
        });
        btnRefreshSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                searchEdt.setText("");
                ArrayList<HikeModel> list = databaseHandler.getAllHikeModel();
                hikeAdapter.onDataChange(list);
            }
        });
        if (databaseHandler.getAllHikeModel().size() > 0) {
            btnReset.setVisibility(View.VISIBLE);
        } else {
            btnReset.setVisibility(View.GONE);
        }
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                databaseHandler.resetDatabase();
                ArrayList<HikeModel> list = databaseHandler.getAllHikeModel();
                hikeAdapter.onDataChange(list);
                btnReset.setVisibility(View.GONE);
            }
        });
    }
}


