package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
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

    private UserResponse generateUserResponseFromEntity(UserEntity userEntity) {
        return userMapper.convertUserEntityToResponse(userEntity);
    }

    public UserResponse createUser(CreateUserRequest userRequest) {
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

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return null; //email exists
        }

        if (userRepository.findByTelephoneNumber(userRequest.getTelephoneNumber()).isPresent()) {
            return null; //telephone exists
        }

        userRequest.setRole(userRequest.getRole().toUpperCase());
        if (Role.isValidUserRole(userRequest.getRole())) {
            return null; //invalid user type
        }

        String hash = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt(9));
        userRequest.setPassword(hash);

        UserEntity userEntity = userMapper.convertUserRequestToEntity(userRequest);
        return userMapper.convertUserEntityToResponse(userRepository.saveAndFlush(userEntity));
    }

    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.mapList(userEntities, UserResponse.class);
    }

    public UserResponse patchUser(UserEntity user, Long user_id, PatchUserRequest patchUserRequest) {
        final var sameUser = user_id.equals(user.getId());
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
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
    public UserResponse updatePassword(UserEntity user, Long user_id, String password) { //should be already hashed, from middleware
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
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

    public UserResponse updateUserName(UserEntity user, Long user_id, String userName) {
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (userName.length() < 3) {
            return null; //too short
        }

        if (userName.length() > 20) {
            return null; //too long
        }

        if (!StringUtils.hasText(userName)) {
            return null; //empty
        }


        originalUser.get().setName(userName);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public UserResponse updateTelephoneNumber(UserEntity user, Long user_id, String telephoneNumber) { //phone checks? some lib maybe?
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (!StringUtils.hasText(telephoneNumber)) {
            return null; //empty
        }


        originalUser.get().setTelephoneNumber(telephoneNumber);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public UserResponse updateEmail(UserEntity user, Long user_id, String email) { //TODO: mail checks
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return null;
        }

        if (!StringUtils.hasText(email)) {
            return null; //empty
        }

        originalUser.get().setEmail(email);
        var savedUser = userRepository.save(originalUser.get());
        return generateUserResponseFromEntity(savedUser);

    }

    public Boolean deleteSingleUser(UserEntity user, Long id_to_delete) {
        final var sameUser = user.getId().equals(id_to_delete);
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return null; //unauthorized
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(id_to_delete) : Optional.of(user);
        if (originalUser.isEmpty()) {
            throw new NotFoundException();
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
