package com.trusta_market.userservice.membership.application.service;

import com.trusta_market.userservice.membership.application.port.in.MembershipUseCase;
import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;
import com.trusta_market.userservice.user.application.port.out.UserRepository;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MembershipService implements MembershipUseCase {

    private final UserRepository userRepository;

    public MembershipService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MembershipResult getMembership(UUID userId) {
        return MembershipResult.from(findUser(userId));
    }

    private User findUser(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new UserException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }
}
