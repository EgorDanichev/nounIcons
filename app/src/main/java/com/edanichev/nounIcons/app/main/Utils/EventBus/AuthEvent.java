package com.edanichev.nounIcons.app.main.Utils.EventBus;

public class AuthEvent {
    private boolean isSuccess;

    public AuthEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
