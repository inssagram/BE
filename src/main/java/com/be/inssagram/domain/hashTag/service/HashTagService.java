package com.be.inssagram.domain.hashTag.service;

import com.be.inssagram.domain.hashTag.documents.index.SearchHashtag;
import com.be.inssagram.domain.hashTag.documents.repository.HashtagSearchRepository;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final HashtagSearchRepository hashtagSearchRepository;

    public void saveHashTag(Post post, String name) {

        if (hashTagRepository.findByPostAndName(post, name) == null) {
            HashTag hashTag = HashTag.builder()
                    .name(name)
                    .post(post)
                    .build();
            hashTagRepository.save(hashTag);
            SearchHashtag searchHashtag = SearchHashtag.builder()
                    .name(name)
                    .build();
            hashtagSearchRepository.save(searchHashtag);
        }
    }

    public List<PostInfoResponse> searchPostWithHashTagName(String name) {
        List<HashTag> hashTagList = hashTagRepository.findByName(name);
        System.out.println(hashTagList.size());
        return hashTagList.stream().map(HashTag::getPost)
                .map(PostInfoResponse::from).toList();
    }


}