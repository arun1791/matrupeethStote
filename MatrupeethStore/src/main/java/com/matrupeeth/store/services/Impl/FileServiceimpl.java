package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.exception.BadApiRequest;
import com.matrupeeth.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceimpl implements FileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceimpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("filename {}:", originalFilename);
        String  filename= UUID.randomUUID().toString();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
//        String fullPathWithFilename=path+ File.separator+fileNameWithExtension;
        String fullPathWithFilename=path+fileNameWithExtension;
        if(extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".png"))
        {
            //file save
            File folder=new File(path);
            if(!folder.exists())
            {
                folder.mkdirs();
            }
            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFilename));
            return  fileNameWithExtension;

        }else
        {
            throw  new BadApiRequest("File with this "+extension+" not allowes");
        }

        //return null;
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);

        return inputStream;
    }
}
