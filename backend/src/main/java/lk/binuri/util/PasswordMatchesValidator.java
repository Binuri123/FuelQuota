package lk.binuri.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    private String passwordField;
    private String confirmPasswordField;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field passwordFieldRef = obj.getClass().getDeclaredField(passwordField);
            passwordFieldRef.setAccessible(true);
            Object passwordValue = passwordFieldRef.get(obj);

            Field confirmPasswordFieldRef = obj.getClass().getDeclaredField(confirmPasswordField);
            confirmPasswordFieldRef.setAccessible(true);
            Object confirmPasswordValue = confirmPasswordFieldRef.get(obj);

            if (confirmPasswordValue == null || confirmPasswordValue.toString().isBlank()) {
                // Let @NotBlank handle this error
                return true;
            }

            return confirmPasswordValue.equals(passwordValue);
        } catch (Exception e) {
            return false;
        }
    }
}
