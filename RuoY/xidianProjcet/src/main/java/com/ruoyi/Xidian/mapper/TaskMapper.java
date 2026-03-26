package com.ruoyi.Xidian.mapper;

import com.ruoyi.Xidian.domain.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    List<Task> selectList();

    void insert(Task task);

    void update(Task task);

    Task selectById(Long id);

    List<Task> selectTaskList(Task task);

    void deleteById(Long id);
}
