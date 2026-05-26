package com.trusta_market.userservice.report.presentation;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.report.application.port.in.ReportUseCase;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.report.presentation.dto.response.GetReportResponse;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 관리자용 사용자 신고 관리 API 컨트롤러
@RestController
@RequestMapping("/api/v1/admin/reports")
@PreAuthorize("hasRole('ADMIN')") // 클래스 레벨에서 관리자 권한 강제
public class AdminReportApiController {

    private final ReportUseCase reportUseCase;

    public AdminReportApiController(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    // 신고 목록 페이징 조회 API
    @GetMapping
    public ResponseEntity<DomainPage<GetReportResponse>> getReportList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ReportStatus status) {
        
        var results = reportUseCase.getReportPage(page, size, status);
        return ResponseEntity.ok(results.map(GetReportResponse::from));
    }

    // 신고 승인 처리 API
    @PatchMapping("/{reportId}/review")
    public ResponseEntity<GetReportResponse> reviewReport(@PathVariable UUID reportId) {
        UUID adminId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        var result = reportUseCase.reviewReport(reportId, adminId);
        return ResponseEntity.ok(GetReportResponse.from(result));
    }

    // 신고 반려 처리 API
    @PatchMapping("/{reportId}/dismiss")
    public ResponseEntity<GetReportResponse> dismissReport(@PathVariable UUID reportId) {
        UUID adminId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        var result = reportUseCase.dismissReport(reportId, adminId);
        return ResponseEntity.ok(GetReportResponse.from(result));
    }
}
