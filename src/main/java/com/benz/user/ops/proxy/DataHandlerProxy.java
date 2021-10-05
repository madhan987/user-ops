package com.benz.user.ops.proxy;

import com.benz.user.ops.dto.UserDataResponse;

public interface DataHandlerProxy {

	UserDataResponse retrieveUserData(String emailId);
}
