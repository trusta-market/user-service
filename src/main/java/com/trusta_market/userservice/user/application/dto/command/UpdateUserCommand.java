package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.Name;

// 유저 정보 수정 명령 객체
public record UpdateUserCommand(
    Name name
) {
}
