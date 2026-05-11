package com.trusta_market.userservice.report.domain.vo;

import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;
import com.trusta_market.userservice.report.domain.exception.ReportException;

public record ReportReason(String value) {
    public ReportReason {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() > 200) {
            throw new ReportException(ReportErrorCode.INVALID_INPUT);
        }
    }

    public static ReportReason of(String value) {
        return new ReportReason(value);
    }
}
