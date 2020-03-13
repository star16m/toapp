package star16m.utils.toapp.api.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import star16m.utils.toapp.api.ApiHeader;
import star16m.utils.toapp.api.ApiResponse;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    enum ExceptionMapper {
        JDBC(JDBCException.class, ApiHeader.ERROR),
        DUPLICATION_EXCEPTION(DataIntegrityViolationException.class, ApiHeader.DUPLICATION_KEY),
        THROWABLE(Throwable.class, ApiHeader.ERROR),
        ;
        private Class<? extends Throwable> exceptionClass;
        private ApiHeader apiHeader;
        Function<Throwable, String> referenceMessageFunction;
        ExceptionMapper(Class<? extends Throwable> exceptionClass, ApiHeader apiHeader) {
            this(exceptionClass, apiHeader, e -> "INTERNAL_ERROR");
        }
        ExceptionMapper(Class<? extends Throwable> exceptionClass, ApiHeader apiHeader, Function<Throwable, String> referenceMessageFunction) {
            this.exceptionClass = exceptionClass;
            this.apiHeader = apiHeader;
            this.referenceMessageFunction = referenceMessageFunction;
        }
        public ApiHeader getApiHeader() {
            return this.apiHeader;
        }
        public String getReferenceMessage(Throwable e) {
            return this.referenceMessageFunction.apply(e);
        }
        static List<ExceptionMapper> EXCEPTION_MAPPER_LIST = EnumUtils.getEnumList(ExceptionMapper.class);
        static ExceptionMapper getHeader(Throwable exception) {
            return EXCEPTION_MAPPER_LIST.stream().filter(ex -> ex.exceptionClass.isInstance(exception)).findFirst().orElse(null);
        }
    }
    @ExceptionHandler(value = {Throwable.class})
    public ApiResponse<String> internalServerError(Throwable ex) {
        log.error("Error occurred.", ex);
        ExceptionMapper mapper = ExceptionMapper.getHeader(ex);
        if (mapper != null) {
            return ApiResponse.of(mapper.getApiHeader(), mapper.getReferenceMessage(ex));
        } else {
            return ApiResponse.of(ApiHeader.ERROR, ex.getMessage());
        }
    }
}
