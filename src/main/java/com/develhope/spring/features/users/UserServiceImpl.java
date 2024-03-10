package com.develhope.spring.features.users;

import com.develhope.spring.features.orders.OrderRepository;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import com.develhope.spring.features.users.dto.PatchUserRequest;
import com.develhope.spring.features.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;


    public Boolean isRequesterIDValid(UserEntity originalUser, Long requester_id) { //still bad and should be in middleware
        if (originalUser == null) {
            return false;
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);

        if (userRequester.isEmpty()) {
            return false;
        }

        return originalUser.getId() == requester_id || userRequester.get().getRole() == Role.ADMIN;
    }

    private UserResponse generateUserResponseFromEntity(UserEntity userEntity) {
        return userMapper.convertUserEntityToResponse(userEntity);
    }

    public UserResponse createUser(CreateUserRequest userRequest) {
        
        /*
        if (!StringUtils.hasText(userRequest.getName())) {
            return null; //empty
        }

        if (!StringUtils.hasText(userRequest.getEmail())) {
            return null; //empty
        }

        if (!StringUtils.hasText(userRequest.getPassword())) {
            return null; //empty
        }

        if (userRequest.getName().length() < 3) {
            return null; //too short
        }

        if (userRequest.getName().length() > 20) {
            return null; //too long
        }

        if (userRequest.getEmail().length() < 5) {
            return null; //too short
        }

        if (userRequest.getEmail().length() > 50) {
            return null; //too long
        }

        if (userRequest.getTelephoneNumber().length() < 5) {
            return null; //too short
        }

        if (userRequest.getTelephoneNumber().length() > 11) {
            return null; //too long
        }

        if (userRequest.getPassword().length() < 5) {
            return null; //too short
        }

        if (userRequest.getPassword().length() > 20) {
            return null; //too long
        }

         if (userRepository.existsByEmail(userRequest.getEmail())) {
            return null; //email exists
        }

        if (userRepository.existsByTelephoneNumber(userRequest.getTelephoneNumber())) {
            return null; //telephone exists
        }

        if (userRepository.existsBySurmame(userRequest.getSurname())) {
            return null; //surname exists
        }

        userRequest.setUserType(userRequest.getUserType().toUpperCase());
        if (!UserType.isValidUserType(userRequest.getUserType())) {
            return null; //invalid user type
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(9));
        userRequest.setPassword(hash);
        */
        UserEntity userEntity = userMapper.convertUserRequestToEntity(userRequest);
        return userMapper.convertUserEntityToResponse(userRepository.saveAndFlush(userEntity));
    }

    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.mapList(userEntities, UserResponse.class);
    }

    //TO DO: general functions for better checks (like, userNameCheck(string) -> bool)
    //right now these are simple, just checking empty/null
    public UserResponse patchUser(Long user_id, PatchUserRequest patchUserRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<UserEntity> originalUser = userRepository.findById(user_id);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (originalUser.get().getId() != requester_id && requesterUser.get().getRole() != Role.ADMIN) {
            return null;
        }


        if (StringUtils.hasText(patchUserRequest.getName())) {
            originalUser.get().setName(patchUserRequest.getName());
        }

        if (StringUtils.hasText(patchUserRequest.getEmail())) {
            originalUser.get().setEmail(patchUserRequest.getEmail());
        }

        if (StringUtils.hasText(patchUserRequest.getTelephoneNumber())) {
            originalUser.get().setTelephoneNumber(patchUserRequest.getTelephoneNumber());
        }

        if (StringUtils.hasText(patchUserRequest.getPassword())) {
            originalUser.get().setPassword(patchUserRequest.getPassword());
        }

        return userMapper.convertUserEntityToResponse(userRepository.save(originalUser.get()));
    }


    public UserResponse getUser(Long id) {
        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return null;
        }
        return generateUserResponseFromEntity(foundUser.get());
    }

    //TODO: proper returns for proper errors;
    public UserResponse updatePassword(Long user_id, String password, Long requester_id) { //should be already hashed, from middleware
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<UserEntity> originalUser = userRepository.findById(user_id);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (originalUser.get().getId() != requester_id && requesterUser.get().getRole() != Role.ADMIN) {
            return null;
        }

        if (password.length() < 5) {
            return null; //too short
        }

        if (password.length() > 20) {
            return null; //too long
        }

        if (!StringUtils.hasText(password)) {
            return null; //empty
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(9));


        originalUser.get().setPassword(hash);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);
    }

    public UserResponse updateUserName(Long user_id, String userName, Long requester_id) {
        if (userName.length() < 3) {
            return null; //too short
        }

        if (userName.length() > 20) {
            return null; //too long
        }

        if (!StringUtils.hasText(userName)) {
            return null; //empty
        }
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<UserEntity> originalUser = userRepository.findById(user_id);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (originalUser.get().getId() != requester_id && requesterUser.get().getRole() != Role.ADMIN) {
            return null;
        }


        originalUser.get().setName(userName);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public UserResponse updateTelephoneNumber(Long user_id, String telephoneNumber, Long requester_id) { //phone checks? some lib maybe?
        if (!StringUtils.hasText(telephoneNumber)) {
            return null; //empty
        }

        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<UserEntity> originalUser = userRepository.findById(user_id);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (originalUser.get().getId() != requester_id && requesterUser.get().getRole() != Role.ADMIN) {
            return null;
        }


        originalUser.get().setTelephoneNumber(telephoneNumber);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public UserResponse updateEmail(Long user_id, String email, Long requester_id) { //TODO: mail checks
        if (!StringUtils.hasText(email)) {
            return null; //empty
        }
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<UserEntity> originalUser = userRepository.findById(user_id);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (originalUser.get().getId() != requester_id && requesterUser.get().getRole() != Role.ADMIN) {
            return null;
        }

        originalUser.get().setEmail(email);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public Boolean deleteSingleUser(Long id_to_delete, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return false;
        }

        final var userRole = requesterUser.get().getRole();
        if (userRole != Role.ADMIN || (id_to_delete != requester_id)) {
            return false; //unhaoutrized
        }

        userRepository.deleteById(id_to_delete);
        return true;
    }

    public ResponseEntity<?> getSalesCountBySellerId(Long sellerId) {
        Optional<UserEntity> seller = userRepository.findById(sellerId);
        if (seller.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (seller.get().getRole() != Role.SELLER) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderRepository.getSalesCountBySellerId(sellerId), HttpStatus.OK);
    }

    public ResponseEntity<?> getSalesTotalPriceBySellerId(Long sellerId) {
        Optional<UserEntity> seller = userRepository.findById(sellerId);
        if (seller.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (seller.get().getRole() != Role.SELLER) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderRepository.getSalesTotalPriceBySellerId(sellerId), HttpStatus.OK);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
