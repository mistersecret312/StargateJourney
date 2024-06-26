package net.povstalec.sgjourney.common.misc;

import javax.annotation.Nullable;
import java.io.Serial;

class ServiceException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -8392300691666425882L;

    ServiceException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
