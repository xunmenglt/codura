package com.xunmeng.codura.pojo;

import com.xunmeng.codura.constants.Languages.CodeLanguageDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LanguageType {
    private String languageId;
    private CodeLanguageDetails language;
}
