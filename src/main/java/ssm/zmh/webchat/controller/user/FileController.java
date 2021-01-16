package ssm.zmh.webchat.controller.user;


import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.model.request.UploadFileInBound;
import ssm.zmh.webchat.model.response.Result;
import ssm.zmh.webchat.service.FileService;
import ssm.zmh.webchat.utils.file.save.FileUtil;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {


    @PostMapping(value = "/api/v1/upload/file")
    public JsonObject uploadFile(UploadFileInBound uploadFileInBound) {


        if ("image".equals(uploadFileInBound.getFileType())) {
            if (uploadFileInBound.getMultipartFile().getContentType() == null) {
                return new Result().add("message", "error").build();
            }
            String fileSuffix = uploadFileInBound.getMultipartFile().getContentType().replace("image/", "");
            String fileName = System.currentTimeMillis() + "." + fileSuffix;
            try {
                String roomDirPath = "private".equals(uploadFileInBound.getRoomType()) ? ContactsService.createRoomDirPath(uploadFileInBound.getFromUserUUID(), uploadFileInBound.getToUserUUID()) : ContactsService.createRoomDirPath(uploadFileInBound.getGroupUUID());
                uploadFileInBound.getMultipartFile().transferTo(FileUtil.getImageFile(roomDirPath, fileName));
                return new Result().add("message", "success").add("src", FileUtil.getImageSrc(roomDirPath + "/" + fileName)).build();
            } catch (IOException e) {
                e.printStackTrace();
                return new Result().add("message", "error").build();
            }


        }
        return new Result().add("message", "error").build();

    }
}
