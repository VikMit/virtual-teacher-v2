package com.project.virtualteacher.utility;

import com.project.virtualteacher.exception_handling.exceptions.IncorrectInputException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class BindingResultCatcher {

    public void proceedInputError(BindingResult errors){
        if (errors.hasErrors()) {
            throw new IncorrectInputException(errors.getAllErrors().get(0).getDefaultMessage());
        }
    }
}
