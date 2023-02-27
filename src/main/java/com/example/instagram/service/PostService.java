package com.example.instagram.service;

import com.example.instagram.dao.PostRepository;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service

public class PostService {
    @Autowired
    PostRepository postRepository;
    public int savePost(Post post) {
        Post savedPost= postRepository.save(post);
        return savedPost.getPostId();
    }

    public JSONArray getPost(int userID, String postId) {
        JSONArray postArr= new JSONArray();
        if(null!=postId && postRepository.findById(Integer.valueOf(postId)).isPresent()){
            Post post = postRepository.findById(Integer.valueOf(postId)).get();
            JSONObject postObj= setPostData(post);
            postArr.put(postObj);
        }
        else{
            List<Post> postList = postRepository.findAll();
            for(Post post: postList){
                JSONObject postObj= setPostData(post);
                postArr.put(postObj);
            }
        }
        return postArr;
    }

    private JSONObject setPostData(Post post) {
        JSONObject masterJson = new JSONObject();
        masterJson.put("postId",post.getPostId());
        masterJson.put("postData",post.getPostData());

        User user= post.getUser();
        JSONObject userJsonObj= new JSONObject();
        userJsonObj.put("userId",user.getUserId());
        userJsonObj.put("firstName",user.getFirstName());
        userJsonObj.put("age",user.getAge());
        masterJson.put("user",userJsonObj);
        return masterJson;
    }

    public void updatePost(String postID, Post updatedPost) {
        if(postRepository.findById(Integer.valueOf(postID)).isPresent()){
            Post olderPost = postRepository.findById(Integer.valueOf(postID)).get();
            updatedPost.setPostId(olderPost.getPostId());
            updatedPost.setUser(olderPost.getUser());
            updatedPost.setCreatedDate(olderPost.getCreatedDate());
            Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
            updatedPost.setUpdatedDate(updatedDate);
            postRepository.save(updatedPost);
        }
    }
}
