package dam.mod.centroplus.repositories;

import java.util.List;

import dam.mod.centroplus.models.RememberToken;

public interface IRememberTokenRepository {

    boolean saveToken(int userId, String tokenHash, String expiresAt);

    RememberToken findByHash(String tokenHash);

    List<RememberToken> findAllValid();

    boolean deleteByUserId(int userId);
}