package com.ruoyi.Xidian.utils;

import com.ruoyi.Xidian.domain.TreeTableVo;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/utils")
public class UUIDGenerateController {
    @GetMapping("/uuid")
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/longuuid")
    public Long generateProjectId() {
        SecureRandom random = new SecureRandom();
        int smallId = random.nextInt(Integer.MAX_VALUE);
        return (long) smallId;
    }

    @PostMapping("/generatePath")
    public String generateProjectPath(String name) {
        return "/" + name;
    }
}
