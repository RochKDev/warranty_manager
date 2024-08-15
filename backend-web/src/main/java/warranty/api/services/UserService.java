package warranty.api.services;

import warranty.api.model.User;

public interface UserService {

    User getUserByEmail(String email);

}
