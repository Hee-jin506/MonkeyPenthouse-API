package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.dao.Room;
import com.monkeypenthouse.core.dao.User;

public interface RoomService {
    Room giveVoidRoomForUser(User user) throws Exception;

    void returnRoomFromUser(Long id) throws Exception;
}
