package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService = new FriendService();

    @PostMapping("/add")
    public String addFriend(@RequestBody FriendRequestDTO friendRequest) {
        return friendService.sendFriendRequest(friendRequest.getUserEmail(), friendRequest.getFriendEmail());
    }
    // Accept Friend Request
    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestBody FriendRequestDTO friendRequest) {
        return friendService.acceptFriendRequest(friendRequest.getUserEmail(), friendRequest.getFriendEmail());
    }

    // Get Pending Friend Requests
    @PostMapping("/requests")
    public List<String> getPendingFriendRequests(@RequestBody UserEmailDTO userEmailDTO) {
        return friendService.getPendingFriendRequests(userEmailDTO.getUserEmail());
    }

    // Get Friends List
    @PostMapping("/list")
    public List<String> getFriendsList(@RequestBody UserEmailDTO userEmailDTO) {
        return friendService.getFriendsList(userEmailDTO.getUserEmail());
    }

    // Remove Friend
    @DeleteMapping("/remove")
    public String removeFriend(@RequestBody FriendRequestDTO friendRequest) {
        return friendService.removeFriend(friendRequest.getUserEmail(), friendRequest.getFriendEmail());
    }
}