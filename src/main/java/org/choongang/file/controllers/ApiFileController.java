package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.choongang.file.entites.FileInfo;
import org.choongang.file.service.FileDeleteService;
import org.choongang.file.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class ApiFileController implements ExceptionRestProcessor {
    private final FileDeleteService deleteService;
    private final FileUploadService uploadService;

    /**
     *
     * @param files
     * @param gid
     * @param location
     * @param imageOnly -> 이미지만 올릴 수 있도록 통제
     * @return
     */
    @PostMapping
    public JSONData<List<FileInfo>> upload(@RequestParam("file")MultipartFile[] files,
                                           @RequestParam(name = "gid", required = false) String gid,
                                           @RequestParam(name = "location", required = false) String location,
                                           @RequestParam(name = "imageOnly", required = false) boolean imageOnly){

        List<FileInfo> uploadedfiles = uploadService.upload(files, gid, location, imageOnly);

//        JSONData<List<FileInfo>> data = new JSONData<>();
//        data.setData(fileInfos);
//        return data;

        return new JSONData<>(uploadedfiles);
    }

    @GetMapping("/{seq}")
    public void delete(@PathVariable("seq") Long seq){
        deleteService.delete(seq);
    }
}
