package c.s.l.network;

import lombok.*;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameRoom {

    ConcurrentHashMap<String, Set<User>> tables = new ConcurrentHashMap<>();


    public boolean addUser(String uid, int userId, SocketAddress user) {
        Set<User> users = tables.get(uid);
        if (users == null) {
            Set<User> list = new LinkedHashSet<>(5);
            boolean result = list.add(User.builder().id(userId).address(user).build());
            tables.put(uid, list);
            return result;
        } else {
            return users.add(User.builder().id(userId).address(user).build());
        }
    }

    public Set<SocketAddress> getUsers(String uid) {
        return tables.get(uid).stream().map(User::getAddress).collect(Collectors.toSet());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(of = "id")
    static class User {
        private int id;
        private String name;
        private SocketAddress address;
    }

}
