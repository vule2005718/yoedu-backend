package com.yoedu.backend.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
// Đánh dấu đây là 1 Controller xử lý HTTP request và trả dữ liệu trực tiếp (không phải trả về view/HTML).
// @RestController = @Controller + @ResponseBody
// -> Nghĩa là: giá trị return của method sẽ được Spring TỰ ĐỘNG convert sang JSON
//    rồi ghi thẳng vào response body, chứ không cần mình tự làm việc đó.

@RequestMapping("/api/health")
// Đặt tiền tố đường dẫn (base URL) chung cho TẤT CẢ các method trong class này.
// Nghĩa là mọi API bên trong class này đều bắt đầu bằng "/api/health"
// (nếu sau này thêm nhiều method khác, mỗi method có thể nối thêm path riêng, vd: "/api/health/db")

public class HealthController {

    @GetMapping
    // Đánh dấu method này xử lý HTTP GET request.
    // Vì không truyền path cụ thể (vd: @GetMapping("/check")), nên method này
    // sẽ nhận request GET đúng tại địa chỉ gốc đã khai báo ở @RequestMapping,
    // tức là: GET /api/health

    public HealthResponse health(){
        // Kiểu trả về là HealthResponse (record đã tạo trước đó)
        // Spring sẽ tự động chuyển object này thành JSON để trả về cho client

        return new HealthResponse(
                "UP",              // status: báo hiệu hệ thống đang hoạt động bình thường
                "yoedu-backend",   // application: tên ứng dụng, để phân biệt khi có nhiều service
                Instant.now()      // timestamp: lấy thời điểm HIỆN TẠI ngay lúc request này được xử lý
        );
    }
}