package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.repository.entity.Room;
import com.monkeypenthouse.core.repository.entity.User;

public interface RoomService {
    Room giveVoidRoomForUser(User user) throws Exception;

    void returnRoomFromUser(Long id) throws Exception;
}
