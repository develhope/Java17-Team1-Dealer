package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.exception.UnauthorizedException;
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

    public ResponseEntity<?> createUser(CreateUserRequest userRequest) {
        if (!StringUtils.hasText(userRequest.getName())) {
            return new ResponseEntity<>("Invalid name: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(userRequest.getEmail())) {
            return new ResponseEntity<>("Invalid email: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(userRequest.getPassword())) {
            return new ResponseEntity<>("Invalid password: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(userRequest.getTelephoneNumber())) {
            return new ResponseEntity<>("Invalid phone number: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getName().length() < 3) {
            return new ResponseEntity<>("Invalid name: too short (min 3 char)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getName().length() > 20) {
            return new ResponseEntity<>("Invalid name: too long (max 20 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getEmail().length() < 5) {
            return new ResponseEntity<>("Invalid mail: too short (min 5 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getEmail().length() > 50) {
            return new ResponseEntity<>("Invalid mail: too long (max 50 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getTelephoneNumber().length() < 3) {
            return new ResponseEntity<>("Invalid phone number: too short (min 3 numbers)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getTelephoneNumber().length() > 11) {
            return new ResponseEntity<>("Invalid phone number: too long (max 11 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getPassword().length() < 5) {
            return new ResponseEntity<>("Invalid password: too short (min 5 char)", HttpStatus.BAD_REQUEST);
        }

        if (userRequest.getPassword().length() > 20) {
            return new ResponseEntity<>("Invalid password: too long (max 20 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("mail already used", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByTelephoneNumber(userRequest.getTelephoneNumber()).isPresent()) {
            return new ResponseEntity<>("phone number already used", HttpStatus.BAD_REQUEST);
        }

        userRequest.setRole(userRequest.getRole().toUpperCase());
        if (Role.isValidUserRole(userRequest.getRole())) {
            return new ResponseEntity<>("Invalid user role", HttpStatus.BAD_REQUEST);
        }

        String hash = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt(9));
        userRequest.setPassword(hash);

        UserEntity userEntity = userMapper.convertUserRequestToEntity(userRequest);
        var userResponse = userMapper.convertUserEntityToResponse(userRepository.saveAndFlush(userEntity));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.mapList(userEntities, UserResponse.class);
    }

    public ResponseEntity<?> patchUser(UserEntity user, Long user_id, PatchUserRequest patchUserRequest) {
        final var sameUser = user_id.equals(user.getId());
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
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

        var userResponse = userMapper.convertUserEntityToResponse(userRepository.save(originalUser.get()));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    public UserResponse getUser(Long id) {
        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return null;
        }
        return generateUserResponseFromEntity(foundUser.get());
    }

    public ResponseEntity<?> updatePassword(UserEntity user, Long user_id, String password) { //should be already hashed, from middleware
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }

        if (password.length() < 5) {
            return new ResponseEntity<>("Invalid password: too short (min 5 chars)", HttpStatus.BAD_REQUEST);
        }

        if (password.length() > 20) {
            return new ResponseEntity<>("Invalid password: too long (max 20 chars)", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(password)) {
            return new ResponseEntity<>("Invalid password: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(9));


        originalUser.get().setPassword(hash);
        var savedUser = userRepository.save(originalUser.get());
        UserResponse userResponse = generateUserResponseFromEntity(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUserName(UserEntity user, Long user_id, String userName) {
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }

        if (userName.length() < 3) {
            return new ResponseEntity<>("Invalid name: too short (min 3 chars)", HttpStatus.BAD_REQUEST);
        }

        if (userName.length() > 20) {
            return new ResponseEntity<>("Invalid name: too long (max 20 chars)", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(userName)) {
            return new ResponseEntity<>("Invalid name: cannot be empty", HttpStatus.BAD_REQUEST);
        }


        originalUser.get().setName(userName);
        var savedUser = userRepository.save(originalUser.get());
        var userResponse = generateUserResponseFromEntity(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> updateTelephoneNumber(UserEntity user, Long user_id, String telephoneNumber) { //phone checks? some lib maybe?
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(telephoneNumber)) {
            return new ResponseEntity<>("Invalid phone number: cannot be empty", HttpStatus.BAD_REQUEST);
        }


        originalUser.get().setTelephoneNumber(telephoneNumber);
        var savedUser = userRepository.save(originalUser.get());
        var userResponse = generateUserResponseFromEntity(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    public ResponseEntity<?> updateEmail(UserEntity user, Long user_id, String email) {
        final var sameUser = user_id == user.getId();
        if (!sameUser && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(user_id) : Optional.of(user);
        if (originalUser.isEmpty()) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(email)) {
            return new ResponseEntity<>("Invalid mail: cannot be empty", HttpStatus.BAD_REQUEST);
        }

        originalUser.get().setEmail(email);
        var savedUser = userRepository.save(originalUser.get());
        var userResponse = generateUserResponseFromEntity(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    public Boolean deleteSingleUser(UserEntity user, Long id_to_delete) {
        final var sameUser = user.getId().equals(id_to_delete);
        if (!sameUser && user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException();
        }

        Optional<UserEntity> originalUser = !sameUser ? userRepository.findById(id_to_delete) : Optional.of(user);
        if (originalUser.isEmpty()) {
            throw new NotFoundException("User not found");
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
