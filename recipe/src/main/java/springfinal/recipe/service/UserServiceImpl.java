package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springfinal.recipe.mapper.RecipeMapper;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.model.User;
import springfinal.recipe.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(RecipeMapper::toDTO)
                .orElse(null);
    }

    @Override
    public UserDTO findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .filter(user -> !user.getIs_deleted())
                .map(RecipeMapper::toDTO)
                .orElse(null);
    }

    @Override
    public boolean registerUser(UserDTO userDTO) {
        if(userRepository.findByNickname(userDTO.getNickname()).isPresent()) {
            return false;
        }

        User user = RecipeMapper.toEntity(userDTO);
        user = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .password(passwordEncoder.encode(user.getPassword()))
                .is_deleted(false)
                .build();

        userRepository.save(user);
        return true;
    }

//    @Override
//    public UserDTO authenticate(String nickname, String password) {
//        return userRepository.findByNickname(nickname)
//                .filter(user -> !user.getIs_deleted()) //탈퇴 계정은 제외
//                .filter(user -> passwordEncoder.matches(password, user.getPassword())) //비밀번호 검증
//                .map(RecipeMapper::toDTO)
//                .orElse(null);
//    }

    @Override
    public boolean updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        if(!existingUser.getNickname().equals(userDTO.getNickname()) && userRepository.findByNickname(userDTO.getNickname()).isPresent()) {
            return false;
        }

        existingUser = User.builder()
                .id(existingUser.getId())
                .nickname(userDTO.getNickname())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .is_deleted(existingUser.getIs_deleted())
                .build();

        userRepository.save(existingUser);
        return true;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        user = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .is_deleted(true)
                .build();

        userRepository.save(user);
    }

}
