package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.util.Base64Util;
import com.udabe.cmmn.util.FileUtil;
import com.udabe.entity.MainContent;
import com.udabe.entity.NewsImage;
import com.udabe.repository.MainContentRepository;
import com.udabe.service.MainContentService;
import com.udabe.service.impl.MainContentServiceImpl;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("${apiPrefix}/mainContent")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainContentController extends BaseCrudController<MainContent, Long> {

    private static final int BUFFER_SIZE = 512 * 1024;

    static Logger logger = LoggerFactory.getLogger(NewsImageController.class);

    private final MainContentService mainContentService;

    private final MainContentServiceImpl mainContentServiceImpl;

    private final MainContentRepository mainContentRepository;
    private ResponseEntity<?> result;

    public MainContentController(MainContentService mainContentService, MainContentServiceImpl mainContentServiceImpl, MainContentRepository mainContentRepository) {
        this.mainContentService = mainContentService;
        this.mainContentServiceImpl = mainContentServiceImpl;
        this.mainContentRepository = mainContentRepository;
    }

    @PostMapping("/addMainContent")
    public ResponseEntity<?> upload(@RequestBody String fileContent, @RequestParam Map<String, Object> param) throws IOException {
        ResponseEntity<?> result = null;

        String uploadDir = FileUtil.getUploadMainContentPath();
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
        // Ghi nội dung tệp tin từ chuỗi vào tệp tin đích
        writeToFile(fileContent, destFile);

        if (chunk == chunks - 1 || (chunk == 0 && chunks == 0)) {
            long size = destFile.length();
            String saveName = middleDir + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
            if (destFile.renameTo(new File(uploadDir + saveName))) {

                MainContent mainContent = new MainContent();
                mainContent.setGroupId(uploadKey);
                mainContent.setContentNm(fileName);
                mainContent.setContentSize(size);
                mainContent.setSaveNm(saveName);
                result = mainContentServiceImpl.save(mainContent);
            }
        }
        return result;
    }

    @PutMapping("/updateMainContent/{seq}")
    public ResponseEntity<?> update(@RequestBody String fileContent, @RequestParam Map<String, Object> param,
                                    @PathVariable(value = "seq") Long seq) throws IOException {

        ResponseEntity<?> result = null;

        String uploadDir = FileUtil.getUploadPathNote();
        String uploadKey = MapUtils.getString(param, "uploadKey");
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

        // Ghi nội dung tệp tin từ chuỗi vào tệp tin đích
        writeToFile(fileContent, destFile);

        if (chunk == chunks - 1 || (chunk == 0 && chunks == 0)) {
            long size = destFile.length();
            String saveName = middleDir + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
            if (destFile.renameTo(new File(uploadDir + saveName))) {
                Optional<MainContent> fileFind = mainContentRepository.findById(seq);
                if (fileFind.isPresent()) {
                    MainContent mainContent = mainContentRepository.findById(seq).get();
                    FileUtil.deleteFile(uploadDir + mainContent.getSaveNm());
                    mainContent.setGroupId(uploadKey);
                    mainContent.setContentNm(fileName);
                    mainContent.setContentSize(size);
                    mainContent.setSaveNm(saveName);
                    result = mainContentServiceImpl.save(mainContent);
                }
            }
        }
        return result;
    }

    @GetMapping("/displayImg/{seq}")
    public ResponseEntity<Object> displayImg(HttpServletRequest request, @PathVariable(value = "seq") Long seq) throws UnsupportedEncodingException {

        Response response = (Response) mainContentServiceImpl.find(seq).getBody();
        MainContent file = (MainContent) response.getData();

        String saveName = FileUtil.getUploadMainContentPath() + file.getSaveNm();

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

            String fileName = URLEncoder.encode(file.getContentNm(), String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + fileName + ";filename*= UTF-8''" + fileName)
                    .body(resource);
        } else {
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<script>alert('file not found')</script>");
        }

    }

    private void writeToFile(String fileContent, File destFile) throws IOException {
        try (Writer writer = new BufferedWriter(new FileWriter(destFile))) {
            writer.write(fileContent);
        }
    }

    @DeleteMapping("/checkDelete/{seq}")
    public ResponseEntity<?> delete(@PathVariable(value = "seq") Long seq) {
        return mainContentServiceImpl.checkDelete(seq);
    }

    @GetMapping("/listByGroupId")
    public ResponseEntity<?> listByGroupId(@RequestParam String groupId) {
        return mainContentServiceImpl.listByGroupId(groupId);
    }
}
