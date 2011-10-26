package org.easysoa.api;

import java.util.List;
import org.easysoa.model.FriendRequest;
import org.easysoa.model.User;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author dirix
 */
@Service
public interface Friends {

    List<User> getFriendsAroundMe(User user, String kmsRadius);
    List<User> searchFriends(String firstname, String surname, String town);
    void sendFriendRequest(User originUser, User targetUser);
    List<FriendRequest> getFriendRequests(User user);
    User acceptFriend(String idFriendRequest);
    User refuseFriend(String idFriendRequest);
    User removeFriend(User user,String idFriend);
}
