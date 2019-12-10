package pl.msc.demoboot.user.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.msc.demoboot.constants.Constants;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.utilties.AppUtils;

public class EditProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        ValidationUtils.rejectIfEmpty(errors, "name", "error.userName.empty");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.userLastName.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.userEmail.empty");

        if (!user.getEmail().equals(null)) {
            boolean isMatch = AppUtils.checkEmailOrPassword(Constants.EMAIL_PATTERN, user.getEmail());
            if(!isMatch){
                errors.rejectValue("email", "error.userEmailIsNotMatch");
            }
        }
    }
    public void validateEmailExists(User user, Errors errors){
        if (user != null) {
            errors.rejectValue("email", "error.userEmailExist");
        }
    }
}
