package com.codergeezer.core.base.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Base64;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author haidv
 * @version 1.0
 */
@Target({
        METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER,
})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ConditionString.Validator.class)
public @interface ConditionString {

    String charset() default "UTF-8";

    String message() default "{validation.constraints.ConditionString.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ConditionString, String> {

        private ConditionString annotation;

        @Override
        public void initialize(ConditionString annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (Strings.isBlank(value)) {
                return true; // skipped.
            }
            String json;
            try {
                json = new String(Base64.getDecoder().decode(value), annotation.charset());
            } catch (IllegalArgumentException | UnsupportedEncodingException e) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("BASE64 decoding error - " + e.getMessage())
                       .addConstraintViolation();
                return false;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.readTree(json);
            } catch (IOException e) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("JSON parsing error - " + e.getMessage())
                       .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}
