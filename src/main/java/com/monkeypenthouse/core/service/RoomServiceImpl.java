package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.entity.Room;
import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.repository.RoomRepository;
import com.monkeypenthouse.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Room giveVoidRoomForUser(User user) throws Exception {
        roomRepository.updateUserIdForVoidRoom(user.getId(), user.getAuthority());
        Optional<Room> roomOptional = roomRepository.findByUserId(user.getId());
        Room room = roomOptional.orElseThrow(() -> new RuntimeException("빈 방이 없습니다."));
        userRepository.updateRoomId(user.getId(), room);
        return room;
    }

    @Override
    public void returnRoomFromUser(Long id) throws Exception {
        roomRepository.deleteUserId(null);
    }
}
