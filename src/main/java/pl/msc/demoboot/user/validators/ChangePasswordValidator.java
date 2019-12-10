package pl.msc.demoboot.user.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.msc.demoboot.constants.Constants;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.utilties.AppUtils;

public class ChangePasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        @SuppressWarnings("unused")
        User user = (User) o;
        ValidationUtils.rejectIfEmpty(errors,"newPassword", "error.userPassword.empty");
    }

    public void checkPasswords(String newPass, Errors errors){
        if(!newPass.equals(null)){
            boolean isMatch = AppUtils.checkEmailOrPassword(Constants.PASSWORD_PATTERN, newPass);
            if(!isMatch){
                errors.rejectValue("newPassword", "error.userPasswordIsNotMatch");
            }
        }
    }
}
