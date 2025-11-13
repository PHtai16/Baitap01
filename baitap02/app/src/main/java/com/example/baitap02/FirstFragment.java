package com.example.baitap02;

import android.os.Bundle;
import android.os.CountDownTimer; // MỚI (Slide 20)
import android.util.Log; // MỚI (Slide 21)
import android.view.ContextMenu; // MỚI (Slide 29)
import android.view.LayoutInflater;
import android.view.MenuItem; // MỚI
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import android.widget.ImageView;
import android.widget.ImageButton;

// --- THÊM TẤT CẢ IMPORT MỚI ---
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.PopupMenu; // MỚI (Slide 27)
import android.widget.Toast; // MỚI
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog; // MỚI (Slide 31)
import android.content.DialogInterface; // MỚI (Slide 31)
import android.app.Dialog; // MỚI (Slide 32)
import android.view.Window; // MỚI (Slide 32)
import android.widget.EditText; // MỚI (Slide 32)

import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    View bg;
    ImageView img1;
    TextView txtSoN;
    Button btnRnd;
    ProgressBar progressBar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Ánh xạ control CŨ ---
        txtSoN = view.findViewById(R.id.textViewSo);
        btnRnd = view.findViewById(R.id.buttonRnd);
        bg = view.findViewById(R.id.fragment_first_root);
        Switch sw = view.findViewById(R.id.switch_bg);
        img1 = view.findViewById(R.id.imageView1);
        ImageButton img2 = view.findViewById(R.id.imageButton1);

        // --- ÁNH XẠ CONTROL MỚI ---
        CheckBox ck = view.findViewById(R.id.checkBox);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        progressBar = view.findViewById(R.id.progressBar);
        SeekBar seekBar = view.findViewById(R.id.seekBar);

        btnRnd.setOnClickListener(v -> {
            Random random = new Random();
            int number = random.nextInt(10);
            txtSoN.setText(String.valueOf(number));
            showPopupMenu(v);
        });

        // --- Code cũ: "Bài tập 1" (Slide 13) ---
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.bg1);
        arrayList.add(R.drawable.bg2);
        arrayList.add(R.drawable.bg3);
        arrayList.add(R.drawable.bg4);
        arrayList.add(R.drawable.bg5);
        Random random = new Random();
        int vitri = random.nextInt(arrayList.size());
        bg.setBackgroundResource(arrayList.get(vitri));

        // --- Code cũ: "Bài tập 2" (Slide 15) ---
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                bg.setBackgroundResource(R.drawable.bg3);
            }else{
                bg.setBackgroundResource(R.drawable.bg4);
            }
        });

        // --- Code cũ: Slide 14 (ImageButton) ---
        // SỬA LẠI: Thêm Alert Dialog (Slide 31)
        img2.setOnClickListener(v -> {
            // Code MỚI: Hiện Alert Dialog (Slide 31)
            showAlertDialog();
        });

        // --- Code MỚI: Slide 32 (Custom Dialog) ---
        // Kích hoạt khi bấm vào ImageView
        img1.setOnClickListener(v -> showCustomDialog());

        // --- CODE MỚI TỪ SLIDE 16 (CHECKBOX) ---
        ck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                bg.setBackgroundResource(R.drawable.facebook);
            }else{
                bg.setBackgroundResource(R.drawable.google);
            }
        });

        // --- CODE MỚI TỪ SLIDE 17 (RADIOGROUP) ---
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton1) {
                bg.setBackgroundResource(R.drawable.facebook);
            } else if (checkedId == R.id.radioButton2) {
                bg.setBackgroundResource(R.drawable.google);
            }
        });

        // --- CODE MỚI TỪ SLIDE 20 (PROGRESSBAR TỰ CHẠY) ---
        startProgressBarTimer(); // Gọi hàm tự chạy

        // --- CODE MỚI TỪ SLIDE 21 (SEEKBAR) ---
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress: giá trị của seekbar
                Log.d("FirstFragment", "Giá trị SeekBar: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("FirstFragment", "Bắt đầu kéo");
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("FirstFragment", "Dừng kéo");
            }
        });

        // --- CODE MỚI TỪ SLIDE 29 (CONTEXT MENU) ---
        // Đăng ký Context Menu cho TextView
        registerForContextMenu(txtSoN);
        // Đăng ký cho cả Button
        registerForContextMenu(btnRnd);
    }

    // --- HÀM MỚI (NGOÀI onViewCreated) ---

    // --- Slide 20: ProgressBar Timer ---
    private void startProgressBarTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int current = progressBar.getProgress();
                if (current >= progressBar.getMax()){
                    current = 0;
                }
                progressBar.setProgress(current + 10);
            }
            @Override
            public void onFinish() {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Hết giờ", Toast.LENGTH_SHORT).show();
                }
                // Bắt đầu lại (tùy chọn)
                this.start();
            }
        };
        countDownTimer.start();
    }

    // --- Slide 27-28: Popup Menu ---
    private void showPopupMenu(View v) {
        if (getContext() == null) return;
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        if (getActivity() != null) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuSetting) {
                Toast.makeText(getContext(), "Bạn chọn Setting", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.menuShare) {
                Toast.makeText(getContext(), "Bạn chọn Share", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.menuLogout) {
                Toast.makeText(getContext(), "Bạn chọn Logout", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        popupMenu.show();
    }

    // --- Slide 29-30: Context Menu ---
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (getActivity() != null) {
            getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        // Xác định xem view nào đã gọi menu
        if (v.getId() == R.id.buttonRnd) {
            menu.setHeaderTitle("Menu cho Button");
        } else if (v.getId() == R.id.textViewSo) {
            menu.setHeaderTitle("Menu cho TextView");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuSetting) {
            if (getContext() != null) Toast.makeText(getContext(), "Context: Setting", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menuShare) {
            if (getContext() != null) Toast.makeText(getContext(), "Context: Share", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menuLogout) {
            if (getContext() != null) Toast.makeText(getContext(), "Context: Logout", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // --- Slide 31: Alert Dialog ---
    private void showAlertDialog() {
        if (getContext() == null) return;
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Thông báo");
        alert.setMessage("Bạn có muốn đổi hình ảnh không?");

        alert.setPositiveButton("Có", (dialog, which) -> {
            // Thay đổi hình ảnh (code cũ từ slide 14)
            img1.setImageResource(R.drawable.google);
            img1.getLayoutParams().width = 550;
            img1.getLayoutParams().height = 550;
        });

        alert.setNegativeButton("Không", (dialog, which) -> {

        });

        alert.show();
    }

    // --- Slide 32: Custom Dialog ---
    private void showCustomDialog() {
        if (getContext() == null) return;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        // Ánh xạ control BÊN TRONG dialog
        EditText edt = dialog.findViewById(R.id.editDialogText);
        Button btnOK = dialog.findViewById(R.id.btnDialogOK);

        btnOK.setOnClickListener(v -> {
            String text = edt.getText().toString();
            Toast.makeText(getContext(), "Bạn đã nhập: " + text, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Đóng dialog
        });

        dialog.show();
    }
}