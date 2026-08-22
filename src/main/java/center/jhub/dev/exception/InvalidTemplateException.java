package center.jhub.dev.exception;

import center.jhub.data.exception.BaseException;

public class InvalidTemplateException extends BaseException {

    public InvalidTemplateException(Throwable e) {
        super("Invalid template format: " + e.getMessage());
    }
}
