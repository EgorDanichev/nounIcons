package com.edanichev.nounIcons.app.main.Utils.EventBus;

public class AuthEvent {
    private boolean isSuccess;

    public AuthEvent(boolean isSucess) {
        this.isSuccess = isSucess;
    }

    public boolean isSucess() {
        return isSuccess;
    }

    public void setSucess(boolean sucess) {
        isSuccess = sucess;
    }
}
