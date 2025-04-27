package com.xunmeng;

import cn.binarywang.tools.generator.ChineseNameGenerator;
import cn.binarywang.tools.generator.EnglishNameGenerator;
import cn.hutool.core.util.RandomUtil;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.utils.DateUtils;
import com.xunmeng.system.pojo.SysUserRole;
import com.xunmeng.system.pojo.UserUseInfo;
import com.xunmeng.system.service.ISysUserRoleService;
import com.xunmeng.system.service.ISysUserService;
import com.xunmeng.system.service.IUserUseInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DataCreateTest {
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private IUserUseInfoService userUseInfoService;
    
    @Autowired
    private ISysUserRoleService userRoleService;
    
    public List<List<String>> createRandomUser(int num){
        ChineseNameGenerator chineseNameGenerator = ChineseNameGenerator.getInstance();
        EnglishNameGenerator englishNameGenerator = EnglishNameGenerator.getInstance();
        List<String> chineseNames=new ArrayList<>();
        List<String> englishNames=new ArrayList<>();
        
        for (int i=0;i<num;i++){
            chineseNames.add(chineseNameGenerator.generate());
            englishNames.add(englishNameGenerator.generate());
        }
        List<List<String>> result=new ArrayList<>();
        result.add(englishNames);
        result.add(chineseNames);
        return result;
    }

    @Test
    public void runAction(){

    }

//    @Test
    public void run(){
        List<List<String>> randomUser = createRandomUser(30);
        List<String> englishNames=randomUser.get(0);
        List<String> chineseNames=randomUser.get(1);
        /*构建用户*/
        for (int i=0;i<englishNames.size();i++){
            String englishName=englishNames.get(i);
            String chineseName=chineseNames.get(i);
            SysUser sysUser = new SysUser();
            sysUser.setUserName(englishName);
            sysUser.setNickName(chineseName);
            sysUser.setPassword("$2a$10$Ct5u4CyIZuzG5.Xa8VIqbuZpX09PiKdEebvVhQ4Zua4kuU7.S3zC.");
            sysUser.setAvatar("/profile/avatar/2024/10/15/1728992736481_20241015194536A001.png");
            sysUser.setEmail(RandomUtil.randomNumbers(10)+"@qq.com");
            sysUser.setEnabled(1);
            sysUser.setRemark("用于编程使用");
            
            //构建用户
            userService.save(sysUser);
            // 构建用户角色
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserName(englishName);
            sysUserRole.setRoleName("programmer");
            userRoleService.save(sysUserRole);
            
            // 构建用户数据，从今天开始，构建包括今天前的30天平台使用信息
            for (int j=0;j<30;j++){
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH,-j);
                Date date = calendar.getTime();
                UserUseInfo useInfo = new UserUseInfo();
                useInfo.setUserId(englishName);
                useInfo.setCodeCompletionChars(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeCompletionTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeCompletionQaTimes(RandomUtil.randomLong(10,1000));
                useInfo.setCodeCompletionQaChars(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeCompletionQaTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setTestCaseWritingChars(RandomUtil.randomLong(1000,100000));
                useInfo.setTestCaseWritingTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setVariableTypeDeclarationChars(RandomUtil.randomLong(1000,100000));
                useInfo.setVariableTypeDeclarationTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeExplanationChars(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeExplanationTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setDcoumentionWritingChars(RandomUtil.randomLong(1000,100000));
                useInfo.setDcoumentionWritingTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeRefactoringChars(RandomUtil.randomLong(1000,100000));
                useInfo.setCodeRefactoringTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setQucikCodeInsertionChars(RandomUtil.randomLong(1000,100000));
                useInfo.setQucikCodeInsertionTokens(RandomUtil.randomLong(1000,100000));
                useInfo.setEditorUsageTime(RandomUtil.randomLong(1000,86400));
                useInfo.setEditorActiveTime(RandomUtil.randomLong(1000,86400));
                useInfo.setAddedCodeLines(RandomUtil.randomLong(100,10000));
                useInfo.setAddedCodeLines(RandomUtil.randomLong(100,10000));
                useInfo.setTotalKeyboardInputs(RandomUtil.randomLong(100,100000));
                useInfo.setCtrlCCount(RandomUtil.randomLong(100,10000));
                useInfo.setCtrlVCount(RandomUtil.randomLong(1000,10000));
                useInfo.setCreateTime(date);
                useInfo.setUpdateTime(date);
                userUseInfoService.save(useInfo);
            }
        }
        
    }
    

    
}
