package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.util.Base64Util;
import com.udabe.cmmn.util.FileUtil;
import com.udabe.entity.NewsImage;
import com.udabe.repository.NewsImageRepository;
import com.udabe.service.NewsImageService;
import com.udabe.service.impl.NewsImageServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("${apiPrefix}/newsImage")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NewsImageController extends BaseCrudController<NewsImage, Long> {

    private static final int BUFFER_SIZE = 512 * 1024;

    static Logger logger = LoggerFactory.getLogger(NewsImageController.class);

    private final NewsImageService newsImageService;

    private final NewsImageServiceImpl newsImageServiceImpl;

    private final NewsImageRepository newsImageRepository;

    public NewsImageController(NewsImageService newsImageService, NewsImageServiceImpl newsImageServiceImpl, NewsImageRepository newsImageRepository) {
        this.newsImageService = newsImageService;
        this.newsImageServiceImpl = newsImageServiceImpl;
        this.newsImageRepository = newsImageRepository;
    }

    @PostMapping("/addImage")
    public ResponseEntity<?> upload(@RequestBody MultipartFile file, @RequestParam Map<String, Object> param) throws IOException {

        ResponseEntity<?> result = null;

        String uploadDir = FileUtil.getUploadImagePath();
        String uploadKey = MapUtils.getString(param, "uploadKey");
        String fileName = MapUtils.getString(param, "name");
        Integer chunk = MapUtils.getInteger(param, "chunk", 0);
        Integer chunks = MapUtils.getInteger(param, "chunks", 0);

        String middleDir = DateFormatUtils.format(new Date(), "/yyyy/MM/");
        String destName = uploadDir + middleDir + Base64Util.encodeString(fileName);

        File destFile = new File(destName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        if (chunk == 0 && destFile.exists()) {
            FileUtil.deleteFile(destFile);
            destFile = new File(destName);
        }

        appendFile(file.getInputStream(), destFile);

        if (chunk == chunks - 1 || (chunk == 0 && chunks == 0)) {
            long size = destFile.length();
            String saveName = middleDir + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
            if (destFile.renameTo(new File(uploadDir + saveName))) {
                NewsImage newsImage = new NewsImage();
                newsImage.setGroupId(uploadKey);
                newsImage.setImageNm(fileName);
                newsImage.setImageSize(size);
                newsImage.setSaveNm(saveName);
                result = newsImageServiceImpl.save(newsImage);
            }
        }
        return result;
    }


    @PutMapping("/update/{newsImageId}")
    public ResponseEntity<?> update(@RequestBody MultipartFile file, @RequestParam Map<String, Object> param,
                                    @PathVariable(value = "newsImageId") Long newsImageId ) throws IOException {

        ResponseEntity<?> result = null;

        String uploadDir = FileUtil.getUploadImagePath();
        String fileName = MapUtils.getString(param, "name");
        Integer chunk = MapUtils.getInteger(param, "chunk", 0);
        Integer chunks = MapUtils.getInteger(param, "chunks", 0);

        String middleDir = DateFormatUtils.format(new Date(), "/yyyy/MM/");
        String destName = uploadDir + middleDir + Base64Util.encodeString(fileName);

        File destFile = new File(destName);
        if (!destFile.getParentFile().exists()) {
            FileUtil.deleteFile(destFile);
            destFile.getParentFile().mkdirs();
        }

        appendFile(file.getInputStream(), destFile);

        if (chunk == chunks - 1 || (chunk == 0 && chunks == 0)) {
            long size = destFile.length();
            String saveName = middleDir + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
            if (destFile.renameTo(new File(uploadDir + saveName))) {
                Optional<NewsImage> fileFind = newsImageRepository.findById(newsImageId);
                if (fileFind.isPresent()) {
                    NewsImage newsImage = newsImageRepository.findById(newsImageId).get();
                    FileUtil.deleteFile(uploadDir + newsImage.getSaveNm());
                    newsImage.setImageNm(fileName);
                    newsImage.setImageSize(size);
                    newsImage.setSaveNm(saveName);
                    result = newsImageServiceImpl.save(newsImage);
                }
            }
        }
        return result;
    }

    private void appendFile(InputStream in, File destFile) {

        boolean append = destFile.exists();

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile, append), BUFFER_SIZE)) {
            in = new BufferedInputStream(in, BUFFER_SIZE);

            int len;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    @GetMapping("/displayImg/{seq}")
    public ResponseEntity<Object> displayImg(HttpServletRequest request, @PathVariable(value = "seq") Long seq) throws UnsupportedEncodingException {

        Response response = (Response) newsImageServiceImpl.find(seq).getBody();
        NewsImage file = (NewsImage) response.getData();

        String saveName = FileUtil.getUploadImagePath() + file.getSaveNm();

        Resource resource = new FileSystemResource(saveName);

        if (resource.exists()) {
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                logger.info("Could not determine file type.");
            }

            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            String fileName = URLEncoder.encode(file.getImageNm(), String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + fileName + ";filename*= UTF-8''" + fileName)
                    .body(resource);
        } else {
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<script>alert('file not found')</script>");
        }

    }

    @DeleteMapping("/checkDelete/{seq}")
    public ResponseEntity<?> delete(@PathVariable(value = "seq") Long seq) {
        return newsImageServiceImpl.checkDelete(seq);
    }

    @GetMapping("/listByGroupId")
    public ResponseEntity<?> listByGroupId(@RequestParam String groupId) {
        return newsImageServiceImpl.listByGroupId(groupId);
    }

    @GetMapping("/key")
    public ResponseEntity<?> key() {
        Map<String, String> data = new HashMap<>();
        data.put("key", String.format("utk:%s", UUID.randomUUID().toString()));
        return ResponseEntity.ok(new Response().setData(data).setMessage("Successfully!"));
    }

    @PostMapping("/saveVideo")
    public ResponseEntity<?> saveVideo(@RequestBody MultipartFile file, @RequestParam Map<String, Object> param) throws IOException {

        ResponseEntity<?> result = null;

        String uploadDir = FileUtil.getUploadImagePath();
        String uploadKey = MapUtils.getString(param, "uploadKey");
        String fileName = MapUtils.getString(param, "name");
        Integer chunk = MapUtils.getInteger(param, "chunk", 0);
        Integer chunks = MapUtils.getInteger(param, "chunks", 0);

        String middleDir = DateFormatUtils.format(new Date(), "/yyyy/MM/");
        String destName = uploadDir + middleDir + Base64Util.encodeString(fileName);

        File destFile = new File(destName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        if (chunk == 0 && destFile.exists()) {
            FileUtil.deleteFile(destFile);
            destFile = new File(destName);
        }

        appendFile(file.getInputStream(), destFile);

        if (chunk == chunks - 1 || (chunk == 0 && chunks == 0)) {
            long size = destFile.length();
            String saveName = middleDir + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
            if (destFile.renameTo(new File(uploadDir + saveName))) {
                NewsImage newsImage = new NewsImage();
                newsImage.setGroupId(uploadKey);
                newsImage.setImageNm(fileName);
                newsImage.setImageSize(size);
                newsImage.setSaveNm(saveName);
                result = newsImageServiceImpl.save(newsImage);
            }
        }
        return result;
    }

}
