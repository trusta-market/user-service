package com.trusta_market.userservice.report.application.service;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.dto.result.ReportResult;
import com.trusta_market.userservice.report.application.port.in.ReportUseCase;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService implements ReportUseCase {

    private final UserReportRepository userReportRepository;
    private final UserValidationUseCase userValidationUseCase;

    public ReportService(UserReportRepository userReportRepository,
                         UserValidationUseCase userValidationUseCase) {
        this.userReportRepository = userReportRepository;
        this.userValidationUseCase = userValidationUseCase;
    }

    @Override
    public ReportResult createReport(CreateReportCommand command) {
        // 신고자 상태 검증
        userValidationUseCase.validateUserCanMutate(command.reporterUserId());
        // 피신고자 존재 여부 및 활성 상태 검증
        userValidationUseCase.validateActiveUser(command.reportedUserId());

        if (command.reporterUserId().equals(command.reportedUserId())) {
            throw new DomainException(ReportErrorCode.SELF_REPORT);
        }

        if (userReportRepository.existsByReporterUserIdAndReportedUserId(command.reporterUserId(), command.reportedUserId())) {
            throw new DomainException(ReportErrorCode.DUPLICATE_REPORT);
        }

        UserReport report = UserReport.create(
                command.reporterUserId(), command.reportedUserId(), command.reason()
        );

        return ReportResult.from(userReportRepository.save(report));
    }
}
