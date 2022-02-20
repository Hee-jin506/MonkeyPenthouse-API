package com.monkeypenthouse.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monkeypenthouse.core.connect.KakaoConnector;
import com.monkeypenthouse.core.connect.NaverConnector;
import com.monkeypenthouse.core.dao.*;
import com.monkeypenthouse.core.dto.KakaoUserDTO;
import com.monkeypenthouse.core.dto.NaverUserDTO;
import com.monkeypenthouse.core.exception.*;
import com.monkeypenthouse.core.repository.RefreshTokenRepository;
import com.monkeypenthouse.core.repository.RoomRepository;
import com.monkeypenthouse.core.repository.UserRepository;
import com.monkeypenthouse.core.security.PrincipalDetails;
import com.monkeypenthouse.core.security.SecurityUtil;
import com.monkeypenthouse.core.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoConnector kakaoConnector;
    private final NaverConnector naverConnector;

    // 회원 추가
    @Override
    @Transactional
    public User add(User user) throws DataIntegrityViolationException, DataNotFoundException {
        try {
            // 회원 정보 저장
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAuthority(Authority.USER);
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("이미 존재하는 회원의 정보입니다.");
        }

        // 회원에게 빈 방 주기
        roomRepository.updateUserIdForVoidRoom(user.getId(), user.getAuthority());
        Optional<Room> roomOptional = roomRepository.findByUserId(user.getId());
        Room room = roomOptional.orElseThrow(() -> new DataNotFoundException("빈 방이 없습니다."));
        userRepository.updateRoomId(user.getId(), room);

        user.setRoom(room);
        return user;
    }

    // Id로 회원 조회
    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) throws DataNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(User.builder().id(id).build()));
    }

    @Override
    public User getUserByEmail(String email) throws DataNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(User.builder().email(email).build()));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmailAndLoginType(String email, LoginType loginType) throws DataNotFoundException {
        return userRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(() -> new DataNotFoundException(User.builder().email(email).loginType(loginType).build()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkPhoneNumDuplicate(String phoneNum) throws ExpectedException {
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw new ExpectedException(HttpStatus.FORBIDDEN, "이미 가입된 회원의 전화번호입니다.");
        }
    }


    @Override
    @Transactional
    public Map<String, Object> login(User user) throws AuthenticationException, AuthFailedException {

        // 1. Login ID/PW를 기반으로 authenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        Authentication authentication = null;
        // 2. 실제로 검증이 이뤄지는 부분
        // authentication 메서드가 실행이 될 때 CustomUserDetailService에서 만들었던 loadUserByUser
        try {
            ProviderManager providerManager = (ProviderManager) authenticationManagerBuilder.getObject();

            for (AuthenticationProvider provider : providerManager.getProviders()) {
                if (provider instanceof DaoAuthenticationProvider) {
                    ((DaoAuthenticationProvider) provider).setHideUserNotFoundExceptions(false);
                }
            }

            authentication = providerManager.authenticate(authenticationToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 정보에서 유저 정보 가져오기
        User loggedInUser = ((PrincipalDetails) authentication.getPrincipal()).getUserInfo();
        Map<String, Object> map = new HashMap<>();
        // 라이프스타일 테스트 미완료 회원 처리
        if (loggedInUser.getLifeStyle() == null) {
            throw new LifeStyleTestNeededException(loggedInUser);
        } else {
            // 4. 인증 정보를 토대로 JWT 토큰, RefreshToken 저장
            Tokens tokens = tokenProvider.generateTokens(authentication);
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokens.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);

            map.put("user", loggedInUser);
            map.put("tokens", tokens);
        }
        return map;
    }

    @Override
    @Transactional
    public Tokens reissue(String refreshToken) throws AuthFailedException {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 3. 저장소에서 UserID를 기반으로 RefreshToken 값 가져옴
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new AuthFailedException(401, "로그인된 사용자가 아닙니다."));

        // 4. refreshToken 일치하는지 검사
        if (!savedRefreshToken.getValue().equals(refreshToken.substring(7))) {
            throw new AuthFailedException(401, "토큰이 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        Tokens newTokens = tokenProvider.generateTokens(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = savedRefreshToken.updateValue(newTokens.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return newTokens;
    }

    @Override
    @Transactional(readOnly = true)
    public User getMyInfo() throws AuthFailedException {
        return userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new AuthFailedException("회원 정보를 찾지 못했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public User authKakao(String token) throws AuthFailedException {
        KakaoUserDTO kakaoUser;
        try {
            kakaoUser = kakaoConnector.getUserInfo(token);
        } catch (Exception e) {
            throw new SocialLoginFailedException(LoginType.KAKAO);
        }
        Optional<User> optionalUser = userRepository.findByEmailAndLoginType(kakaoUser.getKakao_account().getEmail(), LoginType.KAKAO);

        User loggedInUser = optionalUser
                .orElseThrow(() -> {
                    User newUser = User.builder()
                        .name(kakaoUser.getKakao_account().getProfile().getNickname())
                        .gender(kakaoUser.getKakao_account().isHas_gender() ? kakaoUser.getKakao_account().getGender().equals("female") ? 0 : 1 : 2)
                        .email(kakaoUser.getKakao_account().isHas_email() ? kakaoUser.getKakao_account().getEmail() : null)
                        .password(UUID.randomUUID().toString())
                        .loginType(LoginType.KAKAO)
                        .build();
                    return new SocialLoginFailedException(LoginType.KAKAO, newUser);
                });
        if (loggedInUser.getLifeStyle() == null) {
            throw new LifeStyleTestNeededException(loggedInUser);
        }
        return loggedInUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User authNaver(String token) throws AuthFailedException {
        NaverUserDTO naverUser;
        try {
            naverUser = naverConnector.getUserInfo(token);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new SocialLoginFailedException(LoginType.NAVER);
        }
            Optional<User> optionalUser = userRepository.findByEmailAndLoginType(naverUser.getResponse().getEmail(), LoginType.NAVER);

        User loggedInUser = optionalUser
                .orElseThrow(() -> {
                    User newUser = User.builder()
                        .name(naverUser.getResponse().getName())
                        .gender(naverUser.getResponse().getGender() == null ?
                                2 : naverUser.getResponse().getGender().equals("F") ? 0 : 1)
                        .email(naverUser.getResponse().getEmail())
                        .password(UUID.randomUUID().toString())
                        .phoneNum(naverUser.getResponse().getMobile() == null ?
                                null : naverUser.getResponse().getMobile().replace("-", ""))
                        .loginType(LoginType.NAVER)
                        .build();
                    return new SocialLoginFailedException(LoginType.NAVER, newUser);
                });
        if (loggedInUser.getLifeStyle() == null) {
            throw new LifeStyleTestNeededException(loggedInUser);
        }
        return loggedInUser;
    }

    @Override
    public User findEmail(User user) throws AuthFailedException {
        return userRepository.findByNameAndPhoneNum(user.getName(), user.getPhoneNum())
                .orElseThrow(() -> new AuthFailedException("해당 정보의 회원을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void updatePassword(User user) throws AuthFailedException {
        int result = userRepository.updatePassword(
                passwordEncoder.encode(user.getPassword()),
                user.getId());

        if (result == 0) {
            throw new AuthFailedException("해당 정보의 회원을 찾을 수 없습니다.");
        }
    }


    @Override
    @Transactional
    public void updateLifeStyle(User user) throws AuthFailedException {
         int result =  userRepository.updateLifeStyle(
                user.getLifeStyle(),
                user.getId());

        if (result == 0) {
            throw new AuthFailedException("해당 정보의 회원을 찾을 수 없습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) throws DataNotFoundException {
        Long id = userRepository.findByEmail(email).orElseThrow(
                () -> new DataNotFoundException(User.builder().email(email).build())
        ).getId();
        roomRepository.deleteUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public void logout() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        refreshTokenRepository.deleteById(authentication.getName());
    }
}
