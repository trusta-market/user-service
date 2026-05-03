package com.trusta_market.userservice.report.application.service;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.dto.result.ReportResult;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private UserReportRepository userReportRepository;

    @Mock
    private UserValidationUseCase userValidationUseCase;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("신고 생성 성공")
    void createReport_Success() {
        // given
        UUID reporterId = UUID.randomUUID();
        UUID reportedId = UUID.randomUUID();
        CreateReportCommand command = new CreateReportCommand(reporterId, reportedId, "사기 의심");

        when(userReportRepository.existsByReporterUserIdAndReportedUserId(reporterId, reportedId)).thenReturn(false);
        when(userReportRepository.save(any(UserReport.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        ReportResult result = reportService.createReport(command);

        // then
        assertThat(result.status()).isEqualTo("PENDING");
        verify(userValidationUseCase).validateUserCanMutate(reporterId);
        verify(userValidationUseCase).validateActiveUser(reportedId);
    }

    @Test
    @DisplayName("신고 생성 실패 - 자기 자신 신고 불가")
    void createReport_Fail_SelfReport() {
        // given
        UUID userId = UUID.randomUUID();
        CreateReportCommand command = new CreateReportCommand(userId, userId, "테스트");

        // when & then
        assertThatThrownBy(() -> reportService.createReport(command))
                .isInstanceOf(DomainException.class);
    }
}
