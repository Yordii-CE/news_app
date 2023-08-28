package com.example.news;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {
    public IBottomSheetListener listener;
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            dismiss();
        });

        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            TextInputEditText search = view.findViewById(R.id.search_input);
            if(search.length() > 0){
                listener.onSearchButtonClicked(search.getText().toString());
                dismiss();
            }
        });

        return view;
    }
}
