package com.subash.fund.management.mapper;

import com.subash.fund.management.model.FundScript;
import com.subash.fund.management.model.FundView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FundMapper {

    FundMapper INSTANCE = Mappers.getMapper(FundMapper.class);

    FundScript fundViewToFundScript(FundView fundView);

    FundView fundScriptToFundView(FundScript fundScript);

}
