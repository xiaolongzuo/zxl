package cn.zxl.core.security.filter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import cn.zxl.orm.common.CommonBaseService;
import cn.zxl.orm.security.domain.PersistentLogins;

@Component
public class JdbcTokenRepository implements PersistentTokenRepository {

	@Autowired
	private CommonBaseService commonBaseService;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		PersistentLogins persistentLogins = new PersistentLogins(token);
		commonBaseService.save(persistentLogins);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		PersistentLogins persistentLogins = new PersistentLogins();
		persistentLogins.setSeries(series);
		PersistentLogins persistentLoginsInDB = commonBaseService.getUnique(PersistentLogins.class, persistentLogins);
		persistentLoginsInDB.setTokenValue(tokenValue);
		persistentLoginsInDB.setLastUsedDate(lastUsed);
		commonBaseService.update(persistentLoginsInDB);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		PersistentLogins persistentLogins = new PersistentLogins();
		persistentLogins.setSeries(seriesId);
		PersistentLogins persistentLoginsInDB = commonBaseService.getUnique(PersistentLogins.class, persistentLogins);
		if (persistentLoginsInDB == null)
			return null;
		return new PersistentRememberMeToken(persistentLoginsInDB.getUsername(), persistentLoginsInDB.getSeries(), persistentLoginsInDB.getTokenValue(), persistentLoginsInDB.getLastUsedDate());
	}

	@Override
	public void removeUserTokens(String username) {
		PersistentLogins persistentLogins = new PersistentLogins();
		persistentLogins.setUsername(username);
		List<PersistentLogins> persistentLoginsList = commonBaseService.getList(PersistentLogins.class, persistentLogins);
		commonBaseService.delete(persistentLoginsList);
	}

}
