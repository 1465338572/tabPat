package com.example.tabpat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.check.LabelCheck;
import com.example.tabpat.domain.*;
import com.example.tabpat.dto.ArticlesDto;
import com.example.tabpat.dto.ArticlesLabelDto;
import com.example.tabpat.dto.LabelDto;
import com.example.tabpat.form.ArticlesLabelForm;
import com.example.tabpat.form.LabelForm;
import com.example.tabpat.query.ArticlesQuery;
import com.example.tabpat.query.LabelQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.tabpat.code.HttpStatusCode.OK;

@Service
public class LabelService extends BaseService {
    private LabelCheck labelCheck;

    private ArticlesLabelService articlesLabelService;
    @Autowired
    public void setLabelCheck(LabelCheck labelCheck){
        this.labelCheck = labelCheck;
    }

    @Autowired
    public void setArticlesLabelService(ArticlesLabelService articlesLabelService){ this.articlesLabelService = articlesLabelService; }

    @Transactional
    public Result list(LabelQuery labelQuery) throws ServiceException {
        try {
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            List<LabelDo> labelDoList = labelDao.selectAll(userId,labelQuery.getLabelName());
            PageInfo<LabelDo> labelDoPageInfo = new PageInfo<>(labelDoList);

//            //使用源数据记录total
            List<LabelDto> labelDtoList = new ArrayList<>();
            if (labelDoList.isEmpty()) {
                return Result.success(OK, "获取成功", labelDtoList);
            }

            for (LabelDo labelDo : labelDoList) {
                LabelDto labelDto = listShowDto(labelDo);
                labelDtoList.add(labelDto);
            }
            PageInfo<LabelDto> labelDtoPageInfo = new PageInfo<>(labelDtoList);
//            //将源数据total赋值给dtoTotal
            labelDtoPageInfo.setTotal(labelDoPageInfo.getTotal());
            return Result.success(OK, "查询成功",labelDtoPageInfo);

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public Result get(String labelName) throws ServiceException {
        try{
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            LabelDo labelDo = labelDao.getLabelByLabelName(userId,labelName);
            LabelDto labelDto = listShowDto(labelDo);
            return Result.success(OK,"查询成功",labelDto);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    private LabelDto listShowDto(LabelDo labelDo) throws ServiceException {
        LabelDto labelDto = new LabelDto();
        ArticlesLabelForm articlesLabelForm = new ArticlesLabelForm();
        articlesLabelForm.setLabelId(labelDo.getLabelId());
        Integer articleCount = articlesLabelService.getCount(articlesLabelForm);
        labelDto.setLabelId(labelDo.getLabelId());
        labelDto.setLabelName(labelDo.getLabelName());
        labelDto.setArticleCount(articleCount);
        return labelDto;
    }
    @Transactional
    public Result save(LabelForm labelForm) throws ServiceException {
        try {
            Result result = labelCheck.checkSave(labelForm);
            if (result.getCode() != OK) {
                return result;
            }
            LabelDo labelDo = buildLabelSave(labelForm);
            labelDao.insert(labelDo);
            String username = getCurrentUsername();
            UserDo userDo = userDao.getUserByName(username);
            UserLabelDo userLabelDo = buildUserLabelSave(userDo,labelDo);
            userLabelDao.insert(userLabelDo);
            return Result.success(OK, "标签创建成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private LabelDo buildLabelSave(LabelForm labelForm){
        LabelDo labelDo = BeanCopierUtil.create(labelForm,LabelDo.class);
        String labelId = PrimaryKeyUtil.get();
        labelDo.setLabelId(labelId);
        labelDo.setLabelName(labelForm.getLabelName());
        return labelDo;
    }

    private UserLabelDo buildUserLabelSave(UserDo userDo,LabelDo labelDo){
        UserLabelDo userLabelDo = new UserLabelDo();
        userLabelDo.setUserId(userDo.getUserId());
        userLabelDo.setLabelId(labelDo.getLabelId());
        return userLabelDo;
    }

    @Transactional
    public Result update (LabelForm labelForm) throws ServiceException {
        try{
            Result result = labelCheck.checkSave(labelForm);
            if (result.getCode() != OK) {
                return result;
            }
            LabelDo labelDo = buildLabelUpdate(labelForm);
            labelDao.updateById(labelDo);
            return Result.success(OK, "标签更新成功");
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    private LabelDo buildLabelUpdate(LabelForm labelForm){
        LabelDo labelDo = BeanCopierUtil.create(labelForm,LabelDo.class);
        labelDo.setLabelId(labelForm.getLabelId());
        labelDo.setLabelName(labelForm.getLabelName());
        return labelDo;
    }

    @Transactional
    public Result delete(LabelForm labelForm) throws ServiceException{
        try{
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            List<String> labelNames = labelForm.getLabelNames();
            for (String labelName : labelNames){
                LabelDo labelDo = labelDao.getLabelByLabelName(userId,labelName);
                UserLabelDo userLabelDo = userLabelDao.getUserLabelByUserId(labelDo.getLabelId());
                labelDao.deleteById(labelDo);
                userLabelDao.deleteById(userLabelDo);
            }
            return Result.success(OK,"删除成功");
        }catch(Exception e){
            throw new ServiceException(e);
        }
    }

}
