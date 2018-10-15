package c.s.l.network;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {

    ConcurrentHashMap<String, Set<SocketAddress>> tables = new ConcurrentHashMap<>();


    public boolean addUser(String uid, SocketAddress user) {
        Set<SocketAddress> inetSocketAddresses = tables.get(uid);
        if (inetSocketAddresses == null) {
            Set<SocketAddress> list = new HashSet<>(5);
            boolean result = list.add(user);
            tables.put(uid, list);
            return result;
        } else {
            return inetSocketAddresses.add(user);
        }
    }

    public Set<SocketAddress> getUsers(String uid) {
        return tables.get(uid);
    }

}
