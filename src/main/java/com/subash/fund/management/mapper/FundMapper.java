package com.subash.fund.management.mapper;

import com.subash.fund.management.model.FundScript;
import com.subash.fund.management.model.FundView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper interface for converting between {@link FundView} and {@link FundScript}.
 * <p>
 * This interface defines methods for mapping data between the view layer and the database entity layer.
 * MapStruct automatically generates the implementation at build time.
 * </p>
 *
 * <p>Component model is set to "spring" so it can be used as a Spring Bean.</p>
 *
 * <p>
 * Example Usage:
 * <pre>
 *     FundScript entity = fundMapper.fundViewToFundScript(view);
 *     FundView view = fundMapper.fundScriptToFundView(entity);
 * </pre>
 * </p>
 */
@Mapper(componentModel = "spring")
public interface FundMapper {

    /**
     * Singleton instance of the mapper.
     * This can be used in non-Spring contexts where dependency injection is not available.
     */
    FundMapper INSTANCE = Mappers.getMapper(FundMapper.class);

    /**
     * Converts a {@link FundView} object to a {@link FundScript} entity.
     *
     * @param fundView the fund view model to convert
     * @return the converted fund entity
     */
    FundScript fundViewToFundScript(FundView fundView);

    /**
     * Converts a {@link FundScript} entity to a {@link FundView} model.
     *
     * @param fundScript the fund entity to convert
     * @return the converted fund view model
     */
    FundView fundScriptToFundView(FundScript fundScript);

}
