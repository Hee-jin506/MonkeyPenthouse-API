package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.connect.KakaoConnector;
import com.monkeypenthouse.core.connect.NaverConnector;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.dto.exception.AdditionalInfoNeededResponseDTO;
import com.monkeypenthouse.core.entity.*;
import com.monkeypenthouse.core.dto.KakaoUserDTO;
import com.monkeypenthouse.core.dto.NaverUserDTO;
import com.monkeypenthouse.core.exception.*;
import com.monkeypenthouse.core.repository.RefreshTokenRepository;
import com.monkeypenthouse.core.repository.RoomRepository;
import com.monkeypenthouse.core.repository.UserRepository;
import com.monkeypenthouse.core.security.PrincipalDetails;
import com.monkeypenthouse.core.security.SecurityUtil;
import com.monkeypenthouse.core.security.TokenProvider;
import com.monkeypenthouse.core.vo.CheckUserResponseVo;
import com.monkeypenthouse.core.dto.exception.LifeStyleNeededResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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
    public User add(User user) throws DataIntegrityViolationException{
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
        Room room = roomOptional.orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
        userRepository.updateRoomId(user.getId(), room);

        user.setRoom(room);
        return user;
    }

    // Id로 회원 조회
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
    }

    @Override
    public User getUserByEmailAndLoginType(String email, LoginType loginType) {
        return userRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkPhoneNumDuplicate(String phoneNum) {
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw new CommonException(ResponseCode.PHONE_NUMBER_DUPLICATED);
        }
    }


    @Override
    @Transactional
    public Map<String, Object> login(User user) throws AuthenticationException {

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
            throw new CommonException(ResponseCode.PASSWORD_NOT_MATCHED);
        }

        // 3. 인증 정보에서 유저 정보 가져오기
        User loggedInUser = ((PrincipalDetails) authentication.getPrincipal()).getUserInfo();
        Map<String, Object> map = new HashMap<>();
        // 라이프스타일 테스트 미완료 회원 처리
        if (loggedInUser.getLifeStyle() == null) {
            LifeStyleNeededResponseDTO dto = LifeStyleNeededResponseDTO.builder()
                    .id(loggedInUser.getId())
                    .email(loggedInUser.getEmail())
                    .name(loggedInUser.getName())
                    .birth(loggedInUser.getBirth())
                    .gender(loggedInUser.getGender())
                    .roomId(loggedInUser.getRoom().getId())
                    .phoneNum(loggedInUser.getPhoneNum())
                    .build();
            throw new CommonException(ResponseCode.LIFE_STYLE_TEST_NEEDED, dto);
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
    public Tokens reissue(String refreshToken) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 3. 저장소에서 UserID를 기반으로 RefreshToken 값 가져옴
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new CommonException(ResponseCode.REISSUE_FAILED));

        // 4. refreshToken 일치하는지 검사
        if (!savedRefreshToken.getValue().equals(refreshToken.substring(7))) {
            throw new CommonException(ResponseCode.REISSUE_FAILED);
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
    public User getMyInfo() {
        return userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CommonException(ResponseCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public User authKakao(String token) {
        KakaoUserDTO kakaoUser;
        try {
            kakaoUser = kakaoConnector.getUserInfo(token);
        } catch (Exception e) {
            throw new CommonException(ResponseCode.SOCIAL_AUTH_FAILED);
        }
        Optional<User> optionalUser = userRepository.findByEmailAndLoginType(kakaoUser.getKakao_account().getEmail(), LoginType.KAKAO);

        // 비밀번호 랜덤 문자열 생성 숫자+알파벳
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        String generatedString = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        User loggedInUser = optionalUser
                .orElseThrow(() -> {
                    AdditionalInfoNeededResponseDTO dto = AdditionalInfoNeededResponseDTO.builder()
                        .name(kakaoUser.getKakao_account().getProfile().getNickname())
                        .gender(kakaoUser.getKakao_account().isHas_gender() ? kakaoUser.getKakao_account().getGender().equals("female") ? 0 : 1 : 2)
                        .email(kakaoUser.getKakao_account().isHas_email() ? kakaoUser.getKakao_account().getEmail() : null)
                        .password(generatedString)
                        .loginType(LoginType.KAKAO)
                        .build();
                    return new CommonException(ResponseCode.ADDITIONAL_INFO_NEEDED, dto);
                });
        return loggedInUser;
    }

    @Override
    @Transactional
    public User authNaver(String token) {
        NaverUserDTO naverUser;
        try {
            naverUser = naverConnector.getUserInfo(token);
        } catch (Exception e) {
            throw new CommonException(ResponseCode.SOCIAL_AUTH_FAILED);
        }
        Optional<User> optionalUser = userRepository.findByEmailAndLoginType(naverUser.getResponse().getEmail(), LoginType.NAVER);

        User loggedInUser = optionalUser
                .orElseThrow(() -> {
                    AdditionalInfoNeededResponseDTO dto = AdditionalInfoNeededResponseDTO.builder()
                        .name(naverUser.getResponse().getName())
                        .gender(naverUser.getResponse().getGender() == null ?
                                2 : naverUser.getResponse().getGender().equals("F") ? 0 : 1)
                        .email(naverUser.getResponse().getEmail())
                        .password(UUID.randomUUID().toString())
                        .phoneNum(naverUser.getResponse().getMobile() == null ?
                                null : naverUser.getResponse().getMobile().replace("-", ""))
                        .loginType(LoginType.NAVER)
                        .build();
                    return new CommonException(ResponseCode.ADDITIONAL_INFO_NEEDED, dto);
                });
        return loggedInUser;
    }

    @Override
    public User findEmail(String phoneNum) {
        return userRepository.findByPhoneNum(phoneNum)
                .orElseThrow(() -> new CommonException(ResponseCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updatePassword(UserDetails userDetails, String password) {
        int result = userRepository.updatePassword(
                passwordEncoder.encode(password),
                userDetails.getUsername());

        if (result == 0) {
            throw new CommonException(ResponseCode.USER_NOT_FOUND);
        }
    }


    @Override
    @Transactional
    public void updateLifeStyle(User user) {
         int result =  userRepository.updateLifeStyle(
                user.getLifeStyle(),
                user.getId());

        if (result == 0) {
            throw new CommonException(ResponseCode.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteByEmail(String email){
        Long id = userRepository.findByEmail(email).orElseThrow(
                () -> new CommonException(ResponseCode.DATA_NOT_FOUND)
        ).getId();
        roomRepository.deleteUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public void logout() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        refreshTokenRepository.deleteById(authentication.getName());
    }

    @Override
    public CheckUserResponseVo checkUser(String phoneNum, String email) {

        Boolean result = userRepository.existsByPhoneNumAndEmail(phoneNum, email);
        if (!result) {
            return CheckUserResponseVo.builder().result(false).build();
        }
        String token = tokenProvider.generateSimpleToken(email, "GUEST", 1000 * 60 * 15);

        return CheckUserResponseVo
                .builder()
                .result(true)
                .token(token)
                .build();

    }
}
