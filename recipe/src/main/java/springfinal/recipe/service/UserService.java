package springfinal.recipe.service;

import springfinal.recipe.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    //UserDTO authenticate(String nickname, String password);
    boolean registerUser(UserDTO userDTO);
    void updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO findByNickname(String nickname);
}
