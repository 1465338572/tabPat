package com.example.tabpat.check;

import com.example.tabpat.domain.LabelDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.LabelForm;
import com.example.tabpat.service.BaseService;
import com.example.tabpat.service.Result;
import org.springframework.stereotype.Service;


import static com.example.tabpat.code.HttpStatusCode.OK;
import static com.example.tabpat.code.HttpStatusCode.SERVICEERROR;

@Service
public class LabelCheck extends BaseService {

    public Result checkSave(LabelForm labelForm) {
        return checkSaveNonLogic(labelForm);
    }

    private Result checkSaveNonLogic(LabelForm labelForm){
        UserDo userDo = userDao.getUserByName(getCurrentUsername());
        String userId = userDo.getUserId();
        LabelDo labelDo = labelDao.getLabelByLabelName(userId,labelForm.getLabelName());
        if (labelDo != null){
            return Result.failure(SERVICEERROR,"标签名已有");
        }
        return Result.success(OK, "");
    }
}
