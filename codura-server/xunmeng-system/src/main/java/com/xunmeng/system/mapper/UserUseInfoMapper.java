package com.xunmeng.system.mapper;

import com.xunmeng.system.pojo.UserUseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
public interface UserUseInfoMapper extends BaseMapper<UserUseInfo> {

    UserUseInfo selectUserUseInfoSum();

    UserUseInfo selectUserUseInfoByIdSum(String userId);

    
    @Update(
        value = {
                "<script>",
                "UPDATE user_use_info",
                "<set>",
                "<if test='useInfo.editorUsageTime != null and useInfo.editorUsageTime!=0'>editor_usage_time = #{useInfo.editorUsageTime},</if>",
                "<if test='useInfo.editorActiveTime != null and useInfo.editorActiveTime!=0'>editor_active_time = #{useInfo.editorActiveTime},</if>",
                "<if test='useInfo.addedCodeLines != null and useInfo.addedCodeLines!=0'>added_code_lines = #{useInfo.addedCodeLines},</if>",
                "<if test='useInfo.deletedCodeLines != null and useInfo.deletedCodeLines!=0'>deleted_code_lines = #{useInfo.deletedCodeLines},</if>",
                "<if test='useInfo.totalKeyboardInputs != null and useInfo.totalKeyboardInputs!=0'>total_keyboard_inputs = #{useInfo.totalKeyboardInputs},</if>",
                "<if test='useInfo.ctrlCCount != null and useInfo.ctrlCCount!=0'>ctrl_c_count = #{useInfo.ctrlCCount},</if>",
                "<if test='useInfo.ctrlVCount != null and useInfo.ctrlVCount!=0'>ctrl_v_count = #{useInfo.ctrlVCount},</if>",
                "</set>",
                "WHERE user_id = #{useInfo.userId} and DATE(create_time) = CURDATE()",
                "</script>"
        }   
    )
    int updateUserUseEditorUseInfoByUserId(@Param("useInfo") UserUseInfo useInfo);


    Integer selectTotalProgrammerCount();

    Integer selectTotalAiUseTimes();

    Integer selectTotalCodeHelpCount();

    Long selectTotalPlugInUseTime();

    Long selectTotalPlugInActivateUseTime();

    List<UserUseInfo> selectDayUsePlugInStatusInMonth();

    UserUseInfo selectPlugInUsagePercentage();

    List<UserUseInfo> selectActiveUserLeaderboard(@Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);
    
}
