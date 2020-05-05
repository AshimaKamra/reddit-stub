package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.CommentBusinessService;
import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.CommentNotFoundException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/")
public class CommentController {


    @Autowired
    private CommentBusinessService commentBusinessService;

    /**
     * A controller method to post an comment to a specific post.
     *
     * @param commentRequest - This argument contains all the attributes required to store comment details in the database.
     * @param postId    - The uuid of the post whose comment is to be posted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    /**
     * A controller method to edit an comment in the database.
     *
     * @param commentEditRequest - This argument contains all the attributes required to store edited comment details in the database.
     * @param commentId          - The uuid of the comment to be edited in the database.
     * @param authorization     - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentEditResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */

    /**
     * A controller method to delete an comment in the database.
     *
     * @param commentId      - The uuid of the comment to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */

    /**
     * A controller method to fetch all the comments for a specific post in the database.
     *
     * @param postId    - The uuid of the post whose comments are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<CommentDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    //comment on the particular post
    @PostMapping("/createComment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, @RequestBody String postId, @RequestHeader String authorization)
            throws  AuthorizationFailedException,InvalidPostException {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPost(commentBusinessService.getPostByUuid(postId));
        commentEntity.setComment(commentRequest.getComment());
        CommentEntity newComment = commentBusinessService.createComment(commentEntity,postId,authorization);
        return new ResponseEntity<CommentResponse>(new CommentResponse().id(newComment.getUuid()),HttpStatus.OK);

    }

    //edit a comment in database
    @PostMapping("/editCommentContent")
    public ResponseEntity<CommentEditResponse> editCommentContent(@RequestBody CommentEditRequest commentEditRequest, @RequestBody String commentId, @RequestHeader String authorization)
            throws AuthorizationFailedException,CommentNotFoundException {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(commentEditRequest.getContent());
        commentEntity.setUuid(commentId);
        CommentEntity editedCommentEntity = commentBusinessService.editCommentContent(commentEntity,commentId,authorization);
        return new ResponseEntity<CommentEditResponse>(new CommentEditResponse().id(commentEntity.getUuid()),HttpStatus.OK);

    }

    //delete a comment in database
    @PostMapping("/deleteComment")
    public  ResponseEntity<CommentDeleteResponse> deleteComment(@RequestBody  String commentId,@RequestHeader  String authorization)
            throws AuthorizationFailedException,CommentNotFoundException
    {
        CommentEntity commentEntity = commentBusinessService.deleteComment(commentId,authorization);
        return new ResponseEntity<CommentDeleteResponse>(new CommentDeleteResponse().id(commentId),HttpStatus.OK);
    }

    //fetch all the comments of a particular user from database
    @GetMapping("/getAllCommentsToPost")
    public ResponseEntity<List<CommentDetailsResponse>> getAllCommentsToPost(@RequestBody String postId, @RequestHeader String authorization)
            throws AuthorizationFailedException, InvalidPostException {
        List<CommentEntity> commentEntityList = commentBusinessService.getCommentsByPost(postId,authorization).getResultList();
        List<CommentDetailsResponse> responses = new ArrayList<>();

        for (CommentEntity entity: commentEntityList) {
            responses.add(new CommentDetailsResponse().id(entity.getUuid()).commentContent(entity.getComment()));
        }

        return new ResponseEntity<List<CommentDetailsResponse>>(responses,HttpStatus.OK);
    }
}
