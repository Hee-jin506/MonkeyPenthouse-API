package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.entity.Room;
import com.monkeypenthouse.core.entity.User;

public interface RoomService {
    Room giveVoidRoomForUser(User user) throws Exception;

    void returnRoomFromUser(Long id) throws Exception;
}
