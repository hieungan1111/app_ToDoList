package com.example.todolist.sendEmail;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class EmailSender {
    public static void sendEmail(final String email, final String subject, final String content) {
        // Khởi tạo một Thread mới
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "https://sendgrid-email-worker.nganguyetkimthi.workers.dev/";

                try {
                    // Tạo URL đối tượng
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Thiết lập phương thức HTTP POST
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Tạo JSON payload
                    JSONObject payload = new JSONObject();
                    payload.put("to", email);
                    payload.put("subject", subject);
                    payload.put("content", content);

                    // Gửi yêu cầu
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = payload.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    // Nhận phản hồi từ server
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        System.out.println("✅ Gửi email thành công!");
                    } else {
                        System.out.println("❌ Lỗi: " + connection.getResponseMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
