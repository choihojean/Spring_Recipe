package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.mapper.CommentMapper;
import springfinal.recipe.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<CommentDTO> findByRecipeId(long id) {
        return commentRepository.findAll().stream()
                .map(CommentMapper::toDTO)
                .filter(c -> c.getRecipeId().equals(id) && c.getParentCommentId() == 0)
                .collect(Collectors.toList());
    }
}
