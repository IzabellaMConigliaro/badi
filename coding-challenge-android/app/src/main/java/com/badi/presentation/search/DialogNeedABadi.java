package com.badi.presentation.search;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.badi.R;
import com.badi.presentation.navigation.Navigator;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DialogNeedABadi extends DialogFragment {

    public static final String DIALOG_NEED_A_BADI = "dialog_need_a_badi";

    private Dialog dialog;
    private Unbinder unbinder;
    private OnDialogNeedABadiListener onDialogNeedABadiListener;

    interface OnDialogNeedABadiListener {
        void onListRoomClicked();
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag,
                                      OnDialogNeedABadiListener onDialogNeedABadiListener) {
        this.onDialogNeedABadiListener = onDialogNeedABadiListener;

        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_need_badi, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        dialog.dismiss();
    }

    @OnClick(R.id.button_list_room)
    public void onClickListRoom() {
        onDialogNeedABadiListener.onListRoomClicked();
        dialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Binding reset to avoid memory leaks
        unbinder.unbind();
    }

}
