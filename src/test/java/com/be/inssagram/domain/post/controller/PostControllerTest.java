package com.be.inssagram.domain.post.controller;

import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PostController.class, excludeFilters = {})
class PostControllerTest {
    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser()
    void successCreatePost() throws Exception {
        //given
        given(postService.createPost(any())).willReturn(PostInfoResponse
                .builder()
                .postId(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .likeCount(1)
                .location("home")
                .taggedMembers(new HashSet<>())
                .hashTags(new ArrayList<>())
                .build());
        //when
        //then
        mockMvc.perform(post("/post/create")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(
                                CreatePostRequest.builder()
                                        .memberId(2L)
                                        .image(new ArrayList<>())
                                        .contents("AAA")
                                        .location("sweet")
                                        .taggedMembers(new HashSet<>())
                                        .hashTags(new ArrayList<>())
                                        .build()))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(1))
        ;
    }

    @Test
    @WithMockUser()
    void successUpdatePost() throws Exception {
        //given
        given(postService.updatePost(anyLong(), any()))
                .willReturn(PostInfoResponse.builder()
                        .postId(1L)
                        .memberId(1L)
                        .image(new ArrayList<>())
                        .contents("contents")
                        .likeCount(1)
                        .location("home")
                        .taggedMembers(new HashSet<>())
                        .hashTags(new ArrayList<>())
                        .build());
        //when
        //then
        mockMvc.perform(put("/post/update/1")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(UpdatePostRequest
                                .builder()
                                .contents("AAA")
                                .location("sweet")
                                .taggedMembers(new HashSet<>())
                                .hashTags(new ArrayList<>())
                                .build()))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("success"))
                .andExpect(jsonPath("$.data.contents")
                        .value("contents"))
        ;
    }

    @Test
    @WithMockUser()
    void successDeletePost() throws Exception {
        //given
        Long postID = 1L;
        //when
        //then
        mockMvc.perform(delete("/post/delete/{postId}", postID)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser()
    void successSearchPost_detail() throws Exception {
        //given
        given(postService.searchPostDetail(anyLong()))
                .willReturn(PostInfoResponse.builder()
                        .postId(1L)
                        .memberId(1L)
                        .image(new ArrayList<>())
                        .contents("contents")
                        .likeCount(1)
                        .location("home")
                        .taggedMembers(new HashSet<>())
                        .hashTags(new ArrayList<>())
                        .build());
        //when
        //then
        mockMvc.perform(get("/post/{postId}/detail", 1L)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status")
                        .value("success"))
                .andExpect(jsonPath("$.data.contents")
                        .value("contents"))
        ;
    }

    @Test
    @WithMockUser()
    void successSearchPost_all() throws Exception {
        //given
        List<PostInfoResponse> postInfoResponseList =
                Arrays.asList(PostInfoResponse.builder()
                        .postId(1L)
                        .memberId(1L)
                        .image(new ArrayList<>())
                        .contents("contents")
                        .likeCount(1)
                        .location("home")
                        .taggedMembers(new HashSet<>())
                        .hashTags(new ArrayList<>())
                        .build());
        given(postService.searchPostAll())
                .willReturn(postInfoResponseList);
        //when
        //then
        mockMvc.perform(get("/post/all")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status")
                        .value("success"))
                .andExpect(jsonPath("$.data[0].memberId")
                        .value(1L))
        ;
    }
}