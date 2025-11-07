package com.example.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.helloworld.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.HashSet;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- XỬ LÝ SỰ KIỆN CHO CÁC NÚT ---

        // 1. Sự kiện cho nút "Ask me" ở footer
        binding.buttonAskMe.setOnClickListener(v -> showAskMeDialog());

        // 2. Sự kiện cho nút "Phân loại" số chẵn/lẻ
        binding.buttonProcessNumbers.setOnClickListener(v -> processInputNumbers());

        // 3. Sự kiện cho nút "Thực hiện" đảo ngược chuỗi
        binding.buttonReverseString.setOnClickListener(v -> reverseAndUppercaseString());
    }

    private void processInputNumbers() {
        // Lấy chuỗi người dùng nhập vào từ EditText
        String inputText = binding.editTextInputNumbers.getText().toString();

        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(getContext(), "Vui lòng nhập số.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tách chuỗi thành các phần tử riêng biệt
        String[] numberStrings = inputText.split("\\s*[,\\s]\\s*");

        ArrayList<Integer> numbersList = new ArrayList<>();
        // Chuyển đổi các chuỗi thành số và thêm vào ArrayList (vẫn có thể chứa số trùng)
        for (String numStr : numberStrings) {
            if (!numStr.isEmpty()) {
                try {
                    numbersList.add(Integer.parseInt(numStr.trim()));
                } catch (NumberFormatException e) {
                    Log.w("ProcessNumbers", "Bỏ qua giá trị không hợp lệ: " + numStr);
                }
            }
        }

        // =========================================================================
        // ===== THAY ĐỔI Ở ĐÂY: Sử dụng HashSet để loại bỏ số trùng lặp =====
        // =========================================================================

        // 1. Đưa tất cả các số từ ArrayList vào HashSet để tự động loại bỏ trùng lặp.
        HashSet<Integer> uniqueNumbers = new HashSet<>(numbersList);

        // Phân loại số chẵn và số lẻ
        StringBuilder evenNumbersBuilder = new StringBuilder("Các số chẵn : ");
        StringBuilder oddNumbersBuilder = new StringBuilder("Các số lẻ : ");

        // 2. Duyệt qua HashSet (chứa các số duy nhất) thay vì ArrayList ban đầu.
        for (Integer number : uniqueNumbers) {
            if (number % 2 == 0) {
                evenNumbersBuilder.append(number).append(" ");
            } else {
                oddNumbersBuilder.append(number).append(" ");
            }
        }

        // In kết quả ra Log.d
        String TAG = "KetQuaPhanLoai";
        Log.d(TAG, evenNumbersBuilder.toString());
        Log.d(TAG, oddNumbersBuilder.toString());

        // Thông báo cho người dùng biết là đã xử lý xong
        Toast.makeText(getContext(), "Đã xử lý và in kết quả duy nhất ra Logcat!", Toast.LENGTH_SHORT).show();
    }

    private void reverseAndUppercaseString() {
        // Lấy chuỗi người dùng nhập từ EditText
        String originalString = binding.editTextInputString.getText().toString();

        if (TextUtils.isEmpty(originalString)) {
            Toast.makeText(getContext(), "Vui lòng nhập chuỗi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tách chuỗi thành các từ dựa trên khoảng trắng
        String[] words = originalString.trim().split("\\s+");

        // Đảo ngược thứ tự các từ
        StringBuilder reversedWordsBuilder = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            reversedWordsBuilder.append(words[i]).append(" ");
        }

        // Chuyển toàn bộ chuỗi đã đảo ngược thành chữ in hoa và loại bỏ khoảng trắng thừa ở cuối
        String finalResult = reversedWordsBuilder.toString().toUpperCase().trim();

        // 1. Hiển thị kết quả lên View (TextView)
        binding.textViewReversedResult.setText("Kết quả: " + finalResult);

        // 2. In kết quả ra Toast
        Toast.makeText(getContext(), finalResult, Toast.LENGTH_LONG).show();
    }



    private void showAskMeDialog() {
        // (Giữ nguyên phương thức này như trước)
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ask_me, null);
        final EditText messageEditText = dialogView.findViewById(R.id.edit_text_message);

        builder.setView(dialogView)
                .setPositiveButton("Gửi", (dialog, id) -> {
                    String message = messageEditText.getText().toString();
                    if (!message.isEmpty()) {
                        sendEmail(message);
                    } else {
                        Toast.makeText(getContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendEmail(String message) {
        // (Giữ nguyên phương thức này như trước)
        String[] recipients = {"longquyen040@gmail.com"};
        String subject = "Lời nhắn từ Ứng dụng Giới thiệu cá nhân";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy ứng dụng email.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
