package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        log.trace("Called method toCommentDto of class CommentMapper with args: comment = {};", comment);
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Collection<CommentDto> toCommentDto(Collection<Comment> comments) {
        log.trace("Called method toCommentDto of class CommentMapper with args: comments = {};", comments);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    public static Comment toComment(CommentDto commentDto) {
        log.trace("Called method toComment of class CommentMapper with args: commentDto = {};", commentDto);
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .build();
    }
}
