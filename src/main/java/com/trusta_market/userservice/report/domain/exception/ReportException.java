package com.trusta_market.userservice.report.domain.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;

public class ReportException extends CustomException {
    public ReportException(ErrorCodeSpec errorCode) {
        super(errorCode);
    }
}
