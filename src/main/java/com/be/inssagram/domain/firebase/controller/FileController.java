package com.be.inssagram.domain.firebase.controller;

import com.be.inssagram.domain.firebase.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FirebaseStorageService firebaseStorageService;

    @DeleteMapping("/{fileName}")
    public String deleteFile(@PathVariable String fileName) {

        fileName = String.format("%s/%s/%s",
                "post", "yeom", fileName);

        boolean deleteFile = firebaseStorageService.deleteFile(
                fileName);

        return !deleteFile ? "삭제 실패!" : "삭제 성공";
    }
}
