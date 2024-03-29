package com.develop.web.domain.admin.dept.mapper;

import com.develop.web.domain.admin.dept.dto.DeptDetailDto;
import com.develop.web.domain.admin.dept.dto.DeptPathDto;
import com.develop.web.domain.admin.dept.dto.DeptDto;
import com.develop.web.domain.member.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    void insertDept(DeptDto deptDto);

    void deleteDept(Integer deptId);

    List<DeptDetailDto> selectDept();

    List<DeptDetailDto> selectTopDept();

    List<DeptDetailDto> selectDeptType(Integer deptParentId);

    List<UserDto> selectDeptMember(Integer deptId);

    List<DeptPathDto> selectDeptPath(Integer deptId);
}
