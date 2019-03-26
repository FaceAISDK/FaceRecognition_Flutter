package component.android.com.component_base.service;

import component.android.com.component_base.base.ILoginService;

public class DefultLoginService implements ILoginService {
    @Override
    public boolean getLoginStatus() {
        return false;
    }

    @Override
    public int getLoginUserId() {
        return 0;
    }
}
