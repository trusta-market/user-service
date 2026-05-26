package com.trusta_market.userservice.report.presentation;

import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.port.in.ReportUseCase;
import com.trusta_market.userservice.report.presentation.dto.request.PostReportRequest;
import com.trusta_market.userservice.report.presentation.dto.response.GetReportResponse;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 사용자 신고 관련 외부 API 컨트롤러
@RestController
@RequestMapping("/api/v1/reports")
public class ReportApiController {

    private final ReportUseCase reportUseCase;

    public ReportApiController(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    // 타 사용자 신고 API
    @PostMapping
    public ResponseEntity<GetReportResponse> createReport(@RequestBody PostReportRequest request) {
        UUID reporterUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        var result = reportUseCase.createReport(new CreateReportCommand(
                reporterUserId,
                request.reportedUserId(),
                request.reason()));

        return ResponseEntity.status(201).body(GetReportResponse.from(result));
    }
}
