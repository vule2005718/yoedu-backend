package com.yoedu.backend.health;

import java.time.Instant;
// Import kiểu Instant: đại diện cho 1 thời điểm cụ thể trên dòng thời gian (mốc UTC),
// thường dùng thay cho Date/LocalDateTime khi cần chính xác và không phụ thuộc múi giờ

public record HealthResponse(
        String status,        // Trạng thái hiện tại của hệ thống (vd: "UP", "DOWN")
        String application,   // Tên ứng dụng (vd: "yoedu-backend"), giúp phân biệt khi có nhiều service
        Instant timestamp     // Thời điểm kiểm tra, để biết dữ liệu health này được sinh ra lúc nào
){}
// "record" là 1 loại class đặc biệt của Java, dùng để đại diện cho dữ liệu bất biến (immutable data)
// hay còn được hiểu là 1 kiểu dữ liệu chỉ sùng để chứa kết quả
// Chỉ với 1 dòng khai báo này, Java TỰ ĐỘNG sinh ra giúp mình:
//   - Constructor: HealthResponse(status, application, timestamp)
//   - Getter cho từng field: status(), application(), timestamp()